package com.example.hoangha.lab2;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by HoangHa on 10/23/2016.
 */

public class SearchResult {

    @SerializedName("docs")
    private List<Article> articles;

    public List<Article> getArticles () {
        return articles;
    }
}
