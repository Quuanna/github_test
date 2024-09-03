package com.anna.githubtest.data;

public class ListUsers {
    private boolean siteAdmin;
    private String imageUrl;
    private String userName;

    public boolean getSiteAdmin() {
        return siteAdmin;
    }

    public void setSiteAdmin(boolean value) {
        this.siteAdmin = value;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String value) {
        this.imageUrl = value;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String value) {
        this.userName = value;
    }
}
