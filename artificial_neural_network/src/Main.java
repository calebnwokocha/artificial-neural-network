import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Neuron[][] trainingSet = new Neuron[151][13];
        Neuron[][] testingSet = new Neuron[151][13];
        float learningRate = 0.000001f;
        float baseError = 0.03f;

        float[][] dataSet;
        float[][] trainingSetFloat = new float[151][13];
        float[] trainingObjective = new float[151];
        float[][] newTrainingObjective = new float[151][13];

        float[][] testingSetFloat = new float[151][13];
        float[] testingObjective = new float[151];
        float[][] newTestingObjective = new float[151][13];

        int k = 0;
        boolean kTest = false;

        dataSet = read("artificial_neural_network/src/dataset/processed.cleveland.data");

        for (int i = 0; i < dataSet.length - 1; i++) {
            for (int j = 0; j < dataSet[i].length - 1; j++) {
                if (i < (dataSet.length - 1) / 2) {
                    trainingSetFloat[i][j] = dataSet[i][j];
                    if (dataSet[i][dataSet[i].length - 1] > 0) {
                        trainingObjective[i] = 1.0f;
                    }
                } else {
                    testingSetFloat[k][j] = dataSet[i][j];
                    if (dataSet[i][dataSet[i].length - 1] > 0) {
                        testingObjective[k] = 1.0f;
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
                    newTrainingObjective[i][j] = 0.0f;
                    newTestingObjective[i][j] = 0.0f;
                }
            }
        }

        for (int i = 0; i < trainingSetFloat.length; i++) {
            for (int j = 0; j < trainingSetFloat[i].length; j++) {
                trainingSet[i][j] = new Neuron(10);
                testingSet[i][j] = new Neuron(10);
            }
        }

        for (int i = 0; i < trainingSet.length; i++) {
            for (int j = 0; j < trainingSet[i].length; j++) {
                trainingSet[i][j].setValue(trainingSetFloat[i][j]);
                testingSet[i][j].setValue(testingSetFloat[i][j]);
            }
        }

        Network network = network = new Network(10,
                trainingSet[0].length, 10, 11, 12,
                13, 14, 9, 8, 7, newTrainingObjective[0].length);

        ArrayList<Float> errorRecord = new ArrayList<Float>();
        errorRecord = train(network, trainingSet, newTrainingObjective, learningRate, baseError);
        test(network, testingSet, newTestingObjective);

        System.out.println("\n\nRecord of error");
        for (float error : errorRecord) {
            System.out.println(error);
        }
        /*Network network = new Network(3, 1, 5, 3);
        Neuron input = new Neuron(5);
        input.setValue(4);
        network.feed(input);
        for (int i = 0; i < 10000000; i++) {
            network.optimize(new float[]{1.0f, 0.0f, 1.0f}, 0.01f);
        }

        for (int i = 0; i < network.getLayers().length; i++) {
            System.out.println("\nLAYER " + i);
            for (int j = 0; j < network.getLayers()[i].getNeurons().length; j++) {
                System.out.print("Neuron " + j + ": ");
                System.out.print("Value = " + network.getLayers()[i].getNeurons()[j].getValue());
                System.out.print("; Weights = " + Arrays.toString(network.getLayers()[i].getNeurons()[j].getWeights()));
                System.out.print("; Bias = " + network.getLayers()[i].getNeurons()[j].getBias());
                System.out.println("");
            }
        }

        //network.optimize(new float[]{9, 4, 79, 4, 79, 4, 79, 4, 7, 8}, 0.1f);
*/
    }

    public static ArrayList<Float> train (Network network, Neuron[][] trainingSet, float[][] objective, float learningRate, float baseError) {
        System.out.println("_____________________________________________________________________________________" +
                "__________________________________________________________________________________________________");
        System.out.println("Initializing network...");
        System.out.println("Network initialized");
        System.out.println("_______________________________________________________________________________________" +
                "________________________________________________________________________________________________");
        System.out.println("Checking network error...");
        int epoch = 1;
        ArrayList<Float> errorRecord = new ArrayList<Float>();
        network.feed(trainingSet[0]);
        network.optimize(objective[0], learningRate);
        errorRecord.add(network.getNetworkError());
        if (network.getNetworkError() > baseError) {
            System.out.println("Error is high: " + network.getNetworkError() + ". Starting training...");
            int y = 0;
            while (network.getNetworkError() > baseError && y < 100) {
                System.out.println("Epoch " + epoch);
                for (int i = 0; i < trainingSet.length; i++) {
                    System.out.println("_____________________________________________________________________________" +
                            "__________________________________________________________________________________________________________");
                    network.feed(trainingSet[i]);
                    float[] example = new float[trainingSet[i].length];
                    for (int j = 0; j < example.length; j++) {
                        example[j] = trainingSet[i][j].getValue();
                    }
                    float[] prediction = new float[network.getLayers()[network.getLayers().length - 1].getNeurons().length];
                    for (int j = 0; j < prediction.length; j++) {
                        prediction[j] = network.getLayers()[network.getLayers().length - 1].getNeurons()[j].getValue();
                    }
                    System.out.println("Example " + i + ": " + Arrays.toString(example));
                    System.out.println("Prediction: " + Arrays.toString(prediction));
                    System.out.println("Objective: " + Arrays.toString(objective[i]));
                    float error = 0.0f;
                    Util util = new Util();
                    for (int k = 0; k < prediction.length; k++) {
                        error += util.squaredError(objective[i][k], prediction[k]);
                    }
                    System.out.println("Network error: " + error);
                    System.out.println("Optimizing network...");
                    network.optimize(objective[i], learningRate);
                    errorRecord.add(network.getNetworkError());
                    System.out.println("Optimization completed");
                }
                y++;
            }
            System.out.println("______________________________________________________________________________________" +
                    "_________________________________________________________________________________________________");
            epoch += 1;
        } else {
            System.out.println("Error is low: " + network.getNetworkError());
        }

        return errorRecord;
    }

    public static void test (Network network, Neuron[][] testSet, float[][] objective) {
        System.out.println("_____________________________________________________________________________________" +
                "__________________________________________________________________________________________________");
        System.out.println("Starting testing...");
        for (int i = 0; i < testSet.length; i++) {
            System.out.println("__________________________________________________________________________________" +
                    "_____________________________________________________________________________________________________");
            network.feed(testSet[i]);
            float[] test = new float[testSet[i].length];
            for (int j = 0; j < test.length; j++) {
                test[j] = testSet[i][j].getValue();
            }
            float[] prediction = new float[network.getLayers()[network.getLayers().length - 1].getNeurons().length];
            for (int j = 0; j < prediction.length; j++) {
                prediction[j] = network.getLayers()[network.getLayers().length - 1].getNeurons()[j].getValue();
            }
            System.out.println("Test " + i + ": " + Arrays.toString(test));
            System.out.println("Prediction: " + Arrays.toString(prediction));
            System.out.println("Objective: " + Arrays.toString(objective[i]));
            float error = 0.0f;
            Util util = new Util();
            for (int k = 0; k < prediction.length; k++) {
                error += util.squaredError(objective[i][k], prediction[k]);
            }
            System.out.println("Network error: " + error);
        }
    }

    public static float[][] read(String fileName) {
        String[][] datasetString = new String[303][14];
        float[][] datasetFloat = new float[303][14];
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
                    datasetFloat[i][j] = 0.0F;
                } else {
                    datasetFloat[i][j]= Float.parseFloat(datasetString[i][j]);
                }
            }
        }

        return datasetFloat;
    }
}
