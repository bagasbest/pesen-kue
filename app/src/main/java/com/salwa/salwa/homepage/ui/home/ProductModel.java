package com.salwa.salwa.homepage.ui.home;


import android.os.Parcel;
import android.os.Parcelable;

public class ProductModel implements Parcelable {
    private String productId;
    private String shopId;
    private String shopName;
    private String shopDp;
    private String image;
    private String title;
    private String description;
    private double likes;
    private int price;
    private int personRated;
    private String quantity;

    public ProductModel(){}

    protected ProductModel(Parcel in) {
        productId = in.readString();
        shopId = in.readString();
        shopName = in.readString();
        shopDp = in.readString();
        image = in.readString();
        title = in.readString();
        description = in.readString();
        likes = in.readDouble();
        price = in.readInt();
        personRated = in.readInt();
        quantity = in.readString();
    }

    public static final Creator<ProductModel> CREATOR = new Creator<ProductModel>() {
        @Override
        public ProductModel createFromParcel(Parcel in) {
            return new ProductModel(in);
        }

        @Override
        public ProductModel[] newArray(int size) {
            return new ProductModel[size];
        }
    };

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

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopDp() {
        return shopDp;
    }

    public void setShopDp(String shopDp) {
        this.shopDp = shopDp;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLikes() {
        return likes;
    }

    public void setLikes(double likes) {
        this.likes = likes;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPersonRated() {
        return personRated;
    }

    public void setPersonRated(int personRated) {
        this.personRated = personRated;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(productId);
        parcel.writeString(shopId);
        parcel.writeString(shopName);
        parcel.writeString(shopDp);
        parcel.writeString(image);
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeDouble(likes);
        parcel.writeInt(price);
        parcel.writeInt(personRated);
        parcel.writeString(quantity);
    }
}
