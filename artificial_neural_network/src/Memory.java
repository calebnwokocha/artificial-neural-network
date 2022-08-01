/*
 * Author: Caleb Nwokocha
 * School: The University of Manitoba
 * Faculty: Faculty of Science
 */

import org.apache.commons.lang3.ArrayUtils;
import java.util.Arrays;
import java.util.stream.IntStream;

public class Memory extends Optimizer {
    private double[][] objectiveMemory = new double[(int) Math.pow(2, 71)]
            [(int) Math.pow(2, 71)];
    private double[][][][] weightMemory = new double[(int) Math.pow(2, 71)]
            [(int) Math.pow(2, 71)][(int) Math.pow(2, 71)][(int) Math.pow(2, 71)];
    private double[][] neurons;
    private double[][][] preWeights;
    private double[][][] postWeights;
    private int networkSize;
    private int layerSize;

    public Memory (int networkSize, int layerSize) {
        this.networkSize = networkSize;
        this.layerSize = layerSize;
    }

    public void learn (double[] objective, double[] trainingSet,
                       double learningRate, double baseLoss)
    {
        double[][][] objectiveWeights;
        int numOfMemory = 0;

        if (Arrays.stream(objectiveMemory).noneMatch(memory -> memory == objective)) {
            printLine("\nObjective not found");

            objectiveMemory[numOfMemory] = objective;

            this.neurons = new double[(int)multiply(this.networkSize, numOfMemory)][this.layerSize];
            this.preWeights = new double[this.neurons.length - 1][this.layerSize][this.layerSize];
            this.postWeights = new double[this.neurons.length - 1][this.layerSize][this.layerSize];

            // Populate arrays.
            Arrays.stream(this.neurons).forEach(i -> Arrays.fill(i, 0.0));
            if (this.objectiveMemory.length == 1) {
                Arrays.stream(this.preWeights).forEach(i -> Arrays.stream(i).
                        forEach(j -> Arrays.fill(j, 0.0)));
                Arrays.stream(this.postWeights).forEach(i -> Arrays.stream(i).
                        forEach(j -> IntStream.iterate(0, k -> k < j.length, k -> k + 1).
                                forEach((int k) -> j[k] = Math.random())));
            } else {
                objectiveWeights = this.postWeights;
                System.arraycopy(objectiveWeights, 0, this.preWeights, 0, objectiveWeights.length);
                System.arraycopy(objectiveWeights, 0, this.postWeights, 0, objectiveWeights.length);

                IntStream.range(objectiveWeights.length, this.preWeights.length)
                        .forEach(i -> IntStream.range(0, this.preWeights[i].length).forEach(j ->
                                IntStream.range(0, this.preWeights[i][j].length).forEach(k ->
                                        this.preWeights[i][j][k] = 0.0)));

                IntStream.range(objectiveWeights.length, this.postWeights.length)
                        .forEach(i -> IntStream.range(0, this.postWeights[i].length).forEach(j ->
                                IntStream.range(0, this.postWeights[i][j].length).forEach(k ->
                                        this.postWeights[i][j][k] = Math.random())));
            }

            this.postWeights = gradientDescent(this.neurons, this.preWeights, this.postWeights,
                    objective, trainingSet, learningRate, baseLoss);

            weightMemory[numOfMemory] = this.postWeights;
            this.preWeights = this.postWeights;

            numOfMemory += 1;

            printLine("\nWeight is");
            for (double[][] preWeight : this.preWeights) {
                for (double[] doubles : preWeight) {
                    for (double aDouble : doubles) {
                        printText(aDouble + " ");
                    }
                    printLine("");
                }
                printLine("");
            }
        } else {
            printLine("\nObjective found.");
            int objectiveIndex = ArrayUtils.indexOf(objectiveMemory, objective);
            objectiveWeights = weightMemory[objectiveIndex];

            System.arraycopy(objectiveWeights, 0, this.preWeights, 0, objectiveWeights.length);

            printLine("\nNew weight is");
            for (double[][] preWeight : this.preWeights) {
                for (double[] doubles : preWeight) {
                    for (double aDouble : doubles) {
                        printText(aDouble + " ");
                    }
                    printLine("");
                }
                printLine("");
            }

            this.postWeights = gradientDescent(this.neurons, this.preWeights, this.postWeights,
                    objective, trainingSet, learningRate, baseLoss);

            weightMemory[objectiveIndex] = this.postWeights;
        }
    }
}
