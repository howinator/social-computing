package hw1;

import java.util.*;

public class DGS {
    public static void main(String[] args) {
        String fileName = args[0];
        ParseFile fileParser = new ParseFile();
        int[][] matrix = fileParser.parseFile(fileName);
        long startTime = System.nanoTime();
        ResultTuple result = runDGS(matrix);
        long endTime = System.nanoTime();
        DGSOutput.outputAnswer(result);
        System.out.println("\nTotal time taken for DGS is " + (endTime - startTime));
    }

    private static Integer findMatchingWeight(List<TwoTuple<Integer, Integer>> matching, int[][] matrix) {
        Integer matchingSum = 0;
        for (TwoTuple<Integer, Integer> match: matching) {
            matchingSum += matrix[match.first][match.second];
        }
        return matchingSum;
    }

    private static ResultTuple runDGS(int[][] matrix) {
        TwoTuple maxPayoffTuple;
        Integer maxGood;
        Float maxPayoff;
        int numBuyers = matrix.length;
        List<Float> price = new ArrayList<>(Collections.nCopies(numBuyers, (float) 0.0));
        List<Integer> owner = new ArrayList<>(Collections.nCopies(numBuyers, 0));
        Queue<Integer> queue = new ArrayDeque<>(numBuyers);
        int matchingSize = numBuyers;
        float delta = (float) (1.0 / (float) (matchingSize + 1));
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
                if (owner.get(maxGood) != null) queue.add(owner.get(maxGood));
                owner.set(maxGood, i);
                price.set(maxGood, price.get(maxGood) + delta);
            }

        }

        List<TwoTuple<Integer, Integer>> resultList = constructResultList(owner);
        Integer matchingWeight = findMatchingWeight(resultList, matrix);


        return new ResultTuple(resultList, matchingWeight);

    }

    private static List<TwoTuple<Integer, Integer>> constructResultList(List<Integer> ownerList) {
        List<TwoTuple<Integer, Integer>> resultList = new ArrayList<>();
        for (int good = 0; good < ownerList.size(); good++) {
            TwoTuple<Integer, Integer> tuple = new TwoTuple<>(good, ownerList.get(good));
            resultList.add(good, tuple);
        }
        return resultList;
    }

    private static TwoTuple<Integer, Float> findMaxPayoff(int[][] matrix, List<Float> prices, int numBuyers, int bidder) {
        Float maxPayoff = null;
        Integer maxGood = null;
        for (int g=0; g < numBuyers; g++) {
            if (maxPayoff == null || (matrix[g][bidder] - prices.get(g)) > maxPayoff) {
                maxPayoff = (float) matrix[g][bidder] - prices.get(g);
                maxGood = g;
            }
        }
        assert maxPayoff != null;
        return new TwoTuple<>(maxGood, maxPayoff);
//        return resultTuple;

    }


}