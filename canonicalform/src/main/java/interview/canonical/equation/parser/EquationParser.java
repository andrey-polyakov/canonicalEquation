package interview.canonical.equation.parser;

import interview.canonical.equation.evaluator.Equation;
import interview.canonical.equation.evaluator.EquationChunk;
import interview.canonical.equation.parser.exception.ParserException;

import java.util.*;

import static interview.canonical.equation.parser.EquationParser.AutomataExpectation.*;
import static java.util.Collections.unmodifiableSet;
import static java.util.EnumSet.*;

/**
 * Created by Андрей on 22.09.2016.
 */
public class EquationParser {

    public static final char PLUS_SIGN = '+';
    public static final char MINUS_SIGN = '-';
    public static final char POWER_SIGN = '^';
    public static final char POINT_SIGN = '.';

    public static String homogenize(String input) {
        return input.replaceAll(" ", "");
    }

    public Equation parse(String raw) throws ParserException {
        String cleansed = homogenize(raw);
        String[] equationParts = cleansed.split("=");
        if (equationParts.length != 2) {
            throw new ParserException("Equation must be comprised of two parts only separated by equality integerSign, e.g 'x = 0'");
        }
        if (equationParts[0].equals("")) {
            throw new ParserException("Left part from equal integerSign is missing.");
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

    enum AutomataExpectation {
        integerSign,
        integerDigit,
        floatingPoint,
        mantissaDigit,
        variable,
        powerSign,
        power,
        powerDigit;
    }

    private static Set<AutomataExpectation> endings = unmodifiableSet(of(mantissaDigit, powerDigit));

    private EquationChunk parsePart(String part) throws ParserException {
        EquationChunk chunk = new EquationChunk();
        int numberStartIndex = -1, numberEndIndex = 0;
        int powerStartIndex = -1;
        boolean powerSignIsNegative = false;
        Set<AutomataExpectation> expectations = allOf(AutomataExpectation.class);
        expectations.remove(endings);
        expectations.remove(powerSign);
        expectations.remove(powerDigit);
        expectations.remove(power);
        expectations.remove(floatingPoint);
        for (int index = 0; index < part.length(); index++) {
            char token = part.charAt(index);
            if (token == MINUS_SIGN) {
                if (expectations.contains(integerSign)) {
                    chunk.negate();
                    continue;
                }
                if (expectations.contains(powerSign)) {
                    powerSignIsNegative = !powerSignIsNegative;
                    continue;
                }
            }
            if (token == PLUS_SIGN) {
                if (expectations.contains(powerSign) || expectations.contains(integerSign)) {
                    continue;
                }
            }
            if (token == POWER_SIGN) {
                if (expectations.contains(powerSign)) {
                    expectations.clear();
                    expectations.add(powerDigit);
                    expectations.add(powerSign);
                    continue;
                }
            }
            if (token == POINT_SIGN) {
                if (expectations.contains(floatingPoint)) {
                    expectations.clear();
                    expectations.add(mantissaDigit);
                    continue;
                }
            }
            if (Character.isDigit(token)) {
                if (expectations.contains(integerDigit)) {
                    if (numberEndIndex == -1) {
                        numberStartIndex = index;
                    }
                    expectations.clear();
                    expectations.add(integerDigit);
                    expectations.add(floatingPoint);
                    continue;
                }
                if (expectations.contains(mantissaDigit)) {
                    expectations.clear();
                    expectations.add(mantissaDigit);
                    expectations.add(powerSign);
                    expectations.add(variable);
                    continue;
                }
                if (expectations.contains(powerDigit)) {
                    if (powerStartIndex == -1) {
                        powerStartIndex = index;
                    }
                    expectations.clear();
                    expectations.add(mantissaDigit);
                    expectations.add(powerSign);
                    expectations.add(variable);
                    continue;
                }
            }
            if (Character.isLetter(token)) {
                if (expectations.contains(variable)) {
                    if (numberEndIndex > -1) {
                        numberEndIndex = index - 1;
                    }
                    expectations.clear();
                    expectations.add(powerSign);
                    expectations.add(variable);
                    chunk.addVariable(token);
                    continue;
                }
            }
            throw new ParserException("Unexpected token in the expression: " + token);
        }
        int power = 1;
        if (powerStartIndex > -1) {
            power = Integer.parseInt(part.substring(powerStartIndex, part.length()));
        }
        chunk.setPowerPart(power);
        double number = 1;
        if (numberStartIndex > -1) {
            number = Integer.parseInt(part.substring(numberStartIndex, numberEndIndex));
        }
        chunk.setFloatingPointPart(number);
        return chunk;
    }
}
