package br.com.wobbu.breedslist

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.SearchView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity(), MainView {

    lateinit var recyclerBreed: RecyclerView
    lateinit var editSearch: SearchView
    lateinit var viewIamge: RelativeLayout
    lateinit var imgBreed: ImageView
    lateinit var loading: ProgressBar
    lateinit var presenter: MainPresenter
    lateinit var adapter: NewBreedRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerBreed = findViewById(R.id.recycle_breeds)
        editSearch = findViewById(R.id.edit_seach)
        viewIamge = findViewById(R.id.view_image)
        imgBreed = findViewById(R.id.img_breed)
        loading = findViewById(R.id.loading)

        viewIamge.setOnClickListener {
            it.visibility = View.GONE
        }
        editSearch.setOnQueryTextListener(editSearchQuery)

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerBreed.layoutManager = layoutManager

        presenter = MainPresenter(this)
        presenter.loadBreedFromAPI()
    }

    override fun populateBreedList(response: ArrayList<Breed>) {
        adapter = NewBreedRecyclerAdapter(this, response, presenter)
        recyclerBreed.adapter = adapter
    }

    override fun showBreedImage(url: String) {
        loading.visibility = View.VISIBLE
        viewIamge.visibility = View.VISIBLE
        Picasso.get().load(url).into(imgBreed, picassoCallback)
    }

    var picassoCallback = object : Callback {
        override fun onSuccess() {
            loading.visibility = View.GONE
        }

        override fun onError(e: Exception?) {
            loading.visibility = View.GONE
        }
    }

    var editSearchQuery = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(p0: String?): Boolean {
            adapter.getFilter().filter(p0)
            return true
        }

        override fun onQueryTextChange(p0: String?): Boolean {
            adapter.getFilter().filter(p0)
            return true
        }

    }
}
