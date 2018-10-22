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
                                                  PreviouslyChosen previouslyChosen,
                                                  int[] pickList) {
        if (!previouslyChosen.chosenIsEmpty(choosingPerson)) {

            Set<Integer> alreadyChosenSet = previouslyChosen.getPreviouslyChosen(choosingPerson);

            for (int i : pickList) {
                if (!alreadyChosenSet.contains(i)) {
                    return i;
                }
            }
            return null;

        }
        return pickList[0];
    }

    private static Result runSMP(PreferencesLists prefLists, GenderOptimality genderOptimality) {
        int[][] pickList;
        int[][] inversePickList;
        Stack<Integer> freeList = new Stack<>();
        int choosingPerson;

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

            int topPreferencePick = SMP.getFirstUnchosenChoice(choosingPerson, previoslyChosen, pickList[choosingPerson]);
            previoslyChosen.addChosen(choosingPerson, topPreferencePick);
            if (!matchMap.containsKey(topPreferencePick)) {
                matchMap.put(topPreferencePick, choosingPerson);
            } else {
                int previouslyAssignedPerson = matchMap.get(topPreferencePick);
                if (getRankOfMatch(inversePickList[topPreferencePick], choosingPerson) > getRankOfMatch(inversePickList[topPreferencePick], previouslyAssignedPerson)) {
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

    private static Integer getRankOfMatch(int[] rankList, int matchingPerson) {
        for (int i = 0; i < rankList.length; i++) {
            if (matchingPerson == rankList[i]) {
                return i;
            }
        }
        return -1; // something went horribly wrong
    }

}
