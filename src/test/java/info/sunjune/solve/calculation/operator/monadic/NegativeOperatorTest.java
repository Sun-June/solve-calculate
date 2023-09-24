package info.sunjune.solve.calculation.operator.monadic;

import info.sunjune.solve.calculation.error.CalculationException;
import info.sunjune.solve.calculation.error.StandardError;
import info.sunjune.solve.calculation.operator.monadic.NegativeOperator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class NegativeOperatorTest {

    NegativeOperator negative = NegativeOperator.NEGATIVE;

    @Test
    void calculation_should_be_work() throws Exception {
        assertEquals(negative.calculation(3, null), -3);
        assertEquals(negative.calculation(1.1, null), -1.1d);
        assertEquals(negative.calculation(-1.1, null), 1.1d);

        CalculationException ex = assertThrows(CalculationException.class, () -> negative.calculation("1", null));
        assertEquals(ex.getErrorInfo(), StandardError.PARAMETERS_MUST_BE_NUMERIC);
    }

}
