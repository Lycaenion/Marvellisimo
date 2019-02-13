package lycaenion.org.marvelapp.models

import com.google.gson.annotations.SerializedName
import lycaenion.org.marvelapp.SeriesData

class APIResponseSeries(@SerializedName("code")
                        val code : Int,
                        @SerializedName("status")
                        val status : String,
                        @SerializedName("data")
                        val data : SeriesData) {
}