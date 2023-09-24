package info.sunjune.solve.calculation.operator;

import info.sunjune.solve.calculation.define.Context;
import info.sunjune.solve.calculation.define.Operator;
import info.sunjune.solve.calculation.error.CalculationException;
import info.sunjune.solve.calculation.error.StandardError;
import info.sunjune.solve.calculation.util.ValueUtil;

import java.math.BigDecimal;

/**
 * Definition of modulus operation: %.
 * <br>
 * ------------------------------------------------------------------
 * <br>
 * 取模运算定义：%
 * <br>
 * ------------------------------------------------------------------
 * <br>
 * モジュラス演算の定義：%
 */
public class ModuloOperator implements Operator<Number> {

    /**
     * Definition of modulus operation: %.
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 取模运算定义：%
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * モジュラス演算の定義：%
     */
    public static final ModuloOperator MODULO = new ModuloOperator();

    private ModuloOperator() {
    }

    @Override
    public String symbol() {
        return "%";
    }

    @Override
    public int precedence() {
        return 2;
    }

    @Override
    public Number calculation(Object leftValue, Object rightValue, Context<Number> context) throws CalculationException {

        if (!(leftValue instanceof Number) || !(rightValue instanceof Number)) {
            throw new CalculationException(StandardError.PARAMETERS_MUST_BE_NUMERIC);
        }

        Number num1 = (Number) leftValue;
        Number num2 = (Number) rightValue;

        BigDecimal[] result = BigDecimal.valueOf(num1.doubleValue()).divideAndRemainder(BigDecimal.valueOf(num2.doubleValue()));

        return ValueUtil.getReasonableNumber(result[1]);
    }
}
