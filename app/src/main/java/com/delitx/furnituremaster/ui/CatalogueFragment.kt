package com.delitx.furnituremaster.ui

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ScaleFactor
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.delitx.furnituremaster.R
import com.delitx.furnituremaster.data_models.NamedValue
import com.delitx.furnituremaster.data_models.utils.getValuesByPropertyId
import com.delitx.furnituremaster.exceptions.WrongDataException
import com.delitx.furnituremaster.ui.theme.FurnitureMasterTheme
import com.delitx.furnituremaster.ui.util.ImageUtils
import com.delitx.furnituremaster.view_models.CatalogueViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.roundToInt
import kotlin.math.sqrt

@AndroidEntryPoint
class CatalogueFragment : Fragment() {
    private var mIsVocalize = false
    private val viewModel: CatalogueViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel.clearFilters()
        return ComposeView(requireContext()).apply {
            setContent {
                FurnitureMasterTheme(isDarkTheme = isSystemInDarkTheme()) {
                    MainContent()
                }
            }
        }
    }

    override fun onResume() {
        viewModel.clearFilters()
        super.onResume()
    }

    @Preview
    @Composable
    private fun MainContent() {
        BoxWithConstraints() {
            val constraints = constraints
            Column() {
                val properties = viewModel.allProperties.value
                if (properties.isNotEmpty()) {
                    val typeValues = try {
                        getValuesByPropertyId(properties, 2)
                    } catch (e: WrongDataException) {
                        null
                    }
                    typeValues?.let {
                        LazyRow() {
                            items(it.size) { i ->
                                val item = it[i]
                                if (item is NamedValue) {
                                    Log.d("CatalogueFragment", "MainContent: $item")
                                    ItemTypeLayout(
                                        item = item,
                                        modifier = Modifier
                                            .padding(7.dp, 0.dp)
                                            .clickable(onClick = {
                                                viewModel.addChosenValue(
                                                    item.id
                                                )
                                                openProductsCatalogue()
                                            }),
                                        imageWidth = (constraints.maxWidth / 24).dp
                                    )
                                }
                            }
                        }
                    }
                    val collectionValues = viewModel.collections.value
                    Log.d("wrongImagesBug", "MainContent: $collectionValues")
                    LazyColumn(modifier = Modifier.fillMaxHeight(1f)) {
                        itemsIndexed(items = collectionValues) { index, collection ->
                            if (collection is NamedValue) {
                                CollectionLayout(collection)
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun ItemTypeLayout(item: NamedValue, modifier: Modifier, imageWidth: Dp) {
        if (item.catalogueImages.isNotEmpty()) {
            Column(modifier = modifier) {
                Card(
                    shape = CircleShape,
                    border = BorderStroke(2.dp, MaterialTheme.colors.primary),
                    elevation = 12.dp,
                    modifier = Modifier
                        .width(imageWidth)
                        .aspectRatio(1f)
                        .align(Alignment.CenterHorizontally)
                ) {
                    val image = ImageUtils.loadPicture(
                        url = item.catalogueImages[0],
                        defaultImage = R.drawable.ic_launcher_background
                    ).value
                    image?.let {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentScale = object : ContentScale {
                                override fun computeScaleFactor(
                                    srcSize: Size,
                                    dstSize: Size
                                ): ScaleFactor {
                                    return ScaleFactor(
                                        (dstSize.width / sqrt(2f)) / srcSize.width,
                                        (dstSize.height / sqrt(2f)) / srcSize.height
                                    )
                                }
                            },
                            contentDescription = null
                        )
                    }
                }
                Text(
                    text = item.getPropertyName(),
                    color = MaterialTheme.colors.onBackground,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    fontSize = 16.sp
                )
            }
        }
    }

    @Composable
    private fun ItemLocationLayout(item: NamedValue, modifier: Modifier, imageWidth: Dp) {
        if (item.catalogueImages.isNotEmpty()) {
            Column(modifier = modifier) {
                Card(
                    border = BorderStroke(2.dp, MaterialTheme.colors.primary),
                    elevation = 12.dp,
                    modifier = Modifier
                        .width(imageWidth)
                        .aspectRatio(1f)
                        .align(Alignment.CenterHorizontally)
                ) {
                    val image = ImageUtils.loadPicture(
                        url = item.catalogueImages[0],
                        defaultImage = R.drawable.ic_launcher_background
                    ).value
                    image?.let {
                        Image(bitmap = it.asImageBitmap(), contentDescription = null)
                    }
                }
                Text(
                    text = item.getPropertyName(),
                    color = MaterialTheme.colors.onBackground,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    fontSize = 16.sp
                )
            }
        }
    }

    @Composable
    private fun CollectionLayout(collection: NamedValue) {
        Log.d("CollectionLayout", "CollectionLayout: start")
        if (collection.catalogueImages.isNotEmpty()) {
            Column {
                Card(
                    elevation = 12.dp,
                    modifier = Modifier
                        .padding(horizontal = 7.dp, vertical = 4.dp)
                ) {
                    Column {
                        val height = 200.dp
                        LazyRow(
                            modifier = Modifier
                                .height(height)
                                .fillMaxWidth(1f)
                        ) {
                            itemsIndexed(collection.catalogueImages) { position, item ->
                                Log.d("CollectionLayout", "CollectionLayout: list")
                                val image = ImageUtils.loadPicture(
                                    url = item,
                                    imageHeight = LocalDensity.current.run { height.toPx() }
                                        .roundToInt(),
                                    defaultImage = R.drawable.ic_launcher_background
                                ).value
                                image?.let {
                                    Log.d("CollectionLayout", "CollectionLayout: image")
                                    Image(
                                        bitmap = image.asImageBitmap(),
                                        modifier = Modifier
                                            .fillMaxWidth(1f)
                                            .height(200.dp)
                                            .padding(0.dp, 0.dp, 10.dp, 0.dp)
                                            .clickable {
                                                openZoomablePager(
                                                    id = collection.id,
                                                    position = position
                                                )
                                            },
                                        contentScale = ContentScale.FillHeight,
                                        contentDescription = null
                                    )
                                }
                            }
                        }
                        Text(
                            text = collection.name.getText(),
                            fontSize = 20.sp,
                            modifier = Modifier
                                .padding(5.dp)
                                .align(Alignment.CenterHorizontally),
                            color = MaterialTheme.colors.onSurface
                        )
                    }
                }
                Spacer(modifier = Modifier.height(7.dp))
            }
        }
    }

    private fun openProductsCatalogue() {
        val action =
            CatalogueFragmentDirections.actionNavigationCatalogueToNoavigationCatalogueProductsFragment()
        Navigation.findNavController(requireView()).navigate(action)
    }

    private fun openZoomablePager(id: Int, position: Int) {
        val action =
            CatalogueFragmentDirections.actionNavigationCatalogueToZoomablePagerActivity(
                position = position,
                collectionId = id
            )
        Navigation.findNavController(requireView()).navigate(action)
    }

    fun callDialog() {
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.phone_dialog, null, false)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(
                view
            )
            .create()
        val phoneNumber: EditText = view.findViewById(R.id.phone_number)
        val ok: Button = view.findViewById(R.id.ok)
        val cancel: Button = view.findViewById(R.id.cancel)
        ok.setOnClickListener {
            viewModel.call(phoneNumber.text.toString())
            dialog.dismiss()
        }
        cancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}
