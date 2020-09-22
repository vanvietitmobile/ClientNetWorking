package com.example.clientnetworking.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelgetPost {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("PostusModel")
    @Expose
    private List<PostusModel> postusModel = null;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<PostusModel> getPostusModel() {
        return postusModel;
    }

    public void setPostusModel(List<PostusModel> postusModel) {
        this.postusModel = postusModel;
    }

}