package org.example.controller;

import org.example.entities.Client;
import org.example.entities.User;
import org.example.service.ClientService;
import org.example.utils.ConsoleUtils;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.UUID;

public class ClientController {


   private  ClientService clientService;
   private User currentUser;

   public ClientController(ClientService clientService , User currentUser){
       this.clientService = clientService;
       this.currentUser = currentUser;
   }



   public void createClient(){

       try{
           System.out.println("\n=== CRÉATION D'UN NOUVEAU CLIENT ===");

           String cin ;
           while(true){
               cin = ConsoleUtils.readString("Enter Client CIN : ");
               if (cin != null && cin.length() >= 6 && cin.length() <= 20){
                   if(clientService.getClientByCin(cin) ==null){
                       break;
                   }else {
                       System.out.println("this CIN already exist ");
                   }
               }else {

                   System.out.println("Invalid CIN ");
               }

           }
           String nom ;
           while(true){
               nom = ConsoleUtils.readString("Enter client last name : ");
               if (nom != null && !nom.trim().isEmpty() && nom.length() >= 2){
                   nom = nom.trim().toUpperCase();
                   break;
               } else {
                   System.out.println("Invalid last name (min 2 chars).");
               }
           }

           String prenom;
           while (true) {
               prenom = ConsoleUtils.readString("Enter client first name : ");
               if (prenom != null && !prenom.trim().isEmpty() && prenom.length() >= 2) {
                   prenom = prenom.trim().substring(0, 1).toUpperCase() +
                           prenom.trim().substring(1).toLowerCase();
                   break;
               } else {
                   System.out.println("Invalid first name (min 2 chars).");
               }
           }

           String tele;
           while (true) {
               tele = ConsoleUtils.readString("Enter client phone number (format: 06XXXXXXXX) : ");
               if (tele != null && tele.matches("^0[6-7][0-9]{8}$")) {
                   break;
               } else {
                   System.out.println(" Invalid! Forme expected: 06XXXXXXXX ou 07XXXXXXXX");
               }
           }
           String address= ConsoleUtils.readString("Enter client address : ");
               if(address != null && address.trim().isEmpty()){
                   address = null ;
               }
           LocalDate birthDate = null;
           String dateNaissance;
           while(true){
               dateNaissance = ConsoleUtils.readString("Enter Client birthDate : ");
               try {
                   DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                   birthDate = LocalDate.parse(dateNaissance,formatter);

                   LocalDate today = LocalDate.now();

                   if(birthDate.plusYears(18).isAfter(today)){
                       System.out.println("the client must be of legal age (18 years minimum). ");
                   } else{
                       break; // on sort si OK
                   }

               } catch (DateTimeParseException e){
                   System.out.println("Invalid date format. Use dd/MM/yyyy");
               }
           }
              BigDecimal salaire =  ConsoleUtils.readPositiveBigDecimal("Enter client monthly Salary (MAD) : ");


               Client client = clientService.createClient(cin, nom , prenom, tele, address, birthDate,salaire,currentUser.getId());

                if(client!= null){
                    System.out.println("Client create with success");
                    System.out.println("📋 Résumé: " + prenom + " " + nom + " (CIN: " + cin + ")");
                }else {
                    System.out.println("Error creating client.");
                }

       }catch (Exception e){

           System.out.println("Unexpected error");


       }



   }





}
