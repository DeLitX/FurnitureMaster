package com.delitx.furnituremaster.ui.adapters

import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.delitx.furnituremaster.R
import com.delitx.furnituremaster.ui.util.ImageUtils
import com.github.chrisbanes.photoview.PhotoView

class ProductZoomablePagerAdapter() : ListAdapter<String, ProductZoomablePagerAdapter.ProductViewHolder>(
    object :
        DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }) {
    class ProductViewHolder(val v: ImageView) : RecyclerView.ViewHolder(v) {
        fun bind(string: String) {
            ImageUtils.loadPicture(
                url = string,
                imageView = v,
                defaultImage = R.drawable.ic_launcher_background
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = PhotoView(parent.context)
        view.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        view.adjustViewBounds = true
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
