/*
 * This source code is free and its created for the purpose of complete the course "Artificial intelligence"
 * on Polish-Japanese Academy of Information Technology. 
 */
package neuralNet;



/**
 * @author Michal Kostewicz
 * @index S11474
 * Main class
 */
public class MyNeuralNet {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("Welcome in MyNeuralNet. Program will choose if name provided "
                + "in configuration ('Michal') match name enter on the testing phase.");
        
        /*Number of hidden neurons on each of the hidden layer - 
        *Best Options:
        *one layer with 42 neurons
        *3 layers - 42 - 10 - 20
        */
        int [] hiddenNeurons = {42,10,20};
        
       /** 1. tested Name, 2. learning Coeficient, 3. momentum, 4. number of hidden neurons on each hidden layer,
        *  5. argument For Acceptance Function ,  6. how Many Samples to train in percentage (30 % of them will be incorrect)
        * , 7. Chosen function index */
        Controller myConfig = new Controller ("Michal", 1.0 , 0.5, hiddenNeurons, 10, 80, 1);
        myConfig.training();
        myConfig.testNeuralNet();

    }
    
   
    
}
