package info.sunjune.solve.calculation.operator;

import info.sunjune.solve.calculation.define.Context;
import info.sunjune.solve.calculation.define.Operator;
import info.sunjune.solve.calculation.error.CalculationException;
import info.sunjune.solve.calculation.error.StandardError;
import info.sunjune.solve.calculation.util.ValueUtil;

import java.math.BigDecimal;

/**
 * Definition of Exponentiation: ^
 * <br>
 * ------------------------------------------------------------------
 * <br>
 * 指数运算定义: ^
 * <br>
 * ------------------------------------------------------------------
 * <br>
 * 指数演算の定義: ^
 */
public class ExponentOperator implements Operator<Number> {

    /**
     * Definition of Exponentiation: ^
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 指数运算定义: ^
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 指数演算の定義: ^
     */
    public static final ExponentOperator EXPONENT = new ExponentOperator();

    private ExponentOperator() {
    }

    @Override
    public String symbol() {
        return "^";
    }

    @Override
    public int precedence() {
        return 3;
    }

    @Override
    public Number calculation(Object leftValue, Object rightValue, Context<Number> context) throws CalculationException {
        if (!(leftValue instanceof Number) || !(rightValue instanceof Number)) {
            throw new CalculationException(StandardError.PARAMETERS_MUST_BE_NUMERIC);
        }

        Number num1 = (Number) leftValue;
        Number num2 = (Number) rightValue;

        if (num2.intValue() > 999999999) {
            throw new CalculationException(StandardError.EXPECTED_MAXIMUM_VALUE);
        }

        BigDecimal result = BigDecimal.valueOf(num1.doubleValue()).pow(num2.intValue());

        return ValueUtil.getReasonableNumber(result);
    }
}
