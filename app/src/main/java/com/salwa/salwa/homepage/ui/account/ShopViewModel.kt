package com.salwa.salwa.homepage.ui.account

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

class ShopViewModel : ViewModel() {
    private val shopList = MutableLiveData<ArrayList<ShopModel>>()
    private val TAG = ShopViewModel::class.java.simpleName
    private val listItem = ArrayList<ShopModel>()

    fun setProductSold(shopId: String, month: String) {
        listItem.clear()

        try {
            FirebaseFirestore
                .getInstance()
                .collection("shop")
                .document(shopId)
                .collection("transaction")
                .whereEqualTo("month", month)
                .get()
                .addOnSuccessListener { documents ->
                    for(document in documents) {
                        val model = ShopModel()
                        model.name = document.data["name"].toString()
                        model.quantity = document.data["quantity"].toString().toInt()

                        listItem.add(model)
                    }
                    shopList.postValue(listItem)
                }
                .addOnFailureListener {
                    Log.e(TAG, it.message.toString())
                }
        } catch (error: Exception) {
            error.printStackTrace()
        }
    }

    fun getProductSold() : LiveData<ArrayList<ShopModel>> {
        return shopList
    }
}