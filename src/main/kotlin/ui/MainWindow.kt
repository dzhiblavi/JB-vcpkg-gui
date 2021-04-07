package ui

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import model.VcpkgModel

@Composable
fun mainApp(model: VcpkgModel) {
    Row {
        leftPanel(model)
        rightPanel(model)
    }
}
