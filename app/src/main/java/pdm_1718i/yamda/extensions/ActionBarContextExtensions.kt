package pdm_1718i.yamda.extensions

import android.support.v7.widget.ActionBarContextView
import android.widget.TextView

/**
 * Created by orpheu on 11/2/17.
 */


fun ActionBarContextView.getTitleView() : TextView{
    return getChildAt(0) as TextView
}