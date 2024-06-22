/*
 * Copyright (c) 2024.  Use under Apache Free License 2.0.
 */

@file:OptIn(ExperimentalSerializationApi::class)

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import kotlinx.serialization.ExperimentalSerializationApi

@Composable
fun MapView(state :LangState, setLastLanguage :(PLanguage?)->Unit) {
    var size by remember { mutableStateOf(IntSize(1200, 600)) }
    Box(modifier = Modifier.fillMaxSize().onSizeChanged { size = it }) {
        val dpSize = computeGeometry(size.width, size.height)
        val freeWidth = dpSize.width -dpSize.boxWidth;  val freeHeight = dpSize.height
        val numRows = kotlin.math.max(1, (freeHeight.div(dpSize.textHeight)/2).toInt()*2)
        val rowHeight = freeHeight/numRows
        var offsetX :Dp? = null
        val midY = freeHeight/2 - rowHeight/2
        val density = LocalDensity.current
        println("$numRows rows available on $size @${density} = ${dpSize}")
        val lastUsed = mutableMapOf<Int, Dp>()
        for (lang in state.history) {
            val col = computeColumn(lang.inception, freeWidth)
            var row = 255
            for (c in 0..numRows) {
                val candidate = if (c%2==0) c/2  else -(c+2)/2
                if ((lastUsed[candidate] ?: (-1).dp) < col) {
                    row = candidate
                    break
                }
            }
            if (row==255) {
                print('.')
                continue
            }
            if (offsetX==null) {
                offsetX = -col
                println("horizontal offset set to $offsetX")
            }
            LanguageButton(lang, setLastLanguage,
                modifier = Modifier.offset(offsetX+col, midY +rowHeight*row)
            )
            with(density) {
                lastUsed[row] = col + computeTextWitdth(lang.toString())
            }
        }
    }
}

fun computeTextWitdth(text :String) = (12*text.length +10).dp

fun computeColumn(inception :PartialDate, freeWidth :Dp) =
    max(0.dp, (freeWidth*inception.toDays())/HistoryManager.historyLength)

data class Geometry(val width :Dp, val height :Dp, val boxWidth :Dp, val textHeight :Dp) {}

@Composable
fun computeGeometry(width :Int, height :Int) :Geometry {
    val density = LocalDensity.current
    with(density) {
        return Geometry(width.toDp()-10.dp, height.toDp()-25.dp, 250.dp, 50.toDp())
    }
}
