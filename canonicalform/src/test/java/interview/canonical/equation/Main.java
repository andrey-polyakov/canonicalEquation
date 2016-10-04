package interview.canonical.equation;

import interview.canonical.equation.evaluator.EquationConverter;
import interview.canonical.equation.parser.EquationParser;
import interview.canonical.equation.parser.exception.ParserException;

import java.io.*;

/**
 * Launcher class.
 */
public class Main {

    public static void main(String[] argv) throws ParserException, IOException {
        EquationParser parser = new EquationParser();
        if (argv.length != 0) {
            for (String file : argv) {
                try (InputStream fileStream = new FileInputStream(file)) {
                    try(Reader reader = new InputStreamReader(fileStream)) {
                        try (BufferedReader br = new BufferedReader(reader)) {
                            String s = br.readLine();
                            while (s != null) {
                                try {
                                    System.out.println(new EquationConverter().convertToCanonicalForm(parser.parse(s)).toString());
                                } catch (ParserException pe) {
                                    System.err.println(pe.getLocalizedMessage());
                                }
                                s = br.readLine();
                            }
                        }
                    }
                }
            }
            System.exit(0);
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            String s = br.readLine();
            System.out.println(new EquationConverter().convertToCanonicalForm(parser.parse(s)).toString());
        } catch (ParserException pe) {
            System.err.println(pe.getLocalizedMessage());
        }
    }
}
