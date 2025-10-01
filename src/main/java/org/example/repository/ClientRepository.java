package org.example.repository;

import org.example.entities.Client;

public interface ClientRepository {

     void save( Client client);
     Client findByCin(String cin);
}
