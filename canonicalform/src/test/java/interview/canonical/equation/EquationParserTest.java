package interview.canonical.equation;

import interview.canonical.equation.evaluator.Equation;
import interview.canonical.equation.parser.EquationParser;
import interview.canonical.equation.parser.exception.ParserException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Андрей on 22.09.2016.
 */
public class EquationParserTest {

    @Test
    public void exampleGivenInTaskDescription() throws ParserException {
        String given = "x^2 + 3.5xy + y = y^2 - xy + y";
        EquationParser unitUnderTest = new EquationParser();
        Equation transformed = unitUnderTest.parse(given);
        assertEquals("x^2 - y^2 + 4.5xy = 0", transformed);
    }

    @Test(expected = ParserException.class)
    public void missingRightPart() throws ParserException {
        String given = "x^2 = ";
        EquationParser unitUnderTest = new EquationParser();
        Equation transformed = unitUnderTest.parse(given);
    }

    @Test(expected = ParserException.class)
    public void missingLeftPart() throws ParserException {
        String given = " = x ^ 2";
        EquationParser unitUnderTest = new EquationParser();
        Equation transformed = unitUnderTest.parse(given);
    }

}
