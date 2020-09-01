package com.example.githubuserapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DataFavorit(
    var id: Int?,
    var username: String?,
    var avatar: String?,
    var company: String?,
    var location: String?
):Parcelable