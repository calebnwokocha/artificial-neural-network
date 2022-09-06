/*
 * AUTHOR: CALEB PRINCEWILL NWOKOCHA
 * SCHOOL: THE UNIVERSITY OF MANITOBA
 * DEPARTMENT: COMPUTER SCIENCE
 */

import java.util.ArrayList;

public class Layer {
    private final Neuron[] neurons;

    // Layer constructor
    public Layer (int neuronCount, String[] cFunctionName, double[] learningRates, String[] activationType,
                  int[] rowIDs, int[] columnIDs, ArrayList<ArrayList<Integer[]>> links) throws WrongInitialization
    {
        // neuronCount is the number of neurons in the layer.
        if (cFunctionName.length != neuronCount || learningRates.length != neuronCount
                || activationType.length != neuronCount || rowIDs.length != neuronCount
                || columnIDs.length != neuronCount || links.size() != neuronCount)
        { // The length of cFunctionName, weightCounts, learningRates, etc., must equal neuronCount.
            throw new WrongInitialization("Wrong initialization of layer"); }
        else { // Initialize all neurons.
            this.neurons = new Neuron[neuronCount];
            for (int i = 0; i < neurons.length; i++) {
                neurons[i] = new Neuron(cFunctionName[i], learningRates[i], activationType[i], rowIDs[i],
                        columnIDs[i], links.get(i));
            }
        }
    }

    public Neuron[] getNeurons() { return neurons; } // Return layer neurons.

    public void activate (double[][] parameters) { // Activate all neurons.
        for (int i = 0; i < this.neurons.length; i++) { neurons[i].activate(parameters[i]); }
    }

    public void optimize (double[][][][] downstreamDeltas, double delta) { // Optimize all neurons.
        for (int i = 0; i < this.neurons.length; i++) { neurons[i].optimize(downstreamDeltas[i], delta); }
    }

    public void normalize () { // normalize all neurons for their values sum to equal 1.
        double[] neuronValues = new double[neurons.length];
        double[] normalizedValues = new double[neuronValues.length];
        for (int i = 0; i < neuronValues.length; i++) { neuronValues[i] = neurons[i].getValue(); }
        normalizedValues = this.softmax(neuronValues);
        for (int i = 0; i < normalizedValues.length; i++) { neurons[i].normalize(normalizedValues[i]); }
    }

    private double[] softmax (double[] x) {
        double sum = 0.0;
        double[] y = new double[x.length];
        for (double i : x) { sum += Math.pow(Math.E, i); }
        for (int i = 0; i < y.length; i++) { y[i] = Math.pow(Math.E, x[i]) / sum;
        } return y;
    }
}