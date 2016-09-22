package interview.canonical.equation;

import interview.canonical.equation.evaluator.Equation;
import interview.canonical.equation.parser.EquationParser;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Андрей on 22.09.2016.
 */
public class EquationParserTest {

    @Test
    public void givenExample() {
        String given = "";
        EquationParser unitUnderTest = new EquationParser();
        Equation result = unitUnderTest.parse(given);
        assertEquals("x^2 + 3.5xy + y = y^2 - xy + y", result);
    }
}
