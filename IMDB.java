package org.example;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.util.*;


public class IMDB {

    private static IMDB instance = null;

    private static  List<User> users;
    private static  List<Actor> actors;
    private static  List<Request> requests;
    private static  List<Production> productions;
    private static  Map<Actor, User> actorCreator;
    private static  Map<Production, User> productionCreator;
    public static boolean notificationIsOn = false;
    public static User user, admin;

    static {
        users = new ArrayList<>();
        actors = new ArrayList<>();
        requests = new ArrayList<>();
        productions = new ArrayList<>();
        actorCreator = new HashMap<>();
        productionCreator = new HashMap<>();

        admin = new Admin(null, 0, AccountType.ADMIN, "ADMIN");
        users.add(admin);
    }

    public static Production findProduction(String name) {
        for (Production production : productions) {
            if (production.getTitle().equals(name)) {
                return production;
            }
        }
        return null;
    }
    public static Actor findActor(String name) {
        for (Actor actor : actors) {
            if (actor.getName().equals(name)) {
                return actor;
            }
        }
        return null;
    }
    public static User findUser(String name) {
        for (User user : users) {
            if (user.getUsername().equals(name)) {
                return user;
            }
        }
        return null;
    }
    public static User getActorUser(Actor actor) {
        return actorCreator.get(actor);
    }
    public static User getProductionUser(Production production) {
        return productionCreator.get(production);
    }
    public static void addActorUser(Actor actor, User user) {
        actorCreator.put(actor, user);
    }
    public static void addProductionUser(Production production, User user) {
        productionCreator.put(production, user);
    }
    public static void removeActorUser(Actor actor) {
        actorCreator.remove(actor);
    }
    public static void removeProductionUser(Production production) {
        productionCreator.remove(production);
    }
    public static List<User> getUsers() {
        return users;
    }

    public static List<Actor> getActors() {
        return actors;
    }

    public static List<Request> getRequests() {
        return requests;
    }

    public static List<Production> getProductions() {
        return productions;
    }

    private IMDB(){

        notificationIsOn = true;
        parseAccounts();
        ActorsParser();
        parseProduction();
        parseRequests();

        for (Production production : getProductions()) {
            for (String actorName : production.getActorsName()) {
                Actor currentActor = findActor(actorName);
                if (currentActor == null) {
                    currentActor = new Actor(actorName, "no information");
                    ((Staff) admin).addActorSystem(currentActor);
                }
                if (production instanceof Movie) {
                    Map.Entry<String, String> productionEntry = Map.entry(production.getTitle(), "Movie");
                    if (!currentActor.getRoles().contains(productionEntry)) currentActor.addRoles(productionEntry);
                } else {
                    Map.Entry<String, String> productionEntry = Map.entry(production.getTitle(), "Series");
                    if (!currentActor.getRoles().contains(productionEntry)) currentActor.addRoles(productionEntry);
                }
            }
        }

        for (Actor currentActor : getActors()) {
            for (Map.Entry<String, String> roleEntry : currentActor.getRoles()) {
                String productionName = roleEntry.getKey();
                String contentType = roleEntry.getValue();
                Production currentProduction = findProduction(productionName);
                if (currentProduction == null) {
                    if (contentType.equals("Movie")) currentProduction = new Movie(productionName, "no details", 0.0, "0 minutes", "2000");
                    else currentProduction = new Series(productionName, "no details", 0.0, 0, "2000");
                    ((Staff) admin).addProductionSystem(currentProduction);
                }

                if (!currentProduction.getActorsName().contains(currentActor.getName())) currentProduction.addActor(currentActor.getName());
            }
        }

    }


    // Metoda pentru a obtine instanta unica a IMDB
    public static IMDB getInstance() {
        if (instance == null) {
            synchronized (IMDB.class) {
                if (instance == null) {
                    instance = new IMDB();
                }
            }
        }
        return instance;
    }

    public void initializeApplication() {
        Scanner scanner = new Scanner(System.in);

        boolean loggedIn = false;

            while (true) {
                if (!loggedIn) {
                    System.out.println("1. Autentificare");
                    System.out.println("2. Inchide aplicatia");
                    int option = scanner.nextInt();

                    switch (option) {
                        case 1:
                            if (authenticate()) {
                                loggedIn = true;
                            } else {
                                System.out.println("Autentificare esuata. Incercati din nou.");
                            }
                            break;
                        case 2:
                            System.out.println("Aplicatia inchisa.");
                            System.exit(0);
                        default:
                            System.out.println("Optiune invalida.");
                    }
                } else {
                    displayOptions();
                    int choice = scanner.nextInt();
                    String type;
                    switch (choice) {
                        case 1:
                            for(Production production : getProductions()){

                                System.out.println("titlu" + production.getTitle() + "\n directori:" + production.getDirectorsName()
                                +"\n actori"+ production.getActorsName());

                            }
                            break;
                        case 2:
                            // Implementeaza vizualizarea detaliilor tuturor actorilor din sistem
                            break;
                        // Adauga cazurile pentru celelalte optiuni
                        case 11:
                            System.out.println("Delogare...");
                            loggedIn = false;
                            break;
                        default:
                            System.out.println("Optiune invalida.");
                    }
                }
            }


    }
    public static boolean authenticate() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Introduceti numele de utilizator: ");
        String username = scanner.next();
        System.out.println("Introduceti parola: ");
        String password = scanner.next();


        for (User user : users) {
            if (user.getUsername().equals(username) && user.getInformation().getCredentials().getPassword().equals(password)) {
                System.out.println("Autentificare reusita!");
                return true;
            }
        }
            System.out.println("Autentificare esuata. Credentialele nu corespund.");
            return false;

    }
    private static void displayOptions() {
        System.out.println("1. Vizualizare detaliilor tuturor productiilor din sistem");
        System.out.println("2. Vizualizare detaliilor tuturor actorilor din sistem");
        System.out.println("3. Vizualizarea notificarilor primite");
        System.out.println("4. Căutarea unui anumit film/serial/actor");
        System.out.println("5. Adăugarea/Stergerea unei productii/actor din lista de favorite");
        System.out.println("6. Crearea/Retragerea unei cereri");
        System.out.println("7. Adăugarea/Stergerea unei productii/actor din sistem");
        System.out.println("11. Delogare");
    }
    public static void main(String args[]) {
        IMDB applicationInstance = IMDB.getInstance();
        applicationInstance.initializeApplication();
    }



    private void ActorsParser() {
            JSONParser parser = new JSONParser();
            try {
                Object obj = parser.parse(new FileReader("D:\\ANUL-2\\OOP\\TEMA-POO\\src\\main\\resources\\input\\actors.json"));

                JSONArray jsonArray = (JSONArray) obj;

                for (Object arrayElement : jsonArray) {
                    JSONObject jsonObject = (JSONObject) arrayElement;

                    String name = (String) jsonObject.get("name");
                    String biography = (String) jsonObject.get("biography");
                    JSONArray performances = (JSONArray) jsonObject.get("performances");

                    Actor actor = new Actor(name, biography);

                    for (Object arrayElement2 : performances) {
                        JSONObject jsonObject2 = (JSONObject) arrayElement2;

                        String title = (String) jsonObject2.get("title");
                        String type = (String) jsonObject2.get("type");

                        actor.addRoles(actor.createEntry(title, type));
                    }
                    actors.add(actor);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

private void parseAccounts() {
    JSONParser jsonParser = new JSONParser();

    try {
        Object jsonObjects = jsonParser.parse(new FileReader("D:\\ANUL-2\\OOP\\TEMA-POO\\src\\main\\resources\\input\\accounts.json"));

        JSONArray userAccountsArray = (JSONArray) jsonObjects;

        for (Object accountObject : userAccountsArray) {
            JSONObject accountJson = (JSONObject) accountObject;

            String username = (String) accountJson.get("username");
            String experience = (String) accountJson.get("experience");
            int accountExperience = (experience != null) ? Integer.parseInt(experience) : 10000000;

            String userType = (String) accountJson.get("userType");
            AccountType userAccountType = AccountType.valueOf(userType.toUpperCase());

            JSONObject informationJson = (JSONObject) accountJson.get("information");
            JSONObject credentialsJson = (JSONObject) informationJson.get("credentials");
            String email = (String) credentialsJson.get("email");
            String password = (String) credentialsJson.get("password");
            Credentials credentials = new Credentials(email, password);
            String name = (String) informationJson.get("name");
            String country = (String) informationJson.get("country");
            Number age = (Number) informationJson.get("age");
            String gender = (String) informationJson.get("gender");
            String birthDate = (String) informationJson.get("birthDate");

            User.Information information = new User.Information.Builder(credentials).setPersonalInfo(name, country, age, gender, birthDate).build();

            User userAccount = UserFactory.factory(information, userAccountType, accountExperience, username);

            JSONArray favoriteProductionsArray = (JSONArray) accountJson.get("favoriteProductions");

            if (favoriteProductionsArray != null) {
                for (Object productionObj : favoriteProductionsArray) {
                    String productionName = (String) productionObj;
                    Production production = findProduction(productionName);

                    if (production != null) {
                        userAccount.addFavorite(production);
                    }
                }
            }

            JSONArray favoriteActorsArray = (JSONArray) accountJson.get("favoriteActors");

            if (favoriteActorsArray != null) {
                for (Object actorObj : favoriteActorsArray) {
                    String actorName = (String) actorObj;
                    Actor actor = findActor(actorName);

                    if (actor != null) {
                        userAccount.addFavorite(actor);
                    }
                }
            }

            JSONArray productionsContributionArray = (JSONArray) accountJson.get("productionsContribution");

            if (productionsContributionArray != null && userAccount instanceof Staff) {
                for (Object productionContributionObj : productionsContributionArray) {
                    String productionName = (String) productionContributionObj;
                    Production production = findProduction(productionName);

                    if (production != null) {
                        ((Staff) userAccount).getAdded().add(production);
                        addProductionUser(production, userAccount);
                    }
                }
            }

            JSONArray actorsContributionArray = (JSONArray) accountJson.get("actorsContribution");

            if (actorsContributionArray != null && userAccount instanceof Staff) {
                for (Object actorContributionObj : actorsContributionArray) {
                    String actorName = (String) actorContributionObj;
                    Actor actor = findActor(actorName);

                    if (actor != null) {
                        ((Staff) userAccount).getAdded().add(actor);
                        addActorUser(actor, userAccount);
                    }
                }
            }

            JSONArray notificationsArray = (JSONArray) accountJson.get("notifications");

            if (notificationsArray != null) {
                for (Object notificationObj : notificationsArray) {
                    String notificationMessage = (String) notificationObj;
                    if (notificationMessage != null) {
                        userAccount.addToNotifications(notificationMessage);
                    }
                }
            }
            users.add(userAccount);
        }
    } catch (Exception ex) {
        ex.printStackTrace();
    }
}


    private void parseRequests() {
        JSONParser jsonParser = new JSONParser();
        try {
            Object jsonObjects = jsonParser.parse(new FileReader("D:\\ANUL-2\\OOP\\TEMA-POO\\src\\main\\resources\\input\\requests.json"));

            JSONArray requestsArray = (JSONArray) jsonObjects;

            for (Object requestObject : requestsArray) {
                JSONObject requestJson = (JSONObject) requestObject;

                String requestTypeString = (String) requestJson.get("type");
                RequestType requestType;
                switch (requestTypeString) {
                    case "DELETE_ACCOUNT":
                        requestType = RequestType.DELETE_ACCOUNT;
                        break;
                    case "ACTOR_ISSUE":
                        requestType = RequestType.ACTOR_ISSUE;
                        break;
                    case "MOVIE_ISSUE":
                        requestType = RequestType.MOVIE_ISSUE;
                        break;
                    case "OTHERS":
                        requestType = RequestType.OTHERS;
                        break;
                    default:
                        requestType = null;
                        break;
                }

                String CreatedDate = (String) requestJson.get("createdDate");
                String username = (String) requestJson.get("username");
                String to = (String) requestJson.get("to");
                String description = (String) requestJson.get("description");

                Request request;
                String name;
                if (RequestType.ACTOR_ISSUE == requestType) {
                    name = (String) requestJson.get("actorName");
                } else if (RequestType.MOVIE_ISSUE == requestType) {
                    name = (String) requestJson.get("movieTitle");
                } else {
                    name = null;
                }
                request = new Request(requestType, null, name, description, username, to);
                request.getFormattedCreationDate(CreatedDate);

                User requestRequester = IMDB.findUser(request.getRequesterUsername());
                if (requestRequester instanceof Regular) {
                    ((Regular) requestRequester).createRequest(request);
                } else {
                    ((Contributor) requestRequester).createRequest(request);
                }

                requests.add(request);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }



    private void parseProductionData(JSONObject productionData) {
        String title = (String) productionData.get("title");
        String description = (String) productionData.get("plot");
        Double averageRating = (Double) productionData.get("averageRating");
        String type = (String) productionData.get("type");
        Production production;



        if (type.equals("Movie")) {
            String duration = (String) productionData.get("duration");
            Number releaseYear = (Number) productionData.get("releaseYear");
            production = createMovie(title, description, averageRating, duration, releaseYear);
        } else {
            Number numSeasons = (Number) productionData.get("numSeasons");
            Number releaseYear = (Number) productionData.get("releaseYear");

            JSONObject seasonsObject = (JSONObject) productionData.get("seasons");

            production = createSeries(title, description, averageRating, numSeasons.intValue(), releaseYear, seasonsObject);
        }

        System.out.println(production.getTitle());

        JSONArray directorsArray = (JSONArray) productionData.get("directors");
        addDirectorsToProduction(production, directorsArray);

        JSONArray actorsArray = (JSONArray) productionData.get("actors");
        addActorsToProduction(production, actorsArray);

        JSONArray genresArray = (JSONArray) productionData.get("genres");
        addGenresToProduction(production, genresArray);

        JSONArray performances = (JSONArray) productionData.get("ratings");
        addRatingsToProduction(production, performances);



        productions.add(production);
    }

    private Production createMovie(String title, String description, Double averageRating, String duration, Number releaseYear) {
        if (releaseYear != null)
            return new Movie(title, description, averageRating, duration, releaseYear.toString());
        else
            return new Movie(title, description, averageRating, duration, null);
    }

    private Production createSeries(String title, String description, Double averageRating, int numSeasons, Number releaseYear, JSONObject seasonsObject) {
        Production production;
        if (releaseYear != null)
            production = new Series(title, description, averageRating, numSeasons, releaseYear.toString());
        else
            production = new Series(title, description, averageRating, numSeasons, null);

        addSeasonsAndEpisodesToSeries((Series) production, seasonsObject);

        return production;
    }

    private void addSeasonsAndEpisodesToSeries(Series series, JSONObject seasonsObject) {
        for (Object seasonKey : seasonsObject.keySet()) {
            JSONArray episodesArray = (JSONArray) seasonsObject.get(seasonKey);
            series.addSeason((String) seasonKey);

            for (Object episode : episodesArray) {
                JSONObject episodeObject = (JSONObject) episode;

                String episodeName = (String) episodeObject.get("episodeName");
                String episodeDuration = (String) episodeObject.get("duration");

                Episode episodeInstance = new Episode(episodeName, episodeDuration);
                series.addEpisode((String) seasonKey, episodeInstance);
            }
        }
    }

    private void addDirectorsToProduction(Production production, JSONArray directorsArray) {
        for (Object arrayElement2 : directorsArray) {
            String director = (String) arrayElement2;
            production.addDirector(director);
        }
    }

    private void addActorsToProduction(Production production, JSONArray actorsArray) {
        for (Object arrayElement2 : actorsArray) {
            String actor = (String) arrayElement2;
            if (IMDB.findActor(actor) != null) {
                production.addActor(actor);
            }

        }
    }

    private void addGenresToProduction(Production production, JSONArray genresArray) {
        for (Object arrayElement2 : genresArray) {
            String genre = (String) arrayElement2;
            Genre genreProduction = Genre.valueOf(genre);
            production.addGenre(genreProduction);
        }
    }

    private void addRatingsToProduction(Production production, JSONArray performances) {
        for (Object arrayElement2 : performances) {
            JSONObject jsonObject2 = (JSONObject) arrayElement2;

            String username = (String) jsonObject2.get("username");
            Number note = (Number) jsonObject2.get("rating");
            String comment = (String) jsonObject2.get("comment");

            Rating rating = new Rating(username, note.intValue(), comment);
            production.addRating(rating);
        }
    }

    private void parseProduction() {
        JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(new FileReader("D:\\ANUL-2\\OOP\\TEMA-POO\\src\\main\\resources\\input\\production.json"));

            JSONArray jsonArray = (JSONArray) obj;

            for (Object arrayElement : jsonArray) {
                JSONObject jsonObject = (JSONObject) arrayElement;
                parseProductionData(jsonObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

