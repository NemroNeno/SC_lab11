package expressivo;

import java.util.Map;

/**
 * A variable in the expression.
 */
public class Variable implements Expression {
    private final String name;

    public Variable(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Variable name cannot be null or empty.");
        }
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Variable variable = (Variable) obj;
        return name.equals(variable.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public void checkRep() {
        // Invariant: the name must be a non-empty string
        if (name.isEmpty()) {
            throw new IllegalStateException("Variable name cannot be empty.");
        }
    }


    @Override public Expression differentiate(String variable) {
        return name.equals(variable) ? new Number(1) : new Number(0);
    }

    @Override public Expression simplify(Map<String, Double> environment) {
        return environment.containsKey(name) ? new Number(environment.get(name)) : this;
    }
}