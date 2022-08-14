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
    private String activationType;

    // Neuron constructor
    public Neuron (String cFunctionName, int weightCount, double learningRate, String activationType) {
        this.activationType = activationType;
        this.learningRate = learningRate;
        this.cFunctionName = cFunctionName;
        this.weights = new ArrayList<>();
        deltas.add(Math.random()); // Initialize random delta for bias
        for (int i = 0; i < weightCount; i++) {
            this.weights.add(Math.random()); // Initialize random weights
            deltas.add(Math.random()); // Initialize random deltas for weights
        } this.bias = (float) Math.random(); // Initialize random bias
        this.regularize();
    }

    public String getCFunctionName() { return this.cFunctionName; }

    public String getActivationType() { return this.activationType; }

    public ArrayList<Double> getWeights() { return this.weights; }

    public double getValue() { return value; }

    public double getBias() { return bias; }

    public ArrayList<Double> getDeltas() { return deltas; }

    public double getLearningRate() { return learningRate; }

    public void setLearningRate(double learningRate) { this.learningRate = learningRate; }

    public void setCFunctionName (String cFunctionName) { this.cFunctionName = cFunctionName; }

    public void setActivationType (String activationType) { this.activationType = activationType; }

    public void normalize (double normalizedValue) { this.value = normalizedValue; }

    public void activate (double[] parameters) {
        CFunction cFunction = new CFunction(this.cFunctionName, parameters);
        double cValue = cFunction.getValue();
        switch (this.activationType) {
            case "identity" -> this.value = this.identity(cValue);
            case "tanh" -> this.value= this.tanh(cValue);
        }
    }

    public void optimize (double[][][] neighborWeights, double delta) {
        this.setErrorDerivatives(neighborWeights, delta);
        for (int i = 0; i < this.deltas.size(); i++) {
            if (i == 0) { this.bias = this.bias - (this.learningRate * this.deltas.get(i)); }
            else { this.weights.set(i - 1, this.weights.get(i - 1) - (this.learningRate * this.deltas.get(i))); }
        } this.regularize();
    }

    private void regularize () {
        double maxWeight = 0.0;
        for (double weight : this.weights) { maxWeight = (maxWeight + weight + Math.abs(maxWeight - weight)) * 0.5; }
        for (int i = 0; i < weights.size(); i++) { weights.set(i, weights.get(i) - maxWeight); }
    }

    private void setErrorDerivatives(double[][][] neighborWeights, double delta) {
        double sum = 0.0;
        double product = 1.0;
        for (int i = 0; i < neighborWeights.length; i++) {
            for (int j = 0; j < neighborWeights[i].length; j++) {
                for (int k = 0; k < neighborWeights[i][j].length; k++) {
                    sum += neighborWeights[i][j][k];
                } product *= sum;
            } this.deltas.set(i + 1, product * delta);
            // 0th index of errorDeriatives is for bias.
        } // TODO: this.deltas.size(0, )
    }

    private double identity (double x) { return x; }

    private double identityDerivative (double x) { return 1.0; }

    private double tanh (double x) { return (2 / (1 + Math.pow(Math.E, -(2 * x)))) - 1; }

    private double tanhDerivative (double x) { return 1 - Math.pow(tanh(x), 2); }
}
