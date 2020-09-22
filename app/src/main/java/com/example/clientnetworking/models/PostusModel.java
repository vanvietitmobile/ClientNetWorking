package com.example.clientnetworking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostusModel {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("fullname")
    @Expose
    private String fullname;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("active")
    @Expose
    private Boolean active;
    @SerializedName("__v")
    @Expose
    private Integer v;

    public PostusModel(String email, String fullname, String title, String content, String address, String date, String image, Boolean active) {
        this.email = email;
        this.fullname = fullname;
        this.title = title;
        this.content = content;
        this.address = address;
        this.date = date;
        this.image = image;
        this.active = active;
    }

    public PostusModel(String id, String email, String fullname, String title, String content, String address, String date, String image, Boolean active) {
        this.id = id;
        this.email = email;
        this.fullname = fullname;
        this.title = title;
        this.content = content;
        this.address = address;
        this.date = date;
        this.image = image;
        this.active = active;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

}