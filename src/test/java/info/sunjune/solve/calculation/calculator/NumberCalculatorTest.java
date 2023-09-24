package info.sunjune.solve.calculation.calculator;

import com.google.gson.Gson;
import info.sunjune.solve.calculation.calculator.context.CalculationRecord;
import info.sunjune.solve.calculation.define.Context;
import info.sunjune.solve.calculation.error.ErrorInfo;
import info.sunjune.solve.calculation.error.FormulaError;
import info.sunjune.solve.calculation.error.FormulaException;
import info.sunjune.solve.calculation.util.BothValue;
import org.junit.jupiter.api.Test;

import static info.sunjune.solve.calculation.error.FormulaError.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class NumberCalculatorTest {

    @Test
    void base_test() throws Exception {
        NumberCalculator calculator = new NumberCalculator();

        assertEquals(calculator.calculation("1 + 1"), 2);
        assertEquals(calculator.calculation("-1 + -100 + 11 * 10"), 9);
        assertEquals(calculator.calculation("-1 + -100 + 11 * 10 - 10 * 2 + sum(2, 3)"), -6);
        assertEquals(calculator.calculation("-1 + -100 + 11 * 10  + sum(1, 2, 5 * 2, min(5, 6, avg(8, 9 / 3 , 10 + 2 + (5 - 3))))"), 27);
        assertEquals(calculator.calculation("-1 + -100 + 11 * 10 / 2 + 5 / 2"), -43.5d);
        assertEquals(calculator.calculation("1 + round(3.15 * 2.45, 2, \"ROUND_UP\")"), 8.72d);
        assertEquals(calculator.calculation("1 + round(3.15 * 2.45, 2, \"ROUND_DOWN\")"), 8.71d);

        assertEquals(calculator.calculation("1 + 2 ^ 3 / 2 + 1"), 6);
        assertEquals(calculator.calculation("2 + 5 % 2000‰ + 1"), 4);

    }

    @Test
    void record_test() throws Exception {
        NumberCalculator calculator = new NumberCalculator();

        BothValue<Number, Context<Number>> bothValue = calculator.calculationBoth("-1 + -100 + 11 * 10  + sum(1, 2, 5 * 2, min(5, 6, avg(8, 9 / 3 , 10 + 2 + (5 - 3))))");

        assertEquals(bothValue.getLeft(), 27);
        Gson gson = new Gson();
        for (CalculationRecord record : bothValue.getRight().recordList) {
            System.out.println("record::" + gson.toJson(record));
        }
    }


    @Test
    void errorCheck() {
        NumberCalculator calculator = new NumberCalculator();

        String input = "π + sum(10, min(, 10)) - 10";

        FormulaException ex = assertThrows(FormulaException.class, () -> calculator.calculation(input));
        assertEquals(ARGUMENT_MISSING, ex.error);
        assertEquals(",", input.substring(ex.startIndex, ex.endIndex));

        // OPEN_BRACKET
        this.formulaError("π (", "(", INVALID_BRACKET_IN_EXPRESSION);
        this.formulaError("sum(1, 2)(", "(", INVALID_BRACKET_IN_EXPRESSION);

        // CLOSE_BRACKET
        this.formulaError("() + 1", ")", INVALID_BRACKET_POINTLESS);
        this.formulaError("( ) + 1", ")", INVALID_BRACKET_POINTLESS);
        this.formulaError(") 1+1", ")", START_WITH_CLOSE_BRACKET);
        this.formulaError("sum(10,)", ")", ARGUMENT_MISSING);
        this.formulaError("(10+)", ")", ARGUMENT_MISSING);
        this.formulaError("100)", ")", INVALID_PARENTHESIS_MATCH_OPEN_BRACKET);
        this.formulaError("sum(1, 2))", ")", INVALID_PARENTHESIS_MATCH_OPEN_BRACKET);
        this.formulaError("(1 + 2))", ")", INVALID_PARENTHESIS_MATCH_OPEN_BRACKET);

        // FUNCTION_SEPARATOR
        this.formulaError(",100", ",", CAN_NOT_START_WITH_ARGUMENT_SEPARATOR);
        this.formulaError("sum(,", ",", ARGUMENT_MISSING);
        this.formulaError("sum(100,,)", ",", ARGUMENT_MISSING);

        // FUNCTION
        this.formulaError("π max(100)", "max", FUNCTION_MISSING);
        this.formulaError("max(1, 2)min(2, 3)", "min", FUNCTION_MISSING);

        // OPERATOR
        this.formulaError("1 ++ 1", "+", OPERATOR_MISSING);
        this.formulaError("1 * +1", "+", OPERATOR_MISSING);
        this.formulaError("1 * /1", "/", OPERATOR_MISSING);
        this.formulaError("/1", "/", OPERATOR_MISSING);
        this.formulaError("+1", "+", OPERATOR_MISSING);

        // LITERAL
        this.formulaError("sum(1, 2)π", "π", LITERAL_MISSING);


    }

    void formulaError(String formula, String errorStr, ErrorInfo error) {
        NumberCalculator calculator = new NumberCalculator();
        FormulaException ex = assertThrows(FormulaException.class, () -> calculator.calculation(formula));
        assertEquals(error, ex.error);
        assertEquals(errorStr, formula.substring(ex.startIndex, ex.endIndex));
    }

}
