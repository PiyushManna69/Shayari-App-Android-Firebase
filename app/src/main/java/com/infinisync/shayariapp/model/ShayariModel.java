package com.infinisync.shayariapp.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "shayari")
public class ShayariModel {

    @PrimaryKey
    @NonNull
    public String shayariId;

    @ColumnInfo(name = "shayariText")
    public String shayariText;

    @ColumnInfo(name = "shayariAuthor")
    public String shayariAuthor;

    @ColumnInfo(name = "isBookmarked")
    public boolean isBookmarked;

    public ShayariModel() {
    }

    public ShayariModel(String shayariId, String shayariText, String shayariAuthor, boolean isBookmarked) {
        this.shayariId = shayariId;
        this.shayariText = shayariText;
        this.shayariAuthor = shayariAuthor;
        this.isBookmarked = isBookmarked;
    }

    public String getShayariId() {
        return shayariId;
    }

    public void setShayariId(String shayariId) {
        this.shayariId = shayariId;
    }

    public String getShayariText() {
        return shayariText;
    }

    public void setShayariText(String shayariText) {
        this.shayariText = shayariText;
    }

    public String getShayariAuthor() {
        return shayariAuthor;
    }

    public void setShayariAuthor(String shayariAuthor) {
        this.shayariAuthor = shayariAuthor;
    }

    public boolean isBookmarked() {
        return isBookmarked;
    }

    public void setBookmarked(boolean bookmarked) {
        isBookmarked = bookmarked;
    }
}
