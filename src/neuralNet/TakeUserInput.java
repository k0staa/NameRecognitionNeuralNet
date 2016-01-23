/*
 * This source code is free and its created for the purpose of complete the course "Artificial intelligence"
 * on Polish-Japanese Academy of Information Technology. 
 */
package neuralNet;

import java.util.Scanner;

/**
 * @author Michal Kostewicz
 * @index S11474
 * TakeUserInput is static class used mostly to get user input. 
 */
public class TakeUserInput {  
   
    public static double[] getUserInput(int maxLength){
        System.out.println("PROVIDE NAME FOR TESTING (must be "+maxLength+" characters long): ");
        Scanner in = new Scanner(System.in);
        String nameProvided = in.nextLine();
        if(maxLength != nameProvided.length()){
            return null;
        }
        return getBitArrayFromCharArray(nameProvided.toCharArray());
    }
    
        // Method prepere vector of bits from given name 
    public static double [] getBitArrayFromCharArray(char charArray[]){
        double [] arrayToReturn = new double [charArray.length * 7];
        int index = 0;
            for (int c : charArray) {                
                for (char d : (Integer.toBinaryString(c)).toCharArray()) {
                   arrayToReturn[index++] =   Integer.parseInt("" + d);
                   
                    
                }
            
             }
            return arrayToReturn;
    } 
        
}
