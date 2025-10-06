package org.example.service.implementation;

import org.example.entities.Client;
import org.example.repository.ClientRepository;
import org.example.service.ClientService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class ClientServiceImp implements ClientService {

    private ClientRepository clientRepository;

    public ClientServiceImp(ClientRepository clientRepositoy){
        this.clientRepository = clientRepositoy;

    }

private  Client client;
    @Override
    public Client createClient(String cin, String nom, String prenom, String tele, String address, LocalDate birthDate, BigDecimal salaire, UUID tellerId) {

        if (clientRepository.findByCin(cin) != null) {
            System.out.println(" Client with CIN " + cin + " already exists.");
            return null;
        }
        Client client = new Client(cin ,nom,prenom,tele , address,birthDate,salaire,tellerId);

         clientRepository.save(client);
        return client;
    }

    public Client getClientByCin(String cin){

        return clientRepository.findByCin(cin);
    }



    @Override
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }
}
