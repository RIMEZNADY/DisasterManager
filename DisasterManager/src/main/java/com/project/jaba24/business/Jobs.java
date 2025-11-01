package com.project.jaba24.business;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Jobs {
    private long id;
    private String title;
    private String status;
    private String body;
    private String how_to_apply;
    private String city;
    private String country;
    private Location location;
    private List<String> career_categories;
    private LocalDate created_at;
    private LocalDate closing_date;

    public Jobs() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getHow_to_apply() {
        return how_to_apply;
    }

    public void setHow_to_apply(String how_to_apply) {
        this.how_to_apply = how_to_apply;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<String> getCareer_categories() {
        return career_categories;
    }

    public void setCareer_categories(List<String> career_categories) {
        this.career_categories = career_categories;
    }

    public LocalDate getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDate created_at) {
        this.created_at = created_at;
    }

    public LocalDate getClosing_date() {
        return closing_date;
    }

    public void setClosing_date(LocalDate closing_date) {
        this.closing_date = closing_date;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Jobs(
            long id,
            String title,
            String status,
            String body,
            String how_to_apply,
            String city,
            String country, Location location,
            List<String> career_categories,
            String created_at,
            String closing_date
    ) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.body = body;
        this.how_to_apply = how_to_apply;
        this.city = city;
        this.country = country;
        this.location = location;
        this.career_categories = career_categories;
        this.created_at = LocalDate.parse(created_at, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        this.closing_date = LocalDate.parse(closing_date, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }
}
