package lycaenion.org.marvelapp.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.FragmentTransaction
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.github.clans.fab.FloatingActionButton
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmResults
import lycaenion.org.marvelapp.FavoriteSeriesFragment
import lycaenion.org.marvelapp.R
import lycaenion.org.marvelapp.models.databaseModels.FavoriteCharacter
import lycaenion.org.marvelapp.recyclerViewAdapters.FavoriteCharactersViewAdapter

class FavoriteCharactersActivity : AppCompatActivity() {

    private lateinit var characterList : RealmResults<FavoriteCharacter>
    private lateinit var adapter : FavoriteCharactersViewAdapter
    private lateinit var recyclerView : RecyclerView
    private lateinit var realm : Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite_characters)

        var fabSearchCharacter = findViewById<FloatingActionButton>(R.id.nav_search_character)
        var fabSearchSeries = findViewById<FloatingActionButton>(R.id.nav_search_series)
        var fabAllCharacters = findViewById<FloatingActionButton>(R.id.nav_all_characters)
        var fabAllSeries = findViewById<FloatingActionButton>(R.id.nav_all_series)
        var fabFavoriteCharacters = findViewById<FloatingActionButton>(R.id.nav_show_favorite_characters)
        var fabFavoriteSeries = findViewById<FloatingActionButton>(R.id.nav_show_favorite_series)

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


        var linearLayoutManager = LinearLayoutManager(this)
        recyclerView = findViewById(R.id.favorite_characters_view)
        recyclerView.layoutManager = linearLayoutManager
        fetchCharacters()
        initAdapter()


    }

    private fun initAdapter(){
        adapter = FavoriteCharactersViewAdapter(this, characterList)
        recyclerView.adapter = adapter
        println("come and get me")
    }

    private fun fetchCharacters(){
        Realm.init(this)

        val config = RealmConfiguration.Builder()
            .schemaVersion(1)
            .name("favorites.realm")
            .build()

        realm = Realm.getInstance(config)
        characterList = realm.where(FavoriteCharacter::class.java).findAll()

        println("this is the size: " + characterList.size)
        //realm.close()


    }
}
