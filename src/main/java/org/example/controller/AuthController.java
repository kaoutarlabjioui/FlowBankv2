package org.example.controller;

import org.example.entities.Role;
import org.example.entities.User;
import org.example.repository.RoleRepository;
import org.example.repository.UserRepository;
import org.example.service.AuthService;
import org.example.utils.ConsoleUtils;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;

public class AuthController {

    private AuthService authService;
    private RoleRepository roleRepository;


    public AuthController(AuthService authService , RoleRepository roleRepository ){
        this.authService = authService;
        this.roleRepository = roleRepository;

    }

    public String register(){
        try{
        String prenom = ConsoleUtils.readString("enter your first name : ");
        String nom = ConsoleUtils.readString("enter your last name : ");
        String email;
        String password;

        while(true){

            email = ConsoleUtils.readString("Enter your email : ");
            String emailRegex = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
            if(email.matches(emailRegex)) break;
            System.out.println("Invalid email try again ");

        }

        while(true){
            password = ConsoleUtils.readString("Enter your password (min 6 chars, 1 digit , 1 uppercase) :  ");

            if (password.length() >= 6 &&
                    password.matches(".*[0-9].*") &&
                    password.matches(".*[A-Z].*")) {
                break;
            } else {
                System.out.println(" Password invalid try again.");
            }
        }

        List<Role> roles = roleRepository.findAll();
        System.out.println("Available roles : ");
        for(int i=0;i<roles.size();i++){

            System.out.println((i+1) + " - " + roles.get(i).getRoleName());

        }
        int roleChoice = ConsoleUtils.readInt("please choose a role by number : ", 1 ,roles.size());

        Role selectedRole = roles.get(roleChoice -1);
        boolean success = authService.register(prenom,nom,email,password,selectedRole);

           if (success) {
                System.out.println("User registered successfully!");
                return email;
            } else {
                System.out.println("Registration failed.");
                return null;
            }

        } catch (Exception e) {
            System.err.println("An error occurred during registration: " + e.getMessage());

            return null;
        }


    }

    public void login(){
       String username = ConsoleUtils.readString("Enter your username : ");
       String password = ConsoleUtils.readString("Enter your password : ");

       User user = authService.login(username,password);

       if(user != null){
           System.out.println("login successful welcome " + user.getRole().getRoleName() + user.getPrenom() + user.getNom() );
       }else {
           System.out.println("Invalide username or password ");
       }

    }


}
