package pdm_1718i.yamda.extensions

import android.graphics.Bitmap
import java.nio.ByteBuffer

/**
 * Created by orpheu on 1/12/18.
 */


fun Bitmap.convertToByteArray(): ByteArray {
    //minimum number of bytes that can be used to store this bitmap's pixels
    val size = this.byteCount

    //allocate new instances which will hold bitmap
    val buffer = ByteBuffer.allocate(size)
    val bytes = ByteArray(size)

    //copy the bitmap's pixels into the specified buffer
    this.copyPixelsToBuffer(buffer)

    //rewinds buffer (buffer position is set to zero and the mark is discarded)
    buffer.rewind()

    //transfer bytes from buffer into the given destination array
    buffer.get(bytes)

    //return bitmap's pixels
    return bytes
}