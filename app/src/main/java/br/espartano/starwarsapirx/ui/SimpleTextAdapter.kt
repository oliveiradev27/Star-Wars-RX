package br.espartano.starwarsapirx.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.espartano.starwarsapirx.R

class SimpleViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

    val textInfo = itemView.findViewById<TextView>(R.id.text_movies)

    fun bind(movieInfo : String) {
        textInfo.text = movieInfo
    }

}

class SimpleTextAdapter(var movies : MutableList<String>) : RecyclerView.Adapter<SimpleViewHolder>() {

    fun update(movies : List<String>) {
        this.movies.clear()
        this.movies.addAll(movies)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleViewHolder {
         return SimpleViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.item_simple_text, parent,false))
    }

    override fun getItemCount(): Int = movies.size

    override fun onBindViewHolder(holder: SimpleViewHolder, position: Int) {
        holder.bind(movies[position])
    }

}