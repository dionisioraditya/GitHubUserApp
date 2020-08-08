package com.example.githubuserapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class DetailUser : AppCompatActivity() {
    companion object {
        const val EXTRA_PERSON = "extra_person"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_user)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // initialization Component
        val tvUsername: TextView = findViewById(R.id.tv_username_detail)
        val ivImage: ImageView = findViewById(R.id.iv_photo_detail_user)
        val tvdescDetailUser:TextView = findViewById(R.id.desc_user_detail)

        // init variable intent parcelable
        val person = intent.getParcelableExtra(EXTRA_PERSON) as Person
        val txtUsername = person.username
        val textDesc = "Followers: ${person.followers} \nFollowing: ${person.following}"

        // Output Textview & ImageView
        Glide.with(this)
            .load(person.avatar)
            .apply(RequestOptions().override(150,150))
            .into(ivImage)
        tvUsername.text = txtUsername
        tvdescDetailUser.text = textDesc

        val title = "User $txtUsername Detail"
        setActionBarTitle(title)
    }
    private fun setActionBarTitle(str: String){
        if (supportActionBar!= null){
            (supportActionBar as ActionBar).title = str
        }
    }
}