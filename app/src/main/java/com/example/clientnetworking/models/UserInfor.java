package com.example.clientnetworking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserInfor {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("fullname")
    @Expose
    private String fullname;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("hash")
    @Expose
    private String hash;
    @SerializedName("birthday")
    @Expose
    private String birthday;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("permission")
    @Expose
    private Boolean permission;
    @SerializedName("__v")
    @Expose
    private Integer v;
    @SerializedName("data")
    @Expose
    private List<Data> data = null;

    public UserInfor(String id, String fullname, String email, String hash, String birthday, String phone, String address, String image, Boolean permission, List<Data> data) {
        this.id = id;
        this.fullname = fullname;
        this.email = email;
        this.hash = hash;
        this.birthday = birthday;
        this.phone = phone;
        this.address = address;
        this.image = image;
        this.permission = permission;
        this.data = data;
    }

    public UserInfor(String fullname, String email, String birthday, String phone, String address, String image,Integer _v) {
        this.fullname = fullname;
        this.email = email;
        this.image = image;
        this.birthday = birthday;
        this.phone = phone;
        this.address = address;
        this.v = _v;
    }
    public UserInfor(String id) {
        this.id = id;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Boolean getPermission() {
        return permission;
    }

    public void setPermission(Boolean permission) {
        this.permission = permission;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
}
