package br.com.wobbu.breedslist

interface MainView {

    fun populateBreedList(response: ArrayList<Breed>)
    fun showBreedImage(url: String)
}