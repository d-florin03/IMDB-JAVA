package org.example;
import java.util.*;
public abstract class Staff<T extends Comparable<T>> extends User implements StaffInterface{
    private List<Request> userRequests;
    private SortedSet<T> added;

    public Staff(Information information, int experience,AccountType accountType) {
        super(information,experience, accountType);
        init();
    }

    public Staff(Information information, int experience,AccountType accountType, String username) {
        super(information,accountType, experience, username);
        init();
    }
    private void init(){
        userRequests = new ArrayList<>();
        added = new TreeSet<>();
    }

    public List<Request> getUserRequests() {
        return userRequests;
    }
    public SortedSet<T> getAdded() {
        return added;
    }

    public boolean addProductionSystem(Production production) {
        if (production == null || IMDB.findProduction(production.getTitle()) != null) {
            return false;
        }

        experienceStrategy = new Strategy();
        super.updateExperience(experienceStrategy.calculateExperience(true));

        added.add((T) production);
        IMDB.getProductions().add(production);
        IMDB.addProductionUser(production, this);
        return true;
    }

    public boolean removeProductionSystem(String name) {
        if (name == null) {
            return false;
        }

        for (T addObject : added) {
            Object object = addObject;
            if (object instanceof Production) {
                Production production = (Production) object;
                if (production.getTitle().equals(name)) {
                    removeProductionDetails(production);
                    return true;
                }
            }
        }
        return false;
    }

    private void removeProductionDetails(Production production) {
        for (String actorName : production.getActorsName()) {
            Actor actor = IMDB.findActor(actorName);
            String type = (production instanceof Series) ? "Series" : "Movie";
            Map.Entry<String, String> entry = Map.entry(production.getTitle(), type);
            actor.removeRoles(entry);
        }

        for (User user : IMDB.getUsers()) {
            if (user.getFavorites().contains(production)) {
                user.removeFavorite(production);
                user.update("Productia: \"" + production.getTitle() + "\"  care era la favorite, a fost stearsa");
            }
        }

        added.remove(production);
        IMDB.getProductions().remove(production);
        IMDB.removeProductionUser(production);
    }

    public boolean addActorSystem(Actor actor) {
        if (actor == null || IMDB.findActor(actor.getName()) != null) {
            return false;
        }

        experienceStrategy = new Strategy();
        super.updateExperience(experienceStrategy.calculateExperience(true));

        added.add((T) actor);
        IMDB.getActors().add(actor);
        IMDB.addActorUser(actor, this);

        return true;
    }



    public boolean removeActorSystem(String name) {
        if (name == null) {
            return false;
        }

        for (T addObject : added) {
            Object object = addObject;
            if (object instanceof Actor) {
                Actor actor = (Actor) object;
                if (actor.getName().equals(name)) {
                    removeActorDetails(actor);
                    return true;
                }
            }
        }
        return false;
    }

    private void removeActorDetails(Actor actor) {
        for (Map.Entry<String, String> entry : actor.getRoles()) {
            String material = entry.getKey();
            Production production = IMDB.findProduction(material);
            production.getActorsName().remove(actor.getName());
        }

        for (User user : IMDB.getUsers()) {
            if (user.getFavorites().contains(actor)) {
                user.removeFavorite(actor);
                user.update("Actorul: \"" + actor.getName() + "\" pe care il aveai la favorite, a fost sters");
            }
        }

        experienceStrategy = new Strategy();
        super.updateExperience(experienceStrategy.calculateExperience(true));

        added.remove(actor);
        IMDB.getActors().remove(actor);
        IMDB.removeActorUser(actor);
    }


    public void solveRequest(Request request, String decision) {
        User user = IMDB.findUser(request.getRequesterUsername());
        if (user != null) {
            if (request.getType() == RequestType.ACTOR_ISSUE) {
                solveActorRequest(user, request, decision);
            } else if (request.getType() == RequestType.MOVIE_ISSUE) {
                solveProductionRequest(user, request, decision);
            }
        }

        userRequests.remove(request);
        IMDB.getRequests().remove(request);
    }

    private void solveActorRequest(User user, Request request, String decision) {
        Actor actor = IMDB.findActor(request.getTitle());
        if (actor == null) {
            decision = "Reject";
        }
        if (decision.equals("Accept")) {
            user.update("Request-ul pentru actorul \"" + actor.getName() + "\" a fost acceptat : " + request.getDescription());
            handleAcceptedRequest(user);
        } else {
            user.update("Request-ul pentru actorul \"" + actor.getName() + "\" a fost respins : " + request.getDescription());
        }
    }

    private void solveProductionRequest(User user, Request request, String decision) {
        Production production = IMDB.findProduction(request.getTitle());
        if (production == null) {
            decision = "Reject";
        }
        if (decision.equals("Accept")) {
            user.update("Request-ul pentru productia \"" + production.getTitle() + "\" a fost acceptat : " + request.getDescription());
            handleAcceptedRequest(user);
        } else {
            user.update("Request-ul pentru productia \"" + production.getTitle() + "\" a fost respins : " + request.getDescription());
        }
    }

    private void handleAcceptedRequest(User user) {
        user.experienceStrategy = new RequestStrategy();
        user.updateExperience(user.experienceStrategy.calculateExperience(true));
    }


}
