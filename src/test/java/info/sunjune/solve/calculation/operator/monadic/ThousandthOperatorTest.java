package info.sunjune.solve.calculation.operator.monadic;

import info.sunjune.solve.calculation.error.CalculationException;
import info.sunjune.solve.calculation.error.StandardError;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ThousandthOperatorTest {

    ThousandthOperator percentage = ThousandthOperator.THOUSANDTH;

    @Test
    void calculation_should_be_work() throws Exception {
        assertEquals(percentage.calculation(3, null), 0.003d);
        assertEquals(percentage.calculation(1.1, null), 0.0011d);
        assertEquals(percentage.calculation(-1.1, null), -0.0011d);

        CalculationException ex = assertThrows(CalculationException.class, () -> percentage.calculation("1", null));
        assertEquals(ex.getErrorInfo(), StandardError.PARAMETERS_MUST_BE_NUMERIC);
    }

}
