/*
 * Author: Caleb Nwokocha
 * School: The University of Manitoba
 * Faculty: Faculty of Science
 */

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.stream.*;
import static java.lang.Math.*;
import java.util.Scanner;


public class Perceptron extends Statistics {
    private double[][] neurons;
    private double[][][] preWeights; // Previous weight
    private double[][][] postWeights; // Next weight
    private double[][][] preBias; // Previous bias
    private double[][][] postBias; // Next bias
    private final double e = 2.71828;
    private double[] expectedTarget;
    private double[][] targetOutput;

    public Perceptron (int networkSize, int layerSize) {
        this.neurons = new double[networkSize][layerSize];
        this.preWeights = new double[networkSize - 1][layerSize][layerSize];
        this.postWeights = new double[networkSize - 1][layerSize][layerSize];

        // Populate arrays.
        Arrays.stream(this.neurons).forEach(i -> Arrays.fill(i, 0.0));
        Arrays.stream(this.preWeights).forEach(i -> Arrays.stream(i).
                forEach(j -> Arrays.fill(j, 0.0)));
        Arrays.stream(this.postWeights).forEach(i -> Arrays.stream(i).
                forEach(j -> IntStream.iterate(0, k -> k < j.length, k -> k + 1).
                        forEach((int k) -> j[k] = Math.random())));
    }

    public double[][] softmax (double[][] weights) {
        double weightSum = 0.0;
        double maxWeight = 0.0;

        for (int i = 0; i < weights.length; i++) {
            // Find the maximum weight in weights.
            for (int j = 0; j < weights[i].length; j++) {
                maxWeight = multiply(sum(sum(maxWeight, weights[i][j]),
                        Math.abs(sum(maxWeight, -weights[i][j]))), 0.5);
            }

            for (int j = 0; j < weights[i].length; j++) {
                weights[i][j] = sum(weights[i][j], -maxWeight);
                weightSum = sum(weightSum, Math.pow(this.e, weights[i][j]));
            }

            for (int j = 0; j < weights[i].length; j++) {
                weights[i][j] = multiply(Math.pow(this.e, weights[i][j]), 1.0 / weightSum);
            }

            weightSum = 0.0;
        }

        return weights;
    }

    public double sigmoid (double predictedOutput) {
        return 1.0 / sum(1.0, Math.pow(this.e, -predictedOutput));
    }

    public double reLU(double predictedOutput) {
        return multiply(sum(sum(0, predictedOutput),
                Math.abs(sum(0, -predictedOutput))), 0.5);
    }

    public double loss (double[] targetOutput, double[] predictedOutput) {
        double[] outputDifference = new double[predictedOutput.length];
        double loss;

        for (int i = 0; i < outputDifference.length; i++) {
            try {
                outputDifference[i] = sum(targetOutput[i], -predictedOutput[i]);
            } catch (ArrayIndexOutOfBoundsException e) {
                break;
            }
        }

        loss = norm(outputDifference, 2);

        printLine("\nLoss is " + loss);

        return loss;
    }

    public double loss (double[][] targetOutput, double[][] predictedOutput) {
        double[][] outputDifference = sum(targetOutput,
                multiply(new double[][][]{predictedOutput}, new double[]{-1}));
        double loss = norm(outputDifference);
        printLine("\nLoss is " + loss);
        return loss;
    }

    public double[][][] backpropagate (double preLost, double postLost) {
        double preWeightsSum = 0;
        double postWeightsSum = 0;
        double[][][] jacobian = new double[this.preWeights.length]
                [this.preWeights[0].length][this.preWeights[0][0].length];

        for (int i = this.preWeights.length - 1; i >= 0; i--) {
            for (int j = this.preWeights[i].length - 1; j >= 0; j--) {
                for (int k = this.preWeights[i][j].length - 1; k >= 0 ; k--)
                {
                    if (i == this.preWeights.length - 1) {
                        jacobian[i][j][k] = sum(postLost, -preLost) /
                                sum(this.postWeights[i][j][k],
                                        -this.preWeights[i][j][k]);
                    } else {
                        for (int l = 0; l < jacobian[i].length; l++) {
                            for (int m = 0; m < jacobian[i][l].length; m++) {
                                preWeightsSum = sum(preWeightsSum,
                                        this.preWeights[i][l][m]);
                                postWeightsSum = sum(postWeightsSum,
                                        this.postWeights[i][l][m]);
                            }
                        }

                        // Compute the pre and post lost contribution of a neuron.
                        preLost = (Double)multiply(this.neurons[i + 1],
                                this.preWeights[i][j]) / preWeightsSum;
                        postLost = (Double)multiply(this.neurons[i + 1],
                                this.postWeights[i][j]) / postWeightsSum;

                        jacobian[i][j][k] = sum(postLost, -preLost) /
                                sum(this.postWeights[i][j][k],
                                        -this.preWeights[i][j][k]);
                    }
                }
            }
        }

        return jacobian;
    }

    public void train (double[][] targetOutput, double[][] trainingSet,
                       double learningRate, double baseLoss)
    {
        double[][][] jacobian;
        double[] predictedOutput;
        this.targetOutput = targetOutput;
        this.expectedTarget = expectedValues(targetOutput, probabilities(targetOutput));
        double[] expectedTrainingSet = expectedValues(trainingSet, probabilities(trainingSet));
        double preLoss = 0.0; // Previous loss
        double postLoss = baseLoss + 1;  // Assume that next loss is greater than baseLoss.
        int epoch = 0;

        // Begin training.
        while (postLoss > baseLoss) {
            // Update neurons.
            for (int i = 0; i < this.neurons.length; i++) {
                if (i == 0) {
                    // In the first layer, assign training data to neurons.
                    this.neurons[i] = expectedTrainingSet;
                } else { // Feed-forward routine.
                    this.preWeights[i - 1] = this.softmax(this.preWeights[i - 1]);
                    this.postWeights[i - 1] = this.softmax(this.postWeights[i - 1]);
                    this.neurons[i] = multiply(new double[][]{this.neurons[i - 1]},
                            this.postWeights[i - 1])[0];
                    for (int j = 0; j < this.neurons[i].length; j++) {
                        this.neurons[i][j] = this.sigmoid(this.neurons[i][j]);
                    }

                    if (i == this.neurons.length - 1) {
                        // In the last layer, apply softmax.
                        this.neurons[i] = this.softmax(new double[][]{this.neurons[i]})[0];
                    }
                }
            }

            predictedOutput = this.neurons[this.neurons.length - 1]; // Prediction is the last layer.
            postLoss = this.loss(this.expectedTarget, predictedOutput);

            // Update parameters only if learning loss is greater than base loss.
            if (postLoss > baseLoss) {
                jacobian = this.backpropagate(preLoss, postLoss);
                preLoss = postLoss; // Later update because of backpropagation differential operation.
                this.preWeights = this.postWeights;
                this.postWeights = sum(this.preWeights, multiply(new double[][][][]{jacobian},
                        new double[]{-learningRate}));
            }

            epoch += 1;

            if (isDebugging()) {
                printLine("\nFor training set:");
                for (double[] row : trainingSet) {
                    for(double value : row) {
                        printText(value + " ");
                    }
                    printLine("");
                }

                printLine("\nAverage training set is:");
                for (double value : expectedTrainingSet) {
                    printLine(value + "");
                }
                printLine("\nTarget is ");
                for (double value : this.expectedTarget) {
                    printLine(value + "");
                }
                printLine("\nPrediction is ");
                for (double value : predictedOutput) {
                    printLine(value + "");
                }
                printLine("\nLoss is " + postLoss);
                printLine("\nEpoch is " + epoch);
            }
        }
    }

    public void test (double[][] testSet) {
        double loss;
        double[] prediction;

        for (int i = 0; i < testSet.length; i++) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("\nBegin test?");
            String answer = scanner.nextLine();

            if (answer.equals("yes")) {
                for (int j = 0; j < this.neurons.length; j++) {
                    // Check if loop is at first neuron layer,
                    if (j == 0) {
                        // If first layer, then assign training data to layer.
                        this.neurons[j] = testSet[i];
                    } else {
                        // Feed-forward routine.
                        this.neurons[j] = multiply(new double[][]{this.neurons[j - 1]},
                                this.postWeights[j - 1])[0];
                        for (int k = 0; k < this.neurons[j].length; k++) {
                            this.neurons[j][k] = this.sigmoid(this.neurons[j][k]);
                        }
                    }
                }

                prediction = this.neurons[this.neurons.length - 1];
                loss = this.loss(this.targetOutput[i], prediction);

                if (isDebugging()) {
                    printLine("\nFor test set:");
                    for (double value : testSet[i]) {
                        printLine(value + "");
                    }
                    printLine("\nTarget is ");
                    for (double value : this.targetOutput[i]) {
                        printLine(value + "");
                    }
                    printLine("\nPrediction is:");
                    for (double value : prediction) {
                        printLine(value + "");
                    }
                    printLine("\nLoss is " + loss);
                }
            }
        }
    }
}