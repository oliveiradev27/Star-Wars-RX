package br.espartano.starwarsapirx.service

import br.espartano.starwarsapirx.model.api.FilmResult
import br.espartano.starwarsapirx.model.api.Person
import retrofit2.http.GET
import retrofit2.http.Path
import rx.Observable

interface StarWarsApi {

    @GET("films")
    fun listMovies() : Observable<FilmResult>

    @GET("people/{personId}")
    fun loadPerson(@Path("personId") personId : String) : Observable<Person>
}