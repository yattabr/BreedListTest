package br.com.wobbu.breedslist

import com.google.gson.annotations.SerializedName

class Breed {

    var breedType: String = ""
    val breedNames: ArrayList<String> = ArrayList()
    @SerializedName("message")
    val image: String = ""
}