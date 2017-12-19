package pdm_1718i.yamda.data.cache.bitmap


import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.util.Log
import com.android.volley.Cache
import com.android.volley.toolbox.ImageLoader.ImageCache
import pdm_1718i.yamda.extensions.memoize
import java.io.*

/*
    Persistent bitmap cache that uses the the Volley DiskCache
    Android recognizes this cache as a cleanable cache data so it might be cleaned by a cleaning app
     or by the user clicking "clean cache"
 */

class DiskLruImageCache(private val mDiskCache : Cache) : ImageCache {

    private var mCompressFormat = CompressFormat.JPEG
    private var mCompressQuality = 70


    override fun putBitmap(key: String, data: Bitmap) {
            val entry = Cache.Entry()
            val stream = ByteArrayOutputStream()
            data.compress(mCompressFormat, mCompressQuality, stream)
            entry.data = stream.toByteArray()
            mDiskCache.put(key, entry)
            stream.close()
            //Log.d("cache_test_DISK_", "$key was put on disk ")
    }





    //TODO this is temporary, we sould do a soft get on the disk cache so the headers are in order with bitmap access
    override fun getBitmap(key: String): Bitmap? = memoize<String, Bitmap?>({

            var bitmap: Bitmap? = null
            var entry : Cache.Entry?
            try {
                entry =  mDiskCache.get(key)

                if (entry == null) {
                    //Log.d("cache_test_DISK_", "$key was not on disk (snapshot) ")
                    return@memoize null
                }
                bitmap = BitmapFactory.decodeByteArray(entry.data, 0, entry.data.size)

            } catch (e: IOException) {
                e.printStackTrace()
            }

                //Log.d("cache_test_DISK_", if (bitmap == null) "" else "image read from disk " + key)

        return@memoize bitmap
    }).invoke(key)

    fun containsKey(key: String): Boolean {
        val contained =  mDiskCache[key]  != null
        //Log.d("cache_test_DISK_", "cache contained $key image: $contained")
        return contained

    }

    fun clearCache() {
            try {
                mDiskCache.clear()
                    Log.d("cache_test_DISK_", "disk cache CLEARED")

            } catch (e: IOException) {
                    Log.d("cache_test_DISK_", "error while disk cache CLEANING")
                e.printStackTrace()
            }
        }
}
