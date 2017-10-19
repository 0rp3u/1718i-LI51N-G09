package pdm_1718i.yamda.data.server


import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

import pdm_1718i.yamda.R
import pdm_1718i.yamda.ui.App

class TMDBServer(){

    companion object {
        private val requestQueue by lazy { Volley.newRequestQueue(App.instance)}
        private val API_KEY = App.instance.getString(R.string.API_KEY)
        private val URL = App.instance.getString(R.string.TMDB_URL)
        private val COMPLETE_URL = "$URL?api_key=$API_KEY"
    }

     fun requestSearch(search: String){
        val result =  JsonObjectRequest(
                COMPLETE_URL,
                null,
                {
                    //findViewById<TextView>()
                },
                {
                    //textBox.text = it.toString()
                })

    }

}