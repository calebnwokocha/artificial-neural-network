public class Util {
    public Util() {}

    public float tanH(float x) {
        return (float) ((Math.pow(Math.E, x) - Math.pow(Math.E, -x) /
                (Math.pow(Math.E, x) + Math.pow(Math.E, -x))));
    }

    public float tanHDerivative(float x) {
        return (float) (1 - Math.pow(x, 2));
    }

    public float sigmoid (float x) {
        return (float) (1 / (1 + Math.pow(Math.E, -x)));
    }

    public float sigmoidDerivative (float x) {
        return this.sigmoid(x) * (1 - this.sigmoid(x));
    }

    public float reLU (float x) {
        return (float) ((0.0 + x + Math.abs(0 - x)) * 0.5);
    }

    public float reLUDerivative (float x) {
        if (this.reLU(x) == 0) { return 0.0f; }
        else { return 1.0f; }
    }

    public float squaredError(float objective, float output) {
        return (float) (Math.pow((objective - output), 2) * 0.5);
    }

    public float[] softmax (float[] x) {
        float sum = 0.0f;
        float[] y = new float[x.length];
        for (float i : x) { sum += Math.pow(Math.E, i); }
        for (int i = 0; i < y.length; i++) {
            y[i] = (float) (Math.pow(Math.E, x[i]) / sum);
        } return y;
    }

    public float maximum (float[] x) {
        float max = 0.0f;
        for (float i : x) {
            max = (float) ((max + i + Math.abs(max - i)) * 0.5);
        } return max;
    }
}
