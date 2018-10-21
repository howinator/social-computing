package hw2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Result {
    public List<Tuple> choices;

    public Result() {
        this.choices = new ArrayList<>();
    }

    public void addChoice(Integer choosingPerson, Integer chosenPerson) {
        Tuple newTuple = new Tuple(choosingPerson, chosenPerson);
        this.choices.add(newTuple);
    }

    private class Tuple {
        public Integer choosingPerson;

        public Tuple(Integer choosingPerson, Integer chosenPerson) {
            this.choosingPerson = choosingPerson;
            this.chosenPerson = chosenPerson;
        }

        public Integer chosenPerson;
    }

    public void print() {
        Collections.sort(this.choices, new Comparator<Tuple>() {
            @Override
            public int compare(Tuple o1, Tuple o2) {
                return Integer.compare(o1.choosingPerson, o2.choosingPerson);
            }
        });
        for (Tuple match : this.choices) {
            System.out.println("(" + (match.choosingPerson + 1) + ", " + (match.chosenPerson + 1) + ")");
        }
    }
}
