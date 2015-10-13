package com.example.nn.where4eatclient.adapters;

/**
 * Created by nn on 9/22/2015.
 */
public class FoodItem {
    String name;
    String venue;
    String description;
    String user;
    int rating;
    double latitude;
    double longitude;
    String imageurl;
    String thumbsurl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getThumbsurl() {
        return thumbsurl;
    }

    public void setThumbsurl(String thumbsurl) {
        this.thumbsurl = thumbsurl;
    }
}
