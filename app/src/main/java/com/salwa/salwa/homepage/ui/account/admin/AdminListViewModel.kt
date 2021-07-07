package com.salwa.salwa.homepage.ui.account.admin

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

class AdminListViewModel : ViewModel(){
    private val sellerList = MutableLiveData<ArrayList<AdminListModel>>()
    private val TAG = AdminListModel::class.java.simpleName
    private val listItem = ArrayList<AdminListModel>()

    fun setSeller() {
        listItem.clear()

        try {
            FirebaseFirestore
                .getInstance()
                .collection("shop")
                .whereEqualTo("status", "review")
                .get()
                .addOnSuccessListener { documents ->
                    for(document in documents) {
                        val model = AdminListModel()
                        model.name = document.data["name"].toString()
                        model.address = document.data["address"].toString()
                        model.dp = document.data["dp"].toString()
                        model.phone = document.data["phone"].toString()
                        model.status = document.data["status"].toString()
                        model.uid = document.data["uid"].toString()

                        listItem.add(model)
                    }
                    sellerList.postValue(listItem)
                }
                .addOnFailureListener {
                    Log.e(TAG, it.message.toString())
                }
        } catch (error: Exception) {
            error.printStackTrace()
        }
    }

    fun getSeller() : LiveData<ArrayList<AdminListModel>> {
        return sellerList
    }
}