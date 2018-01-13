package pdm_1718i.yamda.extensions

import android.graphics.Bitmap
import android.widget.ImageView
import com.android.volley.VolleyError
import com.android.volley.toolbox.ImageLoader

fun getImageListener(view: ImageView, defaultImageResId: Int, errorImageResId: Int = 0, errorHandler: ((Any) -> Any)? = null, TAG: String): ImageLoader.ImageListener {

    return object : ImageLoader.ImageListener {
        override fun onErrorResponse(error: VolleyError) {
            if(errorHandler !=  null ) errorHandler(error)

            if (errorImageResId != 0 && view.tag == TAG ) {
                view.setImageResource(errorImageResId)
            }
        }

        override fun onResponse(response: ImageLoader.ImageContainer, isImmediate: Boolean) {
            if(view.tag != TAG ) return

            if (response.bitmap != null ) {
                view.setImageBitmap(response.bitmap)
            } else if (defaultImageResId != 0) {
                view.setImageResource(defaultImageResId)
            }
        }
    }
}

fun getImageListener(bitmapCompletionHandler: (bitmap: Bitmap)-> Unit, errorHandler: ((Any) -> Any)? = null): ImageLoader.ImageListener {
    return object : ImageLoader.ImageListener {

        override fun onErrorResponse(error: VolleyError) {
            if(errorHandler !=  null ) errorHandler(error)
        }

        override fun onResponse(response: ImageLoader.ImageContainer, isImmediate: Boolean) {
            response.bitmap?.let{bitmapCompletionHandler(response.bitmap)}
        }
    }
}