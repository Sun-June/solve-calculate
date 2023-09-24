package info.sunjune.solve.calculation.operator;

import info.sunjune.solve.calculation.error.CalculationException;
import info.sunjune.solve.calculation.error.StandardError;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ModuloOperatorTest {

    ModuloOperator modulo = ModuloOperator.MODULO;

    @Test
    void calculation_should_be_work() throws Exception {
        assertEquals(modulo.calculation(3, 1, null), 0);
        assertEquals(modulo.calculation(3, 2, null), 1);
        assertEquals(modulo.calculation(6.7, 2.5, null), 1.7d);

        CalculationException ex = assertThrows(CalculationException.class, () -> modulo.calculation(3, "1", null));
        assertEquals(ex.getErrorInfo(), StandardError.PARAMETERS_MUST_BE_NUMERIC);
    }

}
