/*
 * AUTHOR: CALEB PRINCEWILL NWOKOCHA
 * SCHOOL: THE UNIVERSITY OF MANITOBA
 * DEPARTMENT: COMPUTER SCIENCE
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Neuron[][] trainingSet = new Neuron[151][13];
        Neuron[][] testingSet = new Neuron[151][13];
        double learningRate = 0.1;
        double baseError = 0.03;

        double[][] dataSet;
        double[][] trainingSetFloat = new double[151][13];
        double[] trainingObjective = new double[151];
        double[][] newTrainingObjective = new double[151][13];

        double[][] testingSetFloat = new double[151][13];
        double[] testingObjective = new double[151];
        double[][] newTestingObjective = new double[151][13];

        int k = 0;
        boolean kTest = false;

        dataSet = read("artificial_neural_network/src/dataset/processed.cleveland.data");

        for (int i = 0; i < dataSet.length - 1; i++) {
            for (int j = 0; j < dataSet[i].length - 1; j++) {
                if (i < (dataSet.length - 1) / 2) {
                    trainingSetFloat[i][j] = Math.random();
                    if (dataSet[i][dataSet[i].length - 1] > 0) {
                        trainingObjective[i] = Math.random();
                    }
                } else {
                    testingSetFloat[k][j] = dataSet[i][j];
                    if (dataSet[i][dataSet[i].length - 1] > 0) {
                        testingObjective[k] = Math.random();
                    }
                    kTest = true;
                }
            }
            if (kTest) {
                k++;
            }
        }

        for (int i = 0; i < newTrainingObjective.length; i++) {
            for (int j = 0; j < newTrainingObjective[i].length; j++) {
                if (j == 0) {
                    newTrainingObjective[i][j] = trainingObjective[i];
                    newTestingObjective[i][j] = testingObjective[i];
                } else {
                    newTrainingObjective[i][j] = Math.random();
                    newTestingObjective[i][j] = Math.random();
                }
            }
        }

        for (int i = 0; i < trainingSetFloat.length; i++) {
            for (int j = 0; j < trainingSetFloat[i].length; j++) {
                //trainingSet[i][j] = new Neuron(10);
                //testingSet[i][j] = new Neuron(10);
            }
        }

        for (int i = 0; i < trainingSet.length; i++) {
            for (int j = 0; j < trainingSet[i].length; j++) {
                //trainingSet[i][j].setValue(trainingSetFloat[i][j]);
                //testingSet[i][j].setValue(testingSetFloat[i][j]);
            }
        }

        /*Network network = network = new Network(10,
                trainingSet[0].length, 10, 11, 12,
                13, 14, 9, 8, 7, newTrainingObjective[0].length);
*/
       /* ArrayList<Double> errorRecord = new ArrayList<>();
        errorRecord = train(network, trainingSet, newTrainingObjective, learningRate, baseError);
        test(network, testingSet, newTestingObjective);

        System.out.println("\n\nRecord of error");
        for (double error : errorRecord) {
            System.out.println(error);
        }*/
    }

    public static double[][] read(String fileName) {
        String[][] datasetString = new String[303][14];
        double[][] datasetDouble = new double[303][14];
        int count = 0;

        try {
            File file = new File(fileName);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String data = scanner.nextLine();

                // Copy data to datasetString.
                datasetString[count] = data.split(",");
                count++;
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Convert datasetString to double array.
        for (int i = 0; i < datasetString.length; i++) {
            for (int j = 0; j < datasetString[i].length; j++) {
                if (datasetString[i][j].equals("?")) {
                    datasetDouble[i][j] = 0.0F;
                } else {
                    datasetDouble[i][j]= Float.parseFloat(datasetString[i][j]);
                }
            }
        }

        return datasetDouble;
    }
}