package interview.canonical.equation.evaluator;

/**
 * Condition under which given equation is valid.
 */
public class Assumption implements Comparable<Assumption> {

    public final String condition;

    public Assumption(String condition) {
        this.condition = condition;
    }

    @Override
    public String toString() {
        return condition;
    }

    @Override
    public int compareTo(Assumption o) {
        return condition.compareTo(o.condition);
    }
}
