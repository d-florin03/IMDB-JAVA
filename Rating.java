package org.example;

public class Rating implements Comparable{
    private String username;
    private int note;
    private String comments;

    public Rating(String username, int note, String comments) {
        this.username = username;
        this.note = note;
        this.comments = comments;
    }
    public String getUsername() {
        return this.username;
    }
    public int getNote() {
        return this.note;
    }
    public String getComments() {
        return this.comments;
    }


    public int compareTo(Object o) {
        if (!(o instanceof Rating)) {
            throw new IllegalArgumentException("Object must be of type Rating");
        }

        Rating rating = (Rating) o;

        User user1 = IMDB.findUser(username);
        User user2 = IMDB.findUser(username);

        if (user1 instanceof Admin) {
            return -1;
        }
        if (user2 instanceof Admin) {
            return 1;
        }
        return user2.getExperience() - user1.getExperience();
    }

    @Override
    public String toString() {
        return String.format("%s : %d -> %s", username, note, comments);
    }

}
