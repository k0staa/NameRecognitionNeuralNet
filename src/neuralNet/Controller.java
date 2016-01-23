/*
 * This source code is free and its created for the purpose of complete the course "Artificial intelligence"
 * on Polish-Japanese Academy of Information Technology. 
 */
package neuralNet;

import java.util.Random;

/**
 * @author Michal Kostewicz
 * @index S11474
 * Configurator is used to configure setting involved in whole application.
 */
public class Controller {
    // Creates all possible combinations of name/word provided. Create binary vectors from asci symbols.
    // Neural net will learn from this possiblities.
    OutputPossibilitiesMaker outputPossibilitiesMaker;
    
    // Main class for back propagation neural net
    BackPropagationNeuralNet backPropagationNeuralNet;
    
    /**All the factors below are used for eliminate abrupt changes or smooth the course of the error function */   
    // Momentum is used to is used to accelerate the learning process by "encouraging" the weight changes to
    // continue in the same direction with larger steps. Furthermore, the momentum term prevents the learning 
    // process from settling in a local minimum. by "over stepping" the small "hill"
    double alfaMomentum; 
    // Learning rate (coeficient) that indicates the relative change in weights. If the learning rate is too low,
    // the network will learn very slowly, and if the learning rate is too high, the network may oscillate around 
    // minimum point (refer to Figure 6), overshooting the lowest point with each weight adjustment
    double etaLearningCoefficient;
    
    /** All the factors below are needed to configure size and shape of neural net */   
    // How many inputs are needed in neural net
    int howManyInputs;   
    // How many outputs are needed in neural net
    int howManyOutputs;
    // How many layers of neurons
    int layerAmount; 
    
    /* All the factors below are needed for training and finally for testing of neural net */
    // minimum error when training
    final double minErr = 0.001;
    // Number of iteration allowed - tey are called EPOCH's
    final long maxEpoch = 50000;  
    // Number of input samples 
    int inputSamplesNumber;
    // Input samples
    double[][] inputSamplesArray ;
    double[][] expectedOutFromSamplesArray;
        
    public Controller(String testedOutputName, double learningCoeficient,
            double momentum, int neuronsOnhidden[], double betaForAcceptanceFunct, double percentageOfInputSamples,
            int chosenFunction){
        this.alfaMomentum = momentum;
        this.layerAmount = neuronsOnhidden.length;
        this.etaLearningCoefficient = learningCoeficient;
        this.outputPossibilitiesMaker = new OutputPossibilitiesMaker(testedOutputName);
        this.howManyInputs = this.outputPossibilitiesMaker.getInputPerceptronNeeded();
        this.howManyOutputs = this.outputPossibilitiesMaker.getOutputPerceptronNeeded();
        this.inputSamplesNumber = (int)(this.outputPossibilitiesMaker.getHowManyPossibilities() * (percentageOfInputSamples / 100));
        this.inputSamplesArray = new double[inputSamplesNumber][howManyInputs];
        this.expectedOutFromSamplesArray = new double[inputSamplesNumber][howManyOutputs];
        this.backPropagationNeuralNet = new BackPropagationNeuralNet(this.howManyInputs, this.howManyOutputs,
                this.alfaMomentum, neuronsOnhidden, this.etaLearningCoefficient, betaForAcceptanceFunct, chosenFunction);
    }
    
    /*Training of neural net*/
    public void training(){
    double error = Double.MAX_VALUE;
    int i;
    prepareInputAndOutputForTraining();
    
		for (i = 0; i < this.maxEpoch && error > this.minErr; i++) {
			for (int y = 0; y < inputSamplesArray.length; y++) {
				/*Feed forward*/
				backPropagationNeuralNet.feedForward(inputSamplesArray[y]);
				/*Get error beetween result output and expected*/
				backPropagationNeuralNet.calculateErrorsOnNeurons(expectedOutFromSamplesArray[y]);
				/*Adapt weigts to the given error*/
				backPropagationNeuralNet.weightsAdaptation();
                                if((i % 1 )== 0 && y == 1){
                                    for (int outputIndex = backPropagationNeuralNet.outputLayerIndex ; outputIndex < backPropagationNeuralNet.howManyTotal ; outputIndex++) {
                                             // System.out.println(backPropagationNeuralNet.netOutput[outputIndex]); //FOR DEBUG AND GRAPH
                                    }
                                }
			}
 
			error = backPropagationNeuralNet.getError(inputSamplesArray.length);
			System.out.println( "TRAINING | EPOCH " + i + " | ERROR " + error);
                        //System.out.println( error); //FOR DEBUG AND GRAPH
                        
		}
        //System.out.println("TRAINING END | EPOCH " + i + " | FINAL ERROR " + error);
    }
    
    //Method used to test neural net after its trained
    public void testNeuralNet(){
        boolean testEnd = false;
        while (!testEnd){
		System.out.println("TESTING NEURAL NET (to quit enter blank)");
                double [] tempArray = TakeUserInput.getUserInput(outputPossibilitiesMaker.getTestedOutputNameLength());
                if (tempArray == null || tempArray.length < 1){
                    testEnd = true;                   
                    System.out.println("TESTING ABORTED (due wrong length of name provided)");
                    break;
                }
		double output[] = backPropagationNeuralNet.feedForward(tempArray);
                System.out.println("ANSWER IS: "+output[0]);
 
        } 
    }
    
    //Method used to prepare Inputs and Outputs for training process
   private void prepareInputAndOutputForTraining(){
       //Random is for incorrect answers;
      Random rand = new Random(System.currentTimeMillis());
      for (int i = 0 ;i < this.inputSamplesNumber ; i++){
          //correct ansewrs
          if (i < (this.inputSamplesNumber/1.5)){ 
                  this.inputSamplesArray[i] = outputPossibilitiesMaker.getBinaryPossibilityForOneName(i);                      
                  this.expectedOutFromSamplesArray[i][0] = 1;
          //incorrect ansewrs
          }else{    
              for (int y = 0 ; y < this.howManyInputs ; y++){
                  this.inputSamplesArray[i][y] = rand.nextInt(2);
              }
              this.expectedOutFromSamplesArray[i][0] = 0;
          }
      }                  
    }
}
