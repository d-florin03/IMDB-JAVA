package org.example;

public class UserFactory {
    // Metoda de fabricare care creeaza si returneaza un utilizator in functie de tipul specificat
    private static UserFactory instance = null;

    public static UserFactory getInstance() {
        if (instance == null) {
            instance = new UserFactory();
        }
        return instance;
    }
    public static User factory(User.Information information, AccountType accountType, int experience, String username) {
        switch (accountType) {
            case REGULAR:
                return new Regular(information,accountType, experience, username);
            case CONTRIBUTOR:
                return new Contributor(information,experience, accountType, username);
            case ADMIN:
                return new Admin(information, experience, accountType, username);
            default:
               return null;
        }
    }
}
