package com.example.githubuserapp.model.db


import android.provider.BaseColumns

internal class DatabaseContract {
    internal class UserColumns : BaseColumns{
        companion object{
            const val TABLE_NAME = "favorite_user"
            const val COLUMN_NAME_USERNAME = "username"
            const val COLUMN_NAME_NAME = "name"
            const val COLUMN_NAME_AVATAR_URL = "avatar_url"
            const val COLUMN_NAME_COMPANY = "company"
            const val COLUMN_NAME_LOCATION = "location"
            const val COLUMN_NAME_FOLLOWERS = "followers"
            const val COLUMN_NAME_FOLLOWING = "following"
            const val COLUMN_NAME_REPOSITORY = "respository"
        }
    }
}