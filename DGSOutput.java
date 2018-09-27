public class DGSOutput {
    public static void outputAnswer(ResultTuple result) {
        System.out.println(result.matchingWeight);
        for (TwoTuple<Integer, Integer> match : result.pairings) {
            System.out.println("(" + (match.first + 1) + "," + (match.second  + 1) + ")");
        }
    }
}
