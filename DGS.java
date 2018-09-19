import java.util.*;

public class DGS {
    public static void main(String[] args) {
        String fileName = args[0];
        ParseFile fileParser = new ParseFile();
        int[][] matrix = fileParser.parseFile(fileName);
    }

    class TwoTuple<T, U> {
        public final T first;
        public final U second;
        TwoTuple(T first, U second) {
            this.first = first;
            this.second = second;
        }
        public Integer firstToInt() {
            return (Integer) this.first;
        }
        public Float firstToFloat() {
            return (Float) this.first;
        }
        public Integer secondToInt() {
            return (Integer) this.second;
        }
        public Float secondToFloat() {
            return (Float) this.second;
        }
    }

    class ResultTuple {
        public final Float maxWeight;
        public final List<TwoTuple<Integer, Integer>> pairings;
        public ResultTuple(List<TwoTuple<Integer, Integer>> pairings, Float maxWeight) {
            this.pairings = pairings;
            this.maxWeight = maxWeight;
        }
    }

    public ResultTuple runDGS(int[][] matrix) {
        TwoTuple maxPayoffTuple;
        Integer maxGood;
        Float maxPayoff;
        int numBuyers = matrix.length;
        List<Float> price = new ArrayList<>(numBuyers);
        List<Integer> owner = new ArrayList<>(numBuyers);
        Queue<Integer> queue = new ArrayDeque<>(numBuyers);
        int matchingSize = numBuyers;
        float delta = 1 / (matchingSize + 1);
        for (int i = 0; i < numBuyers; i++) {
            price.set(i, (float) 0.0);
            owner.set(i, null);
            queue.add(i);
        }

        while (!queue.isEmpty()) {
            int i = queue.remove();
            maxPayoffTuple = findMaxPayoff(matrix, price, numBuyers, i);
            maxGood = maxPayoffTuple.firstToInt();
            maxPayoff = maxPayoffTuple.secondToFloat();
            if (maxPayoff >= 0) {
                queue.add(owner.get(maxGood));
                owner.set(maxGood, i);
                price.set(maxGood, price.get(maxGood) + delta);
            }

        }

        List<TwoTuple<Integer, Integer>> resultList = constructResultList(owner);
        Float maxWeight = Collections.max(price);


        return new ResultTuple(resultList, maxWeight);

    }

    private List<TwoTuple<Integer, Integer>> constructResultList(List<Integer> ownerList) {
        List<TwoTuple<Integer, Integer>> resultList = new ArrayList<>();
        for (int good = 0; good < ownerList.size(); good++) {
            TwoTuple<Integer, Integer> tuple = new TwoTuple<>(good, ownerList.get(good));
            resultList.set(good, tuple);
        }
        return resultList;
    }

    private TwoTuple findMaxPayoff(int[][] matrix, List<Float> prices, int numBuyers, int bidder) {
        Float maxPayoff = null;
        Integer maxGood = null;
        for (int g=0; g < numBuyers; g++) {
            if (maxPayoff == null || (matrix[g][bidder] - prices.get(g)) > maxPayoff) {
                maxPayoff = (float) matrix[g][bidder] - prices.get(g);
                maxGood = g;
            }
        }
        assert maxPayoff != null;
        TwoTuple<Integer, Float> resultTuple = new TwoTuple<>(maxGood, maxPayoff);
        return resultTuple;

    }


}