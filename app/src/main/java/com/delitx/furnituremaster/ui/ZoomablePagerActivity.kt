package com.delitx.furnituremaster.ui

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.delitx.furnituremaster.R
import com.delitx.furnituremaster.ui.adapters.ProductZoomablePagerAdapter
import com.delitx.furnituremaster.view_models.ZoomablePagerViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ZoomablePagerActivity : AppCompatActivity() {
    val viewModel: ZoomablePagerViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zoomable_pager)
        bind()
    }

    fun bind() {
        val backButton = findViewById<ImageView>(R.id.back_button)
        val viewPager = findViewById<ViewPager2>(R.id.view_pager)
        backButton.setOnClickListener {
            onBackPressed()
        }
        val adapter = ProductZoomablePagerAdapter()
        viewPager.adapter = adapter
        val values = navArgs<ZoomablePagerActivityArgs>()
        viewModel.getCollection(values.value.collectionId).observe(this) {
            if (it != null) {
                adapter.submitList(it.catalogueImages)
                if (it.catalogueImages.size > values.value.position) {
                    viewPager.setCurrentItem(values.value.position, false)
                }
            }
        }
    }
}
