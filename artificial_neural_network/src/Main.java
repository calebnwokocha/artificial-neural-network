/*
 * Author: Caleb Nwokocha
 * School: The University of Manitoba
 * Faculty: Faculty of Science
 */

public class Main {

    public static void main(String[] args) {
        double[][] dataSet = new double[100000][4];
        double[][] target = new double[dataSet.length / 2][dataSet[0].length];
        double[][] trainingSet = new double[dataSet.length / 2][dataSet[0].length];
        double[][] testSet = new double[dataSet.length / 2][dataSet[0].length];

        for (int i = 0; i < dataSet.length; i++) {
            for (int j = 0; j < dataSet[i].length; j++) {
                dataSet[i][j] = Math.floor(((Math.random() *
                        (dataSet.length - 0 + 1)) + 0));
                if (i < dataSet.length / 2) {
                    trainingSet[i][j] = dataSet[i][j];
                } else if (i > dataSet.length / 2) {
                    testSet[i - ((dataSet.length / 2) + 1)][j] = dataSet[i][j];
                }
            }
        }

        for (int i = 0; i < target.length; i++){
            for (int j = 0; j < target[i].length; j++){
                target[i][j] = Math.random();
            }
        }

        Perceptron agent_1 = new Perceptron(50, 4);
        agent_1.debugger(true);
        agent_1.speak(false);

        target = agent_1.softmax(target);

                agent_1.train(target, trainingSet, 0.00000001, 0.4);
        agent_1.test(trainingSet);

       /* Reinforce reinforce = new Reinforce();
        reinforce.train(6);*/
    }
}