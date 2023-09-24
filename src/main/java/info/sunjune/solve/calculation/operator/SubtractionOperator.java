package info.sunjune.solve.calculation.operator;

import info.sunjune.solve.calculation.define.Context;
import info.sunjune.solve.calculation.define.Operator;
import info.sunjune.solve.calculation.error.CalculationException;
import info.sunjune.solve.calculation.error.StandardError;
import info.sunjune.solve.calculation.util.ValueUtil;

import java.math.BigDecimal;

/**
 * Subtraction Definition: -
 * <br>
 * ------------------------------------------------------------------
 * <br>
 * 减运算定义：-
 * <br>
 * ------------------------------------------------------------------
 * <br>
 * 減算の定義：-
 */
public class SubtractionOperator implements Operator<Number> {

    /**
     * Subtraction Definition: -
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 减运算定义：-
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 減算の定義：-
     */
    public static final SubtractionOperator SUBTRACTION = new SubtractionOperator();

    private SubtractionOperator() {
    }

    @Override
    public String symbol() {
        return "-";
    }

    @Override
    public int precedence() {
        return 1;
    }

    @Override
    public Number calculation(Object leftValue, Object rightValue, Context context) throws CalculationException {
        if (!(leftValue instanceof Number) || !(rightValue instanceof Number)) {
            throw new CalculationException(StandardError.PARAMETERS_MUST_BE_NUMERIC);
        }

        Number num1 = (Number) leftValue;
        Number num2 = (Number) rightValue;
        BigDecimal result = BigDecimal.valueOf(num1.doubleValue()).subtract(BigDecimal.valueOf(num2.doubleValue()));
        return ValueUtil.getReasonableNumber(result);
    }
}
