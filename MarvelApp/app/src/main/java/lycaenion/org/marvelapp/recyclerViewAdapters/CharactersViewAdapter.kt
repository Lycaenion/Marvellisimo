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
import lycaenion.org.marvelapp.OnItemClickListener
import lycaenion.org.marvelapp.R
import lycaenion.org.marvelapp.activities.CharacterActivity
import lycaenion.org.marvelapp.models.characterModels.SearchResultCharacter
import lycaenion.org.marvelapp.models.databaseModels.FavoriteCharacter

class CharactersViewAdapter(var context : Context, var searchResultCharacters : List<SearchResultCharacter> ) : RecyclerView.Adapter<CharactersViewAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharactersViewAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_listitem, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return searchResultCharacters.size
    }

    fun addCharacters(characterList : List<SearchResultCharacter>){

        //if this function stops working, add currentSize : Int to input parameters

        searchResultCharacters = characterList

        //notifyItemRangeChanged(currentSize-1, searchResultCharacters.size)

        notifyDataSetChanged()
    }

    fun isCharacterFavorite(id : Int) : Boolean{

        var realm : Realm

        Realm.init(context)

        val config = RealmConfiguration.Builder()
            .schemaVersion(1)
            .name("favoriteCharacters.realm")
            .build()
        realm = Realm.getInstance(config)

        var favoriteCharacter : FavoriteCharacter? = realm.where(FavoriteCharacter::class.java)
            .equalTo("id", id)
            .findFirst()
        realm.close()

        return favoriteCharacter != null
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        Glide.with(context)
            .asBitmap()
            .load(searchResultCharacters[position].thumbnail.path+ "." + searchResultCharacters[position].thumbnail.extension)
            .into(viewHolder.image)

        viewHolder.imageName.text = searchResultCharacters[position].name

        if(isCharacterFavorite(searchResultCharacters[position].id)){
            viewHolder.favoriteIcon.visibility = View.VISIBLE
            println("True : " + searchResultCharacters[position].name)
        }else{
            println("False : " + searchResultCharacters[position].name)
        }

        viewHolder.setOnItemClickListener(object : OnItemClickListener{
            override fun onItemClick(position: Int, view: View) {
                val intent = Intent(context, CharacterActivity::class.java)
                intent.putExtra("id", searchResultCharacters[position].id)
                context.startActivity(intent)
            }
        })

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