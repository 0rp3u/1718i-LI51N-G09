package pdm_1718i.yamda.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by orpheu on 10/18/17.
 */

data class Movie(
        val poster_path : String,
        val release_date : String,
        val id: Int,
        val title : String,
        val backdrop_path : String,
        val vote_average: Double
) : Parcelable{

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            TODO("vote_average")) {
    }

    override fun writeToParcel(p0: Parcel?, p1: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Movie> {
        override fun createFromParcel(parcel: Parcel): Movie {
            return Movie(parcel)
        }

        override fun newArray(size: Int): Array<Movie?> {
            return arrayOfNulls(size)
        }
    }

}

data class DetailedMovie(
        val poster_path : String?,
        val release_date : String,
        val id: Int,
        val title : String,
        val vote_average: Number,
        val budget : Int,
        val genres : List<Genre>,
        val overview : String?
)

data class Genre(
        val name: String
)