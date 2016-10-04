package interview.canonical.equation.evaluator;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Represents equation. This class is immutable for the sae of design clarity.
 */
public class Equation {

    private final List<EquationPart> leftPart;
    private final List<EquationPart> rightPart;
    private final List<Assumption> conditions;

    public Equation(List<EquationPart> chain, List<EquationPart> rightPart, List<Assumption> conditions) {
        this.leftPart = Collections.unmodifiableList(chain);
        this.rightPart = Collections.unmodifiableList(rightPart);
        this.conditions = conditions;
    }

    public List<EquationPart> getLeftPart() {
        return leftPart;
    }

    public List<EquationPart> getRightPart() {
        return rightPart;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(leftPart.get(0));
        for (int ii = 1; ii < leftPart.size(); ii ++) {
            if (leftPart.get(ii).getConstant().getCoefficient() > 0) {
                sb.append(" + ");
            }
            sb.append(leftPart.get(ii));
        }
        sb.append(" = ");
        sb.append(rightPart.get(0));
        for (int ii = 1; ii < rightPart.size(); ii ++) {
            if (rightPart.get(ii).getConstant().getCoefficient() > 0) {
                sb.append(" + ");
            }
            sb.append(rightPart.get(ii));
        }
        Iterator<Assumption> i = conditions.iterator();
        if (!conditions.isEmpty()) {
            sb.append(" , where: ");
        }
        while (i.hasNext()) {
            sb.append(i.next());
            if (i.hasNext()) {
                sb.append(" and ");
            }
        }
        return sb.toString();
    }
}
