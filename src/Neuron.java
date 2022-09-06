/*
 * AUTHOR: CALEB PRINCEWILL NWOKOCHA
 * SCHOOL: THE UNIVERSITY OF MANITOBA
 * DEPARTMENT: COMPUTER SCIENCE
 */

import java.util.ArrayList;

public class Neuron {
    private String cFunctionName;
    private final ArrayList<Double> weights;
    private ArrayList<Double> deltas;
    private double value, bias, learningRate;
    private ArrayList<Integer[]> links;
    private String activationType;
    private int rowID;
    private int columnID;

    // Neuron constructor
    public Neuron (String cFunctionName, double learningRate, String activationType,
                   int rowID, int columnID, ArrayList<Integer[]> links)
    {
        this.activationType = activationType;
        this.learningRate = learningRate;
        this.cFunctionName = cFunctionName;
        this.weights = new ArrayList<>();
        this.rowID = rowID;
        this.columnID = columnID;
        this.links = links;
        deltas.add(Math.random()); // Initialize random delta of bias.
        for (int i = 0; i < links.size(); i++) {
            this.weights.add(Math.random()); // Initialize random weights.
            deltas.add(Math.random()); // Initialize random deltas of weights.
        } this.bias = (float) Math.random(); // Initialize random bias.
        this.regularize(); // Regularize all weights.
    }

    public String getCFunctionName() { return this.cFunctionName; } // Return name of comprehensive function.

    public String getActivationType() { return this.activationType; } // Return type of activation.

    public ArrayList<Double> getWeights() { return this.weights; } // Return neuron weights.

    public double getValue() { return value; } // Return neuron value.

    public double getBias() { return bias; } // Return neuron bias.

    public ArrayList<Double> getDeltas() { return deltas; } // Return derivatives of error w.r.t bias & weights.

    public double getLearningRate() { return learningRate; } // Return learning rate.

    public void setLearningRate(double learningRate) { this.learningRate = learningRate; }

    public void setCFunctionName (String cFunctionName) { this.cFunctionName = cFunctionName; }

    public void setActivationType (String activationType) { this.activationType = activationType; }

    public void normalize (double normalizedValue) { this.value = normalizedValue; }

    public ArrayList<Integer[]> getLinks() { return links; }

    public void setLinks(ArrayList<Integer[]> links) { this.links = links; }

    public int getColumnID() { return columnID; }

    public void setColumnID(int columnID) { this.columnID = columnID; }

    public int getRowID() { return rowID; }

    public void setRowID(int rowID) { this.rowID = rowID; }

    public void activate (double[] parameters) { // Activate neuron, use parameters for comprehensive function.
        CFunction cFunction = new CFunction(this.cFunctionName, parameters);
        double cValue = cFunction.getValue(); // Result of comprehensive function.
        switch (this.activationType) { // Find activation function, and initialize neuron value.
            case "identity" -> this.value = this.identity(cValue);
            case "tanh" -> this.value= this.tanh(cValue);
        }
    }

    public void optimize (double[][][] neighborWeights, double delta) { // Optimize neuron bias & weights.
        this.setDeltas(neighborWeights, delta); // Calculate derivatives of error w.r.t bias & weights.
        for (int i = 0; i < this.deltas.size(); i++) { // Update bias and all weights.
            if (i == 0) { this.bias = this.bias - (this.learningRate * this.deltas.get(i)); } // delta w.r.t to bias is at index 0.
            else { this.weights.set(i - 1, this.weights.get(i - 1) - (this.learningRate * this.deltas.get(i))); }
        } this.regularize(); // Regularize all weights.
    }

    private void regularize () { // Regularize all weights to avoid overflow & underflow.
        double maxWeight = 0.0; // For all weights, weights[i] = weights[i] - maxWeight.
        for (double weight : this.weights) { maxWeight = (maxWeight + weight + Math.abs(maxWeight - weight)) * 0.5; }
        for (int i = 0; i < weights.size(); i++) { weights.set(i, weights.get(i) - maxWeight); }
    }

    private void setDeltas (double[][][] downstreamDeltas, double delta) {

    }

    private double identity (double x) { return x; }

    private double identityDerivative (double x) { return (x + 1.0) * Math.pow(x + 1.0, -1); }

    private double tanh (double x) { return (2 / (1 + Math.pow(Math.E, -(2 * x)))) - 1; }

    private double tanhDerivative (double x) { return 1 - Math.pow(tanh(x), 2); }
}
