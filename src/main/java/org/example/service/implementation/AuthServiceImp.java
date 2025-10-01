package org.example.service.implementation;

import org.example.entities.Role;
import org.example.entities.User;
import org.example.repository.UserRepository;
import org.example.service.AuthService;
import org.mindrot.jbcrypt.BCrypt;



public class AuthServiceImp implements AuthService {
    private UserRepository userRepository;

    public AuthServiceImp(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    @Override
    public boolean register(String prenom, String nom, String email, String password, Role role) {
        String username = (prenom + "." + nom + "." + role.getRoleName()).toLowerCase();

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        User user = new User(username,nom,prenom,email,hashedPassword,role);

        return userRepository.save(user);

    }

    @Override
    public User login(String username,String password) {
        User user = userRepository.findByUsername(username);
        if(user != null && BCrypt.checkpw(password, user.getPassword())){

            return user;
        }

        return null ;


    }


}

