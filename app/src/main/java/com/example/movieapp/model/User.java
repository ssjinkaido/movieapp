package com.example.movieapp.model;

import android.os.Parcel;
import android.os.Parcelable;

//public class User implements Parcelable {
//    private String mUsername;
//    private String mEmail;
//    private String mProfileImage;
//
//    /**
//     * Default Constructor
//     */
//    public User() {
//    }
//
//    /**
//     * Alternate constructor that defines the user object that contains a username, email, and password
//     * @param username define the username of the user.
//     * @param email define the email of the user.
//     */
//    public User(String username, String email,String mProfileImage) {
//        this.mUsername = username;
//        this.mEmail = email;
//        this.mProfileImage = mProfileImage;
//    }
//
//    /**
//     * The protected constructor for the parcelable class.
//     * @param in the parcel instances.
//     */
//    protected User(Parcel in) {
//        mUsername = in.readString();
//        mEmail = in.readString();
//        mProfileImage= in.readString();
//    }
//
//    /**
//     * Create the parcel using the Creator class.
//     */
//    public static final Creator<User> CREATOR = new Creator<User>() {
//        @Override
//        public User createFromParcel(Parcel in) {
//            return new User(in);
//        }
//
//        @Override
//        public User[] newArray(int size) {
//            return new User[size];
//        }
//    };
//
//    public String getUsername() {
//        return mUsername;
//    }
//
//    public void setUsername(String username) {
//        this.mUsername = username;
//    }
//
//    public String getEmail() {
//        return mEmail;
//    }
//
//    public void setEmail(String email) {
//        this.mEmail = email;
//    }
//
//    public String getProfileImage() {
//        return mProfileImage;
//    }
//
//    public void setProfileImage(String mProfileImage) {
//        this.mProfileImage = mProfileImage;
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel parcel, int flags) {
//        parcel.writeString(mUsername);
//        parcel.writeString(mEmail);
//        parcel.writeString(mProfileImage);
//    }
//}

public class User {
    private String image;
    private String name;
    private String email;

    public User() {

    }

    public User(String image, String name, String email) {
        this.image = image;
        this.name = name;
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
