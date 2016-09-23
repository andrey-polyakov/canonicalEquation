package interview.canonical.equation.parser;

import interview.canonical.equation.evaluator.Equation;
import interview.canonical.equation.evaluator.EquationChunk;
import interview.canonical.equation.parser.exception.ParserException;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Андрей on 22.09.2016.
 */
public class EquationParser {

    public static final char PLUS = '+';
    public static final char MINUS = '-';
    public static final char POWER = '^';
    public static final char POINT = '.';



    public static String homogenize(String input) {
        return input.replaceAll(" ", "");
    }

    public Equation parse(String raw) throws ParserException {
        String cleansed = homogenize(raw);
        String[] equationParts = cleansed.split("=");
        if (equationParts.length != 2) {
            throw new ParserException("Equation must be comprised of two parts only separated by equality sign, e.g 'x = 0'");
        }
        if (equationParts[0].equals("")) {
            throw new ParserException("Left part from equal sign is missing.");
        }
        List<EquationChunk> leftPart = parseSequence(equationParts[0]);
        List<EquationChunk> rightPart = parseSequence(equationParts[1]);
        return new Equation(leftPart, rightPart);
    }

    private List<EquationChunk> parseSequence(String equationPart) throws ParserException {
        String[] parts = equationPart.split("(?=\\+|\\-)");
        List<EquationChunk> sequence = new LinkedList<EquationChunk>();
        for(String part : parts) {
            sequence.add(parsePart(part));
        }
        return sequence;
    }

    private EquationChunk parsePart(String part) throws ParserException {
        EquationChunk chunk = new EquationChunk();
        List<Character> number = new LinkedList<Character>();
        for (char token : part.toCharArray()) {
            if (token == MINUS) {
                chunk.setPositive(false);
            }
            if (token == PLUS) {
                chunk.setPositive(true);
            }
            if (token == POWER) {

            }
            if (token == POINT) {

            }
            if (Character.isDigit(token)) {

            }
            if (Character.isLetter(token)) {

            }
            throw new ParserException("Unexpected token in the expression: " + token);
        }
        return null;
    }
}
