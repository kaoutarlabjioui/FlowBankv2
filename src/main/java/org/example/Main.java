package org.example;

import org.example.config.AppConfig;
import org.example.controller.*;
import org.example.entities.User;
import org.example.jobs.Scheduler;
import org.example.service.AccountService;
import org.example.service.ClientService;
import org.example.service.CreditService;

public class Main {
    public static void main(String[] args) {
        AuthController authController = AppConfig.createAuthController();


        System.out.println("=================================================");
        System.out.println("     💰 FLOWBANK - JAVA CONSOLE BANKING 💰");
        System.out.println("=================================================");

        ClientService clientService = AppConfig.createClientService();
        AccountService accountService = AppConfig.createAccountService();
        CreditService creditService = AppConfig.createCreditService();
        Scheduler.start(clientService, accountService, creditService);

        Menu menu = new Menu(authController, null,null,null,null,null);
        boolean running = true;

        while (running) {
            User currentUser = authController.getCurrentUser();

            if (currentUser == null) {

                running = menu.showLoginMenu();
            } else {

                String role = currentUser.getRole().getRoleName();

                switch (role.toUpperCase()) {
                    case "TELLER" -> {
                        ClientController tellerController = AppConfig.createClientController(currentUser);
                        AccountController accountController = AppConfig.createAccountController();
                        TransactionController transactionController = AppConfig.createTransactionController();
                        FeeRuleController feeRuleController = AppConfig.createFeeRuleController();
                        CreditController creditController = AppConfig.createCreditController();
                        menu = new Menu(authController, tellerController,accountController,transactionController,feeRuleController,creditController);
                        running = menu.tellerMenu();
                    }

                    case "ADMIN" -> {
                        ClientController adminController = AppConfig.createClientController(currentUser);
                        AccountController accountController = AppConfig.createAccountController();
                        TransactionController transactionController = AppConfig.createTransactionController();
                        FeeRuleController feeRuleController = AppConfig.createFeeRuleController();
                        CreditController creditController = AppConfig.createCreditController();
                        menu = new Menu(authController,adminController,accountController,transactionController,feeRuleController,creditController);
                        running = menu.adminMenu();
                    }
                    case "MANAGER" -> {
                        ClientController managerController = AppConfig.createClientController(currentUser);
                        AccountController accountController = AppConfig.createAccountController();
                        TransactionController transactionController = AppConfig.createTransactionController();
                        FeeRuleController feeRuleController = AppConfig.createFeeRuleController();
                        CreditController creditController = AppConfig.createCreditController();
                        menu = new Menu(authController,managerController,accountController,transactionController,feeRuleController,creditController);
                        running = menu.managerMenu();
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
