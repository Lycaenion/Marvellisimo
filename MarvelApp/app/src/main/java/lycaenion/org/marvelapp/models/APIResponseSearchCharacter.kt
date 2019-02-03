package lycaenion.org.marvelapp.models

import com.google.gson.annotations.SerializedName

class APIResponseSearchCharacter(val code : Int,
                                 val status : String,
                                 val data : CharacterData)