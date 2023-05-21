package com.delitx.furnituremaster.ui.util

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Composable
fun DefaultSnackbar(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colors.background
) {
    SnackbarHost(
        hostState = snackbarHostState,
        modifier = modifier,
        snackbar = {
            Snackbar(backgroundColor = backgroundColor, contentColor = MaterialTheme.colors.onBackground, modifier = Modifier.padding(10.dp)) {
                Text(text = it.message, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth(1f), fontSize = 20.sp)
            }
        }
    )
}
class SnackbarController(private val scope: CoroutineScope) {
    private var snackbarJob: Job? = null
    init {
        cancelJob()
    }
    fun showSnackbar(
        snackbarHostState: SnackbarHostState,
        message: String
    ) {
        if (snackbarJob == null) {
            snackbarJob = scope.launch {
                snackbarHostState.showSnackbar(message = message)
                cancelJob()
            }
        } else {
            cancelJob()
            snackbarJob = scope.launch {
                snackbarHostState.showSnackbar(message = message)
                cancelJob()
            }
        }
    }
    private fun cancelJob() {
        snackbarJob?.let {
            it.cancel()
            snackbarJob = Job()
        }
    }
}
