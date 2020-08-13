package com.example.githubuserapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_detail_user.*

class DetailUser : AppCompatActivity() {
    companion object {
        const val EXTRA_PERSON = "extra_person"
        private val TAG = DetailUser::class.java.simpleName
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_user)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setDetailUser()
    }

    private fun setDetailUser(){
        // initialization Component
        progressBar_detail.visibility = View.INVISIBLE
        val tvUsername: TextView = findViewById(R.id.tv_username_detail)
        val ivImage: ImageView = findViewById(R.id.iv_photo_detail_user)
        val tvdescDetailUser:TextView = findViewById(R.id.desc_user_detail)

        // init variable intent parcelable
        val person = intent.getParcelableExtra(EXTRA_PERSON) as Person?
        Log.d(TAG, "check person detail: $person")
        val txtUsername = person?.username
        val lFollowers = resources.getString(R.string.followers)
        val lFollowing = resources.getString(R.string.following)
        val lCompany = resources.getString(R.string.company)
        val lRepository = resources.getString(R.string.repository)
        val lLocation = resources.getString(R.string.location)
        val lName = resources.getString(R.string.name)
        val textDesc = "$lName: ${person?.name} \n$lLocation: ${person?.location} \n$lCompany: ${person?.company} \n$lRepository: ${person?.repository}\n$lFollowers: ${person?.followers} \n$lFollowing: ${person?.following}"

        // Output Textview & ImageView
        Glide.with(this)
            .load(person?.avatar)
            .apply(RequestOptions().override(150,150))
            .into(ivImage)
        tvUsername.text = txtUsername
        tvdescDetailUser.text = textDesc
        val mUsername = person?.username
        setViewPagerAdapter(mUsername)
        val title = "$txtUsername"
        setActionBarTitle(title)
    }
    private fun setActionBarTitle(str: String){
        if (supportActionBar!= null){
            (supportActionBar as ActionBar).title = str
        }
    }
    private fun setViewPagerAdapter(username: String?){
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        sectionsPagerAdapter.username = username
        view_pager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(view_pager)
        supportActionBar?.elevation = 0f
    }
}