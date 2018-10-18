package hw2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class ParseFile {

    PreferencesLists parseFile(String filename) {
        File file = new File(filename);
        BufferedReader reader = null;
        PreferencesLists prefLists = new PreferencesLists(null, null);
        try {
            reader = new BufferedReader(new FileReader(file));
            String text;
            String firstLine = reader.readLine();
            int numberOfPairs = Integer.parseInt(firstLine);
            int[][] parseResult = new int[numberOfPairs * 2][numberOfPairs];
            int currentRowNumber = 0;
            while ((text = reader.readLine()) != null) {
                parseResult[currentRowNumber] = parseLine(text, numberOfPairs);
                currentRowNumber++;
            }

            prefLists.menPreferenceList = Arrays.copyOfRange(parseResult, 0, numberOfPairs);
            prefLists.womenPreferenceList = Arrays.copyOfRange(parseResult, numberOfPairs, (numberOfPairs * 2));

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
        return prefLists;
    }

    private int[] parseLine(String line, int numColumns) {
        int[] rowResult = new int[numColumns];
        String[] splitString = line.split("\\s+");
        int index = 0;
        for (String ele : splitString) {
            rowResult[index] = Integer.parseInt(ele) - 1;
            index++;
        }
        return rowResult;
    }
}
