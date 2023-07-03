package com.example.weather.presentation.base

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.weather.domain.exception.BaseException
import com.example.weather.domain.model.Dialog
import com.example.weather.domain.model.Redirect
import com.example.weather.domain.model.Tag
import com.example.weather.presentation.ui.custom.FullScreenLoading

@Composable
fun ExceptionHandleView(
    modifier: Modifier = Modifier,
    state: ViewState,
    snackBarHostState: SnackbarHostState,
    contentCleared: Boolean = false,
    positiveAction: ((Int?, Any?) -> Unit)? = null,
    negativeAction: ((Int?, Any?) -> Unit)? = null,
    inlineActions: ((List<Tag>) -> Unit)? = null,
    redirectAction: ((Redirect) -> Unit)? = null,
    content: @Composable (ViewState) -> Unit
) {
    when {
        state.isLoading -> FullScreenLoading()
        state.exception != null -> ShowError(
            modifier = modifier,
            state = state,
            hostState = snackBarHostState,
            contentCleared = contentCleared,
            positiveAction = positiveAction,
            negativeAction = negativeAction,
            inlineActions = inlineActions,
            redirectAction = redirectAction,
            content = content
        )
        else -> content(state)
    }
}

@Composable
fun ShowError(
    modifier: Modifier = Modifier,
    state: ViewState,
    hostState: SnackbarHostState,
    contentCleared: Boolean = false,
    positiveAction: ((Int?, Any?) -> Unit)? = null,
    negativeAction: ((Int?, Any?) -> Unit)? = null,
    inlineActions: ((List<Tag>) -> Unit)? = null,
    redirectAction: ((Redirect) -> Unit)? = null,
    content: @Composable (ViewState) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        if (!contentCleared) content(state)

        when (state.exception) {
            is BaseException.OnPageException ->
                ShowOnPageException(onPage = state.exception as BaseException.OnPageException)

            is BaseException.AlertException -> {
                var instanceHashCode by remember { mutableStateOf("") }
                if (instanceHashCode != state.exception?.hashCode) {
                    ShowAlertDialog(dialog = state.exception as BaseException.AlertException) {
                        instanceHashCode = state.exception?.hashCode ?: ""
                    }
                }
                Spacer(modifier = Modifier)
            }

            is BaseException.ToastException -> {
                ShowToast(toast = state.exception as BaseException.ToastException)
                Spacer(modifier = Modifier)
            }

            is BaseException.DialogException -> {
                var instanceHashCode by remember { mutableStateOf("") }
                if (instanceHashCode != state.exception?.hashCode) {
                    ShowDialog(
                        dialog = (state.exception as BaseException.DialogException).dialog,
                        positiveAction = positiveAction,
                        negativeAction = negativeAction
                    ) {
                        instanceHashCode = state.exception?.hashCode ?: ""
                    }
                }
                Spacer(modifier = Modifier)
            }

            is BaseException.SnackBarException -> {
                ShowSnackBar(
                    hostState = hostState,
                    message = (state.exception as BaseException.SnackBarException).message
                )
                Spacer(modifier = Modifier)
            }

            is BaseException.InlineException -> {
                inlineActions?.invoke((state.exception as BaseException.InlineException).tags)
                Spacer(modifier = Modifier)
            }

            is BaseException.RedirectException -> {
                redirectAction?.invoke((state.exception as BaseException.RedirectException).redirect)
                Spacer(modifier = Modifier)
            }
        }
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
fun ShowAlertDialog(
    dialog: BaseException.AlertException,
    onDismiss: () -> Unit
) {
    AlertDialog(
        modifier = Modifier.padding(20.dp),
        onDismissRequest = { onDismiss.invoke() },
        title = dialog.title?.let { { Text(text = dialog.title, color = MaterialTheme.colors.onSecondary) } },
        text = {
            Text(
                text = dialog.message,
                style = MaterialTheme.typography.body2
            )
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = stringResource(id = android.R.string.ok),
                    style = MaterialTheme.typography.button,
                    color = MaterialTheme.colors.primary
                )
            }
        }
    )
}

@Composable
fun ShowToast(toast: BaseException.ToastException) {
    Spacer(modifier = Modifier)
    Toast.makeText(LocalContext.current, toast.message, Toast.LENGTH_LONG).show()
}

@Composable
fun ShowDialog(
    dialog: Dialog,
    positiveAction: ((Int?, Any?) -> Unit)? = null,
    negativeAction: ((Int?, Any?) -> Unit)? = null,
    onDismiss: () -> Unit
) {
    AlertDialog(
        modifier = Modifier.padding(20.dp),
        onDismissRequest = { onDismiss.invoke() },
        title = dialog.title?.let { { Text(text = dialog.title) } },
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
                        .clickable {
                            positiveAction?.invoke(dialog.positiveAction, dialog.positiveObject)
                            onDismiss.invoke()
                        }
                )
            }
        },
        dismissButton = dialog.negativeMessage?.let {
            {
                Text(
                    text = dialog.negativeMessage,
                    style = MaterialTheme.typography.button,
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier
                        .padding(15.dp)
                        .clickable {
                            negativeAction?.invoke(dialog.positiveAction, dialog.positiveObject)
                            onDismiss.invoke()
                        }
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
