package com.salwa.salwa.homepage.ui.cart;

import android.os.Parcel;
import android.os.Parcelable;

public class CartModel implements Parcelable {
    private String addedAt;
    private String bookedBy;
    private String description;
    private int price;
    private String productDp;
    private String productId;
    private String title;
    private int totalProduct;
    private String userUid;

    public CartModel(){}

    public String getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(String addedAt) {
        this.addedAt = addedAt;
    }

    public String getBookedBy() {
        return bookedBy;
    }

    public void setBookedBy(String bookedBy) {
        this.bookedBy = bookedBy;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getProductDp() {
        return productDp;
    }

    public void setProductDp(String productDp) {
        this.productDp = productDp;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTotalProduct() {
        return totalProduct;
    }

    public void setTotalProduct(int totalProduct) {
        this.totalProduct = totalProduct;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    protected CartModel(Parcel in) {
        addedAt = in.readString();
        bookedBy = in.readString();
        description = in.readString();
        price = in.readInt();
        productDp = in.readString();
        productId = in.readString();
        title = in.readString();
        totalProduct = in.readInt();
        userUid = in.readString();
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(addedAt);
        dest.writeString(bookedBy);
        dest.writeString(description);
        dest.writeInt(price);
        dest.writeString(productDp);
        dest.writeString(productId);
        dest.writeString(title);
        dest.writeInt(totalProduct);
        dest.writeString(userUid);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CartModel> CREATOR = new Creator<CartModel>() {
        @Override
        public CartModel createFromParcel(Parcel in) {
            return new CartModel(in);
        }

        @Override
        public CartModel[] newArray(int size) {
            return new CartModel[size];
        }
    };
}
