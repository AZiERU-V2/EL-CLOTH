package com.example.el_clothstoree.Helper

import android.content.Context
import android.widget.Toast
import com.example.el_clothstoree.Model.ItemsModel


class ManagmentCart(val context: Context) {

    private val tinyDB = TinyDB(context)

    fun insertItem(item: ItemsModel) {
        var listProduk = getListCart()
        val existAlready = listProduk.any { it.title == item.title }
        val index = listProduk.indexOfFirst { it.title == item.title }

        if (existAlready) {
            listProduk[index].numberInCart = item.numberInCart
        } else {
            listProduk.add(item)
        }
        tinyDB.putListObject("CartList", listProduk)
        Toast.makeText(context, "Added to your Cart", Toast.LENGTH_SHORT).show()
    }

    fun getListCart(): java.util.ArrayList<ItemsModel> {
        return tinyDB.getListObject("CartList") ?: arrayListOf()
    }

    fun minusItem(listProduk: ArrayList<ItemsModel>, position: Int, listener: ChangeNumberItemsListener) {
        if (listProduk[position].numberInCart == 1) {
            listProduk.removeAt(position)
        } else {
            listProduk[position].numberInCart--
        }
        tinyDB.putListObject("CartList", listProduk)
        listener.onChanged()
    }

    fun plusItem(listProduk: ArrayList<ItemsModel>, position: Int, listener: ChangeNumberItemsListener) {
        listProduk[position].numberInCart++
        tinyDB.putListObject("CartList", listProduk)
        listener.onChanged()
    }

    fun getTotalFee(): Double {
        val listProduk = getListCart()
        var fee = 0.0
        for (item in listProduk) {
            fee += item.price * item.numberInCart
        }
        return fee
    }
}