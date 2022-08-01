import java.util.Scanner;

public class Test extends Optimizer{
    private double[][] neurons;
    private double[][][] postWeights;
    private double[][] targetOutput;

    public Test (double[][] neurons,
                 double[][][] postWeights,
                 double[][] targetOutput)
    {
        this.neurons = neurons;
        this.postWeights = postWeights;
        this.targetOutput = targetOutput;
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
                    } else if (j == this.neurons.length - 1) {
                        // In the last layer, apply softmax.
                        //this.neurons[j] = softmax(new double[][]{this.neurons[j]})[0];
                    } else {
                        // Feed-forward routine.
                        this.neurons[j] = multiply(this.postWeights[i - 1],
                                new double[][]{this.neurons[i - 1]})[0];
                        for (int k = 0; k < this.neurons[j].length; k++) {
                            this.neurons[j][k] = sigmoid(this.neurons[j][k]);
                        }
                    }

                }

                prediction = this.neurons[this.neurons.length - 1];
                loss = loss(this.targetOutput[i], prediction);

                if (isDebugging()) {
                    printLine("\nFor test set:");
                    for (double value : testSet[i]) {
                        printLine(value + "");
                    }
                    printLine("\nTarget is " + this.targetOutput[i]);

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
