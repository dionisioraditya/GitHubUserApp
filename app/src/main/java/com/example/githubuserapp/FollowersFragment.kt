package com.example.githubuserapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.fragment_followers.*
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class FollowersFragment : Fragment() {
    private lateinit var adapter: ListUserFollowersAdapter
    private var listData: ArrayList<DataFollowers> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_followers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val username = arguments?.getString(ARG_USERNAME)
        Log.d(TAG, "username-Followers $username")
        recyclerViewConfig()
        showRecyclerList()
        getDataFollowers(username)
    }

    companion object {
        private val TAG = FollowersFragment::class.java.simpleName
        fun newInstances(username: String?):FollowersFragment{
            val fragment = FollowersFragment()
            val bundle = Bundle()
            bundle.putString(ARG_USERNAME, username)
            fragment.arguments = bundle
            return fragment
        }
        private const val ARG_USERNAME = "username"
    }
    private fun getDataFollowers(id: String?){
        progressBar_followers.visibility = View.INVISIBLE
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token d685ca4e8ebb09f9f58b35898326ad4cab3d8991")
        client.addHeader("User-Agent", "request")
        val url = "https://api.github.com/users/$id/followers"
        client.get(url, object :AsyncHttpResponseHandler(){
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                progressBar_followers.visibility = View.INVISIBLE
                val result = String(responseBody)
                try {
                    val jsonArray = JSONArray(result)
                    for(i in 0 until jsonArray.length()){
                        val responseObject = jsonArray.getJSONObject(i)
                        val username = responseObject.getString("login")
                        getDetailUser(username)
                    }
                }catch (e:Exception){
                    Toast.makeText(activity, e.message,Toast.LENGTH_LONG).show()
                    e.printStackTrace()
                }
            }
            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                progressBar_followers.visibility = View.INVISIBLE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error?.message}"
                }
                Toast.makeText(activity,errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun getDetailUser(id: String){
        progressBar_followers.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token d685ca4e8ebb09f9f58b35898326ad4cab3d8991")
        client.addHeader("User-Agent", "request")
        val url = "https://api.github.com/users/$id"
        client.get(url, object :AsyncHttpResponseHandler(){
            override fun onSuccess(statusCode: Int, headers: Array<out Header>, responseBody: ByteArray
            ) {
                progressBar_followers.visibility = View.INVISIBLE
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
                    val company = responseObject.getString("location")
                    val user = DataFollowers(username, avatar, name, followers, following, repository, location, company)
                    listData.add(user)
                    adapter.setDataFollower(listData)
                }catch (e: Exception){
                    Toast.makeText(activity, e.message, Toast.LENGTH_LONG).show()
                    e.printStackTrace()
                }
            }
            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                progressBar_followers.visibility = View.INVISIBLE
                val errorMessage = when (statusCode){
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error?.message}"
                }
                Toast.makeText(activity,errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun showRecyclerList(){
        rv_list_followers.layoutManager = LinearLayoutManager(activity)
        adapter = ListUserFollowersAdapter()
        rv_list_followers.adapter = adapter
    }

    private fun recyclerViewConfig(){
        rv_list_followers.layoutManager = LinearLayoutManager(rv_list_followers.context)
        rv_list_followers.setHasFixedSize(true)
        rv_list_followers.addItemDecoration(DividerItemDecoration(rv_list_followers.context, DividerItemDecoration.VERTICAL))
    }
}