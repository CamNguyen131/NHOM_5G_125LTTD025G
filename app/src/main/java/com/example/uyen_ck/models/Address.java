package com.example.uyen_ck.models;

import com.google.firebase.firestore.PropertyName;

public class Address {
    private String addressId;
    private String receiverName;
    private String phone;
    private String addressLine;
    private boolean isDefault;

    public Address() {}

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddressLine() {
        return addressLine;
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }
    @PropertyName("default")
    public boolean isDefault() {
        return isDefault;
    }
    @PropertyName("default")
    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }
}
