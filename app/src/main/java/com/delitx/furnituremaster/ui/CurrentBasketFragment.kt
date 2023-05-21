package com.delitx.furnituremaster.ui

import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.delitx.furnituremaster.R
import com.delitx.furnituremaster.data_models.Product
import com.delitx.furnituremaster.data_models.ProductPropertiesSet
import com.delitx.furnituremaster.ui.theme.FurnitureMasterTheme
import com.delitx.furnituremaster.ui.util.ImageUtils
import com.delitx.furnituremaster.ui.util.ImageUtils.loadDrawable
import com.delitx.furnituremaster.view_models.CurrentBasketViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.lang.NumberFormatException

@AndroidEntryPoint
class CurrentBasketFragment : Fragment() {
    val viewModel: CurrentBasketViewModel by viewModels()
    val showPopup = mutableStateOf(false)

    @ExperimentalAnimationApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        arguments?.let {
            val values = CurrentBasketFragmentArgs.fromBundle(it)
            val isCurrent = values.isCurrent
            if (!isCurrent) {
                viewModel.setNotCurrent(values.id)
            }
        }
        return ComposeView(requireContext()).apply {
            setContent {
                FurnitureMasterTheme(isDarkTheme = isSystemInDarkTheme()) {
                    SetupView()
                }
            }
        }
    }

    fun sendEmail(name: String, phoneNumber: String, comment: String) {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.setData(Uri.parse("mailto:"))
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("example@google.com"))
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.order))
        var message =
            "${getString(R.string.name)} : $name\n${getString(R.string.phone_number)} : $phoneNumber\n ${
            getString(R.string.comment)
            } : $comment\n\n"
        for (i in viewModel.currentBasket.value.products) {
            message += i.name.getText() + "\n" + getString(R.string.amount) + " : " + i.amount + "\n" + i.configurationToPresentableString() + "\n\n"
        }
        intent.putExtra(Intent.EXTRA_TEXT, message)
        try {
            startActivity(
                Intent.createChooser(
                    intent,
                    getString(R.string.send_email)
                )
            )
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                requireContext(),
                getString(R.string.no_email_installed),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    val startPadding = 15.dp

    @ExperimentalAnimationApi
    @Preview
    @Composable
    fun SetupView() {
        val basket = viewModel.currentBasket.value
        Scaffold(bottomBar = { BottomBar() }) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(bottom = it.calculateBottomPadding())
            ) {
                Text(text = basket.name, modifier = Modifier.padding(start = startPadding))
                Text(text = basket.phoneNumber, modifier = Modifier.padding(start = startPadding))
                Text(text = basket.comment, modifier = Modifier.padding(start = startPadding))
                Spacer(modifier = Modifier.padding(10.dp))
                if (basket.products.isNotEmpty()) {
                    LazyColumn(modifier = Modifier.fillMaxHeight(1f)) {
                        itemsIndexed(basket.products) { index, product ->
                            ProductItem(product = product, index)
                            Spacer(modifier = Modifier.padding(7.dp))
                        }
                    }
                    if (showPopup.value) {
                        SaveDialog()
                    }
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxSize(1f)) {
                        Text(text = getString(R.string.cart_empty), textAlign = TextAlign.Center)
                        val image = loadDrawable(R.drawable.cart).value
                        image?.let {
                            Image(image.asImageBitmap(), null)
                        }
                        Button(onClick = {
                            val action = CurrentBasketFragmentDirections.actionNavigationCurrentBasketToNavigationCatalogue()
                            Navigation.findNavController(requireView()).navigate(action)
                        }) {
                            Text(text = getString(R.string.go_to_catalogue), fontSize = 25.sp)
                        }
                    }
                }
            }
        }
    }

    @ExperimentalAnimationApi
    @Composable
    fun ProductItem(product: Product, position: Int) {
        AnimatedVisibility(
            visible = !viewModel.deletedProducts.contains(product),
            enter = slideIn(
                initialOffset = { fullSize -> IntOffset(0, fullSize.height) },
                animationSpec = tween(500)
            ),
            exit = slideOut(
                targetOffset = { fullSize -> IntOffset(-fullSize.width, 0) },
                animationSpec = tween(1000)
            ),
            modifier = Modifier.fillMaxWidth(1f)
        ) {

            Card(
                elevation = 12.dp,
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .clickable(onClick = {
                        openConcreteProduct(product.id, product.chosenConfiguration!!, viewModel.currentBasket.value.id)
                    })
                    .fillMaxWidth(1f)
            ) {
                Row {
                    val image = ImageUtils.loadPicture(
                        url = product.imageUrl[0].link,
                        defaultImage = R.drawable.ic_launcher_background
                    ).value
                    image?.let {
                        Image(
                            bitmap = image.asImageBitmap(),
                            modifier = Modifier
                                .fillMaxWidth(0.3f),
                            contentScale = ContentScale.FillWidth,
                            contentDescription = null
                        )
                    }
                    Column(modifier = Modifier.fillMaxWidth(1f)) {
                        Text(text = product.name.getText())
                        Text(text = product.configurationToPresentableString())
                        val price = viewModel.calculatePrice(
                            product,
                            getString(R.string.configuration_not_chosen)
                        ).value
                        Text(text = "${getString(R.string.price)} : $price")
                        val productsAmount =
                            remember { mutableStateOf(product.amount) }
                        Row {
                            Text(text = "${getString(R.string.amount)} : ")
                            Card(
                                modifier = Modifier.clickable {
                                    if (productsAmount.value > 1) {
                                        productsAmount.value--
                                        viewModel.changeProductAmount(product, productsAmount.value)
                                    }
                                },
                                elevation = 7.dp
                            ) {
                                Text(
                                    text = "-",
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(15.dp)
                                )
                            }

                            Text(text = productsAmount.value.toString())
                            Card(
                                modifier = Modifier.clickable {
                                    productsAmount.value++
                                    viewModel.changeProductAmount(product, productsAmount.value)
                                },
                                elevation = 7.dp
                            ) {
                                Text(
                                    text = "+",
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(15.dp)
                                )
                            }
                        }
                        Text(
                            text = "${getString(R.string.price_total)} : ${
                            try {
                                "%.2f".format(price.toDouble() * productsAmount.value)
                                    .replace(',', '.')
                            } catch (e: NumberFormatException) {
                                Log.d("priceCountBug", "ProductItem: ${e.message}")
                                price
                            }
                            }"
                        )
                        val deleteImage =
                            ImageUtils.loadDrawable(id = if (isSystemInDarkTheme()) R.drawable.ic_delete_dark else R.drawable.ic_delete_light).value
                        deleteImage?.let {
                            Image(
                                bitmap = deleteImage.asImageBitmap(),
                                modifier = Modifier
                                    .clickable {
                                        viewModel.deleteProduct(
                                            product,
                                            position
                                        )
                                    }
                                    .align(Alignment.End)
                                    .padding(horizontal = 10.dp),
                                contentDescription = null
                            )
                        }
                        Spacer(modifier = Modifier.padding(7.dp))
                    }
                }
            }
        }
    }

    @Composable
    fun BottomBar() {
        Row(modifier = Modifier.fillMaxWidth(1f)) {
            if (viewModel.isCurrent.value) {
                Button(
                    onClick = {
                        dialog { name: String, phoneNumber: String, comment: String ->
                            val tempBasket = viewModel.currentBasket.value
                            tempBasket.comment = comment
                            tempBasket.name = name
                            tempBasket.phoneNumber = phoneNumber
                            viewModel.saveBasket()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(0.5f)
                ) {
                    Text(
                        text = getString(R.string.save),
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .padding(5.dp),
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                Button(
                    onClick = {
                        androidx.appcompat.app.AlertDialog.Builder(requireContext())
                            .setPositiveButton(getString(R.string.ok)) { dialogInterface: DialogInterface, i: Int ->
                                viewModel.setCurrent(viewModel.id.value)
                            }
                            .setNegativeButton(getString(R.string.cancel)) { dialogInterface: DialogInterface, i: Int ->
                            }.setMessage(getString(R.string.delete_current)).show()
                    },
                    modifier = Modifier.fillMaxWidth(0.5f)
                ) {
                    Text(
                        text = getString(R.string.set_current),
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .padding(5.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
            Button(onClick = {
                dialog(
                    onSucceed = { name: String, phoneNumber: String, comment: String ->
                        sendEmail(name, phoneNumber, comment)
                    }
                )
            }, modifier = Modifier.fillMaxWidth(1f)) {
                Text(
                    text = getString(R.string.to_order),
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(5.dp),
                    textAlign = TextAlign.Center
                )
            }
            }
        }

        fun dialog(onSucceed: (name: String, phoneNumber: String, comment: String) -> Unit) {
            val basket = viewModel.currentBasket.value
            fragmentManager?.let {
                SaveDialogFragment(
                    basket.name,
                    basket.phoneNumber,
                    basket.comment,
                    onSucceed
                ).show(it, null)
            }
        }

        @Composable
        fun SaveDialog() {
            if (showPopup.value) {
                val basket = viewModel.currentBasket.value
                val phoneNumber = remember { mutableStateOf(basket.phoneNumber) }
                val name = remember { mutableStateOf(basket.name) }
                val comment = remember { mutableStateOf(basket.comment) }
                val nameFocusRequester = FocusRequester()
                val nameFocusModifier = Modifier.focusModifier()
                val phoneNumberFocusRequester = FocusRequester()
                val phoneNumberFocusModifier = Modifier.focusModifier()
                val commentFocusRequester = FocusRequester()
                val commentFocusModifier = Modifier.focusModifier()
                val saveAction = {
                    if (name.value == "") {
                        nameFocusRequester.requestFocus()
                    } else if (phoneNumber.value == "") {
                        phoneNumberFocusRequester.requestFocus()
                    } else {
                        val tempBasket = viewModel.currentBasket.value
                        tempBasket.comment = comment.value
                        tempBasket.name = name.value
                        tempBasket.phoneNumber = phoneNumber.value
                        viewModel.saveBasket()
                        showPopup.value = false
                    }
                }
                AlertDialog(
                    onDismissRequest = { showPopup.value = false },
                    text = {
                        Column {
                            TextField(
                                value = name.value,
                                onValueChange = { name.value = it },
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                                placeholder = { Text(text = "Name") },
                                keyboardActions = KeyboardActions(onNext = {
                                    phoneNumberFocusRequester.requestFocus()
                                }),
                                modifier = nameFocusModifier.then(
                                    Modifier.focusRequester(
                                        nameFocusRequester
                                    )
                                )
                            )
                            TextField(
                                value = phoneNumber.value,
                                onValueChange = { phoneNumber.value = it },
                                keyboardOptions = KeyboardOptions(
                                    imeAction = ImeAction.Next,
                                    keyboardType = KeyboardType.Number
                                ),
                                placeholder = { Text(text = getString(R.string.phone_number)) },
                                modifier = phoneNumberFocusModifier.then(
                                    Modifier.focusRequester(
                                        phoneNumberFocusRequester
                                    )
                                ),
                                keyboardActions = KeyboardActions(onNext = {
                                    commentFocusRequester.requestFocus()
                                })
                            )
                            TextField(
                                value = comment.value,
                                onValueChange = { comment.value = it },
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                                placeholder = { Text(text = getString(R.string.comment)) },
                                keyboardActions = KeyboardActions(onDone = {
                                    saveAction()
                                    defaultKeyboardAction(ImeAction.Done)
                                }),
                                modifier = commentFocusModifier.then(
                                    Modifier.focusRequester(
                                        commentFocusRequester
                                    )
                                ),
                            )
                        }
                    }, confirmButton = {
                    Button(onClick = {
                        saveAction()
                    }) {
                        Text(text = getString(R.string.ok), textAlign = TextAlign.Center)
                    }
                }, dismissButton = {
                    Button(onClick = { showPopup.value = false }) {
                        Text(text = getString(R.string.cancel), textAlign = TextAlign.Center)
                    }
                }
                )
            }
        }

        private fun openConcreteProduct(productId: Int, configuration: ProductPropertiesSet, basketId: Int) {
            val action =
                CurrentBasketFragmentDirections.actionNavigationCurrentBasketToNavigationConcreteProduct(
                    productId,
                    configuration.toString(),
                    basketId
                )
            Navigation.findNavController(this.requireView()).navigate(action)
        }
    }
    