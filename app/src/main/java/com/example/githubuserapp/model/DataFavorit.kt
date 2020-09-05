package com.example.githubuserapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DataFavorit(
    var username: String?,
    var name: String?,
    var avatar: String?,
    var followers: String?,
    var following:String?,
    var company: String?,
    var location: String?,
    var repository: String?
):Parcelable