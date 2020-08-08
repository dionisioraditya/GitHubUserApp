package com.example.githubuserapp

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions


class ListUserAdapter (private val listUser: ArrayList<Person>): RecyclerView.Adapter<ListUserAdapter.ListViewHolder>(){
    private  var onItemClickCallback: OnItemClickCallback? = null
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_user, viewGroup,false)
        return ListViewHolder(view)
    }
    override fun getItemCount(): Int {
        return listUser.size
    }
    override fun onBindViewHolder(holder:ListViewHolder, position: Int) {
        //holder.bind(listUser[position])
        val user = listUser[position]
        Glide.with(holder.itemView.context)
            .load(user.avatar)
            .apply(RequestOptions().override(55,55))
            .into(holder.imgPhoto)
        holder.tvtUsername.text = user.username

        // list on click
        holder.itemView.setOnClickListener {
            onItemClickCallback?.onItemClicked(listUser[holder.adapterPosition])
            val intent = Intent(holder.itemView.context,DetailUser::class.java)
            intent.putExtra(DetailUser.EXTRA_PERSON,user)
            holder.itemView.context.startActivity(intent)
        }
    }
    inner class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tvtUsername: TextView = itemView.findViewById(R.id.txt_username)
        val imgPhoto: ImageView = itemView.findViewById(R.id.img_photo)
    }
    interface OnItemClickCallback{
        fun onItemClicked(data: Person)
    }
}

