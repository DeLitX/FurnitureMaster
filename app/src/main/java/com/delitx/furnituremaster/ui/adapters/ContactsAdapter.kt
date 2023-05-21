package com.delitx.furnituremaster.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.delitx.furnituremaster.R
import com.delitx.furnituremaster.data_models.MapMarker

class ContactsAdapter(private val mClickListenerInterface: ClickListenerInterface) :
    ListAdapter<MapMarker, ContactsAdapter.ContactViewHolder>(
        object :
            DiffUtil.ItemCallback<MapMarker>() {
            override fun areItemsTheSame(oldItem: MapMarker, newItem: MapMarker): Boolean {
                return oldItem.latitude == newItem.latitude &&
                    oldItem.longitude == newItem.longitude &&
                    oldItem.comment == newItem.comment
            }

            override fun areContentsTheSame(oldItem: MapMarker, newItem: MapMarker): Boolean {
                return oldItem.latitude == newItem.latitude &&
                    oldItem.longitude == newItem.longitude &&
                    oldItem.comment == newItem.comment
            }
        }) {
    class ContactViewHolder(
        val v: View,
        private val mClickListenerInterface: ClickListenerInterface
    ) : RecyclerView.ViewHolder(v) {
        private val mText: TextView = v.findViewById(R.id.contact_text)
        private var mMarker: MapMarker? = null

        init {
            mText.setOnClickListener {
                mMarker?.let {
                    mClickListenerInterface.onClick(it)
                }
            }
            v.findViewById<ImageView>(R.id.direction).setOnClickListener {
                mMarker?.let {
                    mClickListenerInterface.onDirectionClick(it)
                }
            }
        }

        fun bind(marker: MapMarker) {
            mMarker = marker
            mText.text = marker.comment.getText()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.contact_item, parent, false)
        return ContactViewHolder(view, mClickListenerInterface)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    interface ClickListenerInterface {
        fun onClick(marker: MapMarker)
        fun onDirectionClick(marker: MapMarker)
    }
}
