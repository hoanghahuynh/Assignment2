package com.example.hoangha.lab2;


import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;


/**
 * Created by HoangHa on 10/23/2016.
 */

public class ApiResponse {
    @SerializedName("response")
    private JsonObject response;

    @SerializedName("status")
    private String status;

/*    public JSONObject getResponse() {
        return response;
    }*/
    public JsonObject getResponse() {
        if (response == null)
        {
            return new JsonObject();
        }
        return response;
    }

    public String getStatus() {
        return status;
    }

}
