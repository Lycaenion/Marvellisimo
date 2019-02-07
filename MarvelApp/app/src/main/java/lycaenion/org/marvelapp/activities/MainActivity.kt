package lycaenion.org.marvelapp.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*

import lycaenion.org.marvelapp.R
import lycaenion.org.marvelapp.models.SearchResultCharacter
import lycaenion.org.marvelapp.recyclerViewAdapters.CharactersViewAdapter

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        fab.setOnClickListener{view ->
            var intent = Intent(this, CharacterActivity::class.java)
            intent.putExtra("id",1009546)
            startActivity(intent)
        }



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

    fun initRecyclerViw(context : Context, characters : Array<SearchResultCharacter>){
        val recyclerView : RecyclerView = findViewById(R.id.character_recycler_view)
        val adapter = CharactersViewAdapter(context, characters)
        recyclerView.adapter  = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
    }
}
