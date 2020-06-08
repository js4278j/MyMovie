package com.example.mymovie.Data;

public class MovieItem {

    private String title;
    private String img_url;
    private String detail_link;
    private String release;
    private String director;

    //private boolean favorState;

    public MovieItem(String title, String img_url, String detail_link, String release, String director) {
        this.title = title;
        this.img_url = img_url;
        this.detail_link = detail_link;
        this.release = release;
        this.director = director;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getDetail_link() {
        return detail_link;
    }

    public void setDetail_link(String detail_link) {
        this.detail_link = detail_link;
    }

    public String getRelease() {
        return release;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

}
