package com.mumlatzim.mumlatzimv2.models.bookmark;

public class ScreenshotModel {

    private int id;
    private int postId;
    private String postImage;

    public ScreenshotModel(int id, int postId, String postImage) {
        this.id = id;
        this.postId = postId;
        this.postImage = postImage;
    }

    public int getId() {
        return id;
    }

    public int getPostId() {
        return postId;
    }

    public String getPostImage() {
        return postImage;
    }
}
