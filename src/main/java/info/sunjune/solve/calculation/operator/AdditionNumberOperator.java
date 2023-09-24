package info.sunjune.solve.calculation.operator;

import info.sunjune.solve.calculation.define.Context;
import info.sunjune.solve.calculation.define.Operator;
import info.sunjune.solve.calculation.error.CalculationException;
import info.sunjune.solve.calculation.error.StandardError;
import info.sunjune.solve.calculation.util.ValueUtil;

import java.math.BigDecimal;

/**
 * Addition Operation Definition: +
 * <br>
 * ------------------------------------------------------------------
 * <br>
 * 加运算定义: +
 * <br>
 * ------------------------------------------------------------------
 * <br>
 * 加算の定義: +
 */
public class AdditionNumberOperator implements Operator<Number> {

    /**
     * Addition Operation Definition: +
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 加运算定义: +
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 加算の定義: +
     */
    public static final AdditionNumberOperator ADDITION = new AdditionNumberOperator();

    private AdditionNumberOperator() {
    }

    @Override
    public String symbol() {
        return "+";
    }

    @Override
    public int precedence() {
        return 1;
    }

    @Override
    public Number calculation(Object leftValue, Object rightValue, Context<Number> context) throws CalculationException {
        if (leftValue instanceof Number && rightValue instanceof Number) {
            Number num1 = (Number) leftValue;
            Number num2 = (Number) rightValue;
            BigDecimal result = BigDecimal.valueOf(num1.doubleValue()).add(BigDecimal.valueOf(num2.doubleValue()));
            return ValueUtil.getReasonableNumber(result);
        } else {
            throw new CalculationException(StandardError.PARAMETERS_MUST_BE_NUMERIC);
        }
    }
}
