package info.sunjune.solve.calculation.function;

import com.google.common.collect.Lists;
import info.sunjune.solve.calculation.error.CalculationException;
import info.sunjune.solve.calculation.error.StandardError;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class NumberFunctionTest {

    @Test
    void max_should_be_work() throws Exception {
        assertEquals(NumberFunction.MAX.calculation(Lists.newArrayList(3, 10), null), 10);
        assertEquals(NumberFunction.MAX.calculation(Lists.newArrayList(3.1, 2.9), null), 3.1d);

        CalculationException ex = assertThrows(CalculationException.class, () -> NumberFunction.MAX.calculation(Lists.newArrayList(), null));
        assertEquals(ex.getErrorInfo(), StandardError.INCORRECT_NUMBER_PARAMETERS);
        ex = assertThrows(CalculationException.class, () -> NumberFunction.MAX.calculation(Lists.newArrayList("1", 3), null));
        assertEquals(ex.getErrorInfo(), StandardError.PARAMETERS_MUST_BE_NUMERIC);
    }

    @Test
    void min_should_be_work() throws Exception {
        assertEquals(NumberFunction.MIN.calculation(Lists.newArrayList(3, 10), null), 3);
        assertEquals(NumberFunction.MIN.calculation(Lists.newArrayList(3.1, 2.9), null), 2.9d);

        CalculationException ex = assertThrows(CalculationException.class, () -> NumberFunction.MIN.calculation(Lists.newArrayList(), null));
        assertEquals(ex.getErrorInfo(), StandardError.INCORRECT_NUMBER_PARAMETERS);
        ex = assertThrows(CalculationException.class, () -> NumberFunction.MIN.calculation(Lists.newArrayList("1", 3), null));
        assertEquals(ex.getErrorInfo(), StandardError.PARAMETERS_MUST_BE_NUMERIC);
    }

    @Test
    void sum_should_be_work() throws Exception {
        assertEquals(NumberFunction.SUM.calculation(Lists.newArrayList(3, 10), null), 13);
        assertEquals(NumberFunction.SUM.calculation(Lists.newArrayList(3.1, 2.9), null), 6);

        CalculationException ex = assertThrows(CalculationException.class, () -> NumberFunction.SUM.calculation(Lists.newArrayList(), null));
        assertEquals(ex.getErrorInfo(), StandardError.INCORRECT_NUMBER_PARAMETERS);
        ex = assertThrows(CalculationException.class, () -> NumberFunction.SUM.calculation(Lists.newArrayList("1", 3), null));
        assertEquals(ex.getErrorInfo(), StandardError.PARAMETERS_MUST_BE_NUMERIC);
    }

    @Test
    void avg_should_be_work() throws Exception {
        assertEquals(NumberFunction.AVG.calculation(Lists.newArrayList(3, 10), null), 6.5d);
        assertEquals(NumberFunction.AVG.calculation(Lists.newArrayList(3.1, 2.9), null), 3);

        CalculationException ex = assertThrows(CalculationException.class, () -> NumberFunction.AVG.calculation(Lists.newArrayList(), null));
        assertEquals(ex.getErrorInfo(), StandardError.INCORRECT_NUMBER_PARAMETERS);
        ex = assertThrows(CalculationException.class, () -> NumberFunction.AVG.calculation(Lists.newArrayList("1", 3), null));
        assertEquals(ex.getErrorInfo(), StandardError.PARAMETERS_MUST_BE_NUMERIC);
    }

    @Test
    void round_should_be_work() throws Exception {
        assertEquals(NumberFunction.ROUND.calculation(Lists.newArrayList(3.4), null), 3);
        assertEquals(NumberFunction.ROUND.calculation(Lists.newArrayList(3.5), null), 4);
        assertEquals(NumberFunction.ROUND.calculation(Lists.newArrayList(3.424, 2), null), 3.42d);
        assertEquals(NumberFunction.ROUND.calculation(Lists.newArrayList(3.425, 2), null), 3.43d);
        assertEquals(NumberFunction.ROUND.calculation(Lists.newArrayList(3.424, 2, 1), null), 3.42d);

        CalculationException ex = assertThrows(CalculationException.class, () -> NumberFunction.ROUND.calculation(Lists.newArrayList(), null));
        assertEquals(ex.getErrorInfo(), StandardError.INCORRECT_NUMBER_PARAMETERS);
        ex = assertThrows(CalculationException.class, () -> NumberFunction.ROUND.calculation(Lists.newArrayList("1", 3), null));
        assertEquals(ex.getErrorInfo(), StandardError.PARAMETERS_MUST_BE_NUMERIC);
    }

    @Test
    void length_should_be_work() throws Exception {
        assertEquals(NumberFunction.LENGTH.calculation(Lists.newArrayList(3.4), null), 3);
        assertEquals(NumberFunction.LENGTH.calculation(Lists.newArrayList("ab"), null), 2);

        CalculationException ex = assertThrows(CalculationException.class, () -> NumberFunction.ROUND.calculation(Lists.newArrayList(), null));
        assertEquals(ex.getErrorInfo(), StandardError.INCORRECT_NUMBER_PARAMETERS);
    }

    @Test
    void random_should_be_work() throws Exception {
        assertEquals(NumberFunction.RANDOM.calculation(Lists.newArrayList(), null).getClass(), Integer.class);
    }

}
