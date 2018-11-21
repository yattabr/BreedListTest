package br.com.wobbu.breedslist

import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito

class BreedListTest {

    @Test
    fun performSearchOnBreedList() {
        var breedList = ArrayList<Breed>()
        var breed = Breed()
        breed.breedType = "basenji"
        breedList.add(breed)

        breed = Breed()
        breed.breedType = "hound"
        breed.breedNames.add("afghan")
        breed.breedNames.add("basset")
        breed.breedNames.add("blood")
        breed.breedNames.add("english")
        breed.breedNames.add("ibizan")
        breed.breedNames.add("walker")
        breedList.add(breed)

        breed = Breed()
        breed.breedType = "frise"
        breed.breedNames.add("staffordshire")
        breed.breedNames.add("bichon")
        breedList.add(breed)

        var mainView = Mockito.mock(MainView::class.java)
        var mainPresenter = MainPresenter(mainView)

        var searchResultArray = mainPresenter.performSearch(breedList, "bas")

        Assert.assertEquals(searchResultArray.size, 2)
    }
}