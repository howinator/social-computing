package hw2;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class SMP {

    public static void main(String[] args) {
        String filename = args[0];
        String genderInput = args[1];

        GenderOptimality optimizeForGender = null;

        switch (genderInput) {
            case "m":
                optimizeForGender = GenderOptimality.MAN;
                break;
            case "w":
                optimizeForGender = GenderOptimality.WOMAN;
                break;
        }

        if (optimizeForGender == null) {
            throw new IllegalArgumentException("Optimize for gender must be 'm' or 'w'");
        }

        PreferencesLists prefLists = new ParseFile().parseFile(filename);
    }

    enum GenderOptimality {
        MAN, WOMAN
    }

    private List<Integer> runSMP(PreferencesLists prefLists, GenderOptimality genderOptimality) {
        int[][] pickList;
        Stack<Integer> freeList = new Stack<>();
        List <Integer> result = null;
        int chosen;
        switch (genderOptimality) {
            case MAN:
                pickList = prefLists.menPreferenceList;
                break;
            case WOMAN:
                pickList = prefLists.womenPreferenceList;
                break;
        }
        int numPairs = prefLists.menPreferenceList.length;

        for (int i = 0; i < numPairs; i++) {
            freeList.push(i);
        }
        while (!freeList.empty()) {
           chosen = freeList.pop();
        }
        return result;
    }

}
