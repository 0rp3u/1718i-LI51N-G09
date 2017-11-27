package pdm_1718i.yamda.data.db.def

import android.net.Uri

/**
 * Created by Red on 23/11/2017.
 */
interface ContractInterface{

    val AUTHORITY: String

    val BASE_CONTENT_URI: Uri

    val MEDIA_BASE_SUBTYPE: String
}