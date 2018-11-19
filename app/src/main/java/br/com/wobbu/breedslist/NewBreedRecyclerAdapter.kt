package br.com.wobbu.breedslist

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import java.util.*


class NewBreedRecyclerAdapter(var context: Context,
                              var origList: ArrayList<Breed>,
                              var presenter: MainPresenter) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var itemList: ArrayList<Breed>

    init {
        itemList = origList

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_breed_list, parent, false)
        return VHHeader(v, presenter)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder as VHHeader
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    internal inner class VHHeader(itemView: View, var presenter: MainPresenter) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var txtTitle = itemView.findViewById(R.id.txt_title) as TextView
        var viewBreedName = itemView.findViewById(R.id.view_breed_name) as LinearLayout
        lateinit var item: Breed

        fun bind(currentItem: Breed) {

            item = currentItem

            txtTitle.text = item.breedType
            txtTitle.setOnClickListener(this)
            if (item.breedNames.isEmpty()) {
                viewBreedName.visibility = View.GONE
            } else {
                viewBreedName.visibility = View.VISIBLE
                for (name in item.breedNames) {
                    val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                    val myView = inflater.inflate(R.layout.breed_name, null)
                    val breedName = myView.findViewById(R.id.txt_name) as TextView
                    breedName.text = name
                    breedName.setOnClickListener(this)

                    val layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT)
                    layoutParams.setMargins(0, 0, 0, 60)


                    viewBreedName.addView(breedName, layoutParams)
                }
            }
        }

        override fun onClick(view: View?) {
            var breedName = view as TextView
            if (breedName == txtTitle) {
                presenter.loadBreedImageByName(breedName.text.toString())
            } else {
                var name = breedName.text.toString()
                Toast.makeText(context, "$name is a sub-breeds and they don't have image to display", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val oReturn = FilterResults()
                val results = ArrayList<Breed>()
                val toAdd = ArrayList<Breed>()
                val toRemove = ArrayList<String>()
                if (constraint != null && constraint.isNotEmpty()) {
                    if (origList.size > 0) {
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
                    }
                    results.addAll(toAdd)
                    for (i in 0 until results.size) {
                        results[i].breedNames.removeAll(toRemove)
                    }
                    oReturn.values = results
                    oReturn.count = results.size
                } else {
                    oReturn.values = origList
                    oReturn.count = origList.size
                }
                return oReturn
            }

            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                itemList = ArrayList(results.values as ArrayList<Breed>)
                notifyDataSetChanged()
            }
        }
    }
}