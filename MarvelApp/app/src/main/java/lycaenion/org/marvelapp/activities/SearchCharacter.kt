package lycaenion.org.marvelapp.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_search_character.*
import kotlinx.android.synthetic.main.app_bar_main.*
import lycaenion.org.marvelapp.R
import lycaenion.org.marvelapp.handlers.MarvelCharacterHandler
import lycaenion.org.marvelapp.models.SearchResultCharacter
import lycaenion.org.marvelapp.recyclerViewAdapters.CharactersViewAdapter
import lycaenion.org.marvelapp.recyclerViewAdapters.EndlessRecyclerViewScrollListener

class SearchCharacter : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var characterList : List<SearchResultCharacter> = emptyList()
    private lateinit var scrollListener : EndlessRecyclerViewScrollListener
    private lateinit var adapter : CharactersViewAdapter
    private lateinit var recyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_character)
        //setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(this, drawer_layout_character_search, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout_character_search.addDrawerListener(toggle)
        toggle.syncState()

        nav_view_search_character.setNavigationItemSelectedListener(this)


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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_searchCharacter -> {
                val toast = Toast.makeText(this, "searchCharacter", Toast.LENGTH_SHORT)
                toast.show()
                intent = Intent(this, SearchCharacter::class.java)
                startActivity(intent)
            }
            R.id.nav_searchSeries -> {
                val toast = Toast.makeText(this, "searchSeries", Toast.LENGTH_SHORT)
                toast.show()
            }
            R.id.nav_home -> {
                val toast = Toast.makeText(this, "home", Toast.LENGTH_SHORT)
                toast.show()
            }
        }

        drawer_layout_character_search.closeDrawer(GravityCompat.START)
        return true
    }
}
