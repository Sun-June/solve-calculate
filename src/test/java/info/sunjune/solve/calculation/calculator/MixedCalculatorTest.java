package info.sunjune.solve.calculation.calculator;

import com.google.gson.Gson;
import info.sunjune.solve.calculation.calculator.context.CalculationRecord;
import info.sunjune.solve.calculation.define.Context;
import info.sunjune.solve.calculation.util.BothValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MixedCalculatorTest {

    @Test
    void baseTest() throws Exception {
        MixedCalculator calculator = new MixedCalculator();

        assertEquals(calculator.calculation("1 + 1"), 2);
        assertEquals(calculator.calculation("-1 + -100 + 11 * 10"), 9);
        assertEquals(calculator.calculation("-1 + -100 + 11 * 10 - 10 * 2 + sum(2, 3)"), -6);
        assertEquals(calculator.calculation("-1 + -100 + 11 * 10  + sum(1, 2, 5 * 2, min(5, 6, avg(8, 9 / 3 , 10 + 2 + (5 - 3))))"), 27);
        assertEquals(calculator.calculation("-1 + -100 + 11 * 10 / 2 + 5 / 2"), -43.5d);
        assertEquals(calculator.calculation("1 + round(3.15 * 2.45, 2, \"ROUND_UP\")"), 8.72d);
        assertEquals(calculator.calculation("1 + round(3.15 * 2.45, 2, \"ROUND_DOWN\")"), 8.71d);

        assertEquals(calculator.calculation("1 + 2 ^ 3 / 2 + 1"), 6);
        assertEquals(calculator.calculation("2 + 5 % 2000‰ + 1"), 4);

        assertEquals(calculator.calculation("-1 + -100 + 11 * 10 + \"abc\""), "9abc");
        assertEquals(calculator.calculation("if(1 * 10 > 5, 10, \"abc\") + 2"), 12);

        assertEquals(calculator.calculation("if(1 * 10 < 5, 10, \"abc\") + 2"), "abc2");
        assertEquals(calculator.calculation("if(1 * 10 <= 5 * 2, 10, \"abc\") + 2"), 12);
        assertEquals(this.calculation(calculator, "if(4 * 2.5 <= 5 * 2 && 10 < 3, 10, \"abc\") + 2"), "abc2");
        assertEquals(calculator.calculation("if(1 * 10 <= 5 * 2 || 10 < 3, 10, \"abc\") + 2"), 12);
        assertEquals(calculator.calculation("if(1 * 10 <= 5 * 2 && 1 == 1, 10, \"abc\") + 2"), 12);
    }

    private Object calculation(MixedCalculator calculator, String formula) throws Exception {
        BothValue<Object, Context<Object>> bothValue = calculator.calculationBoth(formula);
        System.out.println("the formula:" + formula);
        Gson gson = new Gson();
        for (CalculationRecord record : bothValue.getRight().recordList) {
            System.out.println("record:" + gson.toJson(record));
        }
        return bothValue.getLeft();
    }

}
