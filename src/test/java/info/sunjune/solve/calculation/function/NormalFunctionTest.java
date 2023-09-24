package info.sunjune.solve.calculation.function;

import com.google.common.collect.Lists;
import info.sunjune.solve.calculation.error.CalculationException;
import info.sunjune.solve.calculation.error.StandardError;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class NormalFunctionTest {

    @Test
    void if_should_be_work() throws Exception {
        assertEquals(NormalFunction.IF.calculation(Lists.newArrayList(3, 10, 11), null), 10);
        assertEquals(NormalFunction.IF.calculation(Lists.newArrayList(false, 10, 11), null), 11);

        CalculationException ex = assertThrows(CalculationException.class, () -> NumberFunction.MAX.calculation(Lists.newArrayList(), null));
        assertEquals(ex.getErrorInfo(), StandardError.INCORRECT_NUMBER_PARAMETERS);
    }

}
