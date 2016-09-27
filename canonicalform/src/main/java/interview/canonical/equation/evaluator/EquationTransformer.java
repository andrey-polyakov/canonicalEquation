package interview.canonical.equation.evaluator;

import java.util.LinkedList;
import java.util.List;

public class EquationTransformer {

    public Equation convertToCanonicalForm(Equation input) {
        List<EquationChunk> left = new LinkedList<EquationChunk>();
        List<EquationChunk> right = new LinkedList<EquationChunk>();
        for (EquationChunk rightChunk : input.getRightPart()) {
            for (EquationChunk leftChunk : input.getLeftPart()) {

            }
        }
        return new Equation(left, right);
    }


}
