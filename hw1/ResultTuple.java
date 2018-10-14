package hw1;
import java.util.List;

class ResultTuple {
    public final Integer matchingWeight;
    public final List<TwoTuple<Integer, Integer>> pairings;
    public ResultTuple(List<TwoTuple<Integer, Integer>> pairings, Integer matchingWeight) {
        this.pairings = pairings;
        this.matchingWeight = matchingWeight;
    }
}
