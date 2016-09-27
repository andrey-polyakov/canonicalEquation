package interview.canonical.equation.evaluator;

import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Андрей on 22.09.2016.
 */
public class EquationChunk {
    private double coefficient = 1.0;
    private long powerPart = 1;
    private Set<Variable> variables = new TreeSet<Variable>();
    private boolean positive = true;
    private boolean positivePower = true;

    public double getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(double coefficient) {
        this.coefficient = coefficient;
    }

    public long getPowerPart() {
        return powerPart;
    }

    public void setPowerPart(long powerPart) {
        this.powerPart = powerPart;
    }

    public Set<Variable> getVariables() {
        return variables;
    }

    public void setVariables(Set<Variable> variables) {
        this.variables = variables;
    }

    public boolean isPositive() {
        return positive;
    }

    public void setPositive(boolean positive) {
        this.positive = positive;
    }

    public void addVariable(Character symbol) {
        Variable variable = new Variable(symbol.toString());
        variables.add(variable);
    }

    public void negate() {
        positive = !positive;
    }

    public void negatePower() {
        positivePower = !positivePower;
    }

    public boolean isPositivePower() {
        return positivePower;
    }

    public void setPositivePower(boolean positivePower) {
        this.positivePower = positivePower;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        if (coefficient != 1.0) {
            sb.append(coefficient);
        }
        for (Variable variable : variables) {
            sb.append(variable.getLetter());
        }
        if (powerPart != 1) {
            sb.append("^");
            sb.append(powerPart);
        }
        return sb.toString();
    }

    public boolean isCompatible(EquationChunk another) {
        if (isCoefficentOnly() && another.isCoefficentOnly()) {
            return true;
        }
        if (variables.size() == another.variables.size() && variables.containsAll(another.variables)) {
            return powerPart == another.powerPart;
        }
        return false;
    }

    public boolean isCoefficentOnly() {
        return powerPart == 1 && variables.isEmpty();
    }

}
