package org.example;

import org.example.controller.*;
import org.example.entities.User;
import org.example.utils.ConsoleColors;
import org.example.utils.ConsoleUtils;

public class Menu {
    private final AuthController authController;
    private final ClientController clientController;
    private final AccountController accountController;
    private final TransactionController transactionController;
    private final FeeRuleController feeRuleController;
    private final CreditController creditController;

    public Menu(AuthController authController, ClientController clientController,AccountController accountController,TransactionController transactionController,FeeRuleController feeRuleController,CreditController creditController) {
        this.authController = authController;
        this.clientController = clientController;
        this.accountController = accountController;
        this.transactionController = transactionController;
        this.feeRuleController = feeRuleController;
        this.creditController =  creditController;
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
        System.out.println(ConsoleColors.PURPLE + "5. Transfer" + ConsoleColors.RESET);
        System.out.println(ConsoleColors.PURPLE + "6. Credit request" + ConsoleColors.RESET);
        System.out.println(ConsoleColors.RED + "7. Logout" + ConsoleColors.RESET);

        int choice = ConsoleUtils.readInt("Enter your choice : ", 1, 7);

        switch (choice) {
            case 1 -> clientController.createClient();
            case 2-> accountController.createAccount();
            case 3 -> transactionController.deposit();
            case 4 -> transactionController.withdraw();
            case 5 -> transactionController.transfer();
            case 6 -> creditController.requestCredit();
            case 7 -> {
                authController.logout();
                return true;
            }
        }
        return true;
    }

    // === ADMIN MENU ===
    public boolean adminMenu() {
        System.out.println(ConsoleColors.CYAN + "\n=== ADMIN MENU ===" + ConsoleColors.RESET);
        System.out.println(ConsoleColors.YELLOW + "1.Register" + ConsoleColors.RESET);
        System.out.println(ConsoleColors.YELLOW+  "2.List FeeRules" + ConsoleColors.RESET);
        System.out.println(ConsoleColors.YELLOW + "3.Add FeeRule" + ConsoleColors.RESET);
        System.out.println(ConsoleColors.YELLOW + "4.Deactivate FeeRule" + ConsoleColors.RESET);
        System.out.println(ConsoleColors.YELLOW + "5.updateFeeRule" + ConsoleColors.RESET);
        System.out.println(ConsoleColors.RED    + "6.Logout" + ConsoleColors.RESET);

        int choice = ConsoleUtils.readInt("Enter choice: ", 1, 6);

        switch (choice) {
            case 1 -> authController.register();
            case 2 -> feeRuleController.listFeeRules() ;
            case 3 -> feeRuleController.createFeeRule();
            case 4 -> feeRuleController.deactivateFeeRule();
            case 5 -> feeRuleController.updateFeeRule();
            case 6 -> authController.logout();
        }
        return true;

    }
    public boolean managerMenu() {
        System.out.println(ConsoleColors.CYAN + "\n=== MANAGER MENU ===" + ConsoleColors.RESET);
        System.out.println(ConsoleColors.GREEN + "1. Approve or reject pending credit requests" + ConsoleColors.RESET);
        System.out.println(ConsoleColors.YELLOW + "2. Approve or reject pending transactions" + ConsoleColors.RESET);
        System.out.println(ConsoleColors.RED + "3. Logout" + ConsoleColors.RESET);

        int choice = ConsoleUtils.readInt("Enter choice: ", 1, 3);

        switch (choice) {
            case 1 -> creditController.processPendingCredits();
            case 2 -> transactionController.processPendingTransactions();
            case 3 -> authController.logout();
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