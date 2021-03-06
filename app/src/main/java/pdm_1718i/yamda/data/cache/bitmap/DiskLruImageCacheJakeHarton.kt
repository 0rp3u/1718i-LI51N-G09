package pdm_1718i.yamda.data.cache.bitmap

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.util.Log
import com.android.volley.toolbox.ImageLoader.ImageCache
import com.jakewharton.disklrucache.DiskLruCache
import pdm_1718i.yamda.extensions.memoize
import java.security.MessageDigest
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.io.OutputStream

/*
    Persistent bitmap cache that uses the DiskCache implementation from Jake Harton
    Adroid does not recognize this cache as a cleanable cache, recognizes as application data
 */

class DiskLruImageCacheJakeHarton(context: Context, uniqueName: String, diskCacheSize: Int = 20000) : ImageCache {

    private lateinit var mDiskCache: DiskLruCache
    private var mCompressFormat = CompressFormat.JPEG
    private var mCompressQuality = 70


    companion object {
        private val IO_BUFFER_SIZE = 8 * 1024
        private val APP_VERSION = 1
        private val VALUE_COUNT = 1
        private var mesageDiget = MessageDigest.getInstance("MD5")
    }

    val cacheFolder: File
        get() = mDiskCache.directory

    init {
        try {
            val diskCacheDir = getDiskCacheDir(context, uniqueName)
            mDiskCache = DiskLruCache.open(diskCacheDir, APP_VERSION, VALUE_COUNT, diskCacheSize.toLong())
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    @Throws(IOException::class, FileNotFoundException::class)
    private fun writeBitmapToFile(bitmap: Bitmap, editor: DiskLruCache.Editor): Boolean {
        var out: OutputStream? = null
        try {
            out = BufferedOutputStream(editor.newOutputStream(0), IO_BUFFER_SIZE)
            return bitmap.compress(mCompressFormat, mCompressQuality, out)
        } finally {
            if (out != null) {
                out.close()
            }
        }
    }

    private fun getDiskCacheDir(context: Context, uniqueName: String): File {

        val cachePath = context.cacheDir.path
        return File(cachePath + File.separator + uniqueName)
    }

    override fun putBitmap(key: String, data: Bitmap) {

            var editor: DiskLruCache.Editor? = null
            val key = createKey(key)
            try {
                editor = mDiskCache.edit(key)
                if (editor == null) {
                    return
                }

                if (writeBitmapToFile(data, editor)) {
                    mDiskCache.flush()
                    editor.commit()
                        //Log.d("cache_test_DISK_", "image put on disk cache " + key)

                } else {
                    editor.abort()
                        //Log.d("cache_test_DISK_", "ERROR on: image put on disk cache " + key)
                }
            } catch (e: IOException) {
                    Log.d("cache_test_DISK_", "ERROR on: image put on disk cache " + key)

                try {
                    if (editor != null) {
                        editor.abort()
                    }
                } catch (ignored: IOException) {
                }

            }
    }

    override fun getBitmap(key: String): Bitmap?{
            var bitmap: Bitmap? = null
            var key2 = createKey(key)
            var snapshot : DiskLruCache.Snapshot? = null
            try {
                snapshot = mDiskCache.get(key2) ?: return null

                val inBuf = snapshot.getInputStream(0)
                if (inBuf != null) {
                    val buffIn = BufferedInputStream(inBuf, IO_BUFFER_SIZE)
                    bitmap = BitmapFactory.decodeStream(buffIn)
                    buffIn.close()
                }


            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                if (snapshot != null) {
                    snapshot.close()
                }
            }
        return bitmap
    }

    fun containsKey(key: String): Boolean {
        var contained = false
        val key = createKey(key)

            var snapshot: DiskLruCache.Snapshot? = null
            try {
                snapshot = mDiskCache.get(key)
                contained = snapshot != null
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                if (snapshot != null) {
                    snapshot.close()
                }
            }
        //Log.d("cache_test_DISK_", "cache contained $key image: $contained")


        mDiskCache

        return contained

    }



    private fun createKey(unsafeKey : String) : String{
        val disg = mesageDiget.digest(unsafeKey.toByteArray())
        return java.math.BigInteger(1, disg).toString(16)
        //return unsafeKey.toLowerCase().substring(unsafeKey.lastIndexOf('/')+1, unsafeKey.lastIndexOf('.'))
    }

    fun clearCache() {

            try {
                mDiskCache.delete()
                    Log.d("cache_test_DISK_", "disk cache CLEARED")

            } catch (e: IOException) {
                    Log.d("cache_test_DISK_", "error while disk cache CLEANING")
                e.printStackTrace()
            }
        }
}
