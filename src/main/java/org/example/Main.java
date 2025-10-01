package org.example;

import org.example.config.AppConfig;
import org.example.controller.AccountController;
import org.example.controller.AuthController;
import org.example.controller.ClientController;
import org.example.entities.User;

public class Main {
    public static void main(String[] args) {
        AuthController authController = AppConfig.createAuthController();


        System.out.println("=================================================");
        System.out.println("     💰 FLOWBANK - JAVA CONSOLE BANKING 💰");
        System.out.println("=================================================");


        Menu menu = new Menu(authController, null,null);
        boolean running = true;

        while (running) {
            User currentUser = authController.getCurrentUser();

            if (currentUser == null) {

                running = menu.showLoginMenu();
            } else {

                String role = currentUser.getRole().getRoleName();

                switch (role.toUpperCase()) {
                    case "TELLER" -> {
                        ClientController tellerController =
                                AppConfig.createClientController(currentUser);
                        AccountController accountController = AppConfig.createAccountController();
                        menu = new Menu(authController, tellerController,accountController);
                        running = menu.tellerMenu();
                    }

                    case "ADMIN" -> {
                        running = menu.adminMenu();
                    }
                    case "AUDITOR" -> {
                        running = menu.auditorMenu();
                    }
                    default -> {
                        System.out.println(" Unknown role. Logging out...");
                        authController.logout();
                    }
                }
            }
        }

        System.out.println("Application terminated.");
    }
}
