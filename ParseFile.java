import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

class ParseFile {

    int[][] parseFile(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        int[][] matrix = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String text;
            String firstLine = reader.readLine();
            int numberRowsAndColumns = Integer.parseInt(firstLine);
            matrix = new int[numberRowsAndColumns][numberRowsAndColumns];
            int currentRowNumber = 0;
            while ((text = reader.readLine()) != null) {
                matrix[currentRowNumber] = parseLine(text, numberRowsAndColumns);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return matrix;

    }

    private int[] parseLine(String line, int numberColumns) {
        int stringLength = line.length();
        String currentNumber = "";
        int[] rowResult = new int[numberColumns];
        int rowPosition = 0;
        for (int charNumber = 0; charNumber < stringLength; charNumber++) {
            char currentChar = line.charAt(charNumber);
            if (currentChar == ' ') {  // TODO should be any whitespace char?
                rowResult[rowPosition] = Integer.parseInt(currentNumber);
                rowPosition++;
            } else {
                currentNumber += currentChar;
            }
        }
        return rowResult;
    }

}