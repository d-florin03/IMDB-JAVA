package org.example;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Request {
    private RequestType requestType;
    private LocalDateTime creationDate;
    private String title;
    private String description;
    private String creatorUsername;
    private String resolverUsername;


    public Request(RequestType requestType, LocalDateTime creationDate, String title,
                   String description, String creatorUsername, String resolverUsername) {
        this.requestType = requestType;
        this.creationDate = creationDate;
        this.title = title;
        this.description = description;
        this.creatorUsername = creatorUsername;
        this.resolverUsername = resolverUsername;
    }

    // Getteri
    public RequestType getType() {
        return requestType;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void getFormattedCreationDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        this.creationDate = LocalDateTime.parse(date, formatter);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getResolverUsername() {
        return resolverUsername;
    }

    public String getRequesterUsername() {
      return creatorUsername;
    }



}
