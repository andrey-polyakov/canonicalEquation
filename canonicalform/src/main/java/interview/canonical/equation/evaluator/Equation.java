package interview.canonical.equation.evaluator;

import java.util.Collections;
import java.util.List;

/**
 * Created by Андрей on 22.09.2016.
 */
public class Equation {

    private final List<EquationChunk> leftPart;
    private final List<EquationChunk> rightPart;

    public Equation(List<EquationChunk> chain, List<EquationChunk> rightPart) {
        this.leftPart = Collections.unmodifiableList(chain);
        this.rightPart = Collections.unmodifiableList(rightPart);
    }

    public List<EquationChunk> getLeftPart() {
        return leftPart;
    }

    public List<EquationChunk> getRightPart() {
        return rightPart;
    }
}
