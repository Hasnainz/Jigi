package com.example.jigi.utils

import android.util.Log
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import com.example.jigi.ui.searchPage.Line

class CharacterRecognition(canvasSize: IntSize) {


    private val grid = Array(canvasSize.width) { ByteArray(canvasSize.height) }

    private fun drawLine(x0: Int, x1: Int, y0: Int, y1: Int) {
        val dx = x1 - x0
        val dy = y1 - y0
        var d = 2 * dy - dx
        var y = y0
        for (x in x0..x1) {
            grid[x][y] = 1
            if (d > 0) {
                y += 1
                d -= 2 * dx
            }
            d += 2 * dy
        }

    }


    private fun drawLines(lines: SnapshotStateList<Line>) {
        for (line in lines) {
            drawLine(
                line.start.x.toInt(),
                line.end.x.toInt(),
                line.start.y.toInt(),
                line.end.y.toInt())
        }
    }

    fun recognise(lines: SnapshotStateList<Line>) : List<String> {
        drawLines(lines)

        return listOf("観")
    }
}