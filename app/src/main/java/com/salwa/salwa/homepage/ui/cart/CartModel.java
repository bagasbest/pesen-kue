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
    private String shopId;
    private String title;
    private int totalProduct;
    private String userUid;
    private String cartId;

    public CartModel(){}


    protected CartModel(Parcel in) {
        addedAt = in.readString();
        bookedBy = in.readString();
        description = in.readString();
        price = in.readInt();
        productDp = in.readString();
        productId = in.readString();
        shopId = in.readString();
        title = in.readString();
        totalProduct = in.readInt();
        userUid = in.readString();
        cartId = in.readString();
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

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
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

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(addedAt);
        parcel.writeString(bookedBy);
        parcel.writeString(description);
        parcel.writeInt(price);
        parcel.writeString(productDp);
        parcel.writeString(productId);
        parcel.writeString(shopId);
        parcel.writeString(title);
        parcel.writeInt(totalProduct);
        parcel.writeString(userUid);
        parcel.writeString(cartId);
    }
}
