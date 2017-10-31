package pdm_1718i.yamda.extensions

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Red on 31/10/2017.
 */

val parser = SimpleDateFormat("yyyy-MM-dd")


fun Calendar.setTime(date: String): Calendar{
    time = parser.parse(date)
    return this
}

fun Calendar.getYear(): Int = get(Calendar.YEAR)

fun Calendar.getDate(): String = parser.format(time)

fun getCalendar(date: String) = Calendar.getInstance().setTime(date)