package org.example.repository;

import org.example.entities.Account;
import org.example.entities.Client;

import java.util.List;

public interface ClientRepository {

     void save( Client client);
     Client findByCin(String cin);
    List<Client> findAll();

}
