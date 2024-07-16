package org.example;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Actor implements Comparable {
    private String name;
    private List<Map.Entry<String, String>> roles;
    private String biography;

    public Actor(String name, String biography) {
        this.name = name;
        this.biography = biography;
        roles = new ArrayList<>();
    }

    public String getName(){
        return name;
    }
    public List<Map.Entry<String, String>> getRoles(){
        return roles;
    }
    public void addRoles(Map.Entry<String, String> entry) {
        roles.add(entry);
    }

    public void removeRoles(Map.Entry<String, String> entry) {
        roles.remove(entry);
    }

    public Map.Entry<String, String> createEntry(String name, String type) {
        Map.Entry<String, String> entry = Map.entry(name, type);
        return entry;
    }


    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Actor: ").append(name).append("\n");
        if (roles != null) {
            result.append("Roles:\n");
            for (Map.Entry<String, String> role : roles) {
                result.append("   - ").append(role.getKey()).append(": ").append(role.getValue()).append("\n");
            }
        }
        result.append("Biography: ").append(biography).append("\n");
        return result.toString();
    }
    public int compareTo(Object o) {
        if (o instanceof Actor)
            return name.compareTo( ((Actor) o).name);
        else
            return name.compareTo( ((Production) o).getTitle() );
    }
}
