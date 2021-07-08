package com.salwa.salwa.homepage.ui.order;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class OrderViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<OrderModel>> orderList = new MutableLiveData<>();
    final ArrayList<OrderModel> orderArrayList = new ArrayList<>();
    private static final String TAG = OrderViewModel.class.getSimpleName();

    // sisi kustomer yang ditamplikan hanya daftar orderan / pemesanan dirinya sendiri
    public void setOrderList(String customerUid) {

        try {
            orderArrayList.clear();
            FirebaseFirestore
                    .getInstance()
                    .collection("order")
                    .whereEqualTo("userUid", customerUid)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                OrderModel orderModel = new OrderModel();
                                orderModel.setAddedAt("" + document.get("addedAt"));
                                orderModel.setBookedBy("" + document.get("bookedBy"));
                                orderModel.setDescription("" + document.get("description"));
                                orderModel.setPrice(Integer.parseInt("" + document.get("price")));
                                orderModel.setProductDp("" + document.get("productDp"));
                                orderModel.setProductId("" + document.get("productId"));
                                orderModel.setAddress("" + document.get("address"));
                                orderModel.setPhone("" + document.get("phone"));
                                orderModel.setTitle("" + document.get("title"));
                                orderModel.setTotalProduct(Integer.parseInt("" + document.get("totalProduct")));
                                orderModel.setUserUid("" + document.get("userUid"));
                                orderModel.setStatus("" + document.get("paymentStatus"));
                                orderModel.setProofPayment("" + document.get("proofPayment"));
                                orderModel.setOrderId("" + document.get("orderId"));
                                orderModel.setShopId("" + document.get("shopId"));
                                orderModel.setPickup(document.getBoolean("isPickup"));
                                orderModel.setPickupDate("" + document.get("pickupDate"));

                                orderArrayList.add(orderModel);
                            }
                            orderList.postValue(orderArrayList);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setOrderListByAdminSide() {
        orderArrayList.clear();
        try {
            FirebaseFirestore
                    .getInstance()
                    .collection("order")
                    .orderBy("paymentStatus", Query.Direction.DESCENDING)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                OrderModel orderModel = new OrderModel();
                                orderModel.setAddedAt("" + document.get("addedAt"));
                                orderModel.setBookedBy("" + document.get("bookedBy"));
                                orderModel.setDescription("" + document.get("description"));
                                orderModel.setPrice(Integer.parseInt("" + document.get("price")));
                                orderModel.setProductDp("" + document.get("productDp"));
                                orderModel.setProductId("" + document.get("productId"));
                                orderModel.setAddress("" + document.get("address"));
                                orderModel.setPhone("" + document.get("phone"));
                                orderModel.setTitle("" + document.get("title"));
                                orderModel.setTotalProduct(Integer.parseInt("" + document.get("totalProduct")));
                                orderModel.setUserUid("" + document.get("userUid"));
                                orderModel.setStatus("" + document.get("paymentStatus"));
                                orderModel.setProofPayment("" + document.get("proofPayment"));
                                orderModel.setOrderId("" + document.get("orderId"));
                                orderModel.setShopId("" + document.get("shopId"));
                                orderModel.setPickup(document.getBoolean("isPickup"));
                                orderModel.setPickupDate("" + document.get("pickupDate"));

                                orderArrayList.add(orderModel);
                            }
                            orderList.postValue(orderArrayList);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setOrderListByShopId(String shopId) {
        orderArrayList.clear();
        try {
            FirebaseFirestore
                    .getInstance()
                    .collection("order")
                    .whereEqualTo("shopId", shopId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                OrderModel orderModel = new OrderModel();
                                orderModel.setAddedAt("" + document.get("addedAt"));
                                orderModel.setBookedBy("" + document.get("bookedBy"));
                                orderModel.setDescription("" + document.get("description"));
                                orderModel.setPrice(Integer.parseInt("" + document.get("price")));
                                orderModel.setProductDp("" + document.get("productDp"));
                                orderModel.setProductId("" + document.get("productId"));
                                orderModel.setAddress("" + document.get("address"));
                                orderModel.setPhone("" + document.get("phone"));
                                orderModel.setTitle("" + document.get("title"));
                                orderModel.setTotalProduct(Integer.parseInt("" + document.get("totalProduct")));
                                orderModel.setUserUid("" + document.get("userUid"));
                                orderModel.setStatus("" + document.get("paymentStatus"));
                                orderModel.setProofPayment("" + document.get("proofPayment"));
                                orderModel.setOrderId("" + document.get("orderId"));
                                orderModel.setShopId("" + document.get("shopId"));
                                orderModel.setPickup(document.getBoolean("isPickup"));
                                orderModel.setPickupDate("" + document.get("pickupDate"));

                                orderArrayList.add(orderModel);
                            }
                            orderList.postValue(orderArrayList);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public LiveData<ArrayList<OrderModel>> getOrderList() {
        return orderList;
    }


}