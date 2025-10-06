package org.example.service;

import org.example.entities.Client;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ClientService {

Client createClient(String cin, String nom , String prenom, String tele, String address, LocalDate birthDate, BigDecimal salaire, UUID tellerId);
 Client getClientByCin(String cin);
    List<Client> getAllClients();
}
