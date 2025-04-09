package com.example.geogenie.geminiJson

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class geminiJsonResponseItem(
    val description: String,
    val name: String,
    val rating: String
):Parcelable