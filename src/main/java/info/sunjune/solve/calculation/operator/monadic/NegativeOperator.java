package info.sunjune.solve.calculation.operator.monadic;

import info.sunjune.solve.calculation.define.Context;
import info.sunjune.solve.calculation.define.MonadicOperator;
import info.sunjune.solve.calculation.error.CalculationException;
import info.sunjune.solve.calculation.error.StandardError;
import info.sunjune.solve.calculation.util.ValueUtil;

import java.math.BigDecimal;

/**
 * Negative Operator: -
 * <br>
 * ------------------------------------------------------------------
 * <br>
 * 负数运算符: -
 * <br>
 * ------------------------------------------------------------------
 * <br>
 * 負数演算子：-
 */
public class NegativeOperator implements MonadicOperator<Number> {

    /**
     * Negative Operator: -
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 负数运算符: -
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 負数演算子：-
     */
    public static final NegativeOperator NEGATIVE = new NegativeOperator();

    private NegativeOperator() {
    }

    @Override
    public String symbol() {
        return "-";
    }

    @Override
    public boolean right() {
        return true;
    }

    @Override
    public Number calculation(Object value, Context context) throws CalculationException {
        if (!(value instanceof Number)) {
            throw new CalculationException(StandardError.PARAMETERS_MUST_BE_NUMERIC);
        }
        Number num = (Number) value;
        BigDecimal result = BigDecimal.valueOf(num.doubleValue()).negate();
        return ValueUtil.getReasonableNumber(result);
    }
}
