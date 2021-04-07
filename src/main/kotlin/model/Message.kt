package model

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

class Word(
    val text: String,
    val style: SpanStyle
)

class Message {
    val words = arrayListOf<Word>()

    fun word(text: String) : Message {
        words.add(Word(text = text, style = SpanStyle()))
        return this
    }

    fun word(text: String, style: SpanStyle) : Message {
        words.add(Word(text = text, style = style))
        return this
    }

    @Composable
    fun toText() {
        Text(
            buildAnnotatedString {
                words.forEach {
                    withStyle(style = it.style) {
                        append(it.text)
                    }
                }
            }
        )
    }
}