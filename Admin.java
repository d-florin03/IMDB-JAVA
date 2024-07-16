package org.example;

import java.util.*;

public class Admin extends Staff {

    public Admin(Information information, int experience, AccountType accountType, String username){
        super(information, experience, accountType, username);

    }

    public boolean deleteUser(String username) {
        User user = IMDB.findUser(username);

        if (user == null || user.getAccountType() == AccountType.ADMIN) {
            return false;
        }

        removeRatingsForUser(user);
        handleRequestsForUser(user);
        transferAddedItemsToAdmin(user);

        IMDB.getUsers().remove(user);
        return true;
    }

    private void removeRatingsForUser(User user) {
        for (Production production : IMDB.getProductions()) {
            for (Rating rating : production.getRatings()) {
                if (rating.getUsername().equals(user.getUsername())) {
                    production.removeRating(user.getUsername());
                    break;
                }
            }
        }
    }

    private void handleRequestsForUser(User user) {
        List<Request> requestsCopy = new ArrayList<>(IMDB.getRequests());

        for (Request request : requestsCopy) {
            String giver = request.getRequesterUsername();
            String receiver = request.getResolverUsername();

            if (giver.equals(user.getUsername())) {
                if (user instanceof Regular) ((Regular) user).removeRequest(request);
                else ((Contributor) user).removeRequest(request);
            }
            else if (receiver.equals(user.getUsername())) {
                User user1 = IMDB.findUser(giver);
                Request newRequest = new Request(RequestType.OTHERS, request.getCreationDate(), request.getTitle(), request.getDescription(), request.getRequesterUsername(), "ADMIN");
                if (user1 instanceof Regular) {
                    ((Regular) user1).removeRequest(request);
                    ((Regular) user1).createRequest(newRequest);
                }
                else {
                    ((Contributor) user1).removeRequest(request);
                    ((Contributor) user1).createRequest(newRequest);
                }
            }
        }

    }

    private void transferAddedItemsToAdmin(User user) {
        if (user instanceof Staff) {
            Staff staffUser = (Staff) user;

            for (Object object : staffUser.getAdded()) {
                if (object instanceof Actor) {
                    Actor actor = (Actor) object;
                    transferActorToAdmin(actor);
                } else if (object instanceof Production) {
                    Production production = (Production) object;
                    transferProductionToAdmin(production);
                }
            }
        }
    }

    private void transferActorToAdmin(Actor actor) {
        IMDB.removeActorUser(actor);
        IMDB.addActorUser(actor, IMDB.admin);
        ((Staff) IMDB.admin).getAdded().add(actor);
    }

    private void transferProductionToAdmin(Production production) {
        IMDB.removeProductionUser(production);
        IMDB.addProductionUser(production, IMDB.admin);
        ((Staff) IMDB.admin).getAdded().add(production);
    }


    public static class RequestsHolder {
        private static List<Request> requests = new ArrayList<>();

        public static void addRequest(Request request) {
            requests.add(request);
        }

        public static void removeRequest(Request request) {
            requests.remove(request);
        }

        public static void solveRequest(Request request, String decision) {
            User requester = IMDB.findUser(request.getRequesterUsername());

            if (requester != null) {
                if (request.getType() == RequestType.DELETE_ACCOUNT || request.getType() == RequestType.OTHERS) {
                    String statusMessage = (decision.equals("Accept")) ? "acceptat" : "respins";
                    requester.update("Request-ul a fost " + statusMessage + " : " + request.getDescription());
                }
            }

            remove(request);
        }

        public static List<Request> getRequestsList() {
            return new ArrayList<>(requests);
        }

        private static void remove(Request request) {
            requests.remove(request);
            IMDB.getRequests().remove(request);
        }
    }



}
