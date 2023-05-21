package com.delitx.furnituremaster.ui

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.viewpager2.widget.ViewPager2
import com.delitx.furnituremaster.R
import com.delitx.furnituremaster.data.local.converters.Converters
import com.delitx.furnituremaster.data_models.*
import com.delitx.furnituremaster.ui.adapters.ProductPagerAdapter
import com.delitx.furnituremaster.ui.animations.ProductPagerTransformer
import com.delitx.furnituremaster.ui.theme.FurnitureMasterTheme
import com.delitx.furnituremaster.ui.theme.MainOrange
import com.delitx.furnituremaster.ui.theme.proximaNovaFontFamily
import com.delitx.furnituremaster.ui.util.*
import com.delitx.furnituremaster.ui.util.ImageUtils.loadDrawable
import com.delitx.furnituremaster.view_models.ConcreteProductViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.ar.core.ArCoreApk
import dagger.hilt.android.AndroidEntryPoint
import java.lang.NumberFormatException

@AndroidEntryPoint
class ConcreteProductFragment : Fragment() {
    val viewModel: ConcreteProductViewModel by viewModels()
    val adapter = ProductPagerAdapter()
    val propertyTextSize = 16.sp
    val valueTextSize = 16.sp
    val categoriesStartPadding = 15.dp
    val enableArButton = mutableStateOf(false)

    @ExperimentalFoundationApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        arguments?.let {
            val safeArgs = ConcreteProductFragmentArgs.fromBundle(it)
            val productId = safeArgs.productId
            val configuration = safeArgs.productConfiguration
            val basketId = safeArgs.basketId
            viewModel.setProduct(
                productId = productId,
                if (configuration == null || configuration == "") null else Converters().toProductPropertiesSet(
                    configuration
                ),
                basketId
            )
        }
        maybeEnableArButton()
        return ComposeView(requireContext()).apply {
            setContent {
                FurnitureMasterTheme(isDarkTheme = isSystemInDarkTheme()) {
                    SetupLayout()
                }
            }
        }
    }

    fun startAr(product: Product) {
        val action = ConcreteProductFragmentDirections.actionNavigationConcreteProductToArActivity(
            product.modelPath!!,
            product.id
        )
        Navigation.findNavController(requireView()).navigate(action)
        /*val file = File.createTempFile(product.id, ".glb")
        viewModel.loadFile(product.modelPath!!, file) {
            val sceneViewerIntent = Intent(Intent.ACTION_VIEW)
            val intentUri = Uri.parse("https://arvr.google.com/scene-viewer/1.0").buildUpon()
                .appendQueryParameter("file", it.toString())
                .appendQueryParameter("title", product.name.getText())
                .build()
            sceneViewerIntent.setData(intentUri)
            startActivity(sceneViewerIntent)
        }*/
    }

    fun maybeEnableArButton() {
        val availability = ArCoreApk.getInstance().checkAvailability(requireContext())
        if (availability.isTransient) {
            // Continue to query availability at 5Hz while compatibility is checked in the background.
            Handler().postDelayed({
                maybeEnableArButton()
            }, 200)
        }
        enableArButton.value = availability.isSupported
    }

    @ExperimentalFoundationApi
    @Composable
    fun SetupLayout() {
        val product = viewModel.product.value
        val price = viewModel.price.value
        val snackbarHostState = remember { SnackbarHostState() }
        val snackbarController = SnackbarController(lifecycleScope)
        if (product != null) {
            Box {
                LazyColumn(modifier = Modifier.fillMaxSize(1f)) {
                    item {
                        MainPager(product)
                    }
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(1f)
                                .padding(horizontal = categoriesStartPadding)
                        ) {
                            Text(
                                text = product.name.getText(),
                                color = MaterialTheme.colors.onBackground,
                                fontSize = 20.sp
                            )
                            Row {
                                // Text(text = getString(R.string.price)+" : \n",fontSize = 20.sp)
                                Text(
                                    text = getString(R.string.price) + " : \n" + price,
                                    color = MaterialTheme.colors.onBackground,
                                    fontSize = 16.sp
                                )
                            }

                            Surface(color = MainOrange) {
                                Spacer(
                                    modifier = Modifier
                                        .height(1.dp)
                                        .fillMaxWidth(1f)
                                )
                            }
                        }
                    }
                    if (enableArButton.value && product.modelPath != null) {
                        item {
                            Box(modifier = Modifier.fillMaxWidth(1f)) {
                                Card(
                                    modifier = Modifier
                                        .clickable {
                                            startAr(product)
                                        }
                                        .align(Alignment.CenterEnd),
                                    elevation = 7.dp
                                ) {
                                    Row(modifier = Modifier.padding(7.dp)) {
                                        val image =
                                            loadDrawable(R.drawable.ic_view_in_ar_new_googblue_48dp).value
                                        image?.let {
                                            Image(
                                                it.asImageBitmap(),
                                                contentDescription = getString(R.string.view_in_ar)
                                            )
                                        }
                                        Text(text = getString(R.string.view_in_ar))
                                    }
                                }
                            }
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.padding(5.dp))
                    }
                    // order product properties similar to their order on server
                    val orderedProperties =
                        mutableListOf<Pair<ProductProperty, List<PropertyValue>>>()
                    for (i in product.order) {
                        val temp =
                            product.propertiesList.entries.find { it.key.id == i } ?: continue
                        orderedProperties.add(Pair(temp.key, temp.value))
                    }
                    itemsIndexed(items = orderedProperties) { position, item ->
                        if (item.second.size == 1) {
                            if (item.second[0] is NumeralValue) {
                                OneOnlyNumeralParameterLayout(
                                    property = item.first,
                                    value = (
                                        viewModel.configuration.value.set[item.first.id]
                                            ?: item.second[0]
                                        ) as NumeralValue // here we try to get value from configuration
                                )
                            } else {
                                OneOnlyNamedParameterLayout(
                                    property = item.first,
                                    value = item.second[0]
                                )
                            }
                        } else {
                            MultipleParameterLayout(
                                property = item.first,
                                values = item.second
                            )
                        }
                    }
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(1f)
                                .padding(horizontal = categoriesStartPadding)
                        ) {
                            Text(
                                text = getString(R.string.price) + " : \n" + price,
                                color = MaterialTheme.colors.onBackground,
                                modifier = Modifier
                                    .align(
                                        Alignment.CenterEnd
                                    )
                                    .padding(7.dp),
                                fontSize = 25.sp,
                                fontFamily = proximaNovaFontFamily
                            )
                        }
                    }
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth(1f)
                                .padding(7.dp)
                                .clickable(
                                    onClick = {
                                        if (viewModel.addToBasket()) {
                                            snackbarController.showSnackbar(
                                                snackbarHostState,
                                                getString(R.string.added_to_cart)
                                            )
                                        } else {

                                            snackbarController.showSnackbar(
                                                snackbarHostState,
                                                getString(R.string.already_in_cart)
                                            )
                                        }
                                    }
                                ),
                            backgroundColor = MainOrange
                        ) {
                            Text(
                                text = getString(R.string.add_to_cart),
                                modifier = Modifier
                                    .fillMaxWidth(1f)
                                    .padding(5.dp),
                                textAlign = TextAlign.Center,
                                fontSize = 20.sp,
                                color = MaterialTheme.colors.onBackground
                            )
                        }
                    }
                }
                DefaultSnackbar(
                    snackbarHostState = snackbarHostState,
                    modifier = Modifier.align(Alignment.BottomCenter),
                    backgroundColor = Color.Green
                )
            }
        }
    }

    @Composable
    fun MainPager(product: Product) {
        AndroidView(modifier = Modifier.fillMaxWidth(1f), factory = {
            val view = LayoutInflater.from(it).inflate(R.layout.product_pager_layout, null)
            val viewPager = view.findViewById<ViewPager2>(R.id.view_pager)
            val tabs = view.findViewById<TabLayout>(R.id.tab_layout)
            val comment = view.findViewById<TextView>(R.id.comment)
            viewPager.setPageTransformer(ProductPagerTransformer())
            viewPager.adapter = adapter
            TabLayoutMediator(tabs, viewPager) { tab, position ->
            }.attach()
            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    comment.text = product.imageUrl[position].comment.getText()
                }
            })
            view
        }) {
            adapter.submitList(product.imageUrl)
        }
    }

    @Composable
    fun OneOnlyNamedParameterLayout(property: ProductProperty, value: PropertyValue) {
        Row(modifier = Modifier.padding(start = categoriesStartPadding)) {
            Text(
                text = "${property.name.getText()} : ",
                fontSize = propertyTextSize,
                modifier = Modifier.alignBy(alignmentLine = FirstBaseline),
                color = MaterialTheme.colors.onBackground,
                style = TextStyle(fontWeight = FontWeight.Bold)
            )
            Text(
                text = value.getPropertyName(),
                fontSize = valueTextSize,
                modifier = Modifier.alignBy(alignmentLine = FirstBaseline),
                color = MaterialTheme.colors.onBackground
            )
        }
    }

    @Composable
    fun OneOnlyNumeralParameterLayout(property: ProductProperty, value: NumeralValue) {
        if (value.minValue == value.maxValue) {
            Row(modifier = Modifier.padding(start = categoriesStartPadding)) {
                Text(
                    text = "${property.name.getText()} : ",
                    fontSize = propertyTextSize,
                    modifier = Modifier.alignBy(alignmentLine = FirstBaseline),
                    color = MaterialTheme.colors.onBackground,
                    style = TextStyle(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = value.getPropertyName(),
                    fontSize = valueTextSize,
                    modifier = Modifier.alignBy(alignmentLine = FirstBaseline),
                    color = MaterialTheme.colors.onBackground
                )
            }
        } else {
            Column() {
                Row(modifier = Modifier.padding(start = categoriesStartPadding)) {
                    Text(
                        text = "${property.name.getText()} : ",
                        fontSize = propertyTextSize,
                        modifier = Modifier.alignBy(alignmentLine = FirstBaseline),
                        color = MaterialTheme.colors.onBackground,
                        style = TextStyle(fontWeight = FontWeight.Bold)
                    )
                    Row {
                        val number = remember { mutableStateOf(value.value.toString()) }
                        AndroidView(factory = {
                            val text = EditText(it)
                            text.layoutParams = ViewGroup.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
                            text.textSize = 16f
                            text.setText(number.value)
                            text.inputType = EditorInfo.TYPE_CLASS_NUMBER
                            text.addTextChangedListener {
                                try {
                                    value.value = it.toString().toInt()
                                    viewModel.choosePropertyValueWithoutUpdate(property, value)
                                } catch (e: NumberFormatException) {
                                    value.value = value.minValue
                                }
                                text.error = try {
                                    val temp = it.toString().trim().toInt()
                                    if (!(temp >= value.minValue && temp <= value.maxValue)) {
                                        getString(R.string.value_not_in_range)
                                    } else {
                                        null
                                    }
                                } catch (e: NumberFormatException) {
                                    getString(R.string.value_not_in_range)
                                }
                            }
                            text
                        })
                        Text(
                            text = "${value.unit.getText()} ${
                            getString(
                                R.string.from_to,
                                value.minValue.toString(),
                                value.maxValue.toString()
                            )
                            }",
                            fontSize = valueTextSize,
                            modifier = Modifier.alignBy(alignmentLine = FirstBaseline),
                            color = MaterialTheme.colors.onBackground
                        )
                    }
                }
            }
        }
    }

    @ExperimentalFoundationApi
    @Composable
    fun MultipleParameterLayout(
        property: ProductProperty,
        values: List<PropertyValue>
    ) {
        val configuration = viewModel.configuration.value
        Column {
            Spacer(modifier = Modifier.padding(7.dp))
            Text(
                text = property.name.getText() + " : ",
                fontSize = propertyTextSize,
                color = MaterialTheme.colors.onBackground,
                modifier = Modifier.padding(start = categoriesStartPadding),
                style = TextStyle(fontWeight = FontWeight.Bold)
            )
            VerticalGrid(
                colNumber = 2,
                items = values,
                modifier = Modifier.fillMaxWidth(1f)
            ) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(5.dp)
                        .clickable(onClick = {
                            viewModel.choosePropertyValue(
                                property,
                                item
                            )
                        }),
                    elevation = 7.dp,
                    backgroundColor = if (configuration.set[property.id]?.id ?: "" == item.id) {
                        MaterialTheme.colors.primary
                    } else MaterialTheme.colors.surface
                ) {
                    Column {

                        if (item is NamedValue && item.image != "") {
                            val image = ImageUtils.loadPicture(
                                url = item.image,
                                defaultImage = R.drawable.ic_launcher_background
                            ).value
                            image?.let {
                                Image(
                                    bitmap = image.asImageBitmap(),
                                    modifier = Modifier.fillMaxWidth(1f),
                                    contentScale = ContentScale.FillWidth,
                                    contentDescription = null
                                )
                            }
                        }
                        Text(
                            text = item.getPropertyName(),
                            Modifier
                                .fillMaxWidth(1f)
                                .padding(7.dp),
                            fontSize = valueTextSize,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colors.onBackground
                        )
                    }
                }
            }
        }
    }
}
