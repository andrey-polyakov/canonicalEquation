package interview.canonical.equation;

import interview.canonical.equation.evaluator.Element;
import interview.canonical.equation.evaluator.Equation;
import interview.canonical.equation.evaluator.EquationConverter;
import interview.canonical.equation.evaluator.EquationPart;
import interview.canonical.equation.parser.EquationParser;
import interview.canonical.equation.parser.exception.ParserException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Андрей on 27.09.2016.
 */
public class EquationConverterTest {

    @Test
    public void exampleTest() throws ParserException {
        EquationConverter ec = new EquationConverter();
        String given = "x^2 + 3.5xy + y = y^2 - xy + y";
        EquationParser unitUnderTest = new EquationParser();
        Equation equation = unitUnderTest.parse(given);
        assertEquals(given, equation.toString());
        Equation canonical = ec.convertToCanonicalForm(equation);
        canonical.toString();
    }

    @Test
    public void elementaryTest() throws ParserException {
        EquationConverter ec = new EquationConverter();
        String given = "x ^ 2 = y ^ 2 + x^2";
        EquationParser unitUnderTest = new EquationParser();
        Equation equation = unitUnderTest.parse(given);
        Equation canonical = ec.convertToCanonicalForm(equation);
        assertTrue(canonical.getLeftPart().get(0).getVariables().contains(new Element("y", 2)));
        assertEquals(1.0, canonical.getLeftPart().get(0).getConstant().getCoefficient(), 0.0001);
        assertEquals(0, canonical.getRightPart().get(0).getConstant().getCoefficient(), 0.0001);
    }

    @Test
    public void xEqualsZeroTest() throws ParserException {
        EquationConverter ec = new EquationConverter();
        String given = "2 = 2 - x";
        EquationParser unitUnderTest = new EquationParser();
        Equation equation = unitUnderTest.parse(given);
        Equation canonical = ec.convertToCanonicalForm(equation);
        assertTrue(canonical.getLeftPart().get(0).getVariables().contains(new Element("x")));
        assertEquals(1.0, canonical.getLeftPart().get(0).getConstant().getCoefficient(), 0.0001);
        assertEquals(0, canonical.getRightPart().get(0).getConstant().getCoefficient(), 0.0001);
    }

    @Test
    public void wTest() throws ParserException {
        EquationConverter ec = new EquationConverter();
        String given = "-4 = 5";
        EquationParser unitUnderTest = new EquationParser();
        Equation equation = unitUnderTest.parse(given);
        Equation canonical = ec.convertToCanonicalForm(equation);
        assertTrue(canonical.getLeftPart().get(0).getVariables().contains(new Element("x")));
        assertEquals(1.0, canonical.getLeftPart().get(0).getConstant().getCoefficient(), 0.0001);
        assertEquals(0, canonical.getRightPart().get(0).getConstant().getCoefficient(), 0.0001);
    }
}
