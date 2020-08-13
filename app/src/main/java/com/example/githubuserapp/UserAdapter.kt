package com.example.githubuserapp

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions


class ListUserAdapter : RecyclerView.Adapter<ListUserAdapter.ListViewHolder>(){
    private  var onItemClickCallback: OnItemClickCallback? = null
    private val mData = ArrayList<Person>()

    companion object{
        private val TAG = ListUserAdapter::class.java.simpleName
    }
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_user, viewGroup,false)
        return ListViewHolder(view)
    }
    override fun getItemCount(): Int {
        return mData.size
    }
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder:ListViewHolder, position: Int) {
        val user = mData[position]
        Log.d(TAG, "test ${user.avatar}")
        Glide.with(holder.itemView.context)
            .load(user.avatar)
            .apply(RequestOptions().override(110,110))
            .into(holder.imgPhoto)
        holder.tvtUsername.text = user.username
        holder.tvName.text = user.name
        val lFollowers = holder.itemView.context.resources.getString(R.string.followers)
        val lFollowing = holder.itemView.context.resources.getString(R.string.following)
        holder.tvFollowers.text = "$lFollowers: ${user.followers}"
        holder.tvFollowing.text = "$lFollowing: ${user.following}"

        // list on click
        holder.itemView.setOnClickListener {
            onItemClickCallback?.onItemClicked(mData[holder.adapterPosition])
            val dataUser = Person(
                user.username,
                user.avatar,
                user.name,
                user.followers,
                user.following,
                user.repository,
                user.location,
                user.company
            )
            val intent = Intent(holder.itemView.context,DetailUser::class.java)
            intent.putExtra(DetailUser.EXTRA_PERSON,dataUser)
            holder.itemView.context.startActivity(intent)
        }
    }
    inner class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tvtUsername: TextView = itemView.findViewById(R.id.txt_username)
        val imgPhoto: ImageView = itemView.findViewById(R.id.img_photo)
        val tvName: TextView = itemView.findViewById(R.id.tv_name)
        val tvFollowers: TextView = itemView.findViewById(R.id.tv_followers)
        val tvFollowing: TextView = itemView.findViewById(R.id.tv_following)
    }
    interface OnItemClickCallback{
        fun onItemClicked(data: Person)
    }
    fun setData(items: ArrayList<Person>){
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }
}

