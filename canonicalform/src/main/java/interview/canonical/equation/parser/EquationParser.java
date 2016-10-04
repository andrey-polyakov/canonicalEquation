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

    public static final char PLUS_TOKEN = '+';
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
        if (equationParts[0].isEmpty() ^ equationParts[1].isEmpty()) {
            throw new ParserException("At least one part of the equation is empty.");
        }
        List<EquationPart> leftPart = parseSequence(equationParts[0]);
        List<EquationPart> rightPart = parseSequence(equationParts[1]);
        return new Equation(leftPart, rightPart);
    }

    enum AutomataExpectation {
        factorSign,
        factorDigit,
        floatingPoint,
        mantissaDigit,
        variable,
        powerToken,
        powerSign,
        powerDigit;
    }

    private static Set<AutomataExpectation> defaultExpectations = unmodifiableSet(of(factorSign, factorDigit, variable));
    private Deque<String> variableNames = new LinkedList<>();
    private Map<String, Long> variablePowers = new HashMap<>();
    private Set<AutomataExpectation> expectations = copyOf(defaultExpectations);
    private double factor = 1;
    private int numberStartIndex = -1, numberEndIndex = -1, powerStartIndex = -1;

    public List<EquationPart> parseSequence(String part) throws ParserException {
        reset();
        factor = 1;
        List<EquationPart> result = new LinkedList<>();
        for (int index = 0; index < part.length(); index++) {
            char token = part.charAt(index);
            if (token == MINUS_SIGN) {
                if (numberStartIndex > -1 && numberEndIndex == -1) {
                    numberEndIndex = index;
                }
                if (expectations.contains(factorSign)) {
                    if (haveUnprocessedItems()) {
                        result.add(createEquationPart(part, index));
                    }
                    factor *= -1;
                    continue;
                }
                if (expectations.contains(powerSign)) {
                    powerStartIndex = index;
                    continue;
                }
                continue;
            }
            if (token == PLUS_TOKEN) {
                if (numberStartIndex > -1 && numberEndIndex == -1) {
                    numberEndIndex = index;
                }
                if (expectations.contains(factorSign)) {
                    if (haveUnprocessedItems()) {
                        result.add(createEquationPart(part, index));
                    }
                    continue;
                }
                if (expectations.contains(powerSign)) {
                    powerStartIndex = index;
                    continue;
                }
                continue;
            }
            if (token == POWER_SIGN) {
                if (expectations.contains(powerToken)) {
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
                if (expectations.contains(factorDigit)) {
                    if (numberStartIndex == -1) {
                        numberStartIndex = index;
                    } else {
                        continue;
                    }
                    expectations.clear();
                    expectations.add(factorDigit);
                    expectations.add(floatingPoint);
                    expectations.add(factorSign);
                    expectations.add(variable);
                    continue;
                }
                if (expectations.contains(mantissaDigit)) {
                    expectations.clear();
                    expectations.add(mantissaDigit);
                    expectations.add(variable);
                    expectations.add(factorSign);
                    continue;
                }
                if (expectations.contains(powerDigit)) {
                    if (powerStartIndex == -1) {
                        powerStartIndex = index;
                    }
                    expectations.clear();
                    expectations.add(powerDigit);
                    expectations.add(variable);
                    expectations.add(factorSign);
                    continue;
                }
            }
            if (Character.isLetter(token)) {
                if (expectations.contains(variable)) {
                    if (expectations.contains(powerDigit)) {
                        assignPower(part, index);
                    }
                    expectations.clear();
                    expectations.add(powerToken);
                    expectations.add(variable);
                    expectations.add(factorSign);
                    expectations.add(factorDigit);
                    if (numberStartIndex > -1 && numberEndIndex == -1) {
                        numberEndIndex = index;
                    }
                    variableNames.add(String.valueOf(token));
                    assignPower(part, index);
                    continue;
                }
            }
            throw new ParserException("Unexpected token in the expression: " + token +
                    " at index " + index +
                    ", expected instead: " + expectations);
        }
        assignPower(part, part.length());
        if (numberStartIndex > -1) {
            if (numberEndIndex == -1) {
                numberEndIndex = part.length();
            }
            parseFactor(part.substring(numberStartIndex, numberEndIndex));
        }
        result.add(new EquationPart(new Element(factor), new HashSet<>(createElements())));
        return result;
    }

    private boolean haveUnprocessedItems() {
        return numberStartIndex > -1 || !variableNames.isEmpty();
    }

    private EquationPart createEquationPart(String part, int index) throws ParserException {
        if (expectations.contains(powerDigit)) {
            assignPower(part, index);
        }
        if (numberStartIndex > -1) {
            parseFactor(part.substring(numberStartIndex, numberEndIndex));
        }
        EquationPart eq = new EquationPart(new Element(factor), createElements());
        reset();
        return eq;
    }

    private void parseFactor(String part) throws ParserException {
        String[] chunks = part.split("\\.");
        if (chunks.length == 2 && chunks[1].length() > 16) {
            throw new ParserException("Only 11 bits of exponent supported. Please shring floating point of " + part);
        }
        factor *= Double.parseDouble(part);
    }

    private void assignPower(String part, int index) throws ParserException {
        Long power = null;
        if (powerStartIndex > -1) {
            try {
                power = Long.parseLong(part.substring(powerStartIndex, index));
            } catch (NumberFormatException nfe) {
                throw new ParserException("Failed to parser power at position " + powerStartIndex + "-"+ index);
            }
            powerStartIndex = -1;
        }
        Long existingPower = variablePowers.get(variableNames.peekLast());
        if (power == null && existingPower != null) {
            power = existingPower;
        } else if (power != null && existingPower != null) {
            power = power + existingPower;
        }
        variablePowers.put(variableNames.peekLast(), power);
    }

    private void reset() {
        factor = 1;
        variableNames.clear();
        variablePowers.clear();
        expectations = copyOf(defaultExpectations);
        numberStartIndex = -1;
        numberEndIndex = -1;
        powerStartIndex = -1;
    }

    private Set<Element> createElements() {
        Set<Element> elements = new HashSet<>();
        while (!variableNames.isEmpty()) {
            String name = variableNames.removeFirst();
            elements.add(new Element(name, variablePowers.get(name)));
        }
        return elements;
    }
}
