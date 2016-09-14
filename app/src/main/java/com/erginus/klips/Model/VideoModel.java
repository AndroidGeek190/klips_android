package com.erginus.klips.Model;

import java.io.Serializable;

/**
 * Created by paramjeet on 10/9/15.
 */
public class VideoModel  implements Serializable{
    public  String name,cat_id, id, artistName, image, video, duration, fav_status, play_status, desc;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getdescription() {
        return desc;
    }

    public void setdescription(String desc) {
        this.desc = desc;
    }

    public String getcat_Id() {
        return cat_id;
    }

    public void setcat_Id(String id) {
        this.cat_id = id;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String name) {
        this.artistName = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }
    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
    public String getFavStatus() {
        return fav_status;
    }

    public void setFavStatus(String duration) {
        this.fav_status = duration;
    }
    public String getPlayStatus() {
        return play_status;
    }

    public void setPlayStatus(String duration) {
        this.play_status = duration;
    }
}
