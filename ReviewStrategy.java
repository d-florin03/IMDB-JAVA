package org.example;

public class ReviewStrategy implements ExperienceStrategy{
    @Override
    public int calculateExperience(boolean val) {
        int add = 5;
        if (val == true) return add;
        else return -add;
    }
}
