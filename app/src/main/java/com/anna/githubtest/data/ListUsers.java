package com.anna.githubtest.data;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import kotlin.jvm.internal.Intrinsics;

public class ListUsers {
    private int id;
    private boolean siteAdmin;
    private String imageUrl;
    private String loginID;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isSiteAdmin() {
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

    public String getLoginID() {
        return loginID;
    }

    public void setLoginID(String value) {
        this.loginID = value;
    }


    @NotNull
    public String toString() {
        return "ListUsers(id=" + this.id + ", siteAdmin=" + this.siteAdmin + ", imageUrl=" + this.imageUrl + ", userName=" + this.loginID + ')';
    }

    public int hashCode() {
        int result = Integer.hashCode(this.id);
        result = result * 31 + Boolean.hashCode(this.siteAdmin);
        result = result * 31 + this.imageUrl.hashCode();
        result = result * 31 + this.loginID.hashCode();
        return result;
    }

    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        } else if (!(other instanceof ListUsers)) {
            return false;
        } else {
            ListUsers var2 = (ListUsers)other;
            if (this.id != var2.id) {
                return false;
            } else if (this.siteAdmin != var2.siteAdmin) {
                return false;
            } else if (!Intrinsics.areEqual(this.imageUrl, var2.imageUrl)) {
                return false;
            } else {
                return Intrinsics.areEqual(this.loginID, var2.loginID);
            }
        }
    }
}
