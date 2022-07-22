/*
 * Author: Caleb Nwokocha
 * School: The University of Manitoba
 * Faculty: Faculty of Science
 */

import java.util.Arrays;
import org.apache.commons.lang.ArrayUtils;

public class Statistics extends Algebra {
    //-------------------------------------------------------------------------------------------------//
    // May not be very useful for machine learning.

    // MINIMUM
    public double minimum(double... sampleSpace) {
        Arrays.sort(sampleSpace);
        double min = sampleSpace[0];
        printLine("\nMinimum of the sample space is " + min);
        return min;
    }

    // MAXIMUM
    public double maximum(double... sampleSpace) {
        Arrays.sort(sampleSpace);
        double max = sampleSpace[sampleSpace.length - 1];
        printLine("\nMaximum of the sample space is " + max);
        return max;
    }

    // MEAN
    public double mean(double... sampleSpace) {
        double sum = sum(sampleSpace);
        double mean = 0;

        if (sampleSpace.length > 0) {
            mean = sum / sampleSpace.length;
        }

        printLine("\nMean of the sample space is " + mean);

        return mean;
    }

    // MEDIAN
    public double median(double... sampleSpace) {
        double median = 0.0;
        double pair1;
        double pair2;
        double[] centerPair;

        Arrays.sort(sampleSpace);

        if (sampleSpace.length % 2 == 0) {
            pair1 = sampleSpace[(sampleSpace.length + 1) / 2];
            pair2 = sampleSpace[(sampleSpace.length + 2) / 2];
            centerPair = new double[]{pair1, pair1};
            median = this.mean(centerPair);
        } else {
            median = sampleSpace[(sampleSpace.length + 1) / 2];
        }

        printLine("\nMedian of the sample space is " + median);

        return median;
    }

    // MODE
    public double mode (double... sampleSpace) {
        double mode = 0.0;
        int maxCount = 0;
        int count;

        for (double sample1 : sampleSpace) {
            count = 0;

            for (double sample2 : sampleSpace) {
                if (sample2 == sample1)
                    count += 1;
            }

            if (count > maxCount) {
                maxCount = count;
                mode = sample1;
            }
        }

        printLine("\nMode of the sample space is " + mode);

        return mode;
    }

    // LOWER-QUARTILE MEDIAN
    public double Q1 (double... sampleSpace) {
        double median = this.median(sampleSpace);
        double[] event = new double[sampleSpace.length];
        double Q1;
        int j = 0;

        for (double sample : sampleSpace) {
            if (sample < median) {
                event[j] = sample;
                j += 1;
            }
        }

        Arrays.sort(event);
        Q1 = this.median(event);

        printLine("\nLower quartile median of the sample space is " + Q1);

        return Q1;
    }

    // UPPER-QUARTILE MEDIAN
    public double Q3 (double... sampleSpace) {
        double median = this.median(sampleSpace);
        double[] event = new double[sampleSpace.length];
        double Q3;
        int j = 0;

        for (double sample : sampleSpace) {
            if (sample > median) {
                event[j] = sample;
                j += 1;
            }
        }

        Arrays.sort(event);
        Q3 = this.median(event);

        printLine("\nUpper quartile median of the sample space is " + Q3);

        return Q3;
    }

    // OUTLIER
    public boolean isOutlier (double[] sampleSpace, double outCome) {
        double interQuartileRange = this.Q3(sampleSpace) -
                this.Q1(sampleSpace);

        boolean isOutlier = outCome < this.Q1(sampleSpace) - (1.5 * interQuartileRange) ||
                outCome > this.Q3(sampleSpace) + (1.5 * interQuartileRange);

        if (isDebugging()) {
            if (isOutlier) {
                printLine("\n" + outCome + " is an outlier in the sample space");
            } else {
                printLine("\n" + outCome + " is not an outlier in the sample space");
            }
        }

        return isOutlier;
    }

    //-------------------------------------------------------------------------------------------------//

    // EVENT PROBABILITY
    public double probability (double[] sampleSpace, double... event) {
        double frequency = 0.0;
        double probability = 0.0;

        if (sampleSpace.length > 0) {
            Arrays.sort(event);

            for (int i = 0; i < event.length; i++) {
                try {
                    if (event[i] == event[i + 1]) {
                        event = ArrayUtils.remove(event, i + 1);
                        i -= 1;
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }

            for (double outcome : event) {
                for (double sample : sampleSpace){
                    if (sample == outcome) {
                        frequency = sum(frequency, 1.0);
                    }
                }

                probability = frequency / sampleSpace.length;
            }
        }

        printLine("\nProbability of the event in the sample space is " + probability);

        return probability;
    }

    // EVENT CONDITIONAL PROBABILITY
    public double probability (double[] sampleSpace, double[] event, double[] givenEvent) {
        double givenEventProbability = this.probability(sampleSpace, givenEvent);
        double conditionalProbability = 0.0;

        if (givenEventProbability > 0.0) {
            conditionalProbability = Math.abs((sum(givenEventProbability,
                    this.probability(sampleSpace, event)) - this.probability(sampleSpace,
                    new double[][]{givenEvent, event}, "or"))
                    / givenEventProbability);
        } else {
            conditionalProbability = this.probability(sampleSpace, event);
        }

        printLine("\nConditional probability of the event in the sample space is "
                + conditionalProbability);

        return conditionalProbability;
    }

    // EVENTS PROBABILITY
    public double probability (double[] sampleSpace, double[][] events, String operator) {
        double[] allEvents = new double[0];
        double probability = 0.0;

        if (sampleSpace.length > 0) {
            if ("and".equals(operator)) {
                probability = this.probability(sampleSpace, events[0]);

                for (int i = 1; i < events.length; i++) {
                    probability = multiply(probability,
                            this.probability(sampleSpace, events[i], events[i - 1]));
                }
                printLine("\nProbability of the events in the sample space is " + probability);
            } else if ("or".equals(operator)) {
                for (double[] event : events) {
                    allEvents = ArrayUtils.addAll(allEvents, event);
                }

                probability = this.probability(sampleSpace, allEvents);
                printLine("\nProbability of any events in the sample space is " + probability);
            } else if ("not".equals(operator)) {
                for (double[] event : events) {
                    allEvents = ArrayUtils.addAll(allEvents, event);
                }

                probability = 1 - this.probability(sampleSpace, allEvents);
                printLine("\nProbability of neither events in the sample space is " + probability);
            }
        }

        return probability;
    }

    // EVENT OUTCOMES PROBABILITIES
    public double[] probabilities (double[] sampleSpace) {
        double[] probabilities = new double[sampleSpace.length];

        Arrays.fill(probabilities, (double) 1 / probabilities.length);

        if (isDebugging()) {
            printLine("\nProbabilities of outcomes in the event are");
            for (double probablity : probabilities) {
                printText(probablity + " ");
            }
            printLine("");
        }

        return probabilities;
    }

    // EVENTS OUTCOMES PROBABILITIES
    public double[][] probabilities (double[][] sampleSpaces) {
        double[][] probabilities;
        int highestSampleSize = 0;

        // Find the highest sample size in sampleSpaces.
        for (double[] sampleSpace : sampleSpaces) {
            highestSampleSize = (int) multiply(sum(sum(highestSampleSize,
                    sampleSpace.length), Math.abs(sum(highestSampleSize,
                    -sampleSpace.length))), 0.5);
        }

        probabilities = new double[sampleSpaces.length][highestSampleSize];

        for (int i = 0; i < probabilities.length; i++) {
            probabilities[i] = probabilities(sampleSpaces[i]);
        }

        if (isDebugging()) {
            printLine("\nProbabilities of outcomes in the events are");
            for (double[] row : probabilities) {
                for (double probability : row) {
                    printText(probability + " ");
                }
                printLine("");
            }
            printLine("");
        }

        return probabilities;
    }

    // EVENT EXPECTATION
    public double expectedValue (double[] sampleSpace, double[] probabilities) {
        double expectedValue = (Double) multiply(sampleSpace, probabilities);
        printLine("\nExpected value of the sample space is " + expectedValue);
        return expectedValue;
    }

    // EVENTS EXPECTATIONS
    // The expectation of a matrix (which rows are sample spaces) is the
    // weighted average of its column elements.
    public double[] expectedValues (double[][] sampleSpaces, double[][] probabilities) {
        double[][] transposedSampleSpaces = transpose(sampleSpaces);
        double[][] transposedProbabilities = probabilities(transpose(probabilities));
        double[] expectedValues = new double[transposedSampleSpaces.length];

        for (int i = 0; i < expectedValues.length; i++) {
            expectedValues[i] = this.expectedValue(transposedSampleSpaces[i],
                    transposedProbabilities[i]);
        }

        if (isDebugging()) {
            printLine("\nExpected values of the sample spaces are");
            for (double expectedValue : expectedValues) {
                printText(expectedValue + " ");
            }
            printLine("");
        }

        return expectedValues;
    }

    public double[][] expectedValues (double[][][] sampleSpaces, double[][][] probabilities) {
        double[][] events = new double[sampleSpaces.length][];
        double[][] eventsProbabilities = new double[probabilities.length][];
        double[][] expectedValues;
        int j = 0;
        int highestSampleSpaceMatrixRow = 0;

        // Find the highest sample space matrix row in sampleSpaces.
        for (double[][] sampleSpaceMatix : sampleSpaces) {
            highestSampleSpaceMatrixRow = (int) multiply(sum(sum(highestSampleSpaceMatrixRow,
                    sampleSpaceMatix.length) + Math.abs(sum(highestSampleSpaceMatrixRow,
                    -sampleSpaceMatix.length))), 0.5);
        }

        expectedValues = new double[highestSampleSpaceMatrixRow][];

        for (int k = 0; k < highestSampleSpaceMatrixRow; k++) {
            for (int i = 0; i < sampleSpaces.length; i++) {
                events[i] = sampleSpaces[i][j];
                try {
                    eventsProbabilities[i] = probabilities[i][j];
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }

            expectedValues[k] = expectedValues(events, eventsProbabilities);

            j += 1;
        }

        if (isDebugging()) {
            printLine("\nExpected values of the observation are");
            for (int i = 0; i < expectedValues.length; i++) {
                for (int k = 0; k < expectedValues[i].length; k++) {
                    printText(expectedValues[i][k] + " ");
                }
                printLine("");
            }
            printLine("");
        }

        return expectedValues;
    }

    // EVENT VARIANCE
    public double variance (double[] sampleSpace, double expectedValue) {
        double squaredSum = 0.0;
        double variance = 0.0;

        if (sampleSpace.length > 1) {
            for (double sample : sampleSpace) {
                squaredSum += Math.pow(sample - expectedValue, 2);
            }

            variance = squaredSum / (sampleSpace.length - 1);
        }

        printLine("\nVariance of the sample space is " + variance);

        return variance;
    }

    // EVENTS VARIANCES
    public double[] variances (double[][] sampleSpaces, double[] expectedValues) {
        double[] variances = new double[sampleSpaces.length];

        for (int i = 0; i < variances.length; i++) {
            variances[i] = this.variance(sampleSpaces[i], expectedValues[i]);
        }

        if (isDebugging()) {
            printLine("\nVariances of the sample spaces are");
            for (double variance : variances) {
                printText(variance + "");
            }
            printLine("");
        }

        return variances;
    }

    // EVENT STANDARD DEVIATION
    public double standardDeviation (double[] sampleSpace, double expectedValue) {
        double standardDeviation = Math.sqrt(this.variance(sampleSpace, expectedValue));
        printLine("\nStandard deviation of the sample space is " + standardDeviation);
        return standardDeviation;
    }

    // EVENTS STANDARD DEVIATIONS
    public double[] standardDeviations (double[][] sampleSpaces, double[] expectedValues) {
        double[] standardDeviations = new double[sampleSpaces.length];

        for (int i = 0; i < standardDeviations.length; i++) {
            standardDeviations[i] = this.standardDeviation(sampleSpaces[i], expectedValues[i]);
        }

        if (isDebugging()) {
            printLine("\nStandard deviations of the sample spaces are");
            for (double standardDeviation : standardDeviations) {
                printText(standardDeviation + "");
            }
            printLine("");
        }

        return standardDeviations;
    }


    // OUTCOME Z-SCORE
    public double zScore (double[] sampleSpace, double[] probabilities, double outCome) {
        double expectedValue = this.expectedValue(sampleSpace, probabilities);
        double standardDeviation = this.standardDeviation(sampleSpace, expectedValue);
        double zScore = 0.0;

        if (standardDeviation > 0.0) {
            zScore = (outCome - expectedValue) / standardDeviation;
        }

        printLine("\nZ-Score of " + outCome + " in the sample space is " + zScore);

        return zScore;
    }

    // EVENT OUTCOMES Z-SCORES
    public double[] zScores (double[] sampleSpaces, double[] probabilities, double[] event) {
        double[] zScores = new double[sampleSpaces.length];
        double expectedValue = this.expectedValue(sampleSpaces, probabilities);
        double standardDeviation = this.standardDeviation(sampleSpaces, expectedValue);

        if (standardDeviation > 0.0) {
            for (int i = 0; i < sampleSpaces.length; i++) {
                zScores[i] = (event[i] - expectedValue) / standardDeviation;
            }
        }

        if (isDebugging()) {
            printLine("\nZ-Scores of outcomes in the sample space are");
            for (double zScore : zScores) {
                printText(zScore + " ");
            }
            printLine("");
        }

        return zScores;
    }

    // EVENTS OUTCOMES Z-SCORES
    public double[][] zScores (double[][] sampleSpaces, double[][] probabilities, double[][] events) {
        double[][] zScores;
        int highestSampleSize = 0;

        // Find the highest sample size in sampleSpaces.
        for (double[] sampleSpace : sampleSpaces) {
            highestSampleSize = (int) multiply(sum(sum(highestSampleSize,
                    sampleSpace.length) + Math.abs(sum(highestSampleSize, sampleSpace.length))),
                    0.5);
        }

        zScores = new double[sampleSpaces.length][highestSampleSize];

        for (int i = 0; i < zScores.length; i++) {
            zScores[i] = this.zScores(sampleSpaces[i], probabilities[i], events[i]);
        }

        if (isDebugging()) {
            printLine("\nZ-Scores of outcomes in the sample space are");
            for (double[] row : zScores) {
                for (double zScore : row) {
                    printText(zScore + " ");
                }
                printLine("");
            }
            printLine("");
        }

        return zScores;
    }

    // EVENTS CORRELATION
    public double correlation (double[][] sampleSpaces, double[][] probabilities) {
        double[][] zScores = zScores(sampleSpaces, probabilities, sampleSpaces);
        double product = (Double) multiply(zScores);
        double correlation = product / (zScores[0].length - 1);
        printLine("\nCorrelation of the sample spaces is " + correlation);
        return correlation;
    }

    // ENTROPY
    public double entropy (double... sampleSpace) {
        double[] probabilities = this.probabilities(sampleSpace);
        return 0.0;
    }
}

