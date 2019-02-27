package com.mumlatzim.mumlatzimv2.models.bookmark;

public class OrderModel {

    private int id;
    private int postId;
    private String postImageUrl;
    private String postTitle;
    private String postUrl;
    private String postCategory;
    private String formattedDate;
    private String postTrack;
    private String postScreenshot;

    public OrderModel(int id, int postId, String postImageUrl, String postTitle, String postUrl, String postCategory, String formattedDate, String postTrack, String postScreenshot)
    {
        this.id = id;
        this.postId = postId;
        this.postImageUrl = postImageUrl;
        this.postTitle = postTitle;
        this.postUrl = postUrl;
        this.postCategory = postCategory;
        this.formattedDate = formattedDate;
        this.postTrack = postTrack;
        this.postScreenshot = postScreenshot;
    }

    public int getId() {
        return id;
    }

    public int getPostId() {
        return postId;
    }

    public String getPostImageUrl() {
        return postImageUrl;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public String getPostUrl() {
        return postUrl;
    }

    public String getPostCategory() {
        return postCategory;
    }

    public String getFormattedDate() {
        return formattedDate;
    }

    public String getPostTrack() {
        return postTrack;
    }

    public String getPostScreenshot() {
        return postScreenshot;
    }
}
