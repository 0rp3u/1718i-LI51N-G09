package pdm_1718i.yamda.data.server

/**
 * Created by orpheu on 10/25/17.
 */

import com.android.volley.toolbox.ImageLoader.ImageCache

import android.graphics.Bitmap
import android.support.v4.util.LruCache

class BitmapLruCache  : LruCache<String, Bitmap>(defaultLruCacheSize), ImageCache {

    override fun sizeOf(key: String?, value: Bitmap): Int {
        return value.rowBytes * value.height / 1024
    }

    override fun getBitmap(url: String): Bitmap? {
        return get(url)
    }

    override fun putBitmap(url: String, bitmap: Bitmap) {
        put(url, bitmap)
    }

    companion object {

        val defaultLruCacheSize: Int
            get() {
                val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()

                return maxMemory / 8
            }
    }
}