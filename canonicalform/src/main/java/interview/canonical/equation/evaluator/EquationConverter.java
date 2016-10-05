package interview.canonical.equation.evaluator;

import java.util.*;

public class EquationConverter {

    public static Equation convertToCanonicalForm(Equation input) {
        List<EquationPart> left = new LinkedList<>(input.getLeftPart());
        List<EquationPart> right = new LinkedList<>(input.getRightPart());
        Iterator<EquationPart> literator, riterator = right.iterator();
        Set<Assumption> conditions = new TreeSet<>();
        while (riterator.hasNext()) {
            EquationPart rightChunk = riterator.next();
            boolean matched = false;
            literator = left.iterator();
            startOver:
            while (literator.hasNext()) {
                EquationPart leftChunk = literator.next();
                boolean foundZeroPower = false;
                for (Element v : leftChunk.getVariables()) {
                    if (v.getPower() <= 0) {
                        conditions.add(new Assumption(v.getLetter() + "!=0"));
                        if (v.getPower() == 0) {
                            left.remove(leftChunk);
                            Set<Element> variables = new HashSet<>(leftChunk.getVariables());
                            variables.remove(v);
                            left.add(new EquationPart(leftChunk.getConstant(), variables));
                        }
                    }
                }
                if (foundZeroPower) {
                    break startOver;
                }
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
        LinkedList<EquationPart> leftCopy = new LinkedList<>(left);
        while (true) {
            startOver:
            for (EquationPart leftChunk : leftCopy) {
                for (Element v : leftChunk.getVariables()) {
                    if (v.getPower() <= 0) {
                        conditions.add(new Assumption(v.getLetter() + "!=0"));
                        if (v.getPower() == 0) {
                            left.remove(leftChunk);
                            Set<Element> variables = new HashSet<>(leftChunk.getVariables());
                            variables.remove(v);
                            left.add(new EquationPart(leftChunk.getConstant(), variables));
                            break startOver;
                        }
                    }
                }
                for (EquationPart potentialMatch : leftCopy) {
                    if (leftChunk.isCompatibleWith(potentialMatch) && leftChunk != potentialMatch) {
                        double product = leftChunk.getConstant().getCoefficient() + potentialMatch.getConstant().getCoefficient();
                        left.remove(leftChunk);
                        left.remove(potentialMatch);
                        if (product != 0.0) {
                            left.add(new EquationPart(new Element(product), leftChunk.getVariables()));
                        }
                        break startOver;
                    }
                }
            }
            break;
        }
        if (right.isEmpty()) {
            right.add(new EquationPart(0.0));
        }
        if (left.isEmpty()) {
            left.add(new EquationPart(0.0));
        }
        return new Equation(left, right, conditions);
    }

}
