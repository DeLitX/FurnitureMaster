package com.delitx.furnituremaster.ui.adapters

import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.delitx.furnituremaster.R
import com.delitx.furnituremaster.ui.util.ImageUtils

class CollectionImageAdapter(private val mClickListenerInterface: ClickListenerInterface) : ListAdapter<String, CollectionImageAdapter.ProductViewHolder>(
    object :
        DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }) {
    class ProductViewHolder(val v: ImageView, private val mClickListenerInterface: ClickListenerInterface) : RecyclerView.ViewHolder(v) {
        fun bind(string: String) {
            v.setOnClickListener {
                mClickListenerInterface.onClick(string)
            }
            ImageUtils.loadPicture(
                url = string,
                imageView = v,
                defaultImage = R.drawable.ic_launcher_background
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = ImageView(parent.context)
        view.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        view.setPadding(0, 0, 20, 0)
        view.adjustViewBounds = true
        return ProductViewHolder(view, mClickListenerInterface)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
interface ClickListenerInterface {
    fun onClick(link: String)
}
