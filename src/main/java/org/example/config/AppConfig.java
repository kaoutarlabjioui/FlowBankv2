package org.example.config;

import org.example.controller.AccountController;
import org.example.controller.AuthController;
import org.example.controller.ClientController;
import org.example.entities.User;
import org.example.repository.AccountRepository;
import org.example.repository.ClientRepository;
import org.example.repository.RoleRepository;
import org.example.repository.UserRepository;
import org.example.repository.implementation.AccountRepositoryImp;
import org.example.repository.implementation.ClientRepositoryImp;
import org.example.repository.implementation.RoleRepositoryImp;
import org.example.repository.implementation.UserRepositoryImp;
import org.example.service.AccountService;
import org.example.service.AuthService;
import org.example.service.ClientService;
import org.example.service.implementation.AccountServiceImp;
import org.example.service.implementation.AuthServiceImp;
import org.example.service.implementation.ClientServiceImp;

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



}
