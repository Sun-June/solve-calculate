package info.sunjune.solve.calculation.operator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdditionOperatorTest {

    AdditionOperator addition = AdditionOperator.ADDITION;

    @Test
    void calculation_should_be_work() throws Exception {

        assertEquals(addition.calculation(1, 2, null), 3);
        assertEquals(addition.calculation(1.1, 2, null), 3.1d);
        assertEquals(addition.calculation(Integer.MAX_VALUE, 2, null), Integer.MAX_VALUE + 2L);
        assertEquals(addition.calculation(Integer.MIN_VALUE, -2, null), Integer.MIN_VALUE - 2L);
        assertEquals(addition.calculation(1, "2", null), "12");

    }

}
