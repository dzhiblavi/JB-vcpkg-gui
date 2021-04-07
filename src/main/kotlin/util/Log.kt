package util

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import model.Message

class Log(private val log: (Message) -> Unit) {
    fun logStyled(message: String, style: SpanStyle) {
        log(
            Message()
                .word(
                    text = message,
                    style = style
                )
        )
    }

    fun logError(message: String) {
        logStyled(message, SpanStyle(
            color = Color.Red,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic
        ))
    }

    fun logPrimary(message: String) {
        logStyled(message, SpanStyle(
            fontWeight = FontWeight.Bold
        ))
    }

    fun logSecondary(message: String) {
        logStyled(message, SpanStyle())
    }
}