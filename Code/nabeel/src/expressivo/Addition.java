package expressivo;

import java.util.Map;

/**
 * An addition operation between two expressions.
 */
public class Addition implements Expression {
    private final Expression left, right;

    public Addition(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return left.toString() + " + " + right.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Addition addition = (Addition) obj;
        return left.equals(addition.left) && right.equals(addition.right);
    }

    @Override
    public int hashCode() {
        int result = left.hashCode();
        result = 31 * result + right.hashCode();
        return result;
    }

    @Override
    public void checkRep() {
        // Ensure left and right are non-null
        if (left == null || right == null) {
            throw new IllegalStateException("Addition requires both left and right operands.");
        }
    }

    @Override
    public Expression simplify(Map<String, Double> environment) {
        // Simplify the left and right operands first
        Expression simplifiedLeft = left.simplify(environment);
        Expression simplifiedRight = right.simplify(environment);

        // Case 1: If both left and right are numbers (constants), combine them
        if (simplifiedLeft instanceof Number && simplifiedRight instanceof Number) {
            double leftValue = ((Number) simplifiedLeft).getValue();
            double rightValue = ((Number) simplifiedRight).getValue();
            return new Number(leftValue + rightValue);
        }

        // Case 2: If only one operand is a number, it can be simplified
        if (simplifiedLeft instanceof Number) {
            double leftValue = ((Number) simplifiedLeft).getValue();
            if (leftValue == 0) {
                return simplifiedRight; // 0 + x simplifies to x
            }
        }

        if (simplifiedRight instanceof Number) {
            double rightValue = ((Number) simplifiedRight).getValue();
            if (rightValue == 0) {
                return simplifiedLeft; // x + 0 simplifies to x
            }
        }

        // Return the addition expression if no simplifications can be made
        return new Addition(simplifiedLeft, simplifiedRight);
    }

    @Override
    public Expression differentiate(String variable) {
        // Apply the rule: (f(x) + g(x))' = f'(x) + g'(x)
        Expression differentiatedLeft = left.differentiate(variable);
        Expression differentiatedRight = right.differentiate(variable);
        return new Addition(differentiatedLeft, differentiatedRight);
    }
}