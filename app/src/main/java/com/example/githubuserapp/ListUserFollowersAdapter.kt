package com.example.githubuserapp

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class ListUserFollowersAdapter:RecyclerView.Adapter<ListUserFollowersAdapter.ListViewHolder>() {
    private val mData = ArrayList<DataFollowers>()
    companion object{
        private val TAG = ListUserFollowersAdapter::class.java.simpleName
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_user,viewGroup,false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val followersData = mData[position]
        Glide.with(holder.itemView.context)
            .load(followersData.avatar)
            .apply(RequestOptions().override(110, 110))
            .into(holder.imgPhoto)
        holder.tvUsername.text = followersData.username
        holder.tvName.text = followersData.name
        holder.tvFollowers.text = "Followers: ${followersData.followers}"
        holder.tvFollowing.text = "Following: ${followersData.following}"
    }
    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgPhoto:ImageView = itemView.findViewById(R.id.img_photo)
        val tvUsername: TextView = itemView.findViewById(R.id.txt_username)
        val tvName: TextView = itemView.findViewById(R.id.tv_name)
        val tvFollowers: TextView = itemView.findViewById(R.id.tv_followers)
        val tvFollowing: TextView = itemView.findViewById(R.id.tv_following)
    }
    fun setDataFollower(items: ArrayList<DataFollowers>){
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }
}