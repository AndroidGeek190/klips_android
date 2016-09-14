package com.erginus.klips.Model;

import java.io.Serializable;

/**
 * Created by paramjeet on 10/9/15.
 */
public class QuoteModel implements Serializable {

public  String name, id, thumbnail, image,subcatgry, favStatus, desc;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getdescription() {
        return desc;
    }

    public void setdescription(String id) {
        this.desc = id;
    }
    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String name) {
        this.thumbnail = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory() {
        return subcatgry;
    }

    public void setCategory(String category) {
        this.subcatgry = category;
    }
    public String getFavStatus() {
        return favStatus;
    }

    public void setFavStatus(String category) {
        this.favStatus = category;
    }

}
