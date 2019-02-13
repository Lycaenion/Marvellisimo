package lycaenion.org.marvelapp.models.seriesModels

import com.google.gson.annotations.SerializedName

class SeriesSearchData(@SerializedName("results")
                       val results : Array<Series>) {

}