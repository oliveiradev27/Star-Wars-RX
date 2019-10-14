package br.espartano.starwarsapirx.model.data

data class Movie(val title: String,
                 val episodeId: Int,
                 val characters: MutableList<Character>)