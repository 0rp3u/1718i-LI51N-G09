package pdm_1718i.yamda.data.server.caches

/**
 * Created by orpheu on 10/25/17.
 */

import android.graphics.Bitmap
import android.support.v4.util.LruCache
import android.util.Log
import com.android.volley.toolbox.ImageLoader.ImageCache

class BitmapLruCache(private val bitmapDiskCache : ImageCache)  : LruCache<String, Bitmap>(defaultLruCacheSize), ImageCache {

    companion object {

        private val K = 1024
        val defaultLruCacheSize: Int
            get() {
                val maxMemory = (Runtime.getRuntime().maxMemory() / K).toInt()
                return maxMemory / 8
            }
    }

    override fun sizeOf(key: String?, value: Bitmap): Int {
        return value.rowBytes * value.height / K
    }

    override fun getBitmap(url: String): Bitmap? {
        var bitmap = get(url)
        Log.d("cache_test_Mem", "cache contained $url image: ${bitmap != null}")

        if(bitmap == null ) {

            bitmap = bitmapDiskCache.getBitmap(url)

            if (bitmap != null ){
                put(url, bitmap)
            }
        }
        return bitmap
    }

    override fun putBitmap(url: String, bitmap: Bitmap) {
        Log.d("cache_test_Mem", "$url image put on cache")

        put(url, bitmap)
        bitmapDiskCache.putBitmap(url, bitmap)
    }
}