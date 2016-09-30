package interview.canonical.equation;

import interview.canonical.equation.evaluator.Equation;
import interview.canonical.equation.evaluator.EquationConverter;
import interview.canonical.equation.parser.EquationParser;
import interview.canonical.equation.parser.exception.ParserException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

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

}
