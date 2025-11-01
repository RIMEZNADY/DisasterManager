package com.project.jaba24.business;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Disaster {
    private long id;
    private String name;
    private String description;
    private String status;
    private String country; // country name
    private Location location;
    private LocalDate date;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public LocalDate getDate() { return date; }

    public void setDate(LocalDate date) { this.date = date; }

    public Disaster(long id, String name, String description, String status, String country, Location location, String dateStr) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.country = country;
        this.location = location;
        this.date = LocalDate.parse(dateStr, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    @Override
    public String toString() {
        return "Disaster{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", country='" + country + '\'' +
                ", location= {" + location.getLatitude() + ", " + location.getLongitude() + '}' +
                '}';
    }
}
