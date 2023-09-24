package info.sunjune.solve.calculation.operator;

import info.sunjune.solve.calculation.error.CalculationException;
import info.sunjune.solve.calculation.error.StandardError;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DivisionOperatorTest {

    DivisionOperator division = DivisionOperator.DIVISION;

    @Test
    void calculation_should_be_work() throws Exception {
        assertEquals(division.calculation(3, 1, null), 3);
        assertEquals(division.calculation(3, 12, null), 0.25d);
        assertEquals(division.calculation(3, 1.1, null), 3 / 1.1);

        CalculationException ex = assertThrows(CalculationException.class, () -> division.calculation(3, "1", null));
        assertEquals(ex.getErrorInfo(), StandardError.PARAMETERS_MUST_BE_NUMERIC);
    }

}
