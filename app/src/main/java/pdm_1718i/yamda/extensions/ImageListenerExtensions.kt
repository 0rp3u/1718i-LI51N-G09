package pdm_1718i.yamda.extensions

import android.widget.ImageView
import com.android.volley.VolleyError
import com.android.volley.toolbox.ImageLoader

/**
 * Created by orpheu on 10/25/17.
 */
fun getImageListener(view: ImageView,
                                 defaultImageResId: Int, errorImageResId: Int, TAG : String): ImageLoader.ImageListener {

    return object : ImageLoader.ImageListener {
        override fun onErrorResponse(error: VolleyError) {
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