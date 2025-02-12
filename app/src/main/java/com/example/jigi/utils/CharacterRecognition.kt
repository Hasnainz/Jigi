package com.example.jigi.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color.rgb
import android.graphics.Paint
import android.util.Log
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.unit.IntSize
import com.example.jigi.ui.searchPage.Line
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.common.ops.CastOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.image.ops.TransformToGrayscaleOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.PriorityQueue


class CharacterRecognition(canvasSize: IntSize) {


    private val h = canvasSize.height
    private val w = canvasSize.width
    private val grid = Array(h) { Array(w) { rgb(255, 255, 255) } }

    private lateinit var interpreter: Interpreter
    private var inputImageWidth: Int = 0
    private var inputImageHeight: Int = 0
    private var modelInputSize: Int = 0

    private val FLOAT_TYPE_SIZE = 4
    private val PIXEL_SIZE = 1
    private val OUTPUT_CLASSES_COUNT = labelsMap.size

    fun createInterpreter(context: Context) {
        val tfLiteOptions = Interpreter.Options()//can be configure to use GPUDelegate
        interpreter = Interpreter(FileUtil.loadMappedFile(context, "model.tflite"), tfLiteOptions)


        val inputShape = interpreter.getInputTensor(0).shape()

        inputImageWidth = inputShape[1]
        inputImageHeight = inputShape[2]
//        modelInputSize = FLOAT_TYPE_SIZE * inputImageWidth * inputImageHeight * PIXEL_SIZE

    }


    private fun getKLargestIndexes(probabilities: FloatArray, k: Int): List<Int> {

        val heap = PriorityQueue<Pair<Int, Float>>(k) {
            p1, p2 ->
            p1.second.compareTo(p2.second)
        }

        for (i in probabilities.indices) {
            heap += Pair(i, probabilities[i])
            if (heap.size > k) {
                heap.remove()
            }
        }

        return (heap.sortedWith() { p1, p2 -> p2.second.compareTo(p1.second) }).map { it.first }

    }


    private fun runInference(bitmap: Bitmap): List<String> {
        val imageProcessor = ImageProcessor.Builder()
                .add(ResizeOp(inputImageHeight, inputImageWidth, ResizeOp.ResizeMethod.BILINEAR))
                .add(TransformToGrayscaleOp())
                .add(CastOp(DataType.FLOAT32))
                .build()


        var tensorImage = TensorImage(DataType.FLOAT32)

        tensorImage.load(bitmap)
        tensorImage = imageProcessor.process(tensorImage)

        val output = Array(1) {
            FloatArray(OUTPUT_CLASSES_COUNT)
        }

        val probabilityBuffer = TensorBuffer.createFixedSize(intArrayOf(1, OUTPUT_CLASSES_COUNT), DataType.FLOAT32)

        // Run inference with the input data.
        interpreter.run(tensorImage.buffer, probabilityBuffer.buffer)

//        val o = output
        val o = probabilityBuffer.floatArray
        val p = o.maxOrNull()
        val i = o.indexOfFirst {
            if (p != null) {
                it >= p
            }
            else {
                false
            }
        }
        Log.d("DOGE", p.toString())
        Log.d("DOGE", i.toString())
        labelsMap[i]?.let { Log.d("DOGE", it) }

        val indexes = getKLargestIndexes(o, 10)

        return indexes.map { labelsMap[it].toString() }


    }


    private fun convertBitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        val byteBuffer = ByteBuffer.allocateDirect(modelInputSize).order(ByteOrder.nativeOrder())

        val pixels = IntArray(inputImageWidth * inputImageHeight)
        bitmap.getPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

        for (pixelValue in pixels) {
            val r = (pixelValue shr 16 and 0xFF)
            val g = (pixelValue shr 8 and 0xFF)
            val b = (pixelValue and 0xFF)

            // Convert RGB to grayscale and normalize pixel value to [0..1].
            val normalizedPixelValue = (r + g + b) / 3.0f
            byteBuffer.putFloat(normalizedPixelValue)
        }

        return byteBuffer

    }

    fun recognise(lines: SnapshotStateList<Line>): List<String> {
        val mybitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(mybitmap)
        canvas.drawColor(rgb(255,255,255))
        val paint = Paint()
        paint.strokeWidth = 10f
        paint.isAntiAlias = true
        lines.forEach{
            canvas.drawLine(it.start.x, it.start.y, it.end.x, it.end.y, paint)
        }



        return runInference(mybitmap)
    }
}