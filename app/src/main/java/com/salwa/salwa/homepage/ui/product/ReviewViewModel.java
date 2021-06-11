package com.salwa.salwa.homepage.ui.product;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ReviewViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<ReviewModel>> listReview = new MutableLiveData<>();
    private static final String TAG = ReviewViewModel.class.getSimpleName();

    public void setReviewList(String productId) {
        final ArrayList<ReviewModel> reviewArrayList = new ArrayList<>();

        try {
            FirebaseFirestore
                    .getInstance()
                    .collection("product")
                    .document(productId)
                    .collection("review")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    ReviewModel reviewModel = new ReviewModel();
                                    reviewModel.setName("" + document.get("name"));
                                    reviewModel.setReviewText("" + document.get("reviewTxt"));
                                    reviewModel.setId("" + document.get("uid"));
                                    reviewModel.setTimeStamp("" + document.get("addedAt"));


                                    reviewArrayList.add(reviewModel);
                                }
                                listReview.postValue(reviewArrayList);
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public LiveData<ArrayList<ReviewModel>> getReviewList() {
        return listReview;
    }
}
