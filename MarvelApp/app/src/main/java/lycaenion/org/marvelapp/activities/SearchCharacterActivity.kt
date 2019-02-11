package lycaenion.org.marvelapp.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.SearchView
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
    private var searchString  = ""
    private lateinit var searchView : SearchView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_character)


        var linearLayoutManager = LinearLayoutManager(this)
        recyclerView = findViewById(R.id.character_recycler_view)
        recyclerView.layoutManager = linearLayoutManager
        searchView = findViewById(R.id.search_character)
        initAdapter()
        initScrollListener(linearLayoutManager)
        setTextListener()
    }

    private fun setTextListener(){
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextChange(newString: String?): Boolean {

                if(newString!!.isEmpty()){
                    searchString = ""
                }else{
                    searchString = newString!!
                }
                
                if(searchString.equals("")){
                    resetAdapter()
                    addCharacters(0, searchString)
                }else{
                    resetAdapter()
                    addCharacters(0, searchString)
                }
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {

                if(query!!.isEmpty()){
                    searchString = ""
                }else{
                    searchString = query!!
                }

                if(searchString.equals("")){
                    resetAdapter()
                    addCharacters(0, searchString)
                }else{
                    addCharacters(0, searchString)
                }
                return false
            }
        })
    }

    private fun resetAdapter(){
        scrollListener.resetState()
        characterList = emptyList()
        adapter.notifyDataSetChanged()
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
                addCharacters(page * 20, searchString)
            }
        }
        recyclerView.addOnScrollListener(scrollListener)
    }

    fun addCharacters(offset : Int, search: String){

        if(search.equals("")){
            if (offset == 0){
                scrollListener.resetState()
                characterList = emptyList()
                adapter.notifyDataSetChanged()
                MarvelCharacterHandler.getAllCharacters(0).observeOn(AndroidSchedulers.mainThread()).subscribe({
                        response -> characterList = characterList + response.data.results.asList()
                    adapter.addCharacters(characterList)
                    adapter.notifyDataSetChanged()
                })
            }else{
                //if this function stops working, add following rows here:
                //var currentSize : Int
                //currentSize = adapter.itemCount
                MarvelCharacterHandler.getAllCharacters(offset).observeOn(AndroidSchedulers.mainThread()).subscribe({
                        response -> characterList = characterList + response.data.results.asList()
                    adapter.addCharacters(characterList)
                    adapter.notifyDataSetChanged()
                })
            }

        }else{

            MarvelCharacterHandler.searchCharacter(search).observeOn(AndroidSchedulers.mainThread()).subscribe({
                response -> characterList = characterList + response.data.results.asList()
                adapter.addCharacters(characterList)
                adapter.notifyDataSetChanged()
            })


        }



    }
}
