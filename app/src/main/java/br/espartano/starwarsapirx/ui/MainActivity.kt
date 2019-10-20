package br.espartano.starwarsapirx.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.espartano.starwarsapirx.R
import br.espartano.starwarsapirx.service.StarWarsService
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    private var movies = mutableListOf<String>()
    private lateinit var textAdapter : SimpleTextAdapter

    private lateinit var progressContainer : View
    private lateinit var recyclerMovies : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressContainer = findViewById(R.id.frame_loading)

        textAdapter = SimpleTextAdapter(movies)
        recyclerMovies = findViewById(R.id.recycler_movies)
        recyclerMovies.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = textAdapter
        }

        loadMovieDetails()
    }

    private fun loadMovieDetails() {
        toggleProgressView()
        val api = StarWarsService()
        api.loadMoviesFull()?.let {
            it.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ movie ->
                    movies.add("${movie.title} -- ${movie.episodeId}\n\n${movie.characters}\n")
                }, { e -> e.printStackTrace()
                        toggleProgressView()
                }, {
                    textAdapter.notifyDataSetChanged()
                    toggleProgressView()
                }
            )
        }
    }

    private fun toggleProgressView() {
        progressContainer.visibility = when (progressContainer.visibility) {
            View.VISIBLE -> View.GONE
            else -> View.VISIBLE
        }
    }
}