/*
 * AUTHOR: CALEB PRINCEWILL NWOKOCHA
 * SCHOOL: THE UNIVERSITY OF MANITOBA
 * DEPARTMENT: COMPUTER SCIENCE
 */

public class CFunction {
    private final int parameterCount;
    private double value;

    public CFunction(String cFunctionName, double... parameters) {
        // TODO: For a neuron, comprehensive functions should be selected by name or by number of parameters.
        this.parameterCount = parameters.length;
        switch (parameters.length) {
            case 1 -> this.cubicVolume(parameters[0]);
            case 2 -> this.force(parameters[0], parameters[2]);
        }
    }

    public int getNumOfParameter() { return parameterCount; }

    public double getValue() { return value; }

    private void cubicVolume (double s) { this.value = Math.pow(s, 3); }

    private void force (double m, double a) { this.value = m * a; }

}
