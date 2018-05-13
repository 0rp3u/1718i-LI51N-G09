package pdm_1718i.yamda.extensions

import java.math.RoundingMode
import java.text.DecimalFormat


fun Number.asRounded(): String{
    val df = DecimalFormat("#.#")
    df.roundingMode = RoundingMode.HALF_UP
    return df.format(this)
}