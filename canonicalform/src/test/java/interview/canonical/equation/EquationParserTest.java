package interview.canonical.equation;

import interview.canonical.equation.evaluator.Equation;
import interview.canonical.equation.evaluator.EquationChunk;
import interview.canonical.equation.evaluator.Variable;
import interview.canonical.equation.parser.EquationParser;
import interview.canonical.equation.parser.exception.ParserException;
import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EquationParserTest {

    @Test
    public void exampleGivenInTaskDescription() throws ParserException {
        String given = "x^2 + 3.5xy + y = y^2 - xy + y";
        EquationParser unitUnderTest = new EquationParser();
        Equation transformed = unitUnderTest.parse(given);
        assertEquals(given, transformed.toString());
    }

    @Test(expected = ParserException.class)
    public void missingRightPart() throws ParserException {
        String given = "x^2 = ";
        EquationParser unitUnderTest = new EquationParser();
        unitUnderTest.parse(given);
    }

    @Test(expected = ParserException.class)
    public void missingLeftPart() throws ParserException {
        String given = " = x ^ 2";
        EquationParser unitUnderTest = new EquationParser();
        unitUnderTest.parse(given);
    }

    @Test
    public void elementaryTest() throws ParserException {
        String given = "2.5xy^3";
        EquationChunk transformed = EquationParser.parsePart(given);
        assertEquals(2.5, transformed.getCoefficient(), 0.001);
        assertEquals(3, transformed.getPowerPart());
        assertTrue(transformed.isPositive());
        assertTrue(transformed.getVariables().contains(new Variable("x")));
        assertTrue(transformed.getVariables().contains(new Variable("y")));
    }

    @Test
    public void negativePowerTest() throws ParserException {
        String given = "2.5^-3";
        EquationChunk transformed = EquationParser.parsePart(given);
        assertEquals(2.5, transformed.getCoefficient(), 0.001);
        assertEquals(3, transformed.getPowerPart());
        assertFalse(transformed.isPositivePower());
        assertTrue(transformed.isPositive());
    }

    @Test
    public void negativeElementaryTest() throws ParserException {
        String given = "-1.111xy^3";
        EquationChunk transformed = EquationParser.parsePart(given);
        assertEquals(1.111, transformed.getCoefficient(), 0.001);
        assertEquals(3, transformed.getPowerPart());
        assertFalse(transformed.isPositive());
        assertTrue(transformed.getVariables().contains(new Variable("x")));
        assertTrue(transformed.getVariables().contains(new Variable("y")));
        assertEquals(transformed.getVariables().size(), 2);
    }

    @Test
    public void numberAloneTest() throws ParserException {
        String given = "998.0";
        EquationChunk transformed = EquationParser.parsePart(given);
        assertEquals(998.0, transformed.getCoefficient(), 0.001);
        assertEquals(1, transformed.getPowerPart());
        assertTrue(transformed.isPositive());
    }

    @Test
    public void numberDoubleNegationTest() throws ParserException {
        String given = "--20008.0";
        EquationChunk transformed = EquationParser.parsePart(given);
        assertEquals(20008.0, transformed.getCoefficient(), 0.001);
        assertEquals(transformed.getPowerPart(), 1);
        assertTrue(transformed.isPositive());
    }

    @Test
    public void numberTripleNegationTest() throws ParserException {
        String given = "---+20008.0";
        EquationChunk transformed = EquationParser.parsePart(given);
        assertEquals(20008.0, transformed.getCoefficient(), 0.001);
        assertEquals(transformed.getPowerPart(), 1);
        assertFalse(transformed.isPositive());
    }
}
