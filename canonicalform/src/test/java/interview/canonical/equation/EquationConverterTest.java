package interview.canonical.equation;

import interview.canonical.equation.evaluator.Element;
import interview.canonical.equation.evaluator.Equation;
import interview.canonical.equation.evaluator.EquationConverter;
import interview.canonical.equation.parser.EquationParser;
import interview.canonical.equation.parser.exception.ParserException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class EquationConverterTest {

    @Test
    public void exampleTest() throws ParserException {
        String given = "x^2 + 3.5xy + y = y^2 - xy + y";
        Equation equation = new EquationParser().parse(given);
        assertEquals(given, equation.toString());
        Equation canonical = EquationConverter.convertToCanonicalForm(equation);
        canonical.toString();
    }

    @Test
    public void zeroTest() throws ParserException {
        String given = "xy^2  = xy^2";
        Equation equation = new EquationParser().parse(given);
        Equation canonical = EquationConverter.convertToCanonicalForm(equation);
        assertTrue(canonical.getLeftPart().get(0).getVariables().isEmpty());
        assertEquals(0, canonical.getLeftPart().get(0).getConstant().getCoefficient(), 0.0001);
        assertEquals(0, canonical.getRightPart().get(0).getConstant().getCoefficient(), 0.0001);
    }

    @Test
    public void elementaryTest() throws ParserException {
        String given = "x ^ 2 = y ^ 2 + x^2";
        Equation equation = new EquationParser().parse(given);
        Equation canonical = EquationConverter.convertToCanonicalForm(equation);
        assertTrue(canonical.getLeftPart().get(0).getVariables().contains(new Element("y", Long.valueOf(2))));
        assertEquals(-1.0, canonical.getLeftPart().get(0).getConstant().getCoefficient(), 0.0001);
        assertEquals(0, canonical.getRightPart().get(0).getConstant().getCoefficient(), 0.0001);
    }

    @Test
    public void xEqualsZeroTest() throws ParserException {
        String given = "2 = 2 - x";
        Equation equation = new EquationParser().parse(given);
        Equation canonical = EquationConverter.convertToCanonicalForm(equation);
        assertTrue(canonical.getLeftPart().get(0).getVariables().contains(new Element("x")));
        assertEquals(1.0, canonical.getLeftPart().get(0).getConstant().getCoefficient(), 0.0001);
        assertEquals(0, canonical.getRightPart().get(0).getConstant().getCoefficient(), 0.0001);
    }

    @Test
    public void twoConstantsTest() throws ParserException {
        String given = "-4 = 5";
        Equation equation = new EquationParser().parse(given);
        Equation canonical = EquationConverter.convertToCanonicalForm(equation);
        assertTrue(canonical.getLeftPart().get(0).getVariables().isEmpty());
        assertEquals(-9.0, canonical.getLeftPart().get(0).getConstant().getCoefficient(), 0.0001);
        assertEquals(0, canonical.getRightPart().get(0).getConstant().getCoefficient(), 0.0001);
    }

    @Test
    public void noConversionTest() throws ParserException {
        String given = "x = 0";
        Equation equation = new EquationParser().parse(given);
        Equation canonical = EquationConverter.convertToCanonicalForm(equation);
        assertTrue(canonical.getLeftPart().get(0).getVariables().contains(new Element("x")));
        assertEquals(0, canonical.getRightPart().get(0).getConstant().getCoefficient(), 0.0001);
    }

    @Test()
    public void negativePower() throws ParserException {
        Equation e = new EquationParser().parse("x^-1 = x+1");
        Equation canonical = EquationConverter.convertToCanonicalForm(e);
        assertTrue(canonical.getLeftPart().get(0).getVariables().contains(new Element("x", Long.valueOf(-1))));
        assertTrue(canonical.getLeftPart().get(1).getVariables().contains(new Element("x")));
        assertEquals(-1, canonical.getLeftPart().get(2).getConstant().getCoefficient(), 0.0001);
    }

    @Test()
    public void leftPartCanonization() throws ParserException {
        Equation e = new EquationParser().parse("y+y+y=y");
        Equation canonical = EquationConverter.convertToCanonicalForm(e);
        assertTrue(canonical.getLeftPart().get(0).getVariables().contains(new Element("2")));
        assertEquals(2, canonical.getLeftPart().get(1).getConstant().getCoefficient(), 0.0001);
    }
}
