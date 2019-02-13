package lycaenion.org.marvelapp.models

import com.google.gson.annotations.SerializedName

class SeriesSearchData(@SerializedName("results")
                       val results : Array<Series>) {

}