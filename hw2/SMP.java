package hw2;

import java.util.*;

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
        Result result = SMP.runSMP(prefLists, optimizeForGender);
        assert result != null;
        result.print();
    }

    enum GenderOptimality {
        MAN, WOMAN
    }

    private static Integer getFirstUnchosenChoice(Integer choosingPerson,
                                           Integer numPairs,
                                           PreviouslyChosen previouslyChosen,
                                           int[] pickList) {
//        Set<Integer> previouslyChosenList = previouslyChosen.;
        if (!previouslyChosen.chosenIsEmpty(choosingPerson)) {
            Set<Integer> allChoices = new HashSet<>();
            for (int i=0; i < numPairs; i++) {
                allChoices.add(i);
            }

            Set<Integer> alreadyChosenSet = previouslyChosen.getPreviouslyChosen(choosingPerson);

            allChoices.removeAll(alreadyChosenSet);
            Set<Integer> possibleChoices = allChoices;

            if (possibleChoices.isEmpty()) {
                return null;
            }

            for (int i : pickList) {
                if (possibleChoices.contains(i)) {
                    return i;
                }
            }
        }
        return pickList[0];
    }

    private static Result runSMP(PreferencesLists prefLists, GenderOptimality genderOptimality) {
        int[][] pickList;
        int[][] inversePickList;
        Stack<Integer> freeList = new Stack<>();
        int choosingPerson;

//        Map<Integer, List<Integer>> previoslyChosenMap = new HashMap<>();
        // maps chosenPerson to choosingPerson {chosenPerson: choosingPersion}
        Map<Integer, Integer> matchMap = new HashMap<>();
        Result result = new Result();
        switch (genderOptimality) {
            case MAN:
                pickList = prefLists.menPreferenceList;
                inversePickList = prefLists.womenPreferenceList;
                break;
            case WOMAN:
                pickList = prefLists.womenPreferenceList;
                inversePickList = prefLists.menPreferenceList;
                break;
            default:
                return null;
        }
        int numPairs = prefLists.menPreferenceList.length;
        PreviouslyChosen previoslyChosen = new PreviouslyChosen(numPairs);

        for (int i = 0; i < numPairs; i++) {
            freeList.push(i);
        }
        while (!freeList.empty()) {
            choosingPerson = freeList.pop();

            int topPreferencePick = SMP.getFirstUnchosenChoice(choosingPerson, numPairs, previoslyChosen, pickList[choosingPerson]);
            if (!matchMap.containsKey(topPreferencePick)) {
                matchMap.put(topPreferencePick, choosingPerson);
                previoslyChosen.addChosen(choosingPerson, topPreferencePick);
            } else {
                int previouslyAssignedPerson = matchMap.get(topPreferencePick);
                if (inversePickList[topPreferencePick][choosingPerson] > inversePickList[topPreferencePick][previouslyAssignedPerson]) {
                    freeList.push(choosingPerson);
                } else {
                    freeList.push(previouslyAssignedPerson);
                    matchMap.put(topPreferencePick, choosingPerson);
                }
            }
        }

        for (Integer chosenPerson : matchMap.keySet()) {
           result.addChoice(matchMap.get(chosenPerson), chosenPerson);
        }
        return result;
    }

}
