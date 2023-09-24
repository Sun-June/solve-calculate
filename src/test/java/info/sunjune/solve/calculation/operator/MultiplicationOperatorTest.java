package info.sunjune.solve.calculation.operator;

import info.sunjune.solve.calculation.error.CalculationException;
import info.sunjune.solve.calculation.error.StandardError;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MultiplicationOperatorTest {

    MultiplicationOperator multiplication = MultiplicationOperator.MULTIPLICATION;

    @Test
    void calculation_should_be_work() throws Exception {

        assertEquals(multiplication.calculation(1, 3, null), 3);
        assertEquals(multiplication.calculation(1.1, 3, null), 3.3d);
        assertEquals(multiplication.calculation(1.1000000000000000000000000000000000001, 3, null), 3.3000000000000000000000000000000000003);
        assertEquals(multiplication.calculation(1.1000000000000000000000000000000000001, 1.000000000000000000000000000000000001, null), 1.1d);

        CalculationException ex = assertThrows(CalculationException.class, () -> multiplication.calculation(3, "1", null));
        assertEquals(ex.getErrorInfo(), StandardError.PARAMETERS_MUST_BE_NUMERIC);
    }

}
