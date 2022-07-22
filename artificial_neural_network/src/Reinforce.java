public class Reinforce extends Statistics {

    // Action Space is a range of activities of parts of
    // an agent. The rows represents each part, while the
    // column represents the range of activities for each
    // part.
    private double[][] actionSpace = new double[4][7];

    // The action array store activities of each part of
    // of an agent.
    private double[] action = new double[actionSpace.length];

    public double reward (double[] action, double[][][] state) {
        return 0.0;
    }

    public double[][][] state (double[] action) {
      return new double[0][][];
    }

    public void train (int searchSize, double discountFactor) {
        double[][] Q = new double[searchSize][searchSize];
        int actionSelector;
        double[][][] state;
        double reward;

        while (true) { // TODO: Change this to while Q is not full.
            // Get action.
            for (int i = 0; i < this.action.length; i++) {
                actionSelector = (int) Math.floor(sum(multiply(
                        Math.random(), sum(this.actionSpace[0].length,
                                -0, 1)), 0));
                this.action[i] = this.actionSpace[i][actionSelector];
            }

            state = state(this.action);
            reward = reward(this.action, state);

        }

        //printLine("Random number is " + actionSelector);
    }
}
