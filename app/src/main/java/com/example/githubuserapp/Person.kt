package com.example.githubuserapp

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Person(
    var username: String?= "",
    var followers: String?= "",
    var following: String?= "",
    var avatar:String?= "",
    var repository: String?= "",
    var location:String?=""
): Parcelable