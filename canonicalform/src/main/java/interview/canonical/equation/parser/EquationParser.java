package interview.canonical.equation.parser;

import interview.canonical.equation.evaluator.Element;
import interview.canonical.equation.evaluator.Equation;
import interview.canonical.equation.evaluator.EquationPart;
import interview.canonical.equation.parser.exception.ParserException;

import java.util.*;

import static interview.canonical.equation.parser.EquationParser.AutomataExpectation.*;
import static java.util.Collections.unmodifiableSet;
import static java.util.EnumSet.*;

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
            throw new ParserException("Equation must be comprised of two parts only separated by equality factorSign, e.g 'x = 0'");
        }
        if (equationParts[0].equals("")) {
            throw new ParserException("Left part from equal factorSign is missing.");
        }
        List<EquationPart> leftPart = parseSequence(equationParts[0]);
        List<EquationPart> rightPart = parseSequence(equationParts[1]);
        return new Equation(leftPart, rightPart);
    }

    private List<EquationPart> parseSequence(String equationPart) throws ParserException {
        String[] parts = equationPart.split("(?=\\+|\\-)");
        List<EquationPart> sequence = new LinkedList<EquationPart>();
        for(String part : parts) {
            sequence.add(parsePart(part));
        }
        return sequence;
    }

    enum AutomataExpectation {
        factorSign,
        integerDigit,
        floatingPoint,
        mantissaDigit,
        variable,
        powerSign,
        power,
        powerDigit;
    }

    private static Set<AutomataExpectation> endings = unmodifiableSet(of(mantissaDigit, powerDigit));

    public static EquationPart parsePart(String part) throws ParserException {
        int numberStartIndex = -1, numberEndIndex = -1, powerStartIndex = -1;
        boolean positive = true;
        List<String> variableNames = new ArrayList<>();
        Map<String, Integer> variablePowers = new HashMap<>();
        Set<AutomataExpectation> expectations = allOf(AutomataExpectation.class);
        expectations.remove(endings);
        expectations.remove(powerSign);
        expectations.remove(powerDigit);
        expectations.remove(power);
        expectations.remove(floatingPoint);
        expectations.remove(mantissaDigit);
        for (int index = 0; index < part.length(); index++) {
            char token = part.charAt(index);
            if (token == MINUS_SIGN) {
                if (expectations.contains(factorSign)) {
                    positive = !positive;
                    continue;
                }
                if (expectations.contains(powerSign)) {
                    powerStartIndex = index;
                    continue;
                }
            }
            if (token == PLUS_SIGN) {
                if (expectations.contains(powerSign) || expectations.contains(factorSign)) {
                    continue;
                }
            }
            if (token == POWER_SIGN) {
                if (numberStartIndex > 0 && numberEndIndex == -1) {
                    numberEndIndex = index;
                }
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
                    if (numberStartIndex == -1) {
                        numberStartIndex = index;
                    } else {
                        continue;
                    }
                    expectations.clear();
                    expectations.add(integerDigit);
                    expectations.add(floatingPoint);
                    continue;
                }
                if (expectations.contains(mantissaDigit)) {
                    expectations.clear();
                    expectations.add(mantissaDigit);
                    expectations.add(variable);
                    continue;
                }
                if (expectations.contains(powerDigit)) {
                    if (powerStartIndex == -1) {
                        powerStartIndex = index;
                    }
                    expectations.clear();
                    expectations.add(powerDigit);
                    expectations.add(variable);
                    continue;
                }
            }
            if (Character.isLetter(token)) {
                if (expectations.contains(variable)) {
                    if (numberStartIndex > -1 && numberEndIndex == -1) {
                        numberEndIndex = index;
                    }
                    int power = 1;
                    if (powerStartIndex > -1) {
                        power = Integer.parseInt(part.substring(powerStartIndex, part.length()));
                        powerStartIndex = -1;
                    }
                    expectations.clear();
                    expectations.add(powerSign);
                    expectations.add(variable);
                    variableNames.add(String.valueOf(token));
                    variablePowers.put(String.valueOf(token), power);
                    continue;
                }
            }
            throw new ParserException("Unexpected token in the expression: " + token);
        }
        int power = 1;
        if (powerStartIndex > -1) {
            power = Integer.parseInt(part.substring(powerStartIndex, part.length()));
            variablePowers.put(variableNames.get(variableNames.size() - 1), power);
        }
        double number = 1;
        if (numberStartIndex > -1) {
            if (numberEndIndex == -1) {
                numberEndIndex = part.length();
            }
            number = Double.parseDouble(part.substring(numberStartIndex, numberEndIndex));
        }
        Set<Element> elements = new HashSet<>();
        for (int ii = 0; ii < variableNames.size(); ii++) {
            elements.add(new Element(variableNames.get(ii), variablePowers.get(variableNames.get(ii))));
        }
        return new EquationPart(new Element(number), new HashSet<>(elements), positive);
    }
}
