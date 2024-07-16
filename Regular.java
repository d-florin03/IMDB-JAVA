package org.example;

import java.util.List;

public class Regular extends User implements RequestsManager{

    public Regular(Information information, int experience, AccountType accountType) {
        super(information, experience,accountType);
    }

    public Regular(Information information, AccountType accountType, int experience,  String username) {
        super(information, accountType, experience,  username);
    }


    public boolean createRequest(Request request) {
        String requesterUsername = request.getRequesterUsername();
        String resolverUsername = request.getResolverUsername();

        if (request.getType() == RequestType.OTHERS || request.getType() == RequestType.DELETE_ACCOUNT) {
            List<Request> adminRequests = Admin.RequestsHolder.getRequestsList();
            for (Request adminRequest : adminRequests) {
                if (adminRequest.getRequesterUsername().equals(requesterUsername) && adminRequest.getType() == request.getType()) {
                    return false;
                }
            }
            Admin.RequestsHolder.addRequest(request);
            IMDB.getRequests().add(request);
            return true;
        } else {
            User resolver = IMDB.findUser(resolverUsername);
            List<Request> assignedRequests = ((Staff) resolver).getUserRequests();
            for (Request assignedRequest : assignedRequests) {
                if (assignedRequest.getRequesterUsername().equals(requesterUsername) && assignedRequest.getType() == request.getType()) {
                    return false;
                }
            }
            User user = IMDB.findUser(resolverUsername);
            user.update("Ai primit un request de la utilizatorul " + requesterUsername);
            ((Staff) resolver).getUserRequests().add(request);
            IMDB.getRequests().add(request);
            return true;
        }
    }


    public boolean removeRequest(Request request) {
        String requesterUsername = request.getRequesterUsername();
        String resolverUsername = request.getResolverUsername();

        if (request.getType() == RequestType.OTHERS || request.getType() == RequestType.DELETE_ACCOUNT) {
            for (Request adminRequest : Admin.RequestsHolder.getRequestsList()) {
                if (adminRequest.getRequesterUsername().equals(requesterUsername) && adminRequest.getType() == request.getType()) {
                    Admin.RequestsHolder.removeRequest(adminRequest);
                    IMDB.getRequests().remove(adminRequest);
                    return true;
                }
            }
            return false;
        } else {
            User resolver = IMDB.findUser(resolverUsername);
            List<Request> assignedRequests = ((Staff) resolver).getUserRequests();
            for (Request assignedRequest : assignedRequests) {
                if (assignedRequest.getRequesterUsername().equals(requesterUsername) && assignedRequest.getType() == request.getType()) {
                    ((Staff) resolver).getUserRequests().remove(assignedRequest);
                    IMDB.getRequests().remove(assignedRequest);
                    return true;
                }
            }
        }
        return false;
    }
}
