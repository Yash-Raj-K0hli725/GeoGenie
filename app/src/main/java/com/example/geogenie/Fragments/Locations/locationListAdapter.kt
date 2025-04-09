package com.example.geogenie.Fragments.Locations

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.geogenie.databinding.ItemLayBinding
import com.example.geogenie.geminiJson.geminiJsonResponseItem

class locationListAdapter :
    ListAdapter<geminiJsonResponseItem, locationListAdapter.listVH>(difUtil()) {
    inner class listVH(val bind: ItemLayBinding) : RecyclerView.ViewHolder(bind.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): listVH {
        return listVH(ItemLayBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: listVH, position: Int) {
        val currentItem = getItem(position)
        holder.bind.apply {
            rating.text = currentItem.rating
            place.text = currentItem.name
            desc.text = currentItem.description
        }
        holder.bind.cvItem.setOnClickListener {
            holder.itemView
                .findNavController()
                .navigate(LocationsDirections.actionLocationsToDetailedView(currentItem))
        }
    }
}

private class difUtil : DiffUtil.ItemCallback<geminiJsonResponseItem>() {
    override fun areItemsTheSame(
        oldItem: geminiJsonResponseItem,
        newItem: geminiJsonResponseItem
    ): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(
        oldItem: geminiJsonResponseItem,
        newItem: geminiJsonResponseItem
    ): Boolean {
        return oldItem.description == newItem.description
    }
}