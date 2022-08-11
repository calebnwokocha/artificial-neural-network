public class Network {
    private Layer[] layers;
    private float networkError;
    private final Util util = new Util();

    // Network constructor
    public Network (int numOfLayers, int... layerSizes) {
        // LayerSize is the number of neuron in a layer.
        if (layerSizes.length != numOfLayers) { // Check if length of layerSizes is equal to numOfLayers.
            // TODO: Throw an exception.
        } else { // Initialize layers and neurons.
            this.layers = new Layer[numOfLayers];
            for (int i = 0; i < this.layers.length; i++) { // For all layers in the network.
                if (i != this.layers.length - 1) { // Check if iteration is at output layer.
                    this.layers[i] = new Layer(layerSizes[i], layerSizes[i + 1]);
                } else { this.layers[i] = new Layer(layerSizes[i], 0); }
            }
        }
    }

    public Layer[] getLayers() { return this.layers; }

    public void setLayers(Layer[] layers) { this.layers = layers; }

    public float getNetworkError() { return networkError; }

    public void setNetworkError(float networkError) { this.networkError = networkError; }

    public void feed (Neuron... input) {
        for (int i = 0; i < this.layers.length; i++) { // For all layers in the network.
            Layer currentLayer = this.layers[i];
            if (i == 0) { // Inject input to input layer.
                currentLayer.setNeurons(input);
            } else { // Feed from the hidden to output layer.
                Neuron[] previousLayerNeurons = this.layers[i - 1].getNeurons();
                for (int k = 0; k < currentLayer.getNeurons().length; k++) { // For all neurons in current layer.
                    float expectedValue = 0.0f;
                    for (Neuron previousLayerNeuron : previousLayerNeurons) {
                        expectedValue += previousLayerNeuron.getValue() * previousLayerNeuron.getWeights()[k];
                    } float biasedValue = expectedValue + currentLayer.getNeurons()[k].getBias();
                    currentLayer.getNeurons()[k].setValue(this.util.reLU(biasedValue));
                }
            }
            if (i > 0) { currentLayer.normalize(); } // Normalize all layer except input layer.
        }
    }

    public void optimize (float[] objective, float learningRate) {
        Layer parentLayer;
        Layer outputLayer = this.layers[this.layers.length - 1];
        float[] output = new float[outputLayer.getNeurons().length]; // Cache values of neurons in output layer.
        float layerError = 0.0f;
        float neuronError = 0.0f;
        float errorDerivative = 0.0f;


        // Output layer optimization.
        if (objective == null && this.networkError > 0) { // If objective is unavailable but there is network error.
            for (int i = 0; i < output.length; i++) { // For each neuron in the output layer.
                output[i] = outputLayer.getNeurons()[i].getValue();
                // The proportion of a neuron value in the network error is the neuron error.
                neuronError = output[i] / this.networkError;
                outputLayer.getNeurons()[i].setError(neuronError);
                parentLayer = this.layers[this.layers.length - 2];
                errorDerivative = (float) (neuronError * util.reLUDerivative(output[i]));
                outputLayer.getNeurons()[i].setErrorDerivative(errorDerivative); // Set error derivative of the neuron.
                outputLayer.getNeurons()[i].setBias(outputLayer.getNeurons()[i].getBias() -
                        (errorDerivative * learningRate)); // Update neuron bias.
                layerError += neuronError; // Add neuron error to output layer error.
            }
        } else { // Compute output error and update biases of output layer.
            for (int i = 0; i < output.length; i++) { // For each neuron in the output layer.
                output[i] = outputLayer.getNeurons()[i].getValue();
                neuronError = this.util.squaredError(output[i], objective[i]);
                outputLayer.getNeurons()[i].setError(neuronError);
                parentLayer = this.layers[this.layers.length - 2];
                errorDerivative = (float) (neuronError * util.reLUDerivative(output[i]));
                outputLayer.getNeurons()[i].setErrorDerivative(errorDerivative); // Set error derivative of the neuron.
                outputLayer.getNeurons()[i].setBias(outputLayer.getNeurons()[i].getBias() -
                        (errorDerivative * learningRate)); // Update neuron bias.
                layerError += neuronError; // Add neuron error to output layer error.
            } this.networkError = layerError;
        }

        // Hidden and input layer optimization.
        for (int i = this.layers.length - 2; i >= 0; i--) { // For all hidden layers.
            Layer currentLayer = this.layers[i];
            float currentValueSum = currentLayer.totalNeuronValue();
            float parentValueSum;

            // Sum all values of neurons in parent layer
            if (i > 0) { // if iteration is not at input layer.
                parentLayer = layers[i - 1];
                parentValueSum = parentLayer.totalNeuronValue();
            } else { parentValueSum = 1.0f; } // Set to 1.0 for error derivation below.

            for (int j = 0; j < currentLayer.getNeurons().length; j++) { // For each neuron in current layer.
                // First compute error of each neuron.
                currentLayer.getNeurons()[j].setError(layerError * (currentLayer.getNeurons()[j].getValue() / currentValueSum));
                neuronError = currentLayer.getNeurons()[j].getError();
                layerError = 0.0f; // Reset layerError
                layerError += neuronError; // Update layerError with error of current layer
                errorDerivative = (float) (neuronError * util.reLUDerivative(currentLayer.getNeurons()[j].getValue())); // Derivative of neuron error.
                currentLayer.getNeurons()[j].setErrorDerivative(errorDerivative);
                // Update weights and bias of each neuron in current layer.
                float[] weights = currentLayer.getNeurons()[j].getWeights();
                for (int k = 0; k < weights.length; k++) {
                    weights[k] = weights[k] - (errorDerivative * learningRate);
                    currentLayer.getNeurons()[j].setWeights(weights);
                    currentLayer.getNeurons()[j].regularize();
                } if (i > 0) { // Do not update bias of neurons in the input layer.
                    currentLayer.getNeurons()[j].setBias(currentLayer.getNeurons()[j].getBias()
                            - (errorDerivative * learningRate));
                }
            }
        }
    }
}
