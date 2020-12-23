package co.uk.happyapper.retailinmotion

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TramItemAdapter(
    private val items: List<TramDestination>,
    private val context: Context
) :
    RecyclerView.Adapter<TramItemAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val destination: TextView = view.findViewById(R.id.destination)
        val time: TextView = view.findViewById(R.id.time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ItemViewHolder(
            LayoutInflater.from(context).inflate(R.layout.tram_line, parent, false)
        )

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.destination.text = items[position].destination
        holder.time.text = items[position].dueMins
    }

    override fun getItemCount(): Int = items.size
}