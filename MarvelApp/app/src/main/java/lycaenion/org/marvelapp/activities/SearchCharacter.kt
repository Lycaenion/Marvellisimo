package lycaenion.org.marvelapp.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import lycaenion.org.marvelapp.R
import lycaenion.org.marvelapp.handlers.MarvelCharacterHandler
import lycaenion.org.marvelapp.models.SearchResultCharacter
import lycaenion.org.marvelapp.recyclerViewAdapters.CharactersViewAdapter
import lycaenion.org.marvelapp.recyclerViewAdapters.EndlessRecyclerViewScrollListener

class SearchCharacter : AppCompatActivity() {

    private var characterList : List<SearchResultCharacter> = emptyList()
    private lateinit var scrollListener : EndlessRecyclerViewScrollListener
    private lateinit var adapter : CharactersViewAdapter
    private lateinit var recyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_character)


        var linearLayoutManager = LinearLayoutManager(this)
        recyclerView = findViewById(R.id.character_recycler_view)
        recyclerView.layoutManager = linearLayoutManager
        initAdapter()
        initScrollListener(linearLayoutManager)
    }

    private fun initAdapter(){

        MarvelCharacterHandler.getAllCharacters(0).observeOn(AndroidSchedulers.mainThread()).subscribe({
            response -> characterList = characterList + response.data.results.asList()
            adapter = CharactersViewAdapter(this, characterList)
            recyclerView.adapter = adapter
        })


    }

    private fun initScrollListener(linearLayoutManager: LinearLayoutManager){
        scrollListener = object : EndlessRecyclerViewScrollListener(linearLayoutManager){
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                addCharacters(page * 20)
            }
        }
        println(characterList.size)
        recyclerView.addOnScrollListener(scrollListener)
    }

    fun addCharacters(offset : Int){
        //if this function stops working, add following rows here:
        //var currentSize : Int
        //currentSize = adapter.itemCount

        if (offset == 0){
            scrollListener.resetState()
            characterList = emptyList()
            println("offset 0")
        }else{
            MarvelCharacterHandler.getAllCharacters(offset).observeOn(AndroidSchedulers.mainThread()).subscribe({
                response -> characterList = characterList + response.data.results.asList()
                adapter.addCharacters(characterList)
                adapter.notifyDataSetChanged()
            })
        }
    }
}
