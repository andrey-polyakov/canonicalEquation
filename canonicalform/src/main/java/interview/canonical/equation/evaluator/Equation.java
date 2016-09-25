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

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        if (!leftPart.get(0).isPositive()) {
            sb.append(" - ");
        }
        sb.append(leftPart.get(0));
        for (int ii = 1; ii < leftPart.size(); ii ++) {
            if (leftPart.get(ii).isPositive()) {
                sb.append(" + ");
            } else {
                sb.append(" - ");
            }
            sb.append(leftPart.get(ii));
        }
        sb.append(" = ");
        if (!rightPart.get(0).isPositive()) {
            sb.append(" - ");
        }
        sb.append(rightPart.get(0));
        for (int ii = 1; ii < rightPart.size(); ii ++) {
            if (rightPart.get(ii).isPositive()) {
                sb.append(" + ");
            } else {
                sb.append(" - ");
            }
            sb.append(rightPart.get(ii));
        }
        return sb.toString();
    }
}
