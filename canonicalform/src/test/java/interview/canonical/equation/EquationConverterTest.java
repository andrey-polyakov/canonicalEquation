package interview.canonical.equation;

import interview.canonical.equation.evaluator.Element;
import interview.canonical.equation.evaluator.Equation;
import interview.canonical.equation.evaluator.EquationConverter;
import interview.canonical.equation.evaluator.EquationPart;
import interview.canonical.equation.parser.EquationParser;
import interview.canonical.equation.parser.exception.ParserException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EquationConverterTest {

    @Test
    public void exampleTest() throws ParserException {
        EquationConverter ec = new EquationConverter();
        String given = "x^2 + 3.5xy + y = y^2 - xy + y";
        Equation equation = EquationParser.parse(given);
        assertEquals(given, equation.toString());
        Equation canonical = ec.convertToCanonicalForm(equation);
        canonical.toString();
    }

    @Test
    public void zeroTest() throws ParserException {
        EquationConverter ec = new EquationConverter();
        String given = "xy^2  = xy^2";
        Equation equation = EquationParser.parse(given);
        Equation canonical = ec.convertToCanonicalForm(equation);
        assertTrue(canonical.getLeftPart().get(0).getVariables().isEmpty());
        assertEquals(0, canonical.getLeftPart().get(0).getConstant().getCoefficient(), 0.0001);
        assertEquals(0, canonical.getRightPart().get(0).getConstant().getCoefficient(), 0.0001);
    }

    @Test
    public void elementaryTest() throws ParserException {
        EquationConverter ec = new EquationConverter();
        String given = "x ^ 2 = y ^ 2 + x^2";
        Equation equation = EquationParser.parse(given);
        Equation canonical = ec.convertToCanonicalForm(equation);
        assertTrue(canonical.getLeftPart().get(0).getVariables().contains(new Element("y", 2)));
        assertEquals(-1.0, canonical.getLeftPart().get(0).getConstant().getCoefficient(), 0.0001);
        assertEquals(0, canonical.getRightPart().get(0).getConstant().getCoefficient(), 0.0001);
    }

    @Test
    public void xEqualsZeroTest() throws ParserException {
        EquationConverter ec = new EquationConverter();
        String given = "2 = 2 - x";
        Equation equation = EquationParser.parse(given);
        Equation canonical = ec.convertToCanonicalForm(equation);
        assertTrue(canonical.getLeftPart().get(0).getVariables().contains(new Element("x")));
        assertEquals(1.0, canonical.getLeftPart().get(0).getConstant().getCoefficient(), 0.0001);
        assertEquals(0, canonical.getRightPart().get(0).getConstant().getCoefficient(), 0.0001);
    }

    @Test
    public void twoConstantsTest() throws ParserException {
        EquationConverter ec = new EquationConverter();
        String given = "-4 = 5";
        Equation equation = EquationParser.parse(given);
        Equation canonical = ec.convertToCanonicalForm(equation);
        assertTrue(canonical.getLeftPart().get(0).getVariables().isEmpty());
        assertEquals(-9.0, canonical.getLeftPart().get(0).getConstant().getCoefficient(), 0.0001);
        assertEquals(0, canonical.getRightPart().get(0).getConstant().getCoefficient(), 0.0001);
    }

    @Test
    public void noConversionTest() throws ParserException {
        EquationConverter ec = new EquationConverter();
        String given = "x = 0";
        Equation equation = EquationParser.parse(given);
        Equation canonical = ec.convertToCanonicalForm(equation);
        assertTrue(canonical.getLeftPart().get(0).getVariables().contains(new Element("x")));
        assertEquals(0, canonical.getRightPart().get(0).getConstant().getCoefficient(), 0.0001);
    }
}
