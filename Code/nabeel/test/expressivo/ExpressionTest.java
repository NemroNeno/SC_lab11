package expressivo;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

public class ExpressionTest {

	
	  private final Expression zero = new Number(0);
	  private final Expression one = new Number(1);
	  private final Expression two = new Number(2);
	  private final Expression x = new Variable("x");
	  private final Expression y = new Variable("y");

	  private final Expression exp1 = new Operation('+', one, x);
	  private final Expression exp2 = new Operation('*', x, one);
	  private final Expression exp3 = new Operation('*', exp1, exp2);
	  private final Expression exp4 = new Operation('*', x, y);
    // Partition for addExpr
    @Test
    public void testAddExprEmpty() {
        Expression expr = new Number(0);
        Expression result = new Addition(expr, new Number(0));
        assertEquals("0.0 + 0.0", result.toString()); // Adjusted formatting for clarity
    }

    @Test
    public void testAddExprMultipleVariables() {
        Expression expr = new Addition(new Variable("x"), new Variable("y"));
        assertEquals("x + y", expr.toString()); // No parentheses in expected output
    }

    @Test
    public void testAddExprSubset() {
        Expression subset = new Variable("x");
        Expression expr = new Addition(subset, new Variable("y"));
        assertTrue(expr.toString().contains(subset.toString()));
    }

    @Test
    public void testAddExprEquals() {
        Expression expr1 = new Addition(new Variable("x"), new Variable("y"));
        Expression expr2 = new Addition(new Variable("x"), new Variable("y"));
        assertEquals(expr1, expr2);
    }

    // Partition for multiplyExpr
    @Test
    public void testMultiplyExprEmpty() {
        Expression expr = new Number(0);
        Expression result = new Multiplication(expr, new Number(0));
        assertEquals("0.0 * 0.0", result.toString()); // Adjusted formatting for clarity
    }

    @Test
    public void testMultiplyExprIdentity() {
        Expression expr = new Multiplication(new Number(1), new Variable("x"));
        assertEquals("1.0 * x", expr.toString()); // No parentheses in expected output
    }

    @Test
    public void testMultiplyExprMultipleVariables() {
        Expression expr = new Multiplication(new Variable("x"), new Variable("y"));
        assertEquals("x * y", expr.toString()); // No parentheses in expected output
    }

    @Test
    public void testMultiplyExprSubset() {
        Expression subset = new Variable("x");
        Expression expr = new Multiplication(subset, new Variable("y"));
        assertTrue(expr.toString().contains(subset.toString()));
    }

    @Test
    public void testMultiplyExprEquals() {
        Expression expr1 = new Multiplication(new Variable("x"), new Variable("y"));
        Expression expr2 = new Multiplication(new Variable("x"), new Variable("y"));
        assertEquals(expr1, expr2);
    }

    // Partition for toString
    @Test
    public void testToStringEmptyExpression() {
        Expression expr = new Number(0);
        assertEquals("0.0", expr.toString());
    }

    @Test
    public void testToStringMultipleVariables() {
        Expression expr = new Addition(new Variable("x"), new Variable("y"));
        assertEquals("x + y", expr.toString());
    }

    // Partition for equals
    @Test
    public void testEqualsReflexive() {
        Expression expr = new Variable("x");
        assertEquals(expr, expr);
    }

    @Test
    public void testEqualsSymmetric() {
        Expression expr1 = new Variable("x");
        Expression expr2 = new Variable("x");
        assertEquals(expr1, expr2);
        assertEquals(expr2, expr1);
    }

    @Test
    public void testEqualsTransitive() {
        Expression expr1 = new Variable("x");
        Expression expr2 = new Variable("x");
        Expression expr3 = new Variable("x");
        assertEquals(expr1, expr2);
        assertEquals(expr2, expr3);
        assertEquals(expr1, expr3);
    }

    @Test
    public void testEqualsDifferentTypes() {
        Expression expr1 = new Variable("x");
        Expression expr2 = new Number(5);
        assertNotEquals(expr1, expr2); // Testing different types
    }

    @Test
    public void testEqualsNumbersCorrectToFiveDecimals() {
        Expression expr1 = new Number(1.12345);
        Expression expr2 = new Number(1.12345);
        assertEquals(expr1, expr2);
    }

    // Partition for hashCode
    @Test
    public void testHashCodeEquality() {
        Expression expr1 = new Addition(new Variable("x"), new Number(5));
        Expression expr2 = new Addition(new Variable("x"), new Number(5));
        assertEquals(expr1.hashCode(), expr2.hashCode());
    }

    @Test
    public void testHashCodeInequality() {
        Expression expr1 = new Addition(new Variable("x"), new Number(5));
        Expression expr2 = new Addition(new Variable("y"), new Number(5));
        assertNotEquals(expr1.hashCode(), expr2.hashCode());
    }

    // Edge case tests
    @Test
    public void testAddExprWithZero() {
        Expression expr = new Addition(new Number(0), new Variable("x"));
        assertEquals("0.0 + x", expr.toString());
    }

    @Test
    public void testMultiplyExprWithOne() {
        Expression expr = new Multiplication(new Number(1), new Variable("x"));
        assertEquals("1.0 * x", expr.toString());
    }
    @Test
    public void testDifferentiateNumber() {
        assertEquals("expected differentiated expression", one.differentiate("x"), zero);
    }

    @Test
    public void testDifferentiateVariable() {
        assertEquals("expected differentiated expression", x.differentiate("x"), one);
    }

    @Test
    public void testDifferentiatePlus() {
        Expression exp = new Operation('+', zero, one);
        assertEquals("expected differentiated expression", exp1.differentiate("x"), exp);
    }

    @Test
    public void testDifferentiateMultiply() {
        Expression exp = new Operation('+', new Operation('*', one, one),
            new Operation('*', x, zero));
        assertEquals("expected differentiated expression", exp2.differentiate("x"), exp);
    }

    @Test
    public void testDifferentiateSingleSameVariable() {
        Expression left = new Operation('*', new Operation('+', zero, one),
            new Operation('*', x, one));
        Expression right = new Operation('*', new Operation('+', one, x),
            new Operation('+', new Operation('*', one, one), new Operation('*', x, zero)));
        Expression exp = new Operation('+', left, right);
        assertEquals("expected differentiated expression", exp3.differentiate("x"), exp);
    }

    @Test
    public void testDifferentiateSingleDifferentVariable() {
        Expression left = new Operation('*', new Operation('+', zero, zero),
            new Operation('*', x, one));
        Expression right = new Operation('*', new Operation('+', one, x),
            new Operation('+', new Operation('*', zero, one), new Operation('*', x, zero)));
        Expression exp = new Operation('+', left, right);
        assertEquals("expected differentiated expression", exp3.differentiate("y"), exp);
    }

    @Test
    public void testDifferentiateMultipleVariables() {
        Expression exp = new Operation('+', new Operation('*', one, y),
            new Operation('*', x, zero));
        assertEquals("expected differentiated expression", exp4.differentiate("x"), exp);
    }

    @Test
    public void testSimplifyNumber() {
        assertEquals("expected simplified expression", one.simplify(Map.of("x", 2.0)), one);
    }

    @Test
    public void testSimplifyVariable() {
        assertEquals("expected simplified expression", x.simplify(Map.of("x", 2.0)), two);
    }

    @Test
    public void testSimplifyPlusNumber() {
        Expression exp = new Operation('+', zero, one);
        assertEquals("expected simplified expression", exp.simplify(Map.of("x", 2.0)), one);
    }

    @Test
    public void testSimplifyPlusExpression() {
        assertEquals("expected simplified expression", exp1.simplify(Map.of("x", 2.0)), new Number(3));
    }

    @Test
    public void testSimplifyMultiplyNumber() {
        Expression exp = new Operation('*', zero, one);
        assertEquals("expected simplified expression", exp.simplify(Map.of("x", 2.0)), zero);
    }

    @Test
    public void testSimplifyMultiplyExpression() {
        assertEquals("expected simplified expression", exp2.simplify(Map.of("x", 2.0)), two);
    }

    @Test
    public void testSimplifySingleSameVariable() {
        assertEquals("expected simplified expression", exp3.simplify(Map.of("x", 2.0)), new Number(6));
    }

    @Test
    public void testSimplifySingleDifferentVariable() {
        assertEquals("expected simplified expression", exp3.simplify(Map.of("y", 2.0)), exp3);
    }

    @Test
    public void testSimplifyMultipleVariables() {
        Expression exp = new Operation('*', two, y);
        assertEquals("expected simplified expression", exp4.simplify(Map.of("x", 2.0)), exp);
    }

}