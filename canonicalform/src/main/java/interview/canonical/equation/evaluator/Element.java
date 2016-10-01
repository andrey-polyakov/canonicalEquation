package interview.canonical.equation.evaluator;

/**
 * Represents either a constant or a variable.
 */
public class Element {
    private final String letter;
    private final long power;
    private final double coefficient;

    public Element(String variableName, long power) {
        letter = variableName;
        this.power = power;
        coefficient = 1.0;
    }

    public Element(String variableName) {
        letter = variableName;
        power = 1;
        coefficient = 1.0;
    }

    public Element(double factor, long power) {
        letter = "";
        this.power = power;
        coefficient = Math.abs(factor);;
    }

    public Element(double factor) {
        letter = "";
        power = 1;
        coefficient = Math.abs(factor);
    }

    public double getCoefficient() {
        return coefficient;
    }

    public String getLetter() {
        return letter;
    }

    public long getPower() {
        return power;
    }

    public boolean isVariable() {
        return letter.isEmpty();
    }

    @Override
    public String toString() {
        if (letter.isEmpty()) {
            if (power != 1) {
                return String.valueOf(coefficient) + "^" + power;
            }
            return String.valueOf(coefficient);
        }
        if (power != 1) {
            if (coefficient != 1.0) {
                return coefficient + String.valueOf(letter) + "^" + power;
            }
            return String.valueOf(letter) + "^" + power;
        }
        return letter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Element element = (Element) o;

        if (power != element.power) return false;
        if (Double.compare(element.coefficient, coefficient) != 0) return false;
        return letter.equals(element.letter);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = letter.hashCode();
        result = 31 * result + (int) (power ^ (power >>> 32));
        temp = Double.doubleToLongBits(coefficient);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}