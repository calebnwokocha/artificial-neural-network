/*
 * Author: Caleb Nwokocha
 * School: The University of Manitoba
 * Faculty: Faculty of Science
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.*;


public class Optimizer extends Statistics {
    private final double e = 2.71828;

    public Optimizer() {}

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

        return loss;
    }

    public double loss (double[][] targetOutput, double[][] predictedOutput) {
        double[][] outputDifference = sum(targetOutput,
                multiply(new double[][][]{predictedOutput}, new double[]{-1}));
        double loss = norm(outputDifference);
        return loss;
    }

    public double[][][] backpropagate (double[][] neurons, double[][][] preWeights,
                                       double[][][] postWeights, double preLost,
                                       double postLost)
    {
        double preWeightsSum = 0;
        double postWeightsSum = 0;
        double[][][] jacobian = new double[preWeights.length]
                [preWeights[0].length][preWeights[0][0].length];

        for (int i = preWeights.length - 1; i >= 0; i--) {
            for (int j = preWeights[i].length - 1; j >= 0; j--) {
                for (int k = preWeights[i][j].length - 1; k >= 0 ; k--)
                {
                    // Jacobian of the last layer.
                    if (i == preWeights.length - 1) {
                        jacobian[i][j][k] = sum(postLost, -preLost) /
                                sum(postWeights[i][j][k],
                                        -preWeights[i][j][k]);
                    } else {
                        // Compute Jacobian of hidden layers.
                        for (int l = 0; l < jacobian[i].length; l++) {
                            for (int m = 0; m < jacobian[i][l].length; m++) {
                                preWeightsSum = sum(preWeightsSum,
                                        preWeights[i][l][m]);
                                postWeightsSum = sum(postWeightsSum,
                                        postWeights[i][l][m]);
                            }
                        }

                        // Compute the pre and post lost contribution
                        // of hidden neurons.
                        preLost = (Double)multiply(neurons[i + 1],
                                preWeights[i][j]) / preWeightsSum;
                        postLost = (Double)multiply(neurons[i + 1],
                                postWeights[i][j]) / postWeightsSum;

                        jacobian[i][j][k] = sum(postLost, -preLost) /
                                sum(postWeights[i][j][k],
                                        -preWeights[i][j][k]);
                    }
                }
            }
        }

        return jacobian;
    }

    public double[][][] gradientDescent(double[][] neurons, double[][][] preWeights,
                                        double[][][] postWeights, double[] objective,
                                        double[] trainingSet, double learningRate,
                                        double baseLoss)
    {
        double[][][] jacobian;
        double[] predictedOutput;
        double preLoss = 0.0; // Previous loss
        double postLoss = baseLoss + 1;  // Assume that next loss is greater than baseLoss.
        int epoch = 0;

        // Begin training.
        while (postLoss > baseLoss) {
            // Update neurons.
            for (int i = 0; i < neurons.length; i++) {
                if (i == 0) {
                    // In the first layer, assign training data to neurons.
                    neurons[i] = trainingSet;
                } else { // Feed-forward routine.
                    preWeights[i - 1] = this.softmax(preWeights[i - 1]);
                    postWeights[i - 1] = this.softmax(postWeights[i - 1]);
                    neurons[i] = multiply(postWeights[i - 1],
                            new double[][]{neurons[i - 1]})[0];
                    for (int j = 0; j < neurons[i].length; j++) {
                        neurons[i][j] = this.sigmoid(neurons[i][j]);
                    }

                    if (i == neurons.length - 1) {
                        // In the last layer, apply softmax.
                        neurons[i] = this.softmax(new double[][]{neurons[i]})[0];
                    }
                }
            }

            predictedOutput = neurons[neurons.length - 1]; // Prediction is the last layer.
            postLoss = this.loss(objective, predictedOutput);
            //postLoss = sum(objective, -predictedOutput[0]);

            // Update parameters only if learning loss is greater than base loss.
            if (postLoss > baseLoss) {
                jacobian = this.backpropagate(neurons, preWeights, postWeights, preLoss, postLoss);
                preLoss = postLoss; // Later update because of backpropagation differential operation.
                preWeights = postWeights;
                postWeights = sum(preWeights, multiply(new double[][][][]{jacobian},
                        new double[]{-learningRate}));
            }

            epoch += 1;

            if (isDebugging()) {
                printLine("\nFor training set:");
                for (double value : trainingSet) {
                    printLine(value + " ");
                }
                printLine("\nObjective is ");
                for (double value : objective) {
                    printLine(value + " ");
                }
                printLine("\nPrediction is ");
                for (double value : predictedOutput) {
                    printLine(value + "");
                }
                printLine("\nLoss is " + postLoss);
                printLine("\nEpoch is " + epoch);
            }
        }

        return postWeights;
    }
}