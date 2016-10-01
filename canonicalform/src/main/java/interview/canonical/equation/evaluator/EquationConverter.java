package interview.canonical.equation.evaluator;

import java.util.LinkedList;
import java.util.List;

public class EquationConverter {

    public Equation convertToCanonicalForm(Equation input) {
        List<EquationPart> left = new LinkedList<>(input.getLeftPart());
        List<EquationPart> right = new LinkedList<>(input.getRightPart());
        for (EquationPart rightChunk : new LinkedList<>(right)) {
            boolean matched = false;
            for (EquationPart leftChunk : new LinkedList<>(left)) {
                if (leftChunk.isCompatibleWith(rightChunk)) {
                    matched = true;
                    EquationPart product = rightChunk.negateAndMergeWith(leftChunk);
                    left.remove(leftChunk);
                    right.remove(rightChunk);
                    if (product != null) {
                        left.add(product);
                    }
                    break;
                }
            }
            if (!matched) {
                right.remove(rightChunk);
                left.add(rightChunk.negate());
            }
        }
        for (EquationPart equationPart : right) {
            right.remove(right);
            left.add(equationPart.negate());
        }
        right.add(new EquationPart(0.0));
        return new Equation(left, right);
    }

}
