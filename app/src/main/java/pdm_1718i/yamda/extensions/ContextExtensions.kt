package pdm_1718i.yamda.extensions

import android.content.Context
import android.widget.Toast


val NO_INTERNET_CONNECTION: CharSequence = "No Internet Connection."

fun Context.toast(message: CharSequence) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()