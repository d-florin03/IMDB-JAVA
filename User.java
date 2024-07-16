package org.example;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public abstract class User<T extends Comparable<T>> implements Observer{
    private Information information;
    private AccountType accountType;
    private String username;
    private int experience;
    private List<String> notifications;
    private SortedSet<T> favorites;
    public ExperienceStrategy experienceStrategy;
    // Constructor
    public User(Information information, AccountType accountType, int experience, String username) {
        this.information = information;
        this.accountType = accountType;
        this.username = username;
        this.experience = experience;
        init();
    }
    public User(Information information, int experience, AccountType accountType){
        this.information = information;
        this.accountType = accountType;
        this.experience = experience;
        init();
    }
    private void init(){
        notifications = new ArrayList<>();
        favorites =  new TreeSet<>();
    }


    public Information getInformation() {
        return information;
    }

    public void setInformation(Information information) {
        this.information = information;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public List<String> getNotifications() {
        return notifications;
    }

    public SortedSet<T> getFavorites() {
        return favorites;
    }

    public void addFavorite(Object object){
        if (object instanceof Actor || object instanceof Production)
            favorites.add((T) object);
    }
    public void removeFavorite(Object object){
        if (object instanceof Actor || object instanceof Production)
            favorites.remove((T) object);
    }
    public void addToNotifications(String notification) {
        notifications.add(notification);
    }

    public void updateExperience(int experience) {
        this.experience += experience;
        if (this.experience < 0) {
            this.experience = 0;
        }
    }
    public void update(String notification) {
        notifications.add(notification);
    }

     public static class Information {
        private Credentials credentials;
        private String name;
        private String country;
        private int age;
        private String genre;
        private LocalDate birthDate;

        //constructor privat pentru a fi accesat doar de builder
        private Information(Builder builder) {
            this.credentials = builder.credentials;
            this.name = builder.name;
            this.country = builder.country;
            this.age = builder.age;
            this.genre = builder.genre;
            this.birthDate = builder.birthDate;
        }

        public Credentials getCredentials() {
            return credentials;
        }

        public String getName() {
            return name;
        }

        public String getCountry() {
            return country;
        }

        public int getAge() {
            return age;
        }

        public String  getGenre() {
            return genre;
        }

        public LocalDate getBirthDate() {
            return birthDate;
        }

        // Setteri pentru a seta atributele
        public void setCredentials(Credentials credentials) {
            this.credentials = credentials;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public void setGender(String genre) {
            this.genre = genre;
        }

        public void setBirthDate(LocalDate birthDate) {
            this.birthDate = birthDate;
        }

        public static class Builder {
            private Credentials credentials;
            private String name;
            private String country;
            private int age;
            private String genre;
            private LocalDate birthDate;

            public Builder (Credentials credentials) {
                this.credentials = credentials;
            }

            public Builder setPersonalInfo(String name, String country,
                                         Number age, String genre, String birthDate) {
                this.name = name;
                this.country = country;
                if (age != null) {
                    this.age = age.intValue();
                }
                this.genre = genre;
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                this.birthDate = LocalDate.parse(birthDate, formatter);
                return this;
            }

            public  Information build() {
                return new Information(this);
            }
        }

    }



}
