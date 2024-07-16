package org.example;

public class Movie extends  Production{
    private String duration;
    private String releaseYear;


    public Movie(String title, String description, Double averageRating, String duration, String releaseYear) {
        super(title, description, averageRating);
        this.duration = duration;
        this.releaseYear = releaseYear;
    }

    public String getDuration() {
        return duration;
    }
    public void setDuration(String duration) {
        this.duration = duration;
    }
    public String getReleaseYear() {
        return releaseYear;
    }
    public void setReleaseYear(String releaseYear) {
        this.releaseYear = releaseYear;
    }

    public void displayInfo() {
        String movieInfo = "Film Title: " + getTitle() + '\n' +
                "Directors: " + getDirectorsName() + '\n' +
                "Actors: " + getActorsName() + '\n' +
                "Genres: " + getGenres() + '\n' +
                "Ratings: " + getRatings() + '\n' +
                "Plot Summary: " + getDescription() + '\n' +
                "Average Rating: " + getAverageRating() + '\n' +
                "Duration: " + getDuration() + '\n' +
                "Release Year: " + getReleaseYear() + '\n';
        System.out.println(movieInfo);

    }


}
