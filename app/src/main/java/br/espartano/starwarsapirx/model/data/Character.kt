package br.espartano.starwarsapirx.model.data

data class Character(val name: String,
                     val gender: String) {

    override fun toString(): String = "$name / $gender"

}