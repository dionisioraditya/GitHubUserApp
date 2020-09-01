package com.example.githubuserapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DataFollowers (
    var username: String?= "",
    var avatar: String?="",
    var name: String?="",
    var followers: Int= 0,
    var following: Int= 0,
    var repository: Int= 0,
    var location:String?="",
    var company: String? = ""
):Parcelable