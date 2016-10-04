package interview.canonical.equation.evaluator;

/**
 * Condition under which given equation is valid.
 */
public class Assumption {

    public final String condition;

    public Assumption(String condition) {
        this.condition = condition;
    }

    @Override
    public String toString() {
        return condition;
    }
}
