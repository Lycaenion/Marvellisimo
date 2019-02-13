package lycaenion.org.marvelapp

import com.google.gson.annotations.SerializedName
import lycaenion.org.marvelapp.models.Series

class SeriesData(@SerializedName("results")
                 val results : Array<Series>) {
}