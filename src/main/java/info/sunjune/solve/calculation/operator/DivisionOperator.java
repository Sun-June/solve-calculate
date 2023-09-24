package info.sunjune.solve.calculation.operator;

import info.sunjune.solve.calculation.define.Context;
import info.sunjune.solve.calculation.define.Operator;
import info.sunjune.solve.calculation.error.CalculationException;
import info.sunjune.solve.calculation.error.StandardError;
import info.sunjune.solve.calculation.util.ValueUtil;

import java.math.BigDecimal;

/**
 * Definition of Division Operation: /
 * <br>
 * ------------------------------------------------------------------
 * <br>
 * 除运算定义: /
 * <br>
 * ------------------------------------------------------------------
 * <br>
 * 除算の定義: /
 */
public class DivisionOperator implements Operator<Number> {

    /**
     * Definition of Division Operation: /
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 除运算定义: /
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 除算の定義: /
     */
    public static final DivisionOperator DIVISION = new DivisionOperator();

    private DivisionOperator() {
    }

    @Override
    public String symbol() {
        return "/";
    }

    @Override
    public int precedence() {
        return 2;
    }

    @Override
    public Number calculation(Object leftValue, Object rightValue, Context context) throws CalculationException {
        if (!(leftValue instanceof Number) || !(rightValue instanceof Number)) {
            throw new CalculationException(StandardError.PARAMETERS_MUST_BE_NUMERIC);
        }

        Number num1 = (Number) leftValue;
        Number num2 = (Number) rightValue;
        if (num2.doubleValue() == 0) {
            throw new CalculationException(StandardError.DIVISION_BY_ZERO);
        }
        BigDecimal result = ValueUtil.divide(num1, num2);
        return ValueUtil.getReasonableNumber(result);
    }
}
