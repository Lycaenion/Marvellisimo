package lycaenion.org.marvelapp.activities


import android.content.Intent
import android.net.Uri
import android.os.Bundle
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

class MainActivity : AppCompatActivity(), FavoriteSeriesFragment.OnFragmentInteractionListener,
    NavigationView.OnNavigationItemSelectedListener {
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_home ->{
                Toast.makeText(this, "You are home", Toast.LENGTH_SHORT)
            }

            R.id.nav_all_characters ->{
                var intent = Intent(this, SearchCharacterActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_all_series ->{
                var intent = Intent(this, SearchSeriesActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_search_character ->{
                var intent = Intent(this, SearchCharacterActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_search_series->{
                var intent = Intent(this, SearchSeriesActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_show_favorite_characters ->{
                var intent = Intent(this, FavoriteCharactersActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_show_favorite_series ->{
                println(favoriteSeriesFragment.toString())
                Toast.makeText(this, "Favorite", Toast.LENGTH_SHORT)
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, favoriteSeriesFragment)
                    .addToBackStack(favoriteSeriesFragment.toString())
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onFragmentInteraction(uri: Uri) {
        println("hello")
    }

    lateinit var favoriteSeriesFragment : FavoriteSeriesFragment

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {

        //var realm : Realm

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)



        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        favoriteSeriesFragment = FavoriteSeriesFragment.newInstance()


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



    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    /*fun initRecyclerViw(context : Context, characters : Array<SearchResultCharacter>){
        val recyclerView : RecyclerView = findViewById(R.id.character_recycler_view)
        val adapter = CharactersViewAdapter(context, characters)
        recyclerView.adapter  = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
    }*/
}
