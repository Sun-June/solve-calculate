package info.sunjune.solve.calculation.operator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CompareOperatorTest {

    CompareOperator greater = CompareOperator.GREATER_THAN;
    CompareOperator greaterEqual = CompareOperator.GREATER_EQUAL_THAN;
    CompareOperator less = CompareOperator.LESS_THAN;
    CompareOperator lessEqual = CompareOperator.LESS_EQUAL_THAN;

    CompareOperator equal = CompareOperator.EQUAL;

    CompareOperator notEqual = CompareOperator.NOT_EQUAL;

    @Test
    void calculation_should_be_work() throws Exception {
        assertEquals(greater.calculation(3, 1, null), true);
        assertEquals(greater.calculation("3", "1", null), true);
        assertEquals(greater.calculation(1, "3", null), false);

        assertEquals(greaterEqual.calculation(3, 1, null), true);
        assertEquals(greaterEqual.calculation("3", "1", null), true);
        assertEquals(greaterEqual.calculation(1, "1", null), true);

        assertEquals(less.calculation(3, 1, null), false);
        assertEquals(less.calculation("3", "1", null), false);
        assertEquals(less.calculation(1, "3", null), true);

        assertEquals(lessEqual.calculation(3, 1, null), false);
        assertEquals(lessEqual.calculation("3", "1", null), false);
        assertEquals(lessEqual.calculation(1, "1", null), true);

        assertEquals(equal.calculation(3, 1, null), false);
        assertEquals(equal.calculation(3, 3d, null), true);
        assertEquals(equal.calculation("3", "3d", null), false);
        assertEquals(equal.calculation("3", "3", null), true);

        assertEquals(notEqual.calculation(3, 1, null), true);
        assertEquals(notEqual.calculation(3, 3d, null), false);
        assertEquals(notEqual.calculation("3", "3d", null), true);
        assertEquals(notEqual.calculation("3", "3", null), false);
    }

}
