package interview.canonical.equation.evaluator;

import java.util.Collections;
import java.util.Set;


public class EquationPart {
    private final Set<Element> variables;
    private final Element constant;

    public EquationPart(Element factor, Set<Element> variables) {
        this.variables = variables;
        constant = factor;
    }

    public EquationPart(double value) {
        constant = new Element(value);
        variables = Collections.emptySet();
    }

    public Set<Element> getVariables() {
        return variables;
    }

    public EquationPart negate() {
        return new EquationPart(constant.negate(), variables);
    }

    @Override
    public String toString() {
        if (constant.getCoefficient() == 0.0) {
            return "0";
        }
        StringBuffer sb = new StringBuffer();
        if (constant.getCoefficient() == 1.0) {
            if (variables.isEmpty()) {
                sb.append(constant.getCoefficient());
            }
        } else if (constant.getCoefficient() == -1.0) {
            if (variables.isEmpty()) {
                sb.append(constant.getCoefficient());
            } else {
                sb.append(" - ");
            }
        } else {
            sb.append(constant.getCoefficient());
        }
        for (Element element : variables) {
            sb.append(element.getLetter());
            if (element.getPower() != 1) {
                sb.append("^");
                sb.append(element.getPower());
            }
        }
        return sb.toString();
    }

    public boolean isCompatibleWith(EquationPart another) {
        if (isCoefficentOnly() && another.isCoefficentOnly()) {
            return true;
        }
        if (variables.size() == another.variables.size() && variables.containsAll(another.variables)) {
            return true;
        }
        return false;
    }

    public boolean isCoefficentOnly() {
        return variables.isEmpty();
    }

    public EquationPart negateAndMergeWith(EquationPart ec) {
        if (!ec.isCompatibleWith(this)) {
            throw new IllegalArgumentException("Incompatible equation parts");
        }
        double product = -constant.getCoefficient() + ec.getConstant().getCoefficient();
        if (product == 0) {
            return null;
        }
        return new EquationPart(new Element(product), variables);
    }

    public Element getConstant() {
        return constant;
    }
}
