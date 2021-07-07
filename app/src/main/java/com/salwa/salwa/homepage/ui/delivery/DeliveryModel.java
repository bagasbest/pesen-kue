package com.salwa.salwa.homepage.ui.delivery;

import android.os.Parcel;
import android.os.Parcelable;

public class DeliveryModel implements Parcelable {

    private String addedAt;
    private String bookedBy;
    private int price;
    private String productDp;
    private String address;
    private String phone;
    private String title;
    private int totalProduct;
    private String deliveryStatus;
    private String deliveryId;
    private String userUid;
    private String shopId;
    private boolean isPickup;
    private String pickupDate;
    private String productId;
    private String cod;
    private String orderId;

    public DeliveryModel() {
    }

    protected DeliveryModel(Parcel in) {
        addedAt = in.readString();
        bookedBy = in.readString();
        price = in.readInt();
        productDp = in.readString();
        address = in.readString();
        phone = in.readString();
        title = in.readString();
        totalProduct = in.readInt();
        deliveryStatus = in.readString();
        deliveryId = in.readString();
        userUid = in.readString();
        shopId = in.readString();
        isPickup = in.readByte() != 0;
        pickupDate = in.readString();
        productId = in.readString();
        cod = in.readString();
        orderId = in.readString();
    }

    public static final Creator<DeliveryModel> CREATOR = new Creator<DeliveryModel>() {
        @Override
        public DeliveryModel createFromParcel(Parcel in) {
            return new DeliveryModel(in);
        }

        @Override
        public DeliveryModel[] newArray(int size) {
            return new DeliveryModel[size];
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(String deliveryId) {
        this.deliveryId = deliveryId;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public boolean isPickup() {
        return isPickup;
    }

    public void setPickup(boolean pickup) {
        isPickup = pickup;
    }

    public String getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(String pickupDate) {
        this.pickupDate = pickupDate;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(addedAt);
        parcel.writeString(bookedBy);
        parcel.writeInt(price);
        parcel.writeString(productDp);
        parcel.writeString(address);
        parcel.writeString(phone);
        parcel.writeString(title);
        parcel.writeInt(totalProduct);
        parcel.writeString(deliveryStatus);
        parcel.writeString(deliveryId);
        parcel.writeString(userUid);
        parcel.writeString(shopId);
        parcel.writeByte((byte) (isPickup ? 1 : 0));
        parcel.writeString(pickupDate);
        parcel.writeString(productId);
        parcel.writeString(cod);
        parcel.writeString(orderId);
    }
}