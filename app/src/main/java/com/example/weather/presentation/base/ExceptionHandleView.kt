package com.example.weather.presentation.base

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.weather.domain.exception.BaseException
import com.example.weather.domain.model.Dialog
import com.example.weather.presentation.ui.custom.FullScreenLoading

@Composable
fun ExceptionHandleView(
    state: ViewState,
    snackBarHostState: SnackbarHostState,
    content: @Composable (ViewState) -> Unit
) {
    when {
        state.isLoading -> FullScreenLoading()
        state.exception != null -> when (state.exception) {
            is BaseException.OnPageException -> ShowOnPageException(onPage = state.exception as BaseException.OnPageException)
            is BaseException.AlertException -> ShowAlertDialog(dialog = state.exception as BaseException.AlertException)
            is BaseException.ToastException -> ShowToast(toast = state.exception as BaseException.ToastException)
            is BaseException.DialogException ->
                ShowDialog(dialog = (state.exception as BaseException.DialogException).dialog)
            is BaseException.SnackBarException ->
                ShowSnackBar(
                    hostState = snackBarHostState,
                    message = (state.exception as BaseException.SnackBarException).message
                )
        }
        else -> content(state)
    }
}

@Composable
fun ShowOnPageException(onPage: BaseException.OnPageException) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = onPage.message,
            style = MaterialTheme.typography.body1,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun ShowAlertDialog(dialog: BaseException.AlertException) {
    AlertDialog(
        modifier = Modifier.padding(20.dp),
        onDismissRequest = { },
        title = {
            dialog.title?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.body1
                )
            }
        },
        text = {
            Text(
                text = dialog.message,
                style = MaterialTheme.typography.body1
            )
        },
        confirmButton = {
            Text(
                text = stringResource(id = android.R.string.ok),
                style = MaterialTheme.typography.button,
                color = MaterialTheme.colors.primary,
                modifier = Modifier
                    .padding(15.dp)
                    .clickable { }
            )
        }
    )
}

@Composable
fun ShowToast(toast: BaseException.ToastException) {
    Spacer(modifier = Modifier)
    Toast.makeText(LocalContext.current, toast.message, Toast.LENGTH_LONG).show()
}

@Composable
fun ShowDialog(dialog: Dialog) {
    AlertDialog(
        modifier = Modifier.padding(20.dp),
        onDismissRequest = { },
        title = {
            dialog.title?.let { Text(text = dialog.title) }
        },
        text = {
            dialog.message?.let {
                Text(
                    text = dialog.message,
                    style = MaterialTheme.typography.body1
                )
            }
        },
        confirmButton = {
            dialog.positiveMessage?.let {
                Text(
                    text = dialog.positiveMessage,
                    style = MaterialTheme.typography.button,
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier
                        .padding(15.dp)
                        .clickable { }
                )
            }
        },
        dismissButton = {
            dialog.negativeMessage?.let {
                Text(
                    text = dialog.negativeMessage,
                    style = MaterialTheme.typography.button,
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier
                        .padding(15.dp)
                        .clickable { }
                )
            }
        }
    )
}

@Composable
fun ShowSnackBar(
    hostState: SnackbarHostState,
    message: String
) {
    LaunchedEffect(message) {
        hostState.showSnackbar(message = message)
    }
}