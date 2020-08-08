package com.example.githubuserapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    //private var listData: ArrayList<Person> = ArrayList()
    private lateinit var adapter: ListUserAdapter
    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showRecyclerList()
        getListDataUser()
    }
    private fun getListDataUser(){
        progressBar.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users"
        client.addHeader("Authorization", "token d685ca4e8ebb09f9f58b35898326ad4cab3d8991")
        client.addHeader("User-Agent", "request")
        client.get(url,object :AsyncHttpResponseHandler(){
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                progressBar.visibility = View.INVISIBLE
                // Parsing JSON
                val listUser = ArrayList<Person>()
                val result = String(responseBody)
                Log.d(TAG, "result: $result")
                try {
                    //val responseObject = JSONObject(result)
                    val items = JSONArray(result)
                    for (i in 0 until items.length()){
                        val item = items.getJSONObject(i)
                        val username = item.getString("login")
                        val avatar = item.getString("avatar_url")
                        val followers = item.getString("followers_url")
                        val following = item.getString("following_url")
                        val user = Person(username,followers, following, avatar)
                        listUser.add(user)
                    }
                    // add data to recyclerView
                    //listData.addAll(listUser)
                    adapter.setData(listUser)
                } catch (e:Exception){
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_LONG).show()
                    e.printStackTrace()
                }
            }
            override fun onFailure(statusCode: Int, headers: Array<Header?>?, responseBody: ByteArray?, error: Throwable?) {
                progressBar.visibility = View.INVISIBLE
                val errorMessage = when (statusCode){
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error?.message}"
                }
                Toast.makeText(this@MainActivity,errorMessage,Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun showRecyclerList(){
        rv_list.layoutManager = LinearLayoutManager(this)
        val listUserAdapter = ListUserAdapter()
        rv_list.adapter = listUserAdapter
        //listUserAdapter.notifyDataSetChanged()
        listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback{
            override fun onItemClicked(data: Person) {
                messageItemClicked(data)
            }
        })
    }
    private fun messageItemClicked(user:Person){
        Toast.makeText(this, "you Choose" + user.username, Toast.LENGTH_SHORT).show()
    }

}