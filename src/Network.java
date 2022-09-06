/*
 * AUTHOR: CALEB PRINCEWILL NWOKOCHA
 * SCHOOL: THE UNIVERSITY OF MANITOBA
 * DEPARTMENT: COMPUTER SCIENCE
 */

import java.util.ArrayList;

public class Network {
    private final Layer[] layers;
    private double error;

    // Network constructor
    public Network (int layerCount, int[] neuronCounts, String[][] cFunctionName, double[][] learningRates,
                    String[][] activationType, int[][] rowIDs, int[][] columnIDs,
                    ArrayList<ArrayList<ArrayList<Integer[]>>> links) throws WrongInitialization
    {
        // neuronCounts contain the number of neurons in each layer of the network.
        if (neuronCounts.length != layerCount || cFunctionName.length != layerCount || learningRates.length != layerCount
                || activationType.length != layerCount)
        { // The length of neuronCounts, cFunctionName, weightCounts, learningRates, and activationType must equal layerCount.
            throw new WrongInitialization("Wrong initialization of network");
        } else { // Initialize all layers.
            this.layers = new Layer[layerCount];
            for (int i = 0; i < this.layers.length; i++) {
                this.layers[i] = new Layer(neuronCounts[i], cFunctionName[i], learningRates[i], activationType[i]);
            }
        }
    }

    public Layer[] getLayers() { return this.layers; } // Return network layers.

    public double getError() { return this.error; } // Return network error.

    public void feedfoward (double[][][] parameters) {
        for (int i = 0; i < this.layers.length; i++) { // For all layers in the network.
            this.layers[i].activate(parameters[i]);  // Activate all layers.
            if (i > 0) { this.layers[i].normalize(); } // Normalize all layer except input layer.
        }
    }

    public void backprop (double error, double[] objective, double[][][][][] downstreamDeltas) {
        this.setError(objective, error); // Calculate network error.
        double delta = Math.pow(this.error * 2, 0.5); // Derivative of network error.
        for (int i = 0; i < layers.length; i++) { // Optimize all layers.
            if (i != layers.length - 1) { layers[i].optimize(downstreamDeltas[i], delta); } // Don't optimize output layer.
        }
    }

    // Set the network error to the error parameter if objective is null,
    // otherwise, calculate the network error using the objective.
    private void setError(double[] objective, double error) {
        if (objective == null) { this.error = error; }
        else { error  = 0.0;
            Layer outputLayer = this.layers[this.layers.length - 1];
            for (int i = 0; i < outputLayer.getNeurons().length; i++) { // Using Squared Euclidean Distance to calculate error.
                error += Math.pow((objective[i] - outputLayer.getNeurons()[i].getValue()), 2) * 0.5;
            } this.error = error;
        }
    }
}
