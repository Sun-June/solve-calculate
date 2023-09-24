package info.sunjune.solve.calculation.operator.monadic;

import info.sunjune.solve.calculation.define.Context;
import info.sunjune.solve.calculation.define.MonadicOperator;
import info.sunjune.solve.calculation.error.CalculationException;
import info.sunjune.solve.calculation.error.StandardError;
import info.sunjune.solve.calculation.util.ValueUtil;

import java.math.BigDecimal;

/**
 * Per mille (‰) operator. The primary purpose is to demonstrate the usage of the left-hand monadic operator. Example: 101‰ = 0.101.
 * <br>
 * ------------------------------------------------------------------
 * <br>
 * 千分运算符: ‰。主要目的为了展示左侧单体运算符的使用，例： 101‰ = 0.101
 * <br>
 * ------------------------------------------------------------------
 * <br>
 * パーミル（‰）演算子。主な目的は左側の単位演算子の使用を示すことです。例： 101‰ = 0.101。
 */
public class ThousandthOperator implements MonadicOperator<Number> {

    /**
     * Per mille (‰) operator. The primary purpose is to demonstrate the usage of the left-hand monadic operator. Example: 101‰ = 0.101.
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 千分运算符: ‰。主要目的为了展示左侧单体运算符的使用，例： 101‰ = 0.101
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * パーミル（‰）演算子。主な目的は左側の単位演算子の使用を示すことです。例： 101‰ = 0.101。
     */
    public static final ThousandthOperator THOUSANDTH = new ThousandthOperator();

    private ThousandthOperator() {
    }

    @Override
    public String symbol() {
        return "‰";
    }

    @Override
    public boolean right() {
        return false;
    }

    @Override
    public Number calculation(Object value, Context context) throws CalculationException {
        if (!(value instanceof Number)) {
            throw new CalculationException(StandardError.PARAMETERS_MUST_BE_NUMERIC);
        }
        Number num = (Number) value;
        BigDecimal result = BigDecimal.valueOf(num.doubleValue()).divide(BigDecimal.valueOf(1000));
        return ValueUtil.getReasonableNumber(result);
    }
}
