package interview.canonical.equation.evaluator;

/**
 * Created by Андрей on 22.09.2016.
 */
public class Variable implements Comparable<Variable> {
    private String letter;

    public Variable(String letter) {
        this.letter = letter;
    }

    public String getLetter() {
        return letter;
    }

    public int compareTo(Variable o) {
        return letter.compareTo(o.letter);
    }

    @Override
    public String toString() {
        return letter;
    }
}
