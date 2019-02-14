package lycaenion.org.marvelapp.recyclerViewAdapters

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.android.synthetic.main.layout_listitem.view.*
import lycaenion.org.marvelapp.R
import lycaenion.org.marvelapp.activities.CharacterActivity
import lycaenion.org.marvelapp.models.characterModels.SearchResultCharacter
import lycaenion.org.marvelapp.models.databaseModels.FavoriteCharacter

class SearchCharactersViewAdapter(var context : Context, var searchResultCharacters : List<SearchResultCharacter> ) : RecyclerView.Adapter<SearchCharactersViewAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchCharactersViewAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_listitem, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return searchResultCharacters.size
    }

    fun addCharacters(characterList : List<SearchResultCharacter>){

        searchResultCharacters = characterList
        notifyDataSetChanged()
    }

    fun emptyList(){
        searchResultCharacters = kotlin.collections.emptyList()
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        Glide.with(context)
            .asBitmap()
            .load(searchResultCharacters[position].thumbnail.path+ "." + searchResultCharacters[position].thumbnail.extension)
            .into(viewHolder.image)

        viewHolder.imageName.text = searchResultCharacters[position].name

        if(checkIfFavorite(searchResultCharacters[position].id)){
            Glide.with(context)
                .load(R.drawable.ic_favorite_heart)
                .into(viewHolder.favoriteIcon)
        }

        viewHolder.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(position: Int, view: View) {
                val intent = Intent(context, CharacterActivity::class.java)
                intent.putExtra("id", searchResultCharacters[position].id)
                context.startActivity(intent)
            }
        })

    }

    private fun checkIfFavorite(id : Int) : Boolean{

        var realm : Realm
        Realm.init(context)

        val config = RealmConfiguration.Builder()
            .schemaVersion(1)
            .name("favorites.realm")
            .build()
        realm = Realm.getInstance(config)

        var favoriteCharacter : FavoriteCharacter? = realm.where(FavoriteCharacter::class.java)
            .equalTo("id", id)
            .findFirst()

        realm.close()

        return favoriteCharacter != null
    }

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view), View.OnClickListener{

        val imageName : TextView = view.search_name
        val image : CircleImageView = view.search_thumbnail
        val favoriteIcon : ImageView = view.favorite_indicator

        private var itemClickListener : OnItemClickListener? = null

        init {
            itemView.setOnClickListener(this)
        }

        fun setOnItemClickListener(itemClickListener: OnItemClickListener){
            this.itemClickListener = itemClickListener
        }

        override fun onClick(v: View?) {
            this.itemClickListener!!.onItemClick(adapterPosition, v!!)
        }

    }
}