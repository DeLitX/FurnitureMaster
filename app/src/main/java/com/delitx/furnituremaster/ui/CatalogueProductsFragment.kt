package com.delitx.furnituremaster.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.delitx.furnituremaster.R
import com.delitx.furnituremaster.data_models.Product
import com.delitx.furnituremaster.data_models.ProductProperty
import com.delitx.furnituremaster.ui.theme.FurnitureMasterTheme
import com.delitx.furnituremaster.ui.theme.MainOrange
import com.delitx.furnituremaster.ui.util.ImageUtils
import com.delitx.furnituremaster.view_models.CatalogueViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CatalogueProductsFragment : Fragment() {
    private val viewModel: CatalogueViewModel by activityViewModels()
    private var mIsVocalize = false

    @ExperimentalFoundationApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                FurnitureMasterTheme(isDarkTheme = isSystemInDarkTheme()) {
                    MainContent()
                }
            }
        }
    }

    @ExperimentalFoundationApi
    @Preview
    @Composable
    private fun MainContent() {
        val list = viewModel.productsToPresent.value
        LazyVerticalGrid(cells = GridCells.Fixed(2), modifier = Modifier.fillMaxSize(1f)) {
            itemsIndexed(items = list) { index, product ->
                ProductLayout(product = product)
                Surface(color = MainOrange) {
                    Spacer(
                        modifier = Modifier
                            .height(1.dp)
                            .padding(bottom = 5.dp)
                    )
                }
            }
        }
    }

    @Composable
    private fun ProductLayout(product: Product) {
        Log.d("ProductImageBug", "ProductLayout: product $product")
        Column {
            Card(
                elevation = 12.dp,
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 4.dp, start = 4.dp, end = 4.dp)
                    .clickable(
                        onClick = { openConcreteProduct(product.id) }
                    )
            ) {
                Column {
                    if (product.imageUrl.isNotEmpty()) {
                        val image = ImageUtils.loadPicture(
                            url = product.imageUrl[0].link,
                            defaultImage = R.drawable.ic_launcher_background
                        ).value
                        image?.let {
                            Image(
                                bitmap = it.asImageBitmap(),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp),
                                contentScale = ContentScale.Fit, contentDescription = null
                            )
                        }
                    }
                }
            }

            Text(
                text = product.name.getText(),
                fontSize = 16.sp,
                modifier = Modifier.padding(5.dp),
                color = MaterialTheme.colors.onSurface
            )
        }
    }

    @Composable
    fun DrawerContent() {
        val properties = viewModel.allProperties.value
        val chosenProperties = viewModel.chosenPropertiesValuesIds.value
        val propertiesNames = properties.map { it.key }
        LazyColumn(modifier = Modifier.padding(10.dp)) {
            itemsIndexed(propertiesNames) { index, property: ProductProperty ->
                if (!property.notShowInFilter) {
                    Column {
                        Text(text = property.name.getText(), fontSize = 20.sp)
                        for (variation in properties[property] ?: listOf()) {
                            Row {
                                Checkbox(
                                    checked = chosenProperties.contains(variation.id),
                                    onCheckedChange = {
                                        if (it) {
                                            viewModel.addChosenValue(variation.id)
                                        } else {
                                            viewModel.removeChosenValue(variation.id)
                                        }
                                    }
                                )
                                Spacer(modifier = Modifier.padding(5.dp))
                                Text(
                                    text = variation.getPropertyName(),
                                    fontSize = 15.sp,
                                    color = MaterialTheme.colors.onSurface
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun openConcreteProduct(productId: Int) {
        val action =
            CatalogueProductsFragmentDirections.actionNoavigationCatalogueProductsFragmentToNavigationConcreteProduct(
                productId,
                ""
            )
        Navigation.findNavController(this.requireView()).navigate(action)
    }
}
