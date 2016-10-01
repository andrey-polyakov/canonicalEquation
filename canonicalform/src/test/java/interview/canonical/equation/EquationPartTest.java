package interview.canonical.equation;

import interview.canonical.equation.evaluator.EquationPart;
import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class EquationPartTest {

    @Test
    public void twoNegativesYieldNothingTest() {
        EquationPart one = new EquationPart(-0.1);
        assertNull(one.negateAndMergeWith(one));
    }

    @Test
    public void twoPositivesYieldNothingTest() {
        EquationPart one = new EquationPart(1.0);
        assertNull(one.negateAndMergeWith(one));
    }

    @Test
    public void productTest() {
        EquationPart one = new EquationPart(1.0);
        EquationPart two = new EquationPart(2.0);
        EquationPart product = one.negateAndMergeWith(two);
        assertEquals(1.0, product.getConstant().getCoefficient(), 0.001);
    }

    @Test
    public void product2Test() {
        EquationPart one = new EquationPart(1.0);
        EquationPart two = new EquationPart(2.0);
        EquationPart product = two.negateAndMergeWith(one);
        assertEquals(-1.0, product.getConstant().getCoefficient(), 0.001);
    }

    @Test
    public void negativeProductTest() {
        EquationPart one = new EquationPart(0.9);
        EquationPart two = new EquationPart(2.111);
        EquationPart product = two.negateAndMergeWith(one);
        assertFalse(product.getConstant().getCoefficient() > 0);
        assertEquals(-1.211, product.getConstant().getCoefficient(), 0.001);
    }

}
