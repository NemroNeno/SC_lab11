package expressivo;

import java.util.Map;

/**
 * A number (literal) in the expression.
 */
public class Number implements Expression {
    private final double value;

    public Number(double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return Double.toString(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Number number = (Number) obj;
        return Double.compare(number.value, value) == 0;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(value);
    }

    @Override
    public void checkRep() {
        // No special rep invariants for a number
    }

    public double getNumber() {
        return value;
    }


    @Override public Expression differentiate(String variable) {
        return new Number(0);
    }

    @Override public Expression simplify(Map<String, Double> environment) {
        return this;
    }

}