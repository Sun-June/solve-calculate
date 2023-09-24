package info.sunjune.solve.calculation.operator;

import info.sunjune.solve.calculation.error.CalculationException;
import info.sunjune.solve.calculation.error.StandardError;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SubtractionOperatorTest {

    SubtractionOperator subtraction = SubtractionOperator.SUBTRACTION;

    @Test
    void calculation_should_be_work() throws Exception {
        assertEquals(subtraction.calculation(3, 1, null), 2);
        assertEquals(subtraction.calculation(3, 1.1, null), 1.9d);
        assertEquals(subtraction.calculation(Integer.MAX_VALUE, -1, null), Integer.MAX_VALUE + 1L);

        CalculationException ex = assertThrows(CalculationException.class, () -> subtraction.calculation(3, "1", null));
        assertEquals(ex.getErrorInfo(), StandardError.PARAMETERS_MUST_BE_NUMERIC);
    }

}
