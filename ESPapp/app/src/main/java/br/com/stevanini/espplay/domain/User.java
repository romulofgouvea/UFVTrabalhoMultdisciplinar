package br.com.stevanini.espplay.domain;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Romulo on 07/04/2017.
 */

public class User implements Serializable {
    private static final long serialVersionUID = 6601006766832473959L;
    @SerializedName("user_id")
    @Expose
    private long userId;
    @SerializedName("user_id_fb")
    @Expose
    private long userIdFb;
    @SerializedName("user_cover")
    @Expose
    private String userCover;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("user_lastname")
    @Expose
    private String userLastname;
    @SerializedName("user_email")
    @Expose
    private String userEmail;
    @SerializedName("user_password")
    @Expose
    private String userPassword;
    @SerializedName("user_genre")
    @Expose
    private String userGenre;
    @SerializedName("user_datebirth")
    @Expose
    private String userDatebirth;
    @SerializedName("user_registration")
    @Expose
    private String userRegistration;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Long getUserIdFb() {
        return userIdFb;
    }

    public void setUserIdFb(Long userIdFb) {
        this.userIdFb = userIdFb;
    }

    public String getUserCover() {
        return userCover;
    }

    public void setUserCover(String userCover) {
        this.userCover = userCover;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserLastname() {
        return userLastname;
    }

    public void setUserLastname(String userLastname) {
        this.userLastname = userLastname;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserGenre() {
        return userGenre;
    }

    public void setUserGenre(String userGenre) {
        this.userGenre = userGenre;
    }

    public String getUserDatebirth() {
        return userDatebirth;
    }

    public void setUserDatebirth(String userDatebirth) {
        this.userDatebirth = userDatebirth;
    }

    public String getUserRegistration() {
        return userRegistration;
    }

    public void setUserRegistration(String userRegistration) {
        this.userRegistration = userRegistration;
    }

    public String toString(){
        return "{" +
                "user_id='" + getUserId() + "'" +
                "user_id_fb='" + getUserIdFb() + "'" +
                "user_cover='" + getUserCover() + "'" +
                "user_name='" + getUserName() + "'" +
                "user_lastname='" + getUserLastname() + "'" +
                "user_email='" + getUserEmail() + "'" +
                "user_password='" + getUserPassword() + "'" +
                "user_genre='" + getUserGenre() + "'" +
                "user_datebirth='" + getUserDatebirth() + "'" +
                "user_registration='" + getUserRegistration() + "'" +
                "}";
    }

}