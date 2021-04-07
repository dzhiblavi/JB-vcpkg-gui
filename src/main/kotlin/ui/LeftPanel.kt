package ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import model.VcpkgModel
import util.Config

@Composable
fun removeButton(model: VcpkgModel) {
    Button(
        enabled = !model.isRunning.value,
        onClick = { model.remove() },
        modifier = Config.BUTTON_MODIFIER
    ) {
        Text("Remove")
    }
}

@Composable
fun installButton(model: VcpkgModel) {
    Button(
        enabled = !model.isRunning.value,
        onClick = { model.install() },
        modifier = Config.BUTTON_MODIFIER
    ) {
        Text("Install")
    }
}

@Composable
fun searchButton(model: VcpkgModel) {
    var text by remember { mutableStateOf("") }

    Row {
        TextField(
            modifier = Modifier
                .clip(Config.CORNER_SHAPE)
                .border(Config.BORDER_STROKE)
                .padding(Config.DEFAULT_PADDING),
            value = (if (model.isRunning.value) "" else text),
            onValueChange = { if (!model.isRunning.value) text = it },
            singleLine = true
        )

        Button(
            enabled = !model.isRunning.value,
            onClick = { model.search(text) },
            modifier = Config.BUTTON_MODIFIER.align(Alignment.CenterVertically)
        ) {
            Text("Search")
        }
    }
}

@Composable
fun cancelButton(model: VcpkgModel) {
    Button(
        enabled = model.isRunning.value,
        onClick = { model.cancel() },
        modifier = Config.BUTTON_MODIFIER
    ) {
        Text("Cancel")
    }
}

@Composable
fun refreshButton(model: VcpkgModel) {
    Button(
        enabled = !model.isRunning.value,
        onClick = { model.update() },
        modifier = Config.BUTTON_MODIFIER
    ) {
        Text("Refresh packages list")
    }
}

@Composable
fun leftPanel(model: VcpkgModel) {
    Column(modifier = Modifier
        .padding(Config.DEFAULT_PADDING)
        .clip(Config.CORNER_SHAPE)
        .background(Config.COLORS.background)
        .border(Config.BORDER_STROKE)
        .width(420.dp)
        .fillMaxHeight()
    ) {
        Box(
            modifier = Modifier
                .padding(Config.DEFAULT_PADDING)
                .clip(Config.CORNER_SHAPE)
        ) {
            Column {
                refreshButton(model)
                searchButton(model)
                installButton(model)
                removeButton(model)
                cancelButton(model)
            }
        }

        val scrollState = rememberLazyListState()

        Box(
            modifier = Modifier
                .padding(Config.DEFAULT_PADDING)
                .clip(Config.CORNER_SHAPE)
                .background(Color.White)
        ) {
            Column(
                modifier = Modifier
                .padding(Config.DEFAULT_PADDING)
                .fillMaxWidth()
                .background(Config.COLORS.background)
            ) {
                Box(
                    modifier = Modifier
                    .clip(Config.CORNER_SHAPE)
                    .border(Config.BORDER_STROKE)
                    .padding(Config.DEFAULT_PADDING)
                    .fillMaxSize()
                ) {
                    LazyColumn(
                        state = scrollState
                    ) {
                        items(model.log) {
                            Text(it.message)
                        }
                    }
                }
            }
        }
    }
}
