package com.salwa.salwa.homepage.ui.product;


import android.os.Parcel;
import android.os.Parcelable;

public class ProductModel implements Parcelable {
    private String productId;
    private String image;
    private String title;
    private String description;
    private int likes;
    private int price;

    public ProductModel(){}

    protected ProductModel(Parcel in) {
        productId = in.readString();
        image = in.readString();
        title = in.readString();
        description = in.readString();
        likes = in.readInt();
        price = in.readInt();
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

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(productId);
        parcel.writeString(image);
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeInt(likes);
        parcel.writeInt(price);
    }
}
