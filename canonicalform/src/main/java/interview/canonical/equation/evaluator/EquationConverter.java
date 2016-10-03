package interview.canonical.equation.evaluator;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class EquationConverter {

    public static Equation convertToCanonicalForm(Equation input) {
        List<EquationPart> left = new LinkedList<>(input.getLeftPart());
        List<EquationPart> right = new LinkedList<>(input.getRightPart());
        Iterator<EquationPart> literator, riterator = right.iterator();
        while (riterator.hasNext()) {
            EquationPart rightChunk = riterator.next();
            boolean matched = false;
            literator = left.iterator();
            while (literator.hasNext()) {
                EquationPart leftChunk = literator.next();
                if (leftChunk.isCompatibleWith(rightChunk)) {
                    matched = true;
                    EquationPart product = rightChunk.negateAndMergeWith(leftChunk);
                    literator.remove();
                    riterator.remove();
                    if (product != null) {
                        left.add(product);
                    }
                    break;
                }
            }
            if (!matched && rightChunk.getConstant().isNotZero()) {
                riterator.remove();
                left.add(rightChunk.negate());
            }
        }
        for (EquationPart equationPart : right) {
            if (equationPart.getConstant().isNotZero()) {
                right.remove(right);
                left.add(equationPart.negate());
            }
        }
        if (right.isEmpty()) {
            right.add(new EquationPart(0.0));
        }
        if (left.isEmpty()) {
            left.add(new EquationPart(0.0));
        }
        return new Equation(left, right);
    }

}
