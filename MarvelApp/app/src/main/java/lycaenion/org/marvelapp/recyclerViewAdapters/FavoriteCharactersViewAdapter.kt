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
import kotlinx.android.synthetic.main.layout_listitem.view.*
import lycaenion.org.marvelapp.R
import lycaenion.org.marvelapp.activities.CharacterActivity
import lycaenion.org.marvelapp.models.databaseModels.FavoriteCharacter

class FavoriteCharactersViewAdapter(var context: Context, var favoriteList : List<FavoriteCharacter>) : RecyclerView.Adapter<FavoriteCharactersViewAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteCharactersViewAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_listitem, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return favoriteList.size
    }

    override fun onBindViewHolder(viewHolder : ViewHolder, position: Int) {
        Glide.with(context)
            .asBitmap()
            .load(favoriteList[position].imgPath)
            .into(viewHolder.img)

        viewHolder.characterName.text = favoriteList[position].name

        viewHolder.favoriteIcon.visibility = View.VISIBLE

        viewHolder.setOnclickListener(object : OnItemClickListener {
            override fun onItemClick(position: Int, view: View) {
                val intent = Intent(context, CharacterActivity::class.java)
                intent.putExtra("id", favoriteList[position].id)
                context.startActivity(intent)
            }
        })
    }

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view), View.OnClickListener{

        val characterName : TextView = view.search_name
        val img : CircleImageView = view.search_thumbnail
        val favoriteIcon : ImageView = view.favorite_indicator

        private var itemClickListener : OnItemClickListener? = null

        init {
            itemView.setOnClickListener(this)
        }

        fun setOnclickListener(itemClickListener: OnItemClickListener){
            this.itemClickListener = itemClickListener
        }

        override fun onClick(v : View?){
            this.itemClickListener!!.onItemClick(adapterPosition, v!!)
        }

    }

}