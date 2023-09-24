package info.sunjune.solve.calculation.operator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RelationalOperatorTest {

    RelationalOperator and = RelationalOperator.AND;
    RelationalOperator or = RelationalOperator.OR;

    @Test
    void calculation_should_be_work() throws Exception {
        assertEquals(and.calculation(3, 1, null), true);
        assertEquals(and.calculation(3, -1, null), false);
        assertEquals(and.calculation("3", "1", null), false);
        assertEquals(and.calculation(true, false, null), false);

        assertEquals(or.calculation("3", 1, null), true);
        assertEquals(or.calculation("3", "1", null), false);
        assertEquals(or.calculation(false, true, null), true);
        assertEquals(or.calculation(false, false, null), false);

    }

}
