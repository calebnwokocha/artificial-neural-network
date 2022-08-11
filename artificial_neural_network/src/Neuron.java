import java.util.stream.Stream;

public class Neuron {
    private float[] weights;
    private float bias;
    private float value;
    private float error;
    private float errorDerivative;

    // Neuron constructor
    public Neuron (int numOfWeights) {
        this.weights = new float[numOfWeights];
        for (int i = 0; i < this.weights.length; i++) {
            this.weights[i] = (float) Math.random();
        } this.bias = (float) Math.random();
        this.regularize();
    }

    public float[] getWeights() { return weights; }

    public void setWeights(float[] weights) { this.weights = weights; }

    public float getBias() { return bias; }

    public void setBias(float bias) { this.bias = bias; }

    public float getValue() { return value; }

    public void setValue(float value) { this.value = value; }

    public float getError() { return error; }

    public void setError(float error) { this.error = error; }

    public float getErrorDerivative() { return errorDerivative; }

    public void setErrorDerivative(float errorDerivative) { this.errorDerivative = errorDerivative; }

    public void regularize () {
        Util util = new Util();
        float max = util.maximum(this.weights);
        for (int i = 0; i < weights.length; i++) {
            weights[i] = weights[i] - max;
        }
    }
}
