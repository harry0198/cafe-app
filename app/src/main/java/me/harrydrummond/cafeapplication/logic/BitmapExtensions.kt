package me.harrydrummond.cafeapplication.logic

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream

/**
 * Converts a byte array to a Bitmap
 * Compresses image so that it is a sensible size and also does not exceed bounds.
 *
 * @see ByteArray
 * @see Bitmap
 */
fun ByteArray.toBitmap(width: Int, height: Int): Bitmap {
    val opt = BitmapFactory.Options()
    opt.inSampleSize = calculateInSampleSize(opt, width, height)
    return BitmapFactory.decodeByteArray(this, 0, this.size, opt)
}

/**
 * Converts a Bitmap to a byte array
 *
 * @see ByteArray
 * @see Bitmap
 */
fun Bitmap.toByteArray(): ByteArray {
    val stream = ByteArrayOutputStream()
    getResizedBitmap(this, maxSize = 400)?.compress(Bitmap.CompressFormat.WEBP_LOSSY, 50, stream)
    return stream.toByteArray()
}

/**
 * https://stackoverflow.com/questions/8417034/how-to-make-bitmap-compress-without-change-the-bitmap-size
 * reduces the size of the image
 * @param image
 * @param maxSize
 * @return
 */
fun getResizedBitmap(image: Bitmap, maxSize: Int): Bitmap? {
    var width = image.width
    var height = image.height
    val bitmapRatio = width.toFloat() / height.toFloat()
    if (bitmapRatio > 1) {
        width = maxSize
        height = (width / bitmapRatio).toInt()
    } else {
        height = maxSize
        width = (height * bitmapRatio).toInt()
    }
    return Bitmap.createScaledBitmap(image, width, height, true)
}

// Taken from https://developer.android.com/topic/performance/graphics/load-bitmap.html#load-bitmap
fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
    // Raw height and width of image
    val (height: Int, width: Int) = options.run { outHeight to outWidth }
    var inSampleSize = 1

    if (height > reqHeight || width > reqWidth) {

        val halfHeight: Int = height / 2
        val halfWidth: Int = width / 2

        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
        // height and width larger than the requested height and width.
        while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
            inSampleSize *= 2
        }
    }

    return inSampleSize
}