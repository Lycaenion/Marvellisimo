package lycaenion.org.marvelapp.models.seriesModels

import com.google.gson.annotations.SerializedName
import lycaenion.org.marvelapp.models.seriesModels.Series

class SeriesData(@SerializedName("results")
                 val results : Array<Series>) {
}