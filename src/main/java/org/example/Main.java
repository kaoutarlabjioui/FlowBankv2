package org.example;

import org.example.utils.DatabaseConnection;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        DatabaseConnection db = DatabaseConnection.getInstance();
        DatabaseConnection db1 = DatabaseConnection.getInstance();
        DatabaseConnection db2 = DatabaseConnection.getInstance();
        DatabaseConnection db3 = DatabaseConnection.getInstance();
        System.out.println(db1 == db2); // true
        System.out.println(db2 == db3);
      /*  if (db.getConnection() != null) {
            System.out.println("✅ Connexion réussie !");
        } else {
            System.out.println("❌ Connexion échouée !");
        }*/
    }

}