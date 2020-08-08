package com.example.githubuserapp

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Person(
    var username: String?= "",
    var followers: Int?= 0,
    var following: Int?= 0,
    var avatar:String?= "",
    var repository: String?= "",
    var location:String?=""
): Parcelable