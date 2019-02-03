package lycaenion.org.marvelapp.models

import com.google.gson.annotations.SerializedName

class CharacterData(@SerializedName("results")
                    val results : Array<SearchResultCharacter>)