package org.example;
import java.util.*;
public class Series extends Production{
    private int numSeasons;
    private String releaseYear;
    private Map<String, List<Episode>> series;

    public Series(String title, String description, Double averageRating, int numSeasons, String releaseYear) {
        super(title, description, averageRating);
    }


    public String getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(String releaseYear) {
        this.releaseYear = releaseYear;
    }

    public Map<String, List<Episode>> getSeries() {
        return series;
    }

    public void setSeries(Map<String, List<Episode>> series) {
        this.series = series;
    }

    public int getNumSeasons() {
        return numSeasons;
    }
    public void setNumSeasons(int numSeasons) {
        this.numSeasons = numSeasons;
    }

    public List<Episode> getEpisodesList(String season) {
        return series.get(season);
    }
    public void addEpisode(String season, Episode episode) {
        List<Episode> episodesList = this.getEpisodesList(season);
        episodesList.add(episode);
    }
    public void addSeason(String season) {
        series.put(season, new ArrayList<>());
    }

    public void displayInfo() {
        String stringBuilder = "Serial : " + getTitle() + '\n' +
                "Directors : " + getDirectorsName() + '\n' +
                "Actors : " + getActorsName() + '\n' +
                "Genre : " + getGenres() + '\n' +
                "Ratings : " + getRatings() + '\n' +
                "Plot : " + getDescription() + '\n' +
                "Average Rating : " + getAverageRating() + '\n' +
                "Number of seasons : " + numSeasons + '\n' +
                "Release Year : " + getReleaseYear() + '\n' +
                "Seasons : " + getSeries() + '\n';
        System.out.println(stringBuilder);
    }



}
