public class Layer {
    private Neuron[] neurons;

    // Layer constructor
    public Layer (int numOfNeurons, int nextLayerSize) {
        this.neurons = new Neuron[numOfNeurons];
        // For all neurons in layer.
        for (int i = 0; i < this.neurons.length; i++) {
            // Number of weights for each neuron equal to
            // size of the next layer.
            this.neurons[i] = new Neuron(nextLayerSize);
        }
    }

    public Neuron[] getNeurons() { return neurons; }

    public void setNeurons(Neuron[] neurons) { this.neurons = neurons; }

    public float totalNeuronValue () {
        float sum = 0.0f;
        for (Neuron neuron : this.neurons) { sum += neuron.getValue(); }
        return sum;
    }

/*
    public float totalN
*/

    public void normalize () {
        Util util = new Util();
        float[] neuronValues = new float[neurons.length];
        for (int i = 0; i < neuronValues.length; i++) {
            neuronValues[i] = neurons[i].getValue();
        }
        neuronValues = util.softmax(neuronValues);
        for (int i = 0; i < neurons.length; i++) {
            neurons[i].setValue(neuronValues[i]);
        }
    }
}
