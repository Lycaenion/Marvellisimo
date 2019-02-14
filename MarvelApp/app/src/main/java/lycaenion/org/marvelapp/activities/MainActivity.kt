package lycaenion.org.marvelapp.activities


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.NavigationView
import android.support.v4.app.FragmentTransaction
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import lycaenion.org.marvelapp.FavoriteSeriesFragment

import lycaenion.org.marvelapp.R

class MainActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {

        //var realm : Realm

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var fabSearchCharacter = findViewById<com.github.clans.fab.FloatingActionButton>(R.id.nav_search_character)
        var fabSearchSeries = findViewById<com.github.clans.fab.FloatingActionButton>(R.id.nav_search_series)
        var fabAllCharacters = findViewById<com.github.clans.fab.FloatingActionButton>(R.id.nav_all_characters)
        var fabAllSeries = findViewById<com.github.clans.fab.FloatingActionButton>(R.id.nav_all_series)
        var fabFavoriteCharacters = findViewById<com.github.clans.fab.FloatingActionButton>(R.id.nav_show_favorite_characters)
        var fabFavoriteSeries = findViewById<com.github.clans.fab.FloatingActionButton>(R.id.nav_show_favorite_series)

        fabSearchCharacter.setOnClickListener {
            view -> startActivity(Intent(this, SearchCharacterActivity::class.java))
        }

        fabSearchSeries.setOnClickListener {
            view -> startActivity(Intent(this, SearchSeriesActivity::class.java))
        }

        fabAllCharacters.setOnClickListener {
            view -> startActivity(Intent(this, SearchCharacterActivity::class.java))
        }

        fabAllSeries.setOnClickListener {
                view -> startActivity(Intent(this, SearchSeriesActivity::class.java))
        }

        fabFavoriteCharacters.setOnClickListener {
            view -> startActivity(Intent(this, FavoriteCharactersActivity::class.java))
        }

        fabFavoriteSeries.setOnClickListener {
                view -> startActivity(Intent(this, FavoriteSeriesActivity::class.java))
        }



        /*Realm.init(this)

        val config = RealmConfiguration.Builder()
            .schemaVersion(1)
            .name("favoriteCharacters.realm")
            .build()


        realm = Realm.getInstance(config)

        var list : RealmResults<FavoriteCharacter> = realm.where(FavoriteCharacter::class.java).findAll()
        //realm.close()

        for( i in list.indices){
            println(list[i]?.name + " is in favorite characters db")
        }*/

/*        fab.setOnClickListener{view ->


*//*
            var intent = Intent(this, SearchCharacterActivity::class.java)
            //intent.putExtra("id",1009546)
            startActivity(intent)*//*
        }*/





        /*var adapter : CharactersViewAdapter

        MarvelCharacterHandler.getAllCharacters().observeOn(AndroidSchedulers.mainThread()).subscribe{
            data -> initRecyclerViw(this, data.data.results)
            println(data.data.results.size)

        }*/


    }



    /*fun initRecyclerViw(context : Context, characters : Array<SearchResultCharacter>){
        val recyclerView : RecyclerView = findViewById(R.id.character_recycler_view)
        val adapter = CharactersViewAdapter(context, characters)
        recyclerView.adapter  = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
    }*/
}
