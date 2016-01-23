/*
 * This source code is free and its created for the purpose of complete the course "Artificial intelligence"
 * on Polish-Japanese Academy of Information Technology. 
 */
package neuralNet;

import java.util.Random;

/**
 * @author Michal Kostewicz
 * @index S11474 
 * BackPropagationNeuralNet
 */
public class BackPropagationNeuralNet {
    
    //Momentum
    double alfaMomentum;

    //Learning coefficient
    double etaLearningCoefficient;
    
    //Coefficient for activation functions
    double betaForActivationFunct;
    
    //Arguments holds index of chosen function
    int chosenFunction;
    
    //Output from each of the neuron
    double netOutput[];

    //Input to each of the neuron
    double neuronIn[][];

    //Amount of layers in this neural net. 
    int layerAmount;

    //Amount of neurons in each of the layer
    int howManyInputs;
    int howManyOutputs;
    int howManyHidden[];
    int howManyHiddenTotal;
    int howManyTotal;

    //Start index of every layer
    final int inputLayerIndex;
    final int hiddenLayerIndex[];
    final int outputLayerIndex;

    //All the weights in every perceptron and all layers + weigts deltas and past weights
    double wAllWeight[];
    double wAllWeightChange[];
    double wAllWeightDelta[];
    
    //Total weights required
    int totalWeightsRequired;

    //Error delta on each one of the neuron
    double errorDelta[];

    //Error on output neurons
    double errorOnNeuronOut[];
    
    //Neural net estimated error on output data
    double estimatedError;
    
    public BackPropagationNeuralNet(int howManyInputs, int howManyOutputs, double momentum,
            int neuronsOnhidden[], double learningCoefficient, double betaForActivationFunct, int chosenFunction) {
        this.layerAmount = neuronsOnhidden.length; 

        //Initialize perceptrons amount
        this.howManyInputs = howManyInputs;
        this.howManyOutputs = howManyOutputs;
        this.howManyHidden = neuronsOnhidden;
        
        this.howManyTotal = howManyInputs + howManyOutputs;  
        for (int z = 0 ; z < this.layerAmount ; z ++){            
            this.howManyTotal += this.howManyHidden[z];
            this.howManyHiddenTotal += this.howManyHidden[z];
        }   
        
        //Initialize total weights required       
        this.totalWeightsRequired = this.howManyInputs * this.howManyHidden[0];
        for (int z = 0 ; z < this.layerAmount- 1 ; z ++){
            if ((z + 1) < this.layerAmount){
            this.totalWeightsRequired += this.howManyHidden[z] * this.howManyHidden[z + 1];
            }
        }
        this.totalWeightsRequired += this.howManyHidden[this.layerAmount - 1] * this.howManyOutputs;
        
        //Initialize index of first layer - Input
        this.inputLayerIndex = 0;

        //Initialize index of hidden layers
        this.hiddenLayerIndex = new int [this.layerAmount];
        this.hiddenLayerIndex[0] = this.howManyInputs;
        for (int z = 1 ; z < this.layerAmount; z ++){
            this.hiddenLayerIndex[z] += this.hiddenLayerIndex[z - 1] + this.howManyHidden[z - 1];
        }
        
        //Initialize index of third layer - Output
        this.outputLayerIndex = this.howManyTotal - this.howManyOutputs;    

        //Initialize momentum
        this.alfaMomentum = momentum;

        //Initialize learning coefficient
        this.etaLearningCoefficient = learningCoefficient;

        //Initialize coefficient for activation functions
        this.betaForActivationFunct = betaForActivationFunct;
        
        //Choose activation function
        this.chosenFunction = chosenFunction;
    
        // Initialize array of input to each of the neuron
        this.neuronIn = new double[layerAmount][];

        // Initialize array of output from each of the neuron
        this.netOutput = new double[this.howManyTotal];

        //Initialize all weights arrays
        this.wAllWeight = new double[this.totalWeightsRequired];
        this.wAllWeightChange = new double[this.totalWeightsRequired];
        this.wAllWeightDelta = new double[this.totalWeightsRequired];

        //Initialize all error arrays
        this.errorDelta = new double[this.howManyTotal];
        this.errorOnNeuronOut = new double[this.howManyTotal];

        this.initializeWeights(new Random(System.currentTimeMillis()));

    }

        /** Method to initialize the weights with random weights */
    public final void initializeWeights(Random randomGen) {
        for (int i = 0; i < this.wAllWeight.length; i++) {
            this.wAllWeight[i] = (((randomGen.nextDouble() % 1000000L) / 1700.0) - 9.8) * 0.0015;
            if (this.wAllWeight[i] == 0) {
                this.wAllWeight[i] = 0.01492;
            }
            this.wAllWeightChange[i] = 0;
            this.wAllWeightDelta[i] = 0;
        }
    }
    
    /** Method gives output from given input data */
    public double[] feedForward(double input[]) {
        double output[] = new double[this.howManyOutputs];

        // Initialize input neurons output with data given from input
        for (int i = 0; i < this.howManyInputs; i++) {
            this.netOutput[i] = input[i];
        }

        // Getting output from hidden neurons. Data is taken from input layer neurons and
        // multiplay with weight that is assigned to this link
        int index = 0;
        for (int i = this.hiddenLayerIndex[0]; i < this.hiddenLayerIndex[0] + this.howManyHidden[0]; i++) {
            double sum = 0;

            for (int j = 0; j < this.howManyInputs; j++) {
                sum += netOutput[j] * this.wAllWeight[index++];
            }
            this.netOutput[i] = choosenFunction(sum);
        }
        
        //Getting output from next layers of hidden neurons. Data is taken from first hidden layer and
        // multiplay with weight that is assigned to this link
        for (int z = 1 ; z < this.layerAmount ; z ++){
            for (int i = this.hiddenLayerIndex[z]; i < this.hiddenLayerIndex[z] + this.howManyHidden[z]; i++) {
                double sum = 0;

                for (int j = this.hiddenLayerIndex[z - 1]; j < this.hiddenLayerIndex[z]; j++) {
                    sum += netOutput[j] * this.wAllWeight[index++];
                }
                this.netOutput[i] = choosenFunction(sum);
            }
        }
        // Getting output from output layer. Same as for hidden layer but finally output from this layer is real output and
        // will be checked for errors and if its training it will be used to backpropagate
        for (int i = this.outputLayerIndex; i < this.howManyTotal; i++) {
            double sum = 0;

            for (int j = this.hiddenLayerIndex[this.layerAmount - 1]; j < this.outputLayerIndex; j++) {
                sum += this.netOutput[j] * this.wAllWeight[index++];
            }
            this.netOutput[i] = choosenFunction(sum);
            output[i - outputLayerIndex] = this.netOutput[i];
        }
        return output;
    }

    /** Method is used to calculate error between actual and expected output one each neuron*/
    public void calculateErrorsOnNeurons(double[] expectedOut) {

        // Resetting all errors (hidden layer and output layer)
        for (int i = this.howManyInputs; i < this.howManyTotal; i++) {
            this.errorOnNeuronOut[i] = 0;
        }

        // Get the errors from neurons in output layer. Get the estimated error. Get error delta at output layer.
        for (int i = this.outputLayerIndex; i < this.howManyTotal; i++) {
            this.errorOnNeuronOut[i] = expectedOut[i - outputLayerIndex] - this.netOutput[i];
            this.estimatedError += this.errorOnNeuronOut[i] * this.errorOnNeuronOut[i];
            this.errorDelta[i] = this.errorOnNeuronOut[i] * this.netOutput[i] * (1 - this.netOutput[i]);
        }

        // Get the errors from last hidden layer and calculate Weights deltas at hidden to output weights
        int index = this.totalWeightsRequired - (this.howManyHidden[this.layerAmount - 1] * this.howManyOutputs);
        for (int i = this.outputLayerIndex; i < this.howManyTotal; i++) {
            for (int j = this.hiddenLayerIndex[this.layerAmount - 1]; j < this.outputLayerIndex; j++) {
                this.wAllWeightDelta[index] += this.errorDelta[i] * this.netOutput[j];
                this.errorOnNeuronOut[j] += this.wAllWeight[index++] * this.errorDelta[i];
            }
        }

        // Get the errors deltas at last hidden layer.
        for (int i = this.hiddenLayerIndex[this.layerAmount - 1]; i < this.outputLayerIndex; i++) {
            this.errorDelta[i] = this.errorOnNeuronOut[i] * this.netOutput[i] * (1 - this.netOutput[i]);
        }
        
        //Get the errors from other hidden layers and calculate Weights deltas at hidden to other hidden weights
        for (int z = 1 ; z < this.layerAmount ; z ++){
            index = this.totalWeightsRequired - (this.howManyHidden[this.layerAmount - 1] * this.howManyOutputs) -
                    (this.howManyHidden[z] * this.howManyHidden[z - 1]);
            for (int i = this.hiddenLayerIndex[z]; i < this.hiddenLayerIndex[z] + this.howManyHidden[z]; i++) {
                for (int j = this.hiddenLayerIndex[z - 1]; j < this.hiddenLayerIndex[z]; j++) {
                    this.wAllWeightDelta[index] += this.errorDelta[i] * this.netOutput[j];
                    this.errorOnNeuronOut[j] += this.wAllWeight[index++] * this.errorDelta[i];
                }
            }

             // Get the errors deltas at hidden layers.
            for (int i = this.hiddenLayerIndex[z - 1]; i < this.hiddenLayerIndex[z]; i++) {
                this.errorDelta[i] = this.errorOnNeuronOut[i] * this.netOutput[i] * (1 - this.netOutput[i]);
            }
        }
        // Get the errors from input layer and calculate Weights deltas at input to hidden weights
        index = 0;
        for (int i = this.hiddenLayerIndex[0]; i < this.hiddenLayerIndex[0] + this.howManyHidden[0]; i++) {
            for (int j = 0; j < this.hiddenLayerIndex[0]; j++) {
                this.wAllWeightDelta[index] += this.errorDelta[i] * this.netOutput[j];
                this.errorOnNeuronOut[j] += this.wAllWeight[index++] * this.errorDelta[i];
            }
        }
    }
    
    /** Back propagate errors to each of the layers in neural net */
    public void weightsAdaptation() {
        for (int i = 0; i < this.wAllWeight.length; i++) {
            this.wAllWeightChange[i] = (this.etaLearningCoefficient * this.wAllWeightDelta[i]) + 
                    (this.alfaMomentum * this.wAllWeightChange[i]);
            this.wAllWeight[i] += this.wAllWeightChange[i];
            this.wAllWeightDelta[i] = 0;
        }
    }

    /** Method calculate squared error of neural net */
    public double getError(int inputSamplesLength) {
        double sqrtError = (double) Math.sqrt(this.estimatedError / (inputSamplesLength * this.howManyOutputs));
        this.estimatedError = 0;
        return sqrtError;
    }

    /** Bellow are functions possible to use in neural net. Sigmoid is the one that make proper results*/
    
    /**Sigmoid activation function*/
    private double sigmoid(double inputSum) {
        return (double) (1.0 / (1 + (double) Math.exp(this.betaForActivationFunct*(-1.0 * inputSum))));
    }
    
        /**Tangent activation function*/
    private double tangent(double inputSum) {
        return (double) Math.tanh(this.betaForActivationFunct*inputSum);
    }
    
        /**Hiperbolic Tangent activation function*/
   private double hiperbolicTangent(double inputSum) {
        return (double) (2.0 / (1 + (double) Math.exp(this.betaForActivationFunct*(-2.0 * inputSum)))) - 1;
    }
   
   private double choosenFunction(double inputSum){
       double sumAfterFunction;
       switch(this.chosenFunction){
           case 1:sumAfterFunction = sigmoid(inputSum);
                  break;
           case 2: sumAfterFunction = tangent(inputSum);
                  break;
           case 3:sumAfterFunction = hiperbolicTangent(inputSum);
               break;
           default: return sigmoid(inputSum);
                     
       }
       return sumAfterFunction;
   }
}
