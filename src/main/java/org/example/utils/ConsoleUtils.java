package org.example.utils;

import java.math.BigDecimal;
import java.util.Scanner;

public class ConsoleUtils {

    private static final Scanner sc = new Scanner(System.in);


    public static String readString(String prompt){

        String input ;
        while(true){
            System.out.print(prompt);
            input = sc.nextLine();
            if(input != null && !input.trim().isEmpty()){
                break;
            }

            System.out.println("Input cannot be empty");
        }

        return input;

    }


    public static int readInt(String prompt , int min, int max){
        int value;
        while(true){

            System.out.print(prompt);
            String input = sc.nextLine();
         try{
             value = Integer.parseInt(input);
             if (value >= min && value<= max) break;
             else System.out.println("enter a number between " + min + "and" + max);
         }catch (NumberFormatException e) {
             System.out.println("Invalid number.Try again");
         }


        }
        return value;
    }

    public static BigDecimal readPositiveBigDecimal(String prompt) {
        BigDecimal value;
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine();
            try {
                value = new  BigDecimal(input);
                if (value.compareTo(BigDecimal.ZERO) > 0) break;
                else System.out.println("Amount must be positive. Try again.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Try again.");
            }
        }
        return value;
    }


}
