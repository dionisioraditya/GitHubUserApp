package com.example.githubuserapp

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: ListUserAdapter
    private var listData: ArrayList<Person> = ArrayList()
    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerViewConfig()
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
                val result = String(responseBody)
                Log.d(TAG, "result: $result")
                try {
                    val items = JSONArray(result)
                    Log.d(TAG,"items: $items")
                    for (i in 0 until items.length()){
                        val item = items.getJSONObject(i)
                        val username = item.getString("login")
                        getDetailUser(username)
                    }
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
    private fun getDataSearch(id: String){
        progressBar.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token d685ca4e8ebb09f9f58b35898326ad4cab3d8991")
        client.addHeader("User-Agent", "request")
        val url = "https://api.github.com/search/users?q=$id"
        client.get(url, object :AsyncHttpResponseHandler(){
            override fun onSuccess(statusCode: Int, headers: Array<out Header>, responseBody: ByteArray) {
                progressBar.visibility = View.INVISIBLE
                val result = String(responseBody)
                Log.d(TAG,"result-search: $result")
                try {
                    val responseObject = JSONObject(result)
                    val items = responseObject.getJSONArray("items")
                    Log.d(TAG,"items-search: $items")
                    for (i in 0 until items.length()) {
                        val item = items.getJSONObject(i)
                        val username = item.getString("login")
                        getDetailUser(username)
                    }
                }catch (e:Exception){
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_LONG).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
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

    private fun getDetailUser(id: String){
        progressBar.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token d685ca4e8ebb09f9f58b35898326ad4cab3d8991")
        client.addHeader("User-Agent", "request")
        val url = "https://api.github.com/users/$id"
        client.get(url, object :AsyncHttpResponseHandler(){
            override fun onSuccess(statusCode: Int, headers: Array<out Header>, responseBody: ByteArray
            ) {
                progressBar.visibility = View.INVISIBLE
                val result = String(responseBody)
                Log.d(TAG,"result-detail: $result")
                try {
                    val responseObject = JSONObject(result)
                    Log.d(TAG,"items-detail: $result")
                    val username = responseObject.getString("login")
                    val name = responseObject.getString("name")
                    val avatar = responseObject.getString("avatar_url")
                    val followers = responseObject.getInt("followers")
                    val following = responseObject.getInt("following")
                    val repository = responseObject.getInt("public_repos")
                    val location = responseObject.getString("location")
                    val company = responseObject.getString("company")
                    val user = Person(username, avatar, name, followers, following, repository, location, company)
                    listData.add(user)
                    adapter.setData(listData)
                }catch (e:Exception){
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_LONG).show()
                    e.printStackTrace()
                }
            }
            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
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
        adapter = ListUserAdapter()
        rv_list.adapter = adapter
        adapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback{
            override fun onItemClicked(data: Person) {
                messageItemClicked(data)
                showSelectedData(data)
            }
        })
    }
    private fun recyclerViewConfig(){
        rv_list.layoutManager = LinearLayoutManager(rv_list.context)
        rv_list.setHasFixedSize(true)
        rv_list.addItemDecoration(DividerItemDecoration(rv_list.context, DividerItemDecoration.VERTICAL))
    }
    private fun messageItemClicked(user:Person){
        Toast.makeText(this, "you Choose" + user.username, Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu,menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)

        searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                Toast.makeText(this@MainActivity, query, Toast.LENGTH_SHORT).show()
                if (query.isEmpty()){
                    return true
                } else{
                    progressBar.visibility = View.INVISIBLE
                    listData.clear()
                    getDataSearch(query)
                }
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.action_change_settings){
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)
        }
        return super.onOptionsItemSelected(item)
    }
    private fun showSelectedData(dataUsers : Person){
        val dataUser = Person(
            dataUsers.username,
            dataUsers.avatar,
            dataUsers.name,
            dataUsers.followers,
            dataUsers.following,
            dataUsers.repository,
            dataUsers.location,
            dataUsers.company
        )
        val intentToDetail = Intent(this@MainActivity, DetailUser::class.java)
        Log.d(TAG, "dataUser Intent $dataUser")
        intentToDetail.putExtra(DetailUser.EXTRA_PERSON, dataUser)
        startActivity(intentToDetail)
    }
}