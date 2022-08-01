import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Arrays;

public class FileHandler extends Debugger {

    public double[][] read (String fileName) {
        String[][] datasetString = new String[303][14];
        double[][] datasetDouble = new double[303][14];
        int count = 0;

        try {
            File file = new File(fileName);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String data = scanner.nextLine();

                // Copy data to datasetString.
                datasetString[count] = data.split(",");
                count++;
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            printLine("\nFile not found.");
            e.printStackTrace();
        }

        // Convert datasetString to double array.
        for (int i = 0; i < datasetString.length; i++) {
            for (int j = 0; j < datasetString[i].length; j++) {
                if (datasetString[i][j].equals("?")) {
                    datasetDouble[i][j] = 0.0;
                } else {
                    datasetDouble[i][j]= Double.parseDouble(datasetString[i][j]);
                }
            }
        }

        printLine("Below is the dataset.");
        for (double[] row : datasetDouble) {
            for (double data : row) {
                printText(data + " ");
            }
            printLine("");
        }

        return datasetDouble;
    }
}
