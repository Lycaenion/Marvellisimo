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
    private lateinit var linearLayoutManager: LinearLayoutManager

    private lateinit var fabSearchCharacter : FloatingActionButton
    private lateinit var fabSearchSeries : FloatingActionButton
    private lateinit var fabAllCharacters : FloatingActionButton
    private lateinit var fabAllSeries : FloatingActionButton
    private lateinit var fabFavoriteCharacters : FloatingActionButton
    private lateinit var fabFavoriteSeries : FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite_characters)
        initMenu()

        linearLayoutManager = LinearLayoutManager(this)
        recyclerView = findViewById(R.id.favorite_characters_view)
        recyclerView.layoutManager = linearLayoutManager
        fetchCharacters()
        initAdapter()
    }

    private fun initMenu(){

        fabSearchCharacter = findViewById(R.id.nav_search_character)
        fabSearchSeries = findViewById(R.id.nav_search_series)
        fabAllCharacters = findViewById(R.id.nav_all_characters)
        fabAllSeries = findViewById(R.id.nav_all_series)
        fabFavoriteCharacters = findViewById(R.id.nav_show_favorite_characters)
        fabFavoriteSeries = findViewById(R.id.nav_show_favorite_series)

        fabSearchCharacter.setOnClickListener {
            startActivity(Intent(this, SearchCharacterActivity::class.java))
        }

        fabSearchSeries.setOnClickListener {
            startActivity(Intent(this, SearchSeriesActivity::class.java))
        }

        fabAllCharacters.setOnClickListener {
            startActivity(Intent(this, SearchCharacterActivity::class.java))
        }

        fabAllSeries.setOnClickListener {
            startActivity(Intent(this, SearchSeriesActivity::class.java))
        }

        fabFavoriteCharacters.setOnClickListener {
            startActivity(Intent(this, FavoriteCharactersActivity::class.java))
        }
        fabFavoriteSeries.setOnClickListener {
            startActivity(Intent(this, FavoriteSeriesActivity::class.java))
        }
    }

    private fun initAdapter(){
        adapter = FavoriteCharactersViewAdapter(this, characterList)
        recyclerView.adapter = adapter
    }

    private fun fetchCharacters(){

        Realm.init(this)

        val config = RealmConfiguration.Builder()
            .schemaVersion(1)
            .name("favorites.realm")
            .build()

        var realm = Realm.getInstance(config)
        characterList = realm.where(FavoriteCharacter::class.java).findAll()

        realm.close()
    }
}
