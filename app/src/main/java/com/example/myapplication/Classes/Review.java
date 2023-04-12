package com.example.myapplication.Classes;

public class Review {
    private String username;
    private String review;
    private String serviceTitle;
    private Float rating;

    public Review(){}
    public Review(String username, String review, String serviceTitle, Float rating){
        this.username = username;
        this.review = review;
        this.serviceTitle = serviceTitle;
        this.rating = rating;
    }

    public String getUsername() {
        return username;
    }

    public String getReview() {
        return review;
    }

    public String getServiceTitle() {
        return serviceTitle;
    }

    public Float getRating() {
        return rating;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public void setServiceTitle(String serviceTitle) {
        this.serviceTitle = serviceTitle;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }
}
