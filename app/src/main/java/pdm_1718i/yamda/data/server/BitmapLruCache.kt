package pdm_1718i.yamda.data.server

/**
 * Created by orpheu on 10/25/17.
 */

import android.graphics.Bitmap
import android.support.v4.util.LruCache
import com.android.volley.toolbox.ImageLoader.ImageCache

class BitmapLruCache  : LruCache<String, Bitmap>(defaultLruCacheSize), ImageCache {

    override fun sizeOf(key: String?, value: Bitmap): Int {
        return value.rowBytes * value.height / K
    }

    override fun getBitmap(url: String): Bitmap? {
        return get(url)
    }

    override fun putBitmap(url: String, bitmap: Bitmap) {
        put(url, bitmap)
    }

    companion object {
        private val K = 1024
        val defaultLruCacheSize: Int
            get() {
                val maxMemory = (Runtime.getRuntime().maxMemory() / K).toInt()
                return maxMemory / 8
            }
    }
}