package expressivo;

import java.util.Map;

/**
 * A multiplication operation between two expressions.
 */
public class Multiplication implements Expression {
    private final Expression left, right;

    public Multiplication(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return left.toString() + " * " + right.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Multiplication multiplication = (Multiplication) obj;
        return left.equals(multiplication.left) && right.equals(multiplication.right);
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
            throw new IllegalStateException("Multiplication requires both left and right operands.");
        }
    }

    @Override
    public Expression simplify(Map<String, Double> environment) {
        // Simplify the left and right operands first
        Expression simplifiedLeft = left.simplify(environment);
        Expression simplifiedRight = right.simplify(environment);

        // Case 1: If both left and right are numbers (constants), multiply them
        if (simplifiedLeft instanceof Number && simplifiedRight instanceof Number) {
            double leftValue = ((Number) simplifiedLeft).getValue();
            double rightValue = ((Number) simplifiedRight).getValue();
            return new Number(leftValue * rightValue);
        }

        // Case 2: If either operand is 0, the result is 0 (anything * 0 = 0)
        if (simplifiedLeft instanceof Number && ((Number) simplifiedLeft).getValue() == 0) {
            return new Number(0); // 0 * x = 0
        }
        if (simplifiedRight instanceof Number && ((Number) simplifiedRight).getValue() == 0) {
            return new Number(0); // x * 0 = 0
        }

        // Case 3: If either operand is 1, return the other operand (anything * 1 = the other operand)
        if (simplifiedLeft instanceof Number && ((Number) simplifiedLeft).getValue() == 1) {
            return simplifiedRight; // 1 * x = x
        }
        if (simplifiedRight instanceof Number && ((Number) simplifiedRight).getValue() == 1) {
            return simplifiedLeft; // x * 1 = x
        }

        // Return the multiplication expression if no simplifications can be made
        return new Multiplication(simplifiedLeft, simplifiedRight);
    }

    @Override
    public Expression differentiate(String variable){
        // Apply the product rule: (f(x) * g(x))' = f'(x) * g(x) + f(x) * g'(x)
        Expression differentiatedLeft = left.differentiate(variable);
        Expression differentiatedRight = right.differentiate(variable);
        Expression leftTimesRight = new Multiplication(differentiatedLeft, right);
        Expression rightTimesLeft = new Multiplication(left, differentiatedRight);
        return new Addition(leftTimesRight, rightTimesLeft);
    }
}