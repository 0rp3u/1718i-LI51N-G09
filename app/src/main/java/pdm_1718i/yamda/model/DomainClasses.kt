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
            parcel.readDouble())

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString( this.poster_path )
        dest.writeString( this.release_date )
        dest.writeInt( this.id )
        dest.writeString( this.title )
        dest.writeString ( this.backdrop_path )
        dest.writeDouble( this.vote_average )
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
        val poster_path: String?,
        val release_date: String,
        val id: Int,
        val title: String,
        val vote_average: Float,
        val budget: Int,
        val genres: List<Genre>,
        val overview: String?
) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readInt(),
            source.readString(),
            source.readFloat(),
            source.readInt(),
            ArrayList<Genre>().apply { source.readList(this, Genre::class.java.classLoader) },
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(poster_path)
        writeString(release_date)
        writeInt(id)
        writeString(title)
        writeFloat(vote_average)
        writeInt(budget)
        writeList(genres)
        writeString(overview)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<DetailedMovie> = object : Parcelable.Creator<DetailedMovie> {
            override fun createFromParcel(source: Parcel): DetailedMovie = DetailedMovie(source)
            override fun newArray(size: Int): Array<DetailedMovie?> = arrayOfNulls(size)
        }
    }
}

data class Genre(
        val name: String
)