package org.example;
import java.util.*;

public abstract class Production implements Comparable {
    private String title;
    private List<String> directors;
    private List<String> actors;
    private List<Genre> genres;
    private List<Rating> ratings;
    private String description;
    private Double averageRating;

    public Production(String title, String description, Double averageRating) {
        this.title = title;
        this.description = description;
        this.averageRating = averageRating;
        directors = new ArrayList<>();
        actors = new ArrayList<>();
        genres = new ArrayList<>();
        ratings = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public List<String> getDirectorsName() {
        return directors;
    }
    public void setDirectorsName(List<String> directorsName) {
        this.directors = directorsName;
    }
    public List<String> getActorsName() {
        return actors;
    }
    public void setActorsName(List<String> actorsName) {
        this.actors = actorsName;
    }
    public List<Genre> getGenres() {
        return genres;
    }
    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }
    public List<Rating> getRatings() {
        return ratings;
    }
    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Double getAverageRating() {
        return averageRating;
    }
    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }
    public void addDirector(String director) {
        directors.add(director);
    }
    public void addActor(String actor) {
        actors.add(actor);
    }
    public void addGenre(Genre genre) {
        genres.add(genre);
    }

    public void calculateAverageRating() {
        averageRating = 0.0;

        for (Rating userRating : ratings) {
            averageRating += userRating.getNote();
        }

        if (!ratings.isEmpty()) {
            averageRating = averageRating / ratings.size();
            averageRating = Math.round(averageRating * 100.0) / 100.0;
        }
    }

    public boolean addRating(Rating newRating) {
        for (Rating existingRating : ratings) {
            if (existingRating.getUsername().equals(newRating.getUsername())) {
                return false;
            }
        }

        if (IMDB.notificationIsOn) {
            notifyAboutRating(newRating);
        }

        User user = IMDB.getProductionUser(this);
        if (user != null) {
            notifyUsers(user, newRating);
        }

        User reviewer = IMDB.findUser(newRating.getUsername());
        if (reviewer != null) {
            updateUserExperience(reviewer, newRating);
        }

        ratings.add(newRating);
        calculateAverageRating();
        return true;
    }

    private void notifyAboutRating(Rating newRating) {
        for (Rating existingRating : ratings) {
            User user = IMDB.findUser(existingRating.getUsername());
            String productionType = (this instanceof Movie) ? "Filmul" : "Serialul";
            String notificationMessage = String.format("%s \"%s\" a primit un review nou de la utilizatorul %s -> %s",
                    productionType, this.title, newRating.getUsername(), String.valueOf(newRating.getNote()));
            user.update(notificationMessage);
        }
    }

    private void notifyUsers(User user, Rating newRating) {
        String productionType = (this instanceof Movie) ? "Filmul" : "Serialul";
        String notificationMessage = String.format("%s \"%s\" pe care l-ai adaugat a primit un review nou de la %s -> %s",
                productionType, this.title, newRating.getUsername(), String.valueOf(newRating.getNote()));
        user.update(notificationMessage);
    }

    private void updateUserExperience(User reviewer, Rating newRating) {
        reviewer.experienceStrategy = new ReviewStrategy();
        reviewer.updateExperience(reviewer.experienceStrategy.calculateExperience(true));
    }

    public boolean removeRating(String username) {

        for (Rating existingRating : ratings) {
            if (existingRating.getUsername().equals(username)) {
                ratings.remove(existingRating);
                calculateAverageRating();


                    if (IMDB.notificationIsOn) {
                        notifyUsersAboutRatingRemoval();
                    }

                    User user = IMDB.getProductionUser(this);
                    if (user != null) {
                        notifyContributorAboutRatingRemoval(user);
                    }

                    User reviewer = IMDB.findUser(existingRating.getUsername());
                    if (reviewer != null) {
                        updateUserExperienceOnRatingRemoval(reviewer);
                    }

                    return true;

            }
        }

        return false;
    }

    private void notifyUsersAboutRatingRemoval() {
        for (Rating remainingRating : ratings) {
            User user = IMDB.findUser(remainingRating.getUsername());
            String productionType = (this instanceof Movie) ? "Filmul" : "Serialul";
            String notificationMessage = String.format("%s \"%s\" a pierdut un review", productionType, this.title);
            user.update(notificationMessage);
        }
    }

    private void notifyContributorAboutRatingRemoval(User user) {
        String productionType = (this instanceof Movie) ? "Filmul" : "Serialul";
        String notificationMessage = String.format("%s \"%s\" pe care l-ai adaugat a pierdut un review", productionType, this.title);
        user.update(notificationMessage);
    }

    private void updateUserExperienceOnRatingRemoval(User reviewer) {
        reviewer.experienceStrategy = new ReviewStrategy();
        reviewer.updateExperience(reviewer.experienceStrategy.calculateExperience(false));
    }


     abstract void displayInfo();

    public int compareTo(Object o) {
        if (o instanceof Production)
            return title.compareTo( ((Production) o).getTitle());
        else
            return title.compareTo( ((Actor) o).getName() );
    }
}
