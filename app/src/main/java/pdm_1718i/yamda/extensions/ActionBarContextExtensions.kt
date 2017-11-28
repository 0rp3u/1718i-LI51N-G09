package pdm_1718i.yamda.extensions

import android.support.v7.widget.ActionBarContextView
import android.widget.TextView

fun ActionBarContextView.getTitleView() : TextView{
    return getChildAt(0) as TextView
}