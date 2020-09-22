package com.example.clientnetworking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RestModel {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("request_code")
    @Expose
    private Integer requestCode;
    private Data data;


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

    public Integer getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(Integer requestCode) {
        this.requestCode = requestCode;
    }
    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

}