package com.example.el_clothstoree.Model

import android.os.Parcel
import android.os.Parcelable

data class ItemsModel(
    var title: String = "",
    var description: ArrayList<String> = ArrayList(),
    var picUrl: ArrayList<String> = ArrayList(),
    var model: ArrayList<String> = ArrayList(),
    var price: Double = 0.0,
    var rating: Double = 0.0,
    var numberInCart: Int = 0,
    var showRecommended: Boolean = false,
    var categoryId: String = "",
    var colors: ArrayList<String> = ArrayList()
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.createStringArrayList() ?: ArrayList(),
        parcel.createStringArrayList() ?: ArrayList(),
        parcel.createStringArrayList() ?: ArrayList(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),
        parcel.readString() ?: "",
        parcel.createStringArrayList() ?: ArrayList()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeStringList(description)
        parcel.writeStringList(picUrl)
        parcel.writeStringList(model)
        parcel.writeDouble(price)
        parcel.writeDouble(rating)
        parcel.writeInt(numberInCart)
        parcel.writeByte(if (showRecommended) 1 else 0)
        parcel.writeString(categoryId)
        parcel.writeStringList(colors)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<ItemsModel> {
        override fun createFromParcel(parcel: Parcel): ItemsModel = ItemsModel(parcel)
        override fun newArray(size: Int): Array<ItemsModel?> = arrayOfNulls(size)
    }

    // Override equals to compare items based on title or other unique fields
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ItemsModel) return false
        return title == other.title // You can expand this logic if necessary
    }

    // Override hashCode to ensure consistency with equals
    override fun hashCode(): Int {
        return title.hashCode() // Or use a combination of fields if necessary
    }
}
