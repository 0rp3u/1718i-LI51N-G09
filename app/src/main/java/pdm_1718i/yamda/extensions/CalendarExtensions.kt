package pdm_1718i.yamda.extensions

import android.text.TextUtils
import java.text.SimpleDateFormat
import java.util.*


val parser = SimpleDateFormat("yyyy-MM-dd")

fun Calendar.setTime(date: String): Calendar{
    time = parser.parse(date)
    return this
}

fun Calendar.getYear(): Int = get(Calendar.YEAR)

fun Calendar.getMonth(): Int = get(Calendar.MONTH)

fun Calendar.getDay(): Int = get(Calendar.DAY_OF_MONTH)

fun Calendar.getDate(): String = parser.format(time)

fun getCalendar(date: String): Calendar?{
    return if(TextUtils.isEmpty(date))
        return null
    else
        Calendar.getInstance().setTime(date)
}

fun getYearFromCalendar(calendar: Calendar?): String{
    return when(calendar == null){
        true -> "?"
        false -> calendar!!.getYear().toString()
    }
}

fun getDateFromCalendar(calendar: Calendar?): String{
   return when(calendar == null){
        true -> "?"
        false -> calendar!!.getDate()
    }
}

fun Calendar.FromPresentInMillis() = timeInMillis - Date().time

fun Calendar.isFuture() = this.time.after(Date())
