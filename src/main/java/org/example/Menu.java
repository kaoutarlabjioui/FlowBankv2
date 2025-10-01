package org.example;

import org.example.controller.AccountController;
import org.example.controller.AuthController;
import org.example.controller.ClientController;
import org.example.entities.User;
import org.example.utils.ConsoleColors;
import org.example.utils.ConsoleUtils;

public class Menu {
    private final AuthController authController;
    private final ClientController clientController;
    private final AccountController accountController;

    public Menu(AuthController authController, ClientController clientController,AccountController accountController) {
        this.authController = authController;
        this.clientController = clientController;
        this.accountController = accountController;
    }

    public boolean showLoginMenu() {
        System.out.println(ConsoleColors.CYAN + "\n=== LOGIN MENU ===" + ConsoleColors.RESET);
        System.out.println(ConsoleColors.YELLOW + "1. Login" + ConsoleColors.RESET);
        System.out.println(ConsoleColors.RED + "2. Exit" + ConsoleColors.RESET);

        int choice = ConsoleUtils.readInt("Enter choice: ", 1, 2);

        switch (choice) {
            case 1 -> authController.login();
            case 2 -> {
                return false; // Quitter le programme
            }
        }
        return true;
    }

    // === MENU TELLER ===
    public boolean tellerMenu() {
        User currentUser = authController.getCurrentUser();

        System.out.println(ConsoleColors.CYAN + "\n=== TELLER MENU ===" + ConsoleColors.RESET);
        System.out.println(ConsoleColors.PURPLE + "1. Create new client" + ConsoleColors.RESET);
        System.out.println(ConsoleColors.PURPLE + "2. Create account" + ConsoleColors.RESET);
        System.out.println(ConsoleColors.PURPLE + "3. Deposit" + ConsoleColors.RESET);
        System.out.println(ConsoleColors.PURPLE + "4. Withdraw" + ConsoleColors.RESET);
        System.out.println(ConsoleColors.PURPLE + "5. Transfer In" + ConsoleColors.RESET);
        System.out.println(ConsoleColors.PURPLE + "6. Transfer Out" + ConsoleColors.RESET);
        System.out.println(ConsoleColors.PURPLE + "7. Credit request" + ConsoleColors.RESET);
        System.out.println(ConsoleColors.RED + "8. Logout" + ConsoleColors.RESET);

        int choice = ConsoleUtils.readInt("Enter your choice : ", 1, 8);

        switch (choice) {
            case 1 -> clientController.createClient();
            case 2-> accountController.createAccount();
            case 8 -> {
                authController.logout(); // ✅ au lieu de manipuler la variable directement
                return true;
            }
        }
        return true;
    }

    // === ADMIN MENU ===
    public boolean adminMenu() {
        System.out.println(ConsoleColors.CYAN + "\n=== ADMIN MENU ===" + ConsoleColors.RESET);
        System.out.println(ConsoleColors.YELLOW + "1. Register" + ConsoleColors.RESET);
        System.out.println(ConsoleColors.RED + "2. Logout" + ConsoleColors.RESET);

        int choice = ConsoleUtils.readInt("Enter choice: ", 1, 2);

        switch (choice) {
            case 1 -> authController.register();
            case 2 -> authController.logout();
        }
        return true;

    }
    // === AUDITOR MENU ===
    public boolean auditorMenu() {
        System.out.println(ConsoleColors.CYAN + "\n=== AUDITOR MENU ===" + ConsoleColors.RESET);
        System.out.println(ConsoleColors.RED + "1. Logout" + ConsoleColors.RESET);

        int choice = ConsoleUtils.readInt("Enter choice: ", 1, 1);
        if (choice == 1) {
            authController.logout(); // ✅
        }
        return true;
    }

}