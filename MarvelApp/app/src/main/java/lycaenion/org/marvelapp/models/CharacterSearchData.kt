package lycaenion.org.marvelapp.models

import com.google.gson.annotations.SerializedName

class CharacterSearchData(@SerializedName("results")
                    val results : Array<SearchResultCharacter>)