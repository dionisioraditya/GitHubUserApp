package com.example.githubuserapp.viewModel

import android.annotation.SuppressLint
import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.githubuserapp.model.DataUser
import com.example.githubuserapp.R
import com.example.githubuserapp.model.DataFavorit
import com.example.githubuserapp.model.db.DatabaseContract.UserColumns.Companion.COLUMN_NAME_AVATAR_URL
import com.example.githubuserapp.model.db.DatabaseContract.UserColumns.Companion.COLUMN_NAME_COMPANY
import com.example.githubuserapp.model.db.DatabaseContract.UserColumns.Companion.COLUMN_NAME_FOLLOWERS
import com.example.githubuserapp.model.db.DatabaseContract.UserColumns.Companion.COLUMN_NAME_FOLLOWING
import com.example.githubuserapp.model.db.DatabaseContract.UserColumns.Companion.COLUMN_NAME_LOCATION
import com.example.githubuserapp.model.db.DatabaseContract.UserColumns.Companion.COLUMN_NAME_NAME
import com.example.githubuserapp.model.db.DatabaseContract.UserColumns.Companion.COLUMN_NAME_REPOSITORY
import com.example.githubuserapp.model.db.DatabaseContract.UserColumns.Companion.COLUMN_NAME_USERNAME
import com.example.githubuserapp.model.db.UserHelper
import com.example.githubuserapp.view.SectionsPagerAdapter
import kotlinx.android.synthetic.main.activity_detail_user.*

class DetailUserActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var userHelper: UserHelper
    private var isFavorite = false
    private var favorites:DataFavorit? = null
    companion object {
        const val EXTRA_PERSON = "extra_person"
        const val EXTRA_FAVORITE = "extra_favorite"
        private val TAG = DetailUserActivity::class.java.simpleName
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_user)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setDetailUser()
        userHelper = UserHelper.getInstance(applicationContext)
        userHelper.open()
        fab_favorite.setOnClickListener(this)
    }

    private fun setDetailUser(){
        // initialization Component
        val tvUsername: TextView = findViewById(R.id.tv_username_detail)
        val ivImage: ImageView = findViewById(R.id.iv_photo_detail_user)
        val tvdescDetailUser:TextView = findViewById(R.id.desc_user_detail)

        // init variable intent parcelable
        val person = intent.getParcelableExtra(EXTRA_PERSON) as DataUser?
        Log.d(TAG, "check person detail: $person")
        val txtUsername = person?.username
        val txtName = person?.name
        val txtAvatar = person?.avatar
        val txtFollowers = person?.followers
        val txtFollowing = person?.following
        val txtLocation = person?.location
        val txtCompany = person?.company
        val txtRepository = person?.repository

        val lFollowers = resources.getString(R.string.followers)
        val lFollowing = resources.getString(R.string.following)
        val lCompany = resources.getString(R.string.company)
        val lRepository = resources.getString(R.string.repository)
        val lLocation = resources.getString(R.string.location)
        val lName = resources.getString(R.string.name)
        val textDesc = "$lName: $txtName \n$lLocation: $txtLocation \n$lCompany: $txtCompany \n$lRepository: $txtRepository \n$lFollowers: $txtFollowers \n$lFollowing: $txtFollowing"

        // Output Textview & ImageView
        Glide.with(this)
            .load(txtAvatar)
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
        val sectionsPagerAdapter =
            SectionsPagerAdapter(
                this,
                supportFragmentManager
            )
        sectionsPagerAdapter.username = username
        view_pager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(view_pager)
        supportActionBar?.elevation = 0f
    }
    @SuppressLint("ShowToast")
    override fun onClick(v: View) {
        val checked: Int = R.drawable.ic_baseline_favorite_red_24
        val unChecked: Int = R.drawable.ic_baseline_favorite_white_24
        if (v.id == R.id.fab_favorite){
            if (isFavorite){
                userHelper.deleteById(favorites?.username.toString())
                Toast.makeText(this, "Removed from Favorite list", Toast.LENGTH_SHORT)
                fab_favorite.setImageResource(unChecked)
                isFavorite = false

            }else{
                val values = ContentValues()
                val person = intent.getParcelableExtra(EXTRA_PERSON) as DataUser?
                Log.d(TAG, "check person detail: $person")
                val txtUsername = person?.username
                val txtName = person?.name
                val txtAvatar = person?.avatar
                val txtFollowers = person?.followers
                val txtFollowing = person?.following
                val txtLocation = person?.location
                val txtCompany = person?.company
                val txtRepository = person?.repository
                values.put(COLUMN_NAME_USERNAME, txtUsername)
                values.put(COLUMN_NAME_NAME, txtName)
                values.put(COLUMN_NAME_AVATAR_URL, txtAvatar)
                values.put(COLUMN_NAME_COMPANY, txtCompany)
                values.put(COLUMN_NAME_LOCATION, txtLocation)
                values.put(COLUMN_NAME_FOLLOWERS, txtFollowers)
                values.put(COLUMN_NAME_FOLLOWING, txtFollowing)
                values.put(COLUMN_NAME_REPOSITORY, txtRepository)
                isFavorite = true
                Log.d(TAG, "Check Database: $values")
                // insert to database
                userHelper.insert(values)
                Toast.makeText(this, "$txtUsername add to favorite", Toast.LENGTH_SHORT).show()
                fab_favorite.setImageResource(checked)
            }
        }
    }
}