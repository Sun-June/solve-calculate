package info.sunjune.solve.calculation.operator;

import info.sunjune.solve.calculation.define.Context;
import info.sunjune.solve.calculation.define.Operator;
import info.sunjune.solve.calculation.error.CalculationException;
import info.sunjune.solve.calculation.util.ValueUtil;

import java.math.BigDecimal;

/**
 * Define addition, supporting addition between numbers and non-numeric strings.
 * <br>
 * ------------------------------------------------------------------
 * <br>
 * 加运算定义，支持数字与非数字之间的字符串加运算
 * <br>
 * ------------------------------------------------------------------
 * <br>
 * 加算の定義、数字と非数字の文字列の加算をサポートします。
 */
public class AdditionOperator implements Operator<Object> {

    /**
     * Define addition, supporting addition between numbers and non-numeric strings.
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 加运算定义，支持数字与非数字之间的字符串加运算
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 加算の定義、数字と非数字の文字列の加算をサポートします。
     */
    public static final AdditionOperator ADDITION = new AdditionOperator();

    private AdditionOperator() {
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
    public Object calculation(Object leftValue, Object rightValue, Context context) throws CalculationException {
        if (leftValue instanceof Number && rightValue instanceof Number) {
            Number num1 = (Number) leftValue;
            Number num2 = (Number) rightValue;
            BigDecimal result = BigDecimal.valueOf(num1.doubleValue()).add(BigDecimal.valueOf(num2.doubleValue()));
            return ValueUtil.getReasonableNumber(result);
        } else {
            if (leftValue instanceof Number) {
                Number num1 = (Number) leftValue;
                leftValue = ValueUtil.getReasonableNumber(BigDecimal.valueOf(num1.doubleValue()));
            }
            if (rightValue instanceof Number) {
                Number num2 = (Number) rightValue;
                rightValue = ValueUtil.getReasonableNumber(BigDecimal.valueOf(num2.doubleValue()));
            }
            return leftValue.toString() + rightValue.toString();
        }
    }
}
