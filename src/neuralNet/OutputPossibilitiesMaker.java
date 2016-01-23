/*
 * This source code is free and its created for the purpose of complete the course "Artificial intelligence"
 * on Polish-Japanese Academy of Information Technology. 
 */
package neuralNet;

import java.util.Arrays;

/**
 * @author Michal Kostewicz
 * @index S11474 
 * OutputPossibilitiesMaker is used to make possible inputs that
 * NeuralNet will accept, like John,joHn,jONH etc. They will be used to train
 * NeuralNet. It will store all posibilities in binary matrix. There are
 * 2^numberOfLetter possibilities
 */
public class OutputPossibilitiesMaker {

    //Possibilities are stored in binary matrix
    private int[][][] binaryPossibilities;
    private final double howManyPossibilities;
    
    //Input neurons needed for program (count number of letter * 7 (bits))
    private int inputPerceptronNeeded;

    //Output neurons needed for program - there are few possibilities :
    // - All possible word modifications number (2^numberofLetter)
    // - 1 perceptron (2 possibilities = 0 or 1)  CHOSEN!
    // - others?
    private final int outputPerceptronNeeded = 1;

    // Length of the name provided
    private final int testedOutputNameLength;

    public OutputPossibilitiesMaker(String testedOutputName) {
        this.testedOutputNameLength = testedOutputName.length();

        // Count all possibilities of provided name
        this.howManyPossibilities =  Math.pow(2, testedOutputNameLength);

        // Prepare array of possibilities
        this.binaryPossibilities = new int[(int)howManyPossibilities][testedOutputNameLength][7];
        this.preparePossibilities(testedOutputName, (int)howManyPossibilities);

        // Input neurons number is multiplication of name length and number of bits in Asci number
        this.inputPerceptronNeeded = testedOutputNameLength * 7;
    }

    public final void preparePossibilities(String testedOutputName, int howManyPossiblities) {
        // Take testedOutputName and change it to lower case
        testedOutputName = testedOutputName.toLowerCase();

        // Prepare String outputs array just for debug purpose
        char[][] possibleOutputsCharArray = new char[howManyPossiblities][testedOutputName.length()];

        // Get every possibility from given string. Its 2^n possibilities where 
        // n - is a number of characters       
        int combinations = 1 << testedOutputName.length();

        for (int i = 0; i < combinations; i++) {
            char[] result = testedOutputName.toCharArray();
            for (int j = 0; j < testedOutputName.length(); j++) {
                if (((i >> j) & 1) == 1) {
                    result[j] = Character.toUpperCase(testedOutputName.charAt(j));
                }
            }

            possibleOutputsCharArray[i] = Arrays.copyOf(result, testedOutputName.length());
            //System.out.println(new String(possibleOutputsCharArray[i] ));         //DEBUG

            // Prepare array that will store binary code for testing and learning neural net
            int characterIndex = 0;
            for (int c : result) {

                int[] bit = new int[7];
                int bitIndex = 0;
                for (char d : (Integer.toBinaryString(c)).toCharArray()) {
                    bit[bitIndex] = Integer.parseInt("" + d);
                    bitIndex++;

                }

                this.binaryPossibilities[i][characterIndex] = Arrays.copyOf(bit, bit.length);
                //System.out.println("Word: "+ i +"Char: " + characterIndex + Arrays.toString(this.binaryPossibilities[i][characterIndex]));    //DEBUG
                characterIndex++;

            }
        }
    }
    
    // Method gets one possibility from binary possibilities array from given index
    public double[] getBinaryPossibilityForOneName(int index) {
        double[] returnArray = new double[this.inputPerceptronNeeded];
        int returnArrIndex = 0;
        for (int i = 0; i < this.testedOutputNameLength; i++) {
            for (int y = 0; y < 7; y++) {
                returnArray[returnArrIndex++] = this.binaryPossibilities[index][i][y];
            }
        }
        return returnArray;
    }  
    
    //Getters and setters
    public int[][][] getBinaryPossibilities() {
        return binaryPossibilities;
    }

    public void setBinaryPossibilities(int[][][] binaryPossibilities) {
        this.binaryPossibilities = binaryPossibilities;
    }

    public int getInputPerceptronNeeded() {
        return inputPerceptronNeeded;
    }

    public void setInputPerceptronNeeded(int inputPerceptronNeeded) {
        this.inputPerceptronNeeded = inputPerceptronNeeded;
    }

    public int getOutputPerceptronNeeded() {
        return outputPerceptronNeeded;
    }
    
    public int getTestedOutputNameLength() {
        return testedOutputNameLength;
    }
    
    public double getHowManyPossibilities() {
        return howManyPossibilities;
    }

}
