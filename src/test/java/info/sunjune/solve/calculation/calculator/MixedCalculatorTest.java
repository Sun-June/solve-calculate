package info.sunjune.solve.calculation.calculator;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import info.sunjune.solve.calculation.calculator.context.CalculationRecord;
import info.sunjune.solve.calculation.calculator.context.MixedContext;
import info.sunjune.solve.calculation.calculator.item.Kind;
import info.sunjune.solve.calculation.define.Context;
import info.sunjune.solve.calculation.error.CalculationException;
import info.sunjune.solve.calculation.error.StandardError;
import info.sunjune.solve.calculation.util.BothValue;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

        assertEquals(calculator.calculation("toNumber(\"12\" + \"12\") + 1"), 1213);
        assertEquals(calculator.calculation("if(regularMatch(\"^\\d{2}$\", \"999\") && 1 == 1, 10, \"abc\") + 2"), "abc2");
        assertEquals(calculator.calculation("if(regularMatch(\"^\\d{3}$\", \"999\") && 1 == 1, 10, \"abc\") + 2"), 12);

        Map<String, Object> values = Maps.newHashMap();
        values.put("number1", 11.1);
        values.put("number2", 4);
        values.put("number3", 6.0);
        values.put("str", "66");
        assertEquals(calculator.calculation("number3 / 2 + if(regularMatch(\"^d{2}$\", str) && number1 >= number2, 10, \"abc\" + str) + 2", new MixedContext() {
            @Override
            public Object getCustomerLiteralValue(String literal) {
                Object value = super.getCustomerLiteralValue(literal);
                if (value == null) {
                    return values.get(literal);
                }
                return value;
            }
        }), "3abc662");
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

    @Test
    void calculationError() {
        MixedCalculator calculator = new MixedCalculator();
        String input = "100 - 50 / (2 - min(2, 2000)) + 1";
        CalculationException ex = assertThrows(CalculationException.class, () -> calculator.calculation(input));
        assertEquals(ex.getErrorInfo(), StandardError.DIVISION_BY_ZERO);
        assertEquals(ex.context.pendingItem.source, "/");
        System.out.println("the formula:" + input);
        Gson gson = new Gson();
        List<CalculationRecord> recordList = ex.context.recordList;
        for (CalculationRecord record : recordList) {
            if (record.kind != Kind.LITERAL) {
                System.out.println("record:" + gson.toJson(record));
            }
        }
    }

    @Test
    void customer_context_should_be_work() throws Exception {
        Map<String, Object> values = Maps.newHashMap();
        values.put("number1", 100);
        values.put("number2", 50);
        values.put("str", "abc");

        MixedCalculator calculator = new MixedCalculator();
        Object result = calculator.calculation("number1 - number2 + 10 + str", new MixedContext() {
            @Override
            public Object getCustomerLiteralValue(String literal) {
                Object value = super.getCustomerLiteralValue(literal);
                if (value == null) {
                    return values.get(literal);
                }
                return value;
            }
        });
        assertEquals(result, "60abc");
    }

}
