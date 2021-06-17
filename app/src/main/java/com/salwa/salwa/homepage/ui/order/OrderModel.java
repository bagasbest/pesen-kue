package com.salwa.salwa.homepage.ui.order;

import android.os.Parcel;
import android.os.Parcelable;


public class OrderModel implements Parcelable {

    private String addedAt;
    private String bookedBy;
    private String description;
    private int price;
    private String productDp;
    private String productId;
    private String kecamatan;
    private String kelurahan;
    private String address;
    private String phone;
    private String title;
    private int totalProduct;
    private String userUid;
    private String status;
    private String proofPayment;
    private String orderId;

    public OrderModel() {}

    protected OrderModel(Parcel in) {
        addedAt = in.readString();
        bookedBy = in.readString();
        description = in.readString();
        price = in.readInt();
        productDp = in.readString();
        productId = in.readString();
        kecamatan = in.readString();
        kelurahan = in.readString();
        address = in.readString();
        phone = in.readString();
        title = in.readString();
        totalProduct = in.readInt();
        userUid = in.readString();
        status = in.readString();
        proofPayment = in.readString();
        orderId = in.readString();
    }

    public static final Creator<OrderModel> CREATOR = new Creator<OrderModel>() {
        @Override
        public OrderModel createFromParcel(Parcel in) {
            return new OrderModel(in);
        }

        @Override
        public OrderModel[] newArray(int size) {
            return new OrderModel[size];
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

    public String getKecamatan() {
        return kecamatan;
    }

    public void setKecamatan(String kecamatan) {
        this.kecamatan = kecamatan;
    }

    public String getKelurahan() {
        return kelurahan;
    }

    public void setKelurahan(String kelurahan) {
        this.kelurahan = kelurahan;
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

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProofPayment() {
        return proofPayment;
    }

    public void setProofPayment(String proofPayment) {
        this.proofPayment = proofPayment;
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
        parcel.writeString(description);
        parcel.writeInt(price);
        parcel.writeString(productDp);
        parcel.writeString(productId);
        parcel.writeString(kecamatan);
        parcel.writeString(kelurahan);
        parcel.writeString(address);
        parcel.writeString(phone);
        parcel.writeString(title);
        parcel.writeInt(totalProduct);
        parcel.writeString(userUid);
        parcel.writeString(status);
        parcel.writeString(proofPayment);
        parcel.writeString(orderId);
    }
}
