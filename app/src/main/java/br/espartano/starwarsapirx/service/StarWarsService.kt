package br.espartano.starwarsapirx.service

import android.net.Uri
import br.espartano.starwarsapirx.model.api.Person
import br.espartano.starwarsapirx.model.data.Character
import br.espartano.starwarsapirx.model.data.Movie
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rx.Observable

class StarWarsService {

    private val baseUrl = "http://swapi.co/api/"
    private val service : StarWarsApi
    private val personCache = mutableMapOf<String, Person>()

    init {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)

         GsonBuilder()
             .setLenient()
             .create()

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build()

        service = retrofit.create(StarWarsApi::class.java)
    }

    fun loadMovies(): Observable<Movie>? {
        return service.listMovies()
            .flatMap { filmsResult -> Observable.from(filmsResult.results) }
            .map { film -> Movie(film.title, film.episodeId, ArrayList()) }
    }

    fun loadMoviesFull(): Observable<Movie>? {
        return service.listMovies()
            .flatMap { filmResults -> Observable.from(filmResults.results) }
            .flatMap { film ->
                Observable.zip(
                    Observable.just(Movie(film.title, film.episodeId, ArrayList())),
                    Observable.from(film.personUrls)
                        .flatMap { personUrl ->
                            Observable
                                .concat(getCache(personUrl),
                                    service
                                        .loadPerson(Uri.parse(personUrl).lastPathSegment!!)
                                        .doOnNext { person -> personCache[personUrl] = person })
                                .first()
                        }
                        .map { person -> Character(person.name, person.gender) }
                        .toList()
                ){ movie, characters ->
                    movie.characters.addAll(characters)
                    movie
                }
            }
    }

    private fun getCache(url : String) : Observable<Person> = Observable.from(personCache.keys)
            .filter{ key -> key == url }
            .flatMap { Observable.just(personCache[url]) }

}