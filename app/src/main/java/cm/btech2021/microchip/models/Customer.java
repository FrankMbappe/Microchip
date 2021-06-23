package cm.btech2021.microchip.models;

import android.location.Location;
import android.net.Uri;

import java.util.Date;

public class Customer {
    String chipNumber;
    String idCardNumber;
    String idCardPhotoUriPath;
    String fullName;
    Date dateOfBirth;
    Location sellLocation;

    public Customer() {
    }

    public Customer(String chipNumber, String idCardNumber) {
        this.chipNumber = chipNumber;
        this.idCardNumber = idCardNumber;
    }

    public Customer(String chipNumber, String idCardNumber, String idCardPhotoUriPath, String fullName, Date dateOfBirth, Location sellLocation) {
        this.chipNumber = chipNumber;
        this.idCardNumber = idCardNumber;
        this.idCardPhotoUriPath = idCardPhotoUriPath;
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.sellLocation = sellLocation;
    }

    public boolean isValid() {
        return chipNumber != null && idCardNumber != null && idCardPhotoUriPath != null
                && fullName != null && dateOfBirth != null && sellLocation != null;
    }

    public String getChipNumber() {
        return chipNumber;
    }

    public void setChipNumber(String chipNumber) {
        this.chipNumber = chipNumber;
    }

    public String getIdCardNumber() {
        return idCardNumber;
    }

    public void setIdCardNumber(String idCardNumber) {
        this.idCardNumber = idCardNumber;
    }

    public String getIdCardPhotoUriPath() {
        return idCardPhotoUriPath;
    }

    public void setIdCardPhotoUriPath(String idCardPhotoUriPath) {
        this.idCardPhotoUriPath = idCardPhotoUriPath;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Location getSellLocation() {
        return sellLocation;
    }

    public void setSellLocation(Location sellLocation) {
        this.sellLocation = sellLocation;
    }
}
