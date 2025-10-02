package org.example.config;

import org.example.controller.*;
import org.example.entities.User;
import org.example.repository.*;
import org.example.repository.implementation.*;
import org.example.service.*;
import org.example.service.implementation.*;

public class AppConfig {


        public static AuthController createAuthController(){
            RoleRepository roleRepository = new RoleRepositoryImp();
            UserRepository userRepository = new UserRepositoryImp(roleRepository);
            AuthService    authService = new AuthServiceImp(userRepository);

                return new AuthController(authService,roleRepository);

        }

        public static ClientController createClientController(User currentUser){
            RoleRepository roleRepository = new RoleRepositoryImp();
            UserRepository userRepository = new UserRepositoryImp(roleRepository);
            ClientRepository clientRepository = new ClientRepositoryImp(userRepository);
            ClientService clientService = new ClientServiceImp(clientRepository);


            return new ClientController(clientService,currentUser);

        }


        public static AccountController createAccountController(){
            RoleRepository roleRepository = new RoleRepositoryImp();
            UserRepository userRepository = new UserRepositoryImp(roleRepository);

            AccountRepository accountRepository = new AccountRepositoryImp();
            AccountService accountservice = new AccountServiceImp(accountRepository);

            ClientRepository clientRepository = new ClientRepositoryImp(userRepository);
            ClientService clientService = new ClientServiceImp(clientRepository);

            return new AccountController(accountservice,clientService);
        }


    public static TransactionController createTransactionController() {
        AccountRepository accountRepository = new AccountRepositoryImp();
        AccountService accountService = new AccountServiceImp(accountRepository);
        RoleRepository roleRepository = new RoleRepositoryImp();
        UserRepository userRepository = new UserRepositoryImp(roleRepository);
        ClientRepository clientRepository = new ClientRepositoryImp(userRepository);
        ClientService clientService = new ClientServiceImp(clientRepository);
        FeeRuleRepository feeRuleRepository = new FeeRuleRepositoryImp();
        FeeRuleService feeRuleService = new FeeRuleServiceImp(feeRuleRepository);
        TransactionRepository transactionRepository = new TransactionRepositoryImp();
        TransactionService transactionService = new TransactionServiceImp(transactionRepository, feeRuleService, accountRepository);
        return new TransactionController(transactionService, accountService, clientService);
    }

    public static FeeRuleController createFeeRuleController(){

            FeeRuleRepository feeRuleRepository = new FeeRuleRepositoryImp();
            FeeRuleService feeRuleService = new FeeRuleServiceImp(feeRuleRepository);

            return new FeeRuleController(feeRuleService);
    }




}
