package interview.canonical.equation.evaluator;

import java.util.LinkedList;
import java.util.List;

public class EquationConverter {

    public Equation convertToCanonicalForm(Equation input) {
        List<EquationPart> left = new LinkedList<EquationPart>();
        List<EquationPart> right = new LinkedList<EquationPart>();
        for (EquationPart rightChunk : input.getRightPart()) {
            for (EquationPart leftChunk : input.getLeftPart()) {
                if (leftChunk.isCompatibleWith(rightChunk)) {
                    left.add(rightChunk.negateAndMergeWith(leftChunk));
                }
            }
        }
        for (EquationPart equationPart : right) {
            left.add(equationPart.negate());
        }
        return new Equation(left, right);
    }

}
