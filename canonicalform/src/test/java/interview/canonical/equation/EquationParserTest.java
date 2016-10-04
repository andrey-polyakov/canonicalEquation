package interview.canonical.equation;

import interview.canonical.equation.evaluator.Element;
import interview.canonical.equation.evaluator.Equation;
import interview.canonical.equation.parser.EquationParser;
import interview.canonical.equation.parser.exception.ParserException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EquationParserTest {

    @Test
    public void exampleGivenInTaskDescription() throws ParserException {
        String given = "x^2 + 3.5xy + y = y^2 - xy + y";
        Equation transformed = new EquationParser().parse(given);
        assertEquals(given, transformed.toString());
    }

    @Test
    public void variablesOrder() throws ParserException {
        String given = "x^-1y^-1z = zx^-1y^-1";
        Equation transformed = new EquationParser().parse(given);
        assertTrue(transformed.getLeftPart().get(0).getVariables().contains(new Element("x", Long.valueOf(-1))));
        assertTrue(transformed.getLeftPart().get(0).getVariables().contains(new Element("y", Long.valueOf(-1))));
        assertTrue(transformed.getLeftPart().get(0).getVariables().contains(new Element("z")));
        assertTrue(transformed.getRightPart().get(0).getVariables().contains(new Element("x", -1)));
        assertTrue(transformed.getRightPart().get(0).getVariables().contains(new Element("y", -1)));
        assertTrue(transformed.getRightPart().get(0).getVariables().contains(new Element("z")));
    }

    @Test
    public void variablesOrder2() throws ParserException {
        String given = "x^-1-2y^-1x = xy^-1+x-1";
        Equation transformed = new EquationParser().parse(given);
        assertTrue(transformed.getLeftPart().get(0).getVariables().contains(new Element("x", -1)));
        assertTrue(transformed.getLeftPart().get(1).getVariables().contains(new Element("y", -1)));
        assertEquals(-2, transformed.getLeftPart().get(1).getConstant().getCoefficient(), 0.0001);
        assertTrue(transformed.getLeftPart().get(1).getVariables().contains(new Element("x")));
        assertTrue(transformed.getRightPart().get(0).getVariables().contains(new Element("x")));
        assertTrue(transformed.getRightPart().get(0).getVariables().contains(new Element("y", -1)));
        assertTrue(transformed.getRightPart().get(1).getVariables().contains(new Element("x")));
        assertEquals(-1, transformed.getRightPart().get(2).getConstant().getCoefficient(), 0.0001);

    }

    @Test(expected = ParserException.class)
    public void missingRightPart() throws ParserException {
        new EquationParser().parse("x^2 = ");
    }

    @Test(expected = ParserException.class)
    public void missingLeftPart() throws ParserException {
        String given = " = x ^ 2";
        EquationParser unitUnderTest = new EquationParser();
        unitUnderTest.parse(given);
    }

    @Test
    public void elementaryTest() throws ParserException {
        String given = "2.5xy^3=0";
        Equation transformed = new EquationParser().parse(given);
        assertEquals(2.5, transformed.getLeftPart().get(0).getConstant().getCoefficient(), 0.001);
        assertTrue(transformed.getLeftPart().get(0).getVariables().contains(new Element("x")));
        assertTrue(transformed.getLeftPart().get(0).getVariables().contains(new Element("y", 3)));
    }

    @Test
    public void negativePowerTest() throws ParserException {
        String given = "w^-3=0";
        Equation transformed = new EquationParser().parse(given);
        assertTrue(transformed.getLeftPart().get(0).getVariables().contains(new Element("w", -3)));
    }

    @Test
    public void negativePower2Test() throws ParserException {
        String given = "x^-3=0";
        Equation transformed = new EquationParser().parse(given);
        assertTrue(transformed.getLeftPart().get(0).getVariables().contains(new Element("x", -3)));
    }

    @Test
    public void negativeElementaryTest() throws ParserException {
        String given = "-1.111xy^3=0";
        Equation transformed = new EquationParser().parse(given);
        assertEquals(-1.111, transformed.getLeftPart().get(0).getConstant().getCoefficient(), 0.001);
        assertTrue(transformed.getLeftPart().get(0).getVariables().contains(new Element("x")));
        assertTrue(transformed.getLeftPart().get(0).getVariables().contains(new Element("y", 3)));
        assertEquals(transformed.getLeftPart().get(0).getVariables().size(), 2);
    }

    @Test
    public void numberAloneTest() throws ParserException {
        String given = "998.0=0";
        Equation transformed = new EquationParser().parse(given);
        assertEquals(998.0, transformed.getLeftPart().get(0).getConstant().getCoefficient(), 0.001);
        assertEquals(1, transformed.getLeftPart().get(0).getConstant().getPower());
    }

    @Test
    public void numberDoubleNegationTest() throws ParserException {
        String given = "--20008.0=m";
        Equation transformed = new EquationParser().parse(given);
        assertEquals(20008.0, transformed.getLeftPart().get(0).getConstant().getCoefficient(), 0.001);
        assertEquals(transformed.getLeftPart().get(0).getConstant().getPower(), 1);
    }

    @Test
    public void numberTripleNegationTest() throws ParserException {
        String given = "---+20008.0=-z";
        Equation transformed = new EquationParser().parse(given);
        assertEquals(-20008.0, transformed.getLeftPart().get(0).getConstant().getCoefficient(), 0.001);
        assertEquals(transformed.getLeftPart().get(0).getConstant().getPower(), 1);
    }

    @Test
    public void multiplePowersTest() throws ParserException {
        String given = "x^2y^45 = 0";
        Equation equation = new EquationParser().parse(given);
        assertTrue(equation.getLeftPart().get(0).getVariables().contains(new Element("y", 45)));
        assertTrue(equation.getLeftPart().get(0).getVariables().contains(new Element("x", 2)));
    }

    @Test
    public void xInPowerOneTest() throws ParserException {
        String given = "x^1 = 0";
        Equation equation = new EquationParser().parse(given);
        assertTrue(equation.getLeftPart().get(0).getVariables().contains(new Element("x", 1)));
    }

    @Test
    public void xSquaredTest() throws ParserException {
        String given = "x^4x^-2 = 0";
        Equation equation = new EquationParser().parse(given);
        assertTrue(equation.getLeftPart().get(0).getVariables().contains(new Element("x", 2)));
    }

    @Test
    public void xCubeTest() throws ParserException {
        String given = "xx^2 = 0";
        Equation equation = new EquationParser().parse(given);
        assertTrue(equation.getLeftPart().get(0).getVariables().contains(new Element("x", 3)));
    }

}
