package interview.canonical.equation;

import interview.canonical.equation.evaluator.Element;
import interview.canonical.equation.evaluator.Equation;
import interview.canonical.equation.evaluator.EquationConverter;
import interview.canonical.equation.evaluator.EquationPart;
import interview.canonical.equation.parser.EquationParser;
import interview.canonical.equation.parser.exception.ParserException;
import org.junit.Assert;
import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EquationParserTest {

    @Test
    public void exampleGivenInTaskDescription() throws ParserException {
        String given = "x^2 + 3.5xy + y = y^2 - xy + y";
        Equation transformed = new EquationParser().parse(given);
        assertEquals(given, transformed.toString());
    }

    @Test(expected = ParserException.class)
    public void missingRightPart() throws ParserException {
        new EquationParser().parse("x^2 = ");
    }

    @Test()
    public void zeroPower() throws ParserException {
        Equation e = new EquationParser().parse("x^0=0");
        Assert.assertFalse(e.getLeftPart().get(0).getConstant().isNotZero());
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
}
