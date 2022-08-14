/*
 * AUTHOR: CALEB PRINCEWILL NWOKOCHA
 * SCHOOL: THE UNIVERSITY OF MANITOBA
 * DEPARTMENT: COMPUTER SCIENCE
 */

public class Layer {
    private Neuron[] neurons;

    // Layer constructor
    public Layer (int neuronCount, String[] cFunctionName, int[] weightCounts, double[] learningRates,
                  String[] activationType) throws WrongInitialization
    {
        // neuronCount is the number of neurons in the layer.
        if (cFunctionName.length != neuronCount || weightCounts.length != neuronCount
                || learningRates.length != neuronCount || activationType.length != neuronCount)
        { // The length of cFunctionName, weightCounts, learningRates, and activationType must equal neuronCount.
            throw new WrongInitialization("Wrong initialization of layer"); }
        else { // Initialize all neurons.
            this.neurons = new Neuron[neuronCount];
            for (int i = 0; i < neurons.length; i++) {
                neurons[i] = new Neuron(cFunctionName[i], weightCounts[i], learningRates[i], activationType[i]);
            }
        }
    }

    public Neuron[] getNeurons() { return neurons; } // Returns layer neurons.

    public void activate (double[][] parameters) { // Activate all neurons.
        for (int i = 0; i < this.neurons.length; i++) { neurons[i].activate(parameters[i]); }
    }

    public void optimize (double[][][][] neighborWeights, double delta) { // Optimize all neurons.
        for (int i = 0; i < this.neurons.length; i++) { neurons[i].optimize(neighborWeights[i], delta); }
    }

    public void normalize () {
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