package br.com.stevanini.espplay.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Romulo on 13/04/2017.
 */

public class PlaceAPI implements Serializable {
    private static final long serialVersionUID = 6601006766832473959L;

    @SerializedName("place_user_id")
    @Expose
    private long placeUserId;
    @SerializedName("place_addr_id")
    @Expose
    private long placeAddrId;
    @SerializedName("place_id")
    @Expose
    private long placeId;
    @SerializedName("place_thumb")
    @Expose
    private String placeThumb;
    @SerializedName("place_name")
    @Expose
    private String placeNome;
    @SerializedName("place_description")
    @Expose
    private String placeDescription;
    @SerializedName("place_latitude")
    @Expose
    private String placeLatitude;
    @SerializedName("place_longitude")
    @Expose
    private String placeLongitude;
    @SerializedName("place_status")
    @Expose
    private long placeStatus;
    @SerializedName("place_registration")
    @Expose
    private String placeRegistration;

    public long getPlaceUserId() {
        return placeUserId;
    }

    public void setPlaceUserId(long placeUserId) {
        this.placeUserId = placeUserId;
    }

    public long getPlaceAddrId() {
        return placeAddrId;
    }

    public void setPlaceAddrId(long placeAddrId) {
        this.placeAddrId = placeAddrId;
    }

    public long getPlaceId() {
        return placeId;
    }

    public void setPlaceId(long placeId) {
        this.placeId = placeId;
    }

    public String getPlaceThumb() {
        return placeThumb;
    }

    public void setPlaceThumb(String placeThumb) {
        this.placeThumb = placeThumb;
    }

    public String getPlaceNome() {
        return placeNome;
    }

    public void setPlaceNome(String placeNome) {
        this.placeNome = placeNome;
    }

    public String getPlaceDescription() {
        return placeDescription;
    }

    public void setPlaceDescription(String placeDescription) {
        this.placeDescription = placeDescription;
    }

    public String getPlaceLatitude() {
        return placeLatitude;
    }

    public void setPlaceLatitude(String placeLatitude) {
        this.placeLatitude = placeLatitude;
    }

    public String getPlaceLongitude() {
        return placeLongitude;
    }

    public void setPlaceLongitude(String placeLongitude) {
        this.placeLongitude = placeLongitude;
    }

    public long getPlaceStatus() {
        return placeStatus;
    }

    public void setPlaceStatus(long placeStatus) {
        this.placeStatus = placeStatus;
    }

    public String getPlaceRegistration() {
        return placeRegistration;
    }

    public void setPlaceRegistration(String placeRegistration) {
        this.placeRegistration = placeRegistration;
    }

    public String toString(){
        return "{" +
                "place_user_id='" + getPlaceUserId() + "'" +
                "place_addr_id='" + getPlaceAddrId() + "'" +
                "place_id='" + getPlaceId() + "'" +
                "place_thumb='" + getPlaceThumb() + "'" +
                "place_name='" + getPlaceNome() + "'" +
                "place_description='" + getPlaceDescription() + "'" +
                "place_latitude='" + getPlaceLatitude() + "'" +
                "place_longitude='" + getPlaceLongitude() + "'" +
                "place_status='" + getPlaceStatus() + "'" +
                "place_registration='" + getPlaceRegistration() + "'" +
                "}";
    }
}
