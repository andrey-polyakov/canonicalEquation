package interview.canonical.equation.evaluator;

import java.util.*;

/**
 * Created by Андрей on 22.09.2016.
 */
public class EquationPart {
    private final Set<Element> variables;
    private final Element constant;
    private final boolean positive;

    public EquationPart(Set<Element> variables, boolean positive) {
        this.variables = variables;
        this.positive = positive;
        constant = new Element(1.0);
    }

    public EquationPart(Element factor, Set<Element> variables, boolean positive) {
        this.variables = variables;
        this.positive = positive;
        constant = factor;
    }

    public EquationPart(double value) {
        positive = value > 0;
        constant = new Element(value);
        variables = Collections.emptySet();
    }

    public Set<Element> getVariables() {
        return variables;
    }

    public boolean isPositive() {
        return positive;
    }

    public EquationPart negate() {
        if (constant != null) {
            return new EquationPart(-constant.getCoefficient());
        }
        return new EquationPart(variables, !positive);
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        if (constant.getCoefficient() != 1.0 || variables.isEmpty()) {
            sb.append(constant.getCoefficient());
            if (constant.getPower() != 1) {
                sb.append(constant.getPower());
            }
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
        double product = constant.getCoefficient();
        if (positive) {
            product = -1 * product;
        }
        if (ec.isPositive()) {
            product += ec.getConstant().getCoefficient();
        } else {
            product -= ec.getConstant().getCoefficient();
        }
        if (product == 0) {
            return null;
        }
        boolean sign = product > 0;
        return new EquationPart(new Element(product), variables, sign);
    }

    public EquationPart addVariable(char token) {
        Element element = new Element(String.valueOf(token));
        Set<Element> extendedVirablesList = new HashSet<>(variables);
        extendedVirablesList.add(element);
        return new EquationPart(extendedVirablesList, positive);
    }

    public Element getConstant() {
        return constant;
    }
}
