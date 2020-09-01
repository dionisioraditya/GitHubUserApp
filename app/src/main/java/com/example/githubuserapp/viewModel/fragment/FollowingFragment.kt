package com.example.githubuserapp.viewModel.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserapp.model.DataFollowing
import com.example.githubuserapp.view.ListFollowingAdapter
import com.example.githubuserapp.R
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.fragment_following.*
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception


class FollowingFragment : Fragment() {
    private var listData:ArrayList<DataFollowing> = ArrayList()
    private lateinit var adapter: ListFollowingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_following, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val username = arguments?.getString(ARG_USERNAME)
        recyclerViewConfig()
        showRecyclerList()
        getDataFollowing(username)
    }
    companion object {
        fun newInstances(username: String?): FollowingFragment {
            val fragment =
                FollowingFragment()
            val bundle = Bundle()
            bundle.putString(ARG_USERNAME, username)
            fragment.arguments = bundle
            return fragment
        }
        private const val ARG_USERNAME = "username"
    }
    private fun getDataFollowing(id: String?){
        progressBar_following.visibility = View.INVISIBLE
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token d685ca4e8ebb09f9f58b35898326ad4cab3d8991")
        client.addHeader("User-Agent", "request")
        val url = "https://api.github.com/users/$id/following"
        client.get(url, object : AsyncHttpResponseHandler(){
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                progressBar_following.visibility = View.INVISIBLE
                val result = String(responseBody)
                try {
                    val jsonArray = JSONArray(result)
                    for(i in 0 until jsonArray.length()){
                        val responseObject = jsonArray.getJSONObject(i)
                        val username = responseObject.getString("login")
                        getDetailUser(username)
                    }
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
                progressBar_following.visibility = View.INVISIBLE
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
        progressBar_following.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token d685ca4e8ebb09f9f58b35898326ad4cab3d8991")
        client.addHeader("User-Agent", "request")
        val url = "https://api.github.com/users/$id"
        client.get(url, object :AsyncHttpResponseHandler(){
            override fun onSuccess(statusCode: Int, headers: Array<out Header>, responseBody: ByteArray
            ) {
                progressBar_following.visibility = View.INVISIBLE
                val result = String(responseBody)
                try {
                    val responseObject = JSONObject(result)
                    val username = responseObject.getString("login")
                    val name = responseObject.getString("name")
                    val avatar = responseObject.getString("avatar_url")
                    val followers = responseObject.getInt("followers")
                    val following = responseObject.getInt("following")
                    val repository = responseObject.getInt("public_repos")
                    val location = responseObject.getString("location")
                    val company = responseObject.getString("location")
                    val user = DataFollowing(
                        username,
                        avatar,
                        name,
                        followers,
                        following,
                        repository,
                        location,
                        company
                    )
                    listData.add(user)
                    adapter.setDataFollowing(listData)
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
                progressBar_following.visibility = View.INVISIBLE
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
        rv_list_following.layoutManager = LinearLayoutManager(activity)
        adapter = ListFollowingAdapter()
        rv_list_following.adapter = adapter
    }
    private fun recyclerViewConfig(){
        rv_list_following.layoutManager = LinearLayoutManager(rv_list_following.context)
        rv_list_following.setHasFixedSize(true)
        rv_list_following.addItemDecoration(DividerItemDecoration(rv_list_following.context, DividerItemDecoration.VERTICAL))
    }
}