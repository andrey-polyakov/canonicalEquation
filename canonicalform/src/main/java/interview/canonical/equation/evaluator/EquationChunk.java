package interview.canonical.equation.evaluator;

import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Андрей on 22.09.2016.
 */
public class EquationChunk {
    private double floatingPointPart = 1.0;
    private long powerPart = 1;
    private Set<Variable> variables = new TreeSet<Variable>();
    private boolean positive = true;

    public double getFloatingPointPart() {
        return floatingPointPart;
    }

    public void setFloatingPointPart(double floatingPointPart) {
        this.floatingPointPart = floatingPointPart;
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
}
