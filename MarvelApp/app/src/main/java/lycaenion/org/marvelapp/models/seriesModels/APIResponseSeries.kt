package lycaenion.org.marvelapp.models.seriesModels

import com.google.gson.annotations.SerializedName

class APIResponseSeries(@SerializedName("code")
                        val code : Int,
                        @SerializedName("status")
                        val status : String,
                        @SerializedName("data")
                        val data : SeriesData
) {
}