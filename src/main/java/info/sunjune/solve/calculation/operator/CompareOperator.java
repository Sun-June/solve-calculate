package info.sunjune.solve.calculation.operator;

import info.sunjune.solve.calculation.define.Context;
import info.sunjune.solve.calculation.define.Operator;
import info.sunjune.solve.calculation.error.CalculationException;
import info.sunjune.solve.calculation.error.StandardError;

/**
 * Comparison Operator
 * <br>
 * ------------------------------------------------------------------
 * <br>
 * 比对运算符
 * <br>
 * ------------------------------------------------------------------
 * <br>
 * 比較演算子
 */
public class CompareOperator implements Operator<Boolean> {

    /**
     * Greater Than Comparison Operator: >
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 大于比对运算符: >
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 大なり比較演算子：＞
     */
    public static final CompareOperator GREATER_THAN = new CompareOperator(">");

    /**
     * Greater than or equal to comparison operator: >=
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 大于或等于比对运算符: >=
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 大なりまたは等しい比較演算子：>=
     */
    public static final CompareOperator GREATER_EQUAL_THAN = new CompareOperator(">=");

    /**
     * Less Than Comparison Operator: <
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 小于比对运算符: <
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 小なり比較演算子： <
     */
    public static final CompareOperator LESS_THAN = new CompareOperator("<");

    /**
     * Less than or equal to comparison operator: <=
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 小于或等于比对运算符: <=
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 小なりイコール比較演算子：<=
     */
    public static final CompareOperator LESS_EQUAL_THAN = new CompareOperator("<=");

    /**
     * Equality Comparison Operator: ==
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 等于比对运算符: ==
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 等号比較演算子：==
     */
    public static final CompareOperator EQUAL = new CompareOperator("==");

    /**
     * Not Equal Comparison Operator: !=
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 不等比对运算符: !=
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 不等号比較演算子: !=
     */
    public static final CompareOperator NOT_EQUAL = new CompareOperator("!=");

    private final String symbol;

    public CompareOperator(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String symbol() {
        return symbol;
    }

    @Override
    public int precedence() {
        return 0;
    }

    @Override
    public Boolean calculation(Object leftValue, Object rightValue, Context context) throws CalculationException {
        if (leftValue instanceof Number && rightValue instanceof Number) {
            Number num1 = (Number) leftValue;
            Number num2 = (Number) rightValue;
            switch (symbol) {
                case ">":
                    return num1.doubleValue() > num2.doubleValue();
                case ">=":
                    return num1.doubleValue() >= num2.doubleValue();
                case "<":
                    return num1.doubleValue() < num2.doubleValue();
                case "<=":
                    return num1.doubleValue() <= num2.doubleValue();
                case "==":
                    return num1.doubleValue() == num2.doubleValue();
                case "!=":
                    return num1.doubleValue() != num2.doubleValue();
                default:
                    throw new CalculationException("unknown operator: " + symbol, StandardError.UNKNOWN_OPERATOR);
            }
        }
        int compare = leftValue.toString().compareTo(rightValue.toString());
        switch (symbol) {
            case ">":
                return compare > 0;
            case ">=":
                return compare >= 0 || leftValue == rightValue || leftValue.equals(rightValue);
            case "<":
                return compare < 0;
            case "<=":
                return compare <= 0 || leftValue == rightValue || leftValue.equals(rightValue);
            case "==":
                return leftValue == rightValue || leftValue.equals(rightValue);
            case "!=":
                return leftValue != rightValue && !leftValue.equals(rightValue);
            default:
                throw new CalculationException("unknown operator: " + symbol, StandardError.UNKNOWN_OPERATOR);
        }
    }

}
