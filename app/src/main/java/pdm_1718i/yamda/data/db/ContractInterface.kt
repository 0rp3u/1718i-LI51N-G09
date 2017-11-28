package pdm_1718i.yamda.data.db

import android.net.Uri


interface ContractInterface{

    val AUTHORITY: String

    val BASE_CONTENT_URI: Uri

    val MEDIA_BASE_SUBTYPE: String
}