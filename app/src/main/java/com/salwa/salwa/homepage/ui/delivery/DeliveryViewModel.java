package com.salwa.salwa.homepage.ui.delivery;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class DeliveryViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<DeliveryModel>> deliveryList = new MutableLiveData<>();
    final ArrayList<DeliveryModel> deliveryModelArrayList = new ArrayList<>();
    private static final String TAG = DeliveryViewModel.class.getSimpleName();

    // sisi kustomer yang ditamplikan hanya daftar orderan sendiri
    public void setDeliveryList(String customerUid) {
        deliveryModelArrayList.clear();
        try {
            FirebaseFirestore
                    .getInstance()
                    .collection("delivery")
                    .whereEqualTo("userUid", customerUid)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                DeliveryModel deliveryModel = new DeliveryModel();
                                deliveryModel.setAddedAt("" + document.get("addedAt"));
                                deliveryModel.setBookedBy("" + document.get("bookedBy"));
                                deliveryModel.setPrice(Integer.parseInt("" + document.get("price")));
                                deliveryModel.setProductDp("" + document.get("productDp"));
                                deliveryModel.setAddress("" + document.get("address"));
                                deliveryModel.setPhone("" + document.get("phone"));
                                deliveryModel.setTitle("" + document.get("title"));
                                deliveryModel.setTotalProduct(Integer.parseInt("" + document.get("totalProduct")));
                                deliveryModel.setUserUid("" + document.get("userUid"));
                                deliveryModel.setDeliveryStatus("" + document.get("deliveryStatus"));
                                deliveryModel.setDeliveryId("" + document.get("deliveryId"));
                                deliveryModel.setShopId("" + document.get("shopId"));
                                deliveryModel.setPickupDate("" + document.get("pickupDate"));
                                deliveryModel.setPickup(document.getBoolean("isPickup"));
                                deliveryModel.setProductId("" + document.get("productId"));
                                deliveryModel.setCod("" + document.get("cod"));
                                deliveryModel.setOrderId("" + document.get("orderId"));

                                deliveryModelArrayList.add(deliveryModel);
                            }
                            deliveryList.postValue(deliveryModelArrayList);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // sisi admin yang ditamplikan seluruh daftar orderan yang siap dikirim
    public void setDeliveryBySellerSide(String shopId) {
        deliveryModelArrayList.clear();

        try {
            FirebaseFirestore
                    .getInstance()
                    .collection("delivery")
                    .whereEqualTo("shopId", shopId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                DeliveryModel deliveryModel = new DeliveryModel();
                                deliveryModel.setAddedAt("" + document.get("addedAt"));
                                deliveryModel.setBookedBy("" + document.get("bookedBy"));
                                deliveryModel.setPrice(Integer.parseInt("" + document.get("price")));
                                deliveryModel.setProductDp("" + document.get("productDp"));
                                deliveryModel.setAddress("" + document.get("address"));
                                deliveryModel.setPhone("" + document.get("phone"));
                                deliveryModel.setTitle("" + document.get("title"));
                                deliveryModel.setTotalProduct(Integer.parseInt("" + document.get("totalProduct")));
                                deliveryModel.setUserUid("" + document.get("userUid"));
                                deliveryModel.setDeliveryStatus("" + document.get("deliveryStatus"));
                                deliveryModel.setDeliveryId("" + document.get("deliveryId"));
                                deliveryModel.setShopId("" + document.get("shopId"));
                                deliveryModel.setPickupDate("" + document.get("pickupDate"));
                                deliveryModel.setPickup(document.getBoolean("isPickup"));
                                deliveryModel.setProductId("" + document.get("productId"));
                                deliveryModel.setCod("" + document.get("cod"));
                                deliveryModel.setOrderId("" + document.get("orderId"));



                                deliveryModelArrayList.add(deliveryModel);
                            }
                            deliveryList.postValue(deliveryModelArrayList);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // menampilkan semua delivery (khusus admin
    public void setAllDelivery() {
        deliveryModelArrayList.clear();
        try {
            FirebaseFirestore
                    .getInstance()
                    .collection("delivery")
                    .orderBy("deliveryStatus", Query.Direction.ASCENDING)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                DeliveryModel deliveryModel = new DeliveryModel();
                                deliveryModel.setAddedAt("" + document.get("addedAt"));
                                deliveryModel.setBookedBy("" + document.get("bookedBy"));
                                deliveryModel.setPrice(Integer.parseInt("" + document.get("price")));
                                deliveryModel.setProductDp("" + document.get("productDp"));
                                deliveryModel.setAddress("" + document.get("address"));
                                deliveryModel.setPhone("" + document.get("phone"));
                                deliveryModel.setTitle("" + document.get("title"));
                                deliveryModel.setTotalProduct(Integer.parseInt("" + document.get("totalProduct")));
                                deliveryModel.setUserUid("" + document.get("userUid"));
                                deliveryModel.setDeliveryStatus("" + document.get("deliveryStatus"));
                                deliveryModel.setDeliveryId("" + document.get("deliveryId"));
                                deliveryModel.setShopId("" + document.get("shopId"));
                                deliveryModel.setPickupDate("" + document.get("pickupDate"));
                                deliveryModel.setPickup(document.getBoolean("isPickup"));
                                deliveryModel.setProductId("" + document.get("productId"));
                                deliveryModel.setCod("" + document.get("cod"));
                                deliveryModel.setOrderId("" + document.get("orderId"));

                                deliveryModelArrayList.add(deliveryModel);
                            }
                            deliveryList.postValue(deliveryModelArrayList);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public LiveData<ArrayList<DeliveryModel>> getDeliveryList() {
        return deliveryList;
    }
}
