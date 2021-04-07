package util

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.lightColors
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp

class Config {
    companion object {
        val DEFAULT_PADDING = 4.dp
        val COLORS = lightColors()
        val BORDER_STROKE = BorderStroke(2.dp, SolidColor(COLORS.onBackground))
        val CORNER_SHAPE = RoundedCornerShape(4.dp)
        val BUTTON_MODIFIER = Modifier
            .padding(DEFAULT_PADDING)
            .fillMaxWidth()
    }
}
