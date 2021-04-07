import androidx.compose.desktop.Window
import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.IntSize
import model.VcpkgModel
import ui.mainApp
import java.io.File

fun main() {
    return Window(
        title = "Vcpkg GUI",
        size = IntSize(1200, 900)
    ) {
        val model = remember { VcpkgModel(File("./test/vcpkg")) }
        MaterialTheme(
            colors = lightColors()
        ) {
            mainApp(model)
        }
    }
}
