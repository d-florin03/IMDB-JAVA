//package org.example;
//
//import java.util.Scanner;
//
//public class TerminalApplication {
//    private static Scanner scanner = new Scanner(System.in);
//    private static User currentUser;
//
//    public static void main(String[] args) {
//        // Autentificare
//        currentUser = authenticateUser();
//
//        // Meniul principal
//        while (true) {
//            displayMainMenu();
//            int option = getUserOption();
//
//            // Execută opțiunea aleasă
//            switch (option) {
//                case 1:
//                    currentUser.viewProductionDetails();
//                    break;
//                case 2:
//                    currentUser.viewActorDetails();
//                    break;
//                case 3:
//                    currentUser.viewNotifications();
//                    break;
//                case 4:
//                    currentUser.search();
//                    break;
//                case 5:
//                    currentUser.manageFavorites();
//                    break;
//                case 6:
//                    currentUser.createOrWithdrawRequest();
//                    break;
//                case 7:
//                    currentUser.manageSystemContent();
//                    break;
//                case 8:
//                    currentUser.viewAndResolveRequests();
//                    break;
//                case 9:
//                    currentUser.updateInformation();
//                    break;
//                case 10:
//                    currentUser.addOrRemoveReview();
//                    break;
//                case 11:
//                    System.out.println("Deconectare...");
//                    return;
//                default:
//                    System.out.println("Opțiune invalidă. Alegeți din nou.");
//            }
//        }
//    }
//
//    private static User authenticateUser() {
//        System.out.print("Introduceți numele de utilizator: ");
//        String username = scanner.nextLine();
//        System.out.print("Introduceți parola: ");
//        String password = scanner.nextLine();
//
//        // Implementează logica de autentificare
//        // Returnează un obiect User autentificat
//        return UserFactory.createUser();
//    }
//
//    private static void displayMainMenu() {
//        System.out.println("Opțiuni disponibile:");
//        for (String option : currentUser.getAvailableOptions()) {
//            System.out.println(option);
//        }
//    }
//
//    private static int getUserOption() {
//        System.out.print("Alegeți o opțiune: ");
//        return scanner.nextInt();
//    }
//}