package com.delitx.furnituremaster.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.delitx.furnituremaster.R
import com.delitx.furnituremaster.ui.theme.FurnitureMasterTheme
import com.delitx.furnituremaster.ui.util.ImageUtils
import com.delitx.furnituremaster.view_models.SavedBasketsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SavedBasketsFragment : Fragment() {
    val viewModel: SavedBasketsViewModel by viewModels()

    @ExperimentalAnimationApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                FurnitureMasterTheme(isDarkTheme = isSystemInDarkTheme()) {
                    SetupView()
                }
            }
        }
    }

    @ExperimentalAnimationApi
    @Composable
    fun SetupView() {
        val list = viewModel.basketList.value
        LazyColumn(modifier = Modifier.padding(horizontal = 5.dp)) {
            itemsIndexed(list) { index, basket ->
                AnimatedVisibility(
                    visible = !viewModel.deletedBaskets.contains(basket),
                    enter = slideIn(
                        initialOffset = { fullSize -> IntOffset(0, fullSize.height) },
                        animationSpec = tween(500)
                    ),
                    exit = slideOut(
                        targetOffset = { fullSize -> IntOffset(-fullSize.width, 0) },
                        animationSpec = tween(1000)
                    )
                ) {
                    Card(
                        modifier = Modifier
                            .fillParentMaxWidth(1f)
                            .padding(5.dp)
                            .clickable(onClick = { openConcreteBasket(basket.id) }),
                        elevation = 12.dp
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(text = basket.name)
                            Spacer(modifier = Modifier.padding(7.dp))
                            Text(text = basket.phoneNumber)
                            Spacer(modifier = Modifier.padding(7.dp))
                            Text(text = basket.comment)
                            Spacer(modifier = Modifier.padding(7.dp))
                            val deleteImage =
                                ImageUtils.loadDrawable(id = if (isSystemInDarkTheme()) R.drawable.ic_delete_dark else R.drawable.ic_delete_light).value
                            deleteImage?.let {
                                Image(
                                    bitmap = deleteImage.asImageBitmap(),
                                    modifier = Modifier
                                        .clickable {
                                            viewModel.deleteBasket(basket)
                                        }
                                        .align(Alignment.End)
                                        .padding(horizontal = 10.dp),
                                    contentDescription = null
                                )
                            }
                            Spacer(modifier = Modifier.padding(7.dp))
                            LazyRow {
                                itemsIndexed(basket.products) { index, product ->
                                    val bitmap = ImageUtils.loadPicture(
                                        url = product.imageUrl[0].link,
                                        defaultImage = R.drawable.ic_launcher_background
                                    ).value
                                    bitmap?.let {
                                        Row {
                                            Card(modifier = Modifier.height(150.dp).aspectRatio(1f).padding(10.dp), elevation = 7.dp) {
                                                Image(
                                                    bitmap = bitmap.asImageBitmap(),
                                                    contentDescription = null
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun openConcreteBasket(id: Int) {
        val action =
            SavedBasketsFragmentDirections.actionNavigationSavedBasketsToNavigationCurrentBasket(
                isCurrent = viewModel.currentBasketId.value == id, id = id
            )
        Navigation.findNavController(this.requireView()).navigate(action)
    }
}
