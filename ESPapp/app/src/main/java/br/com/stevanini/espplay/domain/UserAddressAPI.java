package br.com.stevanini.espplay.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Romulo on 07/04/2017.
 */

public class UserAddressAPI implements Serializable {
    private static final long serialVersionUID = 6601006766832473959L;

    @SerializedName("user_id")
    @Expose
    private long userId;
    @SerializedName("addr_id")
    @Expose
    private long addrId;
    @SerializedName("addr_state")
    @Expose
    private String addrState;
    @SerializedName("addr_city")
    @Expose
    private String addrCity;
    @SerializedName("addr_street")
    @Expose
    private String addrStreet;
    @SerializedName("addr_number")
    @Expose
    private String addrNumber;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getAddrId() {
        return addrId;
    }

    public void setAddrId(long addrId) {
        this.addrId = addrId;
    }

    public String getAddrState() {
        return addrState;
    }

    public void setAddrState(String addrState) {
        this.addrState = addrState;
    }

    public String getAddrCity() {
        return addrCity;
    }

    public void setAddrCity(String addrCity) {
        this.addrCity = addrCity;
    }

    public String getAddrStreet() {
        return addrStreet;
    }

    public void setAddrStreet(String addrStreet) {
        this.addrStreet = addrStreet;
    }

    public String getAddrNumber() {
        return addrNumber;
    }

    public void setAddrNumber(String addrNumber) {
        this.addrNumber = addrNumber;
    }

    public String toString() {
        return "{" +
                "user_id='" + getUserId() + "'" +
                "addr_id='" + getAddrId() + "'" +
                "addr_state='" + getAddrState() + "'" +
                "addr_city='" + getAddrCity() + "'" +
                "addr_street='" + getAddrStreet() + "'" +
                "addr_number='" + getAddrNumber() + "'" +
                "}";
    }

}