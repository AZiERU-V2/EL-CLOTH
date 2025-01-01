package com.example.el_clothstoree.Helper

import android.content.Context
import android.widget.Toast
import com.example.el_clothstoree.Model.ItemsModel

class ManagementFavorite(val context: Context) {

    private val tinyDB = TinyDB(context)

    // Adds an item to the favorites list
    fun addFavorite(item: ItemsModel) {
        val favoriteList = getFavoriteList()

        // Check if item is already in the favorites list based on title
        if (favoriteList.contains(item)) {
            Toast.makeText(context, "Already in Favorites", Toast.LENGTH_SHORT).show()
        } else {
            favoriteList.add(item)
            tinyDB.putListObject("FavoriteList", favoriteList)
            Toast.makeText(context, "Added to Favorites", Toast.LENGTH_SHORT).show()
        }
    }

    // Removes an item from the favorites list
    fun removeFavorite(item: ItemsModel) {
        val favoriteList = getFavoriteList()

        // Removes the item by comparing the entire object (ItemsModel will use title to compare equality)
        if (favoriteList.remove(item)) {
            tinyDB.putListObject("FavoriteList", favoriteList)
            Toast.makeText(context, "Removed from Favorites", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Item not found in Favorites", Toast.LENGTH_SHORT).show()
        }
    }

    // Retrieves the list of favorite items from TinyDB
    fun getFavoriteList(): ArrayList<ItemsModel> {
        return tinyDB.getListObject("FavoriteList") ?: arrayListOf()  // Ensure a non-null list is returned
    }

    // Checks if the item is in the favorite list
    fun isFavorite(item: ItemsModel): Boolean {
        return getFavoriteList().contains(item)  // Check for the presence of the item in the favorite list
    }
}
