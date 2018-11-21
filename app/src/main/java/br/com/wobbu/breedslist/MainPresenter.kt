package br.com.wobbu.breedslist

import android.util.Log
import com.google.gson.Gson
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONException
import org.json.JSONObject
import java.net.URL


class MainPresenter(var mainView: MainView) {

    fun loadBreedFromAPI() {
        doAsync {
            val result = URL("https://dog.ceo/api/breeds/list/all").readText()
            uiThread {
                Log.d("MyResponse", result)
                var breedList = ArrayList<Breed>()
                try {
                    val jsonObject = JSONObject(result)
                    val messageObj = jsonObject.getJSONObject("message")

                    val iter = messageObj.keys()
                    while (iter.hasNext()) {
                        val key = iter.next()
                        Log.w("key", key)

                        var breed = Breed()
                        breed.breedType = key

                        val lineItems = messageObj.getJSONArray(key)
                        if (lineItems.length() > 0) {
                            for (i in 0 until lineItems.length()) {
                                breed.breedNames.add(lineItems.getString(i))
                            }
                        }

                        breedList.add(breed)
                    }

                    mainView.populateBreedList(breedList)

                } catch (e: JSONException) {
                    Log.e("QueryUtils", "Problem parsing the JSON results", e)
                }
            }
        }
    }

    fun loadBreedImageByName(name: String) {
        doAsync {
            val json = URL("https://dog.ceo/api/breed/$name/images/random").readText()
            uiThread {
                var response = Gson().fromJson(json, Breed::class.java)
                mainView.showBreedImage(response.image)
            }
        }
    }

    fun performSearch(origList: ArrayList<Breed>, constraint: CharSequence): ArrayList<Breed> {
        val results = java.util.ArrayList<Breed>()
        val toAdd = java.util.ArrayList<Breed>()
        val toRemove = java.util.ArrayList<String>()

        for (cd in origList) {
            if (cd.breedNames.isNotEmpty()) {
                for (i in cd.breedNames) {
                    if (i.toLowerCase().contains(constraint.toString().toLowerCase())) {
                        toAdd.add(cd)
                    } else {
                        toRemove.add(i)
                    }
                }
            }

            if (cd.breedType.toLowerCase().contains(constraint.toString().toLowerCase())) {
                toAdd.add(cd)
            }
        }

        results.addAll(toAdd)
        for (i in 0 until results.size) {
            results[i].breedNames.removeAll(toRemove)
        }

        return results
    }
}