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
import com.google.mlkit.vision.common.InputImage
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
    private val kanjiLabels = labels0 + labels1 + labels2


    private val h = canvasSize.height
    private val w = canvasSize.width

    private lateinit var interpreter: Interpreter
    private var inputImageWidth: Int = 64
    private var inputImageHeight: Int = 64

    private val OUTPUT_CLASSES_COUNT = kanjiLabels.size

    fun createInterpreter(context: Context) {
        val tfLiteOptions = Interpreter.Options()
        interpreter = Interpreter(FileUtil.loadMappedFile(context, "model.tflite"), tfLiteOptions)


        val inputShape = interpreter.getInputTensor(0).shape()

        inputImageWidth = inputShape[1]
        inputImageHeight = inputShape[2]
    }


    private fun getKLargestIndexes(probabilities: FloatArray, k: Int = 10): List<Int> {

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
//        heap.filter { it.second > 0.0005 }
        return ((heap.filter { it.second > 0.0005 }) .sortedWith() { p1, p2 -> p2.second.compareTo(p1.second) }).map { it.first }

    }


    private fun runInference(bitmap: Bitmap): List<String> {
        val imageProcessor = ImageProcessor.Builder()
//                .add(ResizeOp(64, 64, ResizeOp.ResizeMethod.BILINEAR))
                .add(TransformToGrayscaleOp())
                .add(CastOp(DataType.FLOAT32))
                .build()


        var tensorImage = TensorImage(DataType.FLOAT32)

        tensorImage.load(bitmap)
        tensorImage = imageProcessor.process(tensorImage)



        val probabilityBuffer = TensorBuffer.createFixedSize(intArrayOf(1, OUTPUT_CLASSES_COUNT), DataType.FLOAT32)

        // Run inference with the input data.
        interpreter.run(tensorImage.buffer, probabilityBuffer.buffer)


        val indexes = getKLargestIndexes(probabilityBuffer.floatArray, 10)


        return indexes.map { kanjiLabels[it].toString() }


    }



    fun recognise(lines: SnapshotStateList<Line>): List<String> {
        val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(rgb(255,255,255))
        val paint = Paint()
        paint.strokeWidth = 10f
        paint.isAntiAlias = true
        lines.forEach{
            canvas.drawLine(it.start.x, it.start.y, it.end.x, it.end.y, paint)
        }

        return runInference(Bitmap.createScaledBitmap(bitmap, inputImageWidth, inputImageHeight, true))
    }
}