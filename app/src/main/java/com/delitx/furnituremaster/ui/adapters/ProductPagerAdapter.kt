package com.delitx.furnituremaster.ui.adapters

import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.delitx.furnituremaster.R
import com.delitx.furnituremaster.data_models.CommentedImage
import com.delitx.furnituremaster.ui.util.ImageUtils

class ProductPagerAdapter() : ListAdapter<CommentedImage, ProductPagerAdapter.ProductViewHolder>(object :
        DiffUtil.ItemCallback<CommentedImage>() {
        override fun areItemsTheSame(oldItem: CommentedImage, newItem: CommentedImage): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CommentedImage, newItem: CommentedImage): Boolean {
            return oldItem.link == newItem.link
        }
    }) {
    class ProductViewHolder(val v: ImageView) : RecyclerView.ViewHolder(v) {
        fun bind(string: String) {
            ImageUtils.loadPicture(url = string, imageView = v, defaultImage = R.drawable.ic_launcher_background)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = ImageView(parent.context)
        view.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        view.adjustViewBounds = true
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position).link)
    }
}
