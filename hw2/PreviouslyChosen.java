package hw2;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PreviouslyChosen {
    private Map<Integer, Set<Integer>> previouslyChosenMap;

    PreviouslyChosen(int numPairs) {
        this.previouslyChosenMap = new HashMap<>();
        for (int i=0; i < numPairs; i++) {
            this.previouslyChosenMap.put(i, new HashSet<>());
        }
    }

    public void addChosen(Integer choosing, Integer chosen) {
        this.previouslyChosenMap.get(choosing).add(chosen);
    }

    public Set<Integer> getPreviouslyChosen(Integer choosing) {
        return this.previouslyChosenMap.get(choosing);
    }

    public boolean chosenIsEmpty(Integer choosing) {
        return this.previouslyChosenMap.get(choosing).isEmpty();
    }
}
