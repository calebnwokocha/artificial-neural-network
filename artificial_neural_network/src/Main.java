/*
 * Author: Caleb Nwokocha
 * School: The University of Manitoba
 * Faculty: Faculty of Science
 */

import java.util.ArrayList;


public class Main {

    public static void main(String[] args) {
        double[][] dataSet;
        double[][] trainingSet = new double[152][13];
        double[] trainingTarget = new double[152];
        double[][] newTrainingTarget = new double[152][13];
        double[][] testSet = new double[152][13];
        double[] testTarget = new double[152];
        double[][] newTestTarget = new double[152][13];
        int k = 0;
        boolean kTest = false;

        FileHandler fileHandler = new FileHandler();
        dataSet = fileHandler.read("artificial_neural_network/src/dataset/processed.cleveland.data");

        for (int i = 0; i < dataSet.length; i++) {
            for (int j = 0; j < dataSet[i].length - 1; j++) {
                if (i < dataSet.length / 2) {
                    trainingSet[i][j] = dataSet[i][j];
                    trainingTarget[i] = dataSet[i][dataSet[i].length - 1];
                } else {
                    testSet[k][j] = dataSet[i][j];
                    testTarget[k] = dataSet[i][dataSet[i].length - 1];
                    kTest = true;
                }
            }
            if (kTest) {
                k++;
            }
        }

        for (int i = 0; i < newTrainingTarget.length; i++) {
            for (int j = 0; j < newTrainingTarget[i].length; j++) {
                if (j == 0) {
                    newTrainingTarget[i][j] = trainingTarget[i];
                    newTestTarget[i][j] = testTarget[i];
                } else {
                    newTrainingTarget[i][j] = 0.0;
                    newTestTarget[i][j] = 0.0;
                }
            }
        }


       /* trainingSet = new double[][] {
                {1, 2, 3, 4, 6, 7},
                {1, 2, 2, 4, 7, 7},
                {9, 2, 3, 4, 3, 7},
                {1, 5, 3, 6, 6, 7}
        };

        double[][] trainingObjective = new double[][] {
                {0.5, 0, 0.1, 0, 0.2, 0.2},
                {0.5, 0, 0, 0, 0, 0.5},
                {0.1, 0.1, 0.5, 0, 0.3, 0},
                {0.5, 0, 0, 0, 0, 0.5},
        };*/

        /*double[][] l = trainingSet;
        double[][] m = trainingObjective;

        ArrayList<double[][]> u = new ArrayList<>();

        u.add(trainingSet);
        u.add(trainingObjective);

        for (double[][] preWeight : u) {
            for (double[] doubles : preWeight) {
                for (double aDouble : doubles) {
                    System.out.print(aDouble + " ");
                }
                System.out.println("");
            }
            System.out.println("");
        }

        System.out.println("Index is " + u.indexOf(l));
        System.out.println("Is contained " + u.contains(l));
*/

        /*Memory memory = new Memory(10, 6);
        memory.debugger(true);
        for (int i = 0; i < trainingObjective.length; i++) {
            memory.learn(trainingObjective[0], trainingSet[i], 0.001, 0.9);
            System.out.println("\nNext iteration.");
        }*/

        Optimizer agent_1 = new Optimizer();
        agent_1.debugger(true);
        agent_1.speak(false);

        //target = agent_1.softmax(target);

        double[][] neuron = new double[2][2];
        double[][][] preWeights = new double[neuron.length - 1][neuron[0].length][neuron[0].length];
        double[][][] postWeights = new double[neuron.length - 1][neuron[0].length][neuron[0].length];
        double[][][] solutionWeights = new double[neuron.length - 1][neuron[0].length][neuron[0].length];

        for (int i = 0; i < neuron.length; i++) {
            for(int j = 0; j < neuron[i].length; j++) {
                neuron[i][j] = 0.0;
            }
        }

        System.out.println("This weight");
        for (int i = 0; i < preWeights.length; i++) {
            for(int j = 0; j < preWeights[i].length; j++) {
                for (int y = 0; y < preWeights[i][j].length; y++) {
                    preWeights[i][j][y] = 0.0;
                    postWeights[i][j][y] = Math.random();
                    System.out.print(postWeights[i][j][y] + " ");
                }
                System.out.println();
            }
            System.out.println();
        }

        for (int i = 0; i < newTrainingTarget.length; i++) {
            if (i == 0) {
                solutionWeights = agent_1.gradientDescent(neuron, preWeights, postWeights, newTrainingTarget[i],
                        trainingSet[i], 0.00000001, 4);
            } else {
                solutionWeights = agent_1.gradientDescent(neuron, preWeights, solutionWeights, newTrainingTarget[i],
                        trainingSet[i], 0.00000001, 4);
            }
        }

        Test agent_1_test = new Test(neuron, solutionWeights, newTestTarget);
        agent_1_test.test(testSet);



       /* Reinforce reinforce = new Reinforce();
        reinforce.gradientDescent(6);*/
    }
}