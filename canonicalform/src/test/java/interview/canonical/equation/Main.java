package interview.canonical.equation;

import interview.canonical.equation.evaluator.EquationConverter;
import interview.canonical.equation.parser.EquationParser;
import interview.canonical.equation.parser.exception.ParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Андрей on 01.10.2016.
 */
public class Main {

    public static void main(String[] argv) throws ParserException, IOException {
        EquationParser parser = new EquationParser();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String s = br.readLine();
        System.out.println(new EquationConverter().convertToCanonicalForm(parser.parse(s)).toString());
    }
}
