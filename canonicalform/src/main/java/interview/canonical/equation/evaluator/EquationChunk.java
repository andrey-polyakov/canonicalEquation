package interview.canonical.equation.evaluator;

import java.util.Collections;
import java.util.Set;

/**
 * Created by Андрей on 22.09.2016.
 */
public class EquationChunk {
    private double floatingPointPart = 1.0;
    private long powerPart = 1;
    private Set<Variable> variables = Collections.emptySet();
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
}
