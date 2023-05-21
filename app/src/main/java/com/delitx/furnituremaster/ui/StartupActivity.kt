package com.delitx.furnituremaster.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.delitx.furnituremaster.R
import com.delitx.furnituremaster.data.ErrorStates
import com.delitx.furnituremaster.data.ProductsRepository
import com.delitx.furnituremaster.ui.theme.FurnitureMasterTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StartupActivity : AppCompatActivity() {
    @Inject
    lateinit var repository: ProductsRepository

    private val mIsShowUpdateDialog = mutableStateOf(false)
    private val mIsInternetConnectionAvailable = mutableStateOf(true)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
            ComposeView(this).apply {
                setContent {
                    FurnitureMasterTheme(isDarkTheme = isSystemInDarkTheme()) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center
                        ) {
                            if (!mIsInternetConnectionAvailable.value) {
                                Text(
                                    getString(R.string.internet_connection_unavailable),
                                    modifier = Modifier.padding(10.dp)
                                )
                            }
                        }
                        if (mIsShowUpdateDialog.value) {
                            UpdateVersionDialog()
                        }
                    }
                }
            }
        )
        repository.errorStates.observe(this) {
            when (it) {
                ErrorStates.VersionIncorrect -> {
                    mIsShowUpdateDialog.value = true
                }
                ErrorStates.InternetConnectionError -> {
                    mIsInternetConnectionAvailable.value = false
                }
                ErrorStates.AllRight -> {
                }
            }
        }

        repository.isDataUpdated.observe(this) {
            if (it) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    @Composable
    private fun UpdateVersionDialog() {
        Dialog(onDismissRequest = {
            closeApp()
        }) {
            Card(modifier = Modifier.fillMaxWidth(1f)) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(7.dp)
                ) {
                    Text(
                        text = getString(R.string.need_to_update),
                        modifier = Modifier.fillMaxWidth(1f),
                        textAlign = TextAlign.Center
                    )
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth(1f)
                    ) {
                        Button(onClick = { closeApp() }) {
                            Text(text = getString(R.string.cancel))
                        }
                        Button(onClick = {
                            openPlayMarket()
                            closeApp()
                        }) {
                            Text(text = getString(R.string.ok))
                        }
                    }
                }
            }
        }
    }

    private fun closeApp() {
        finish()
    }

    private fun openPlayMarket() {
        val packageName = packageName
        try {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=$packageName")
                )
            )
        } catch (e: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                )
            )
        }
    }
}
