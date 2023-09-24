package info.sunjune.solve.calculation.operator;

import info.sunjune.solve.calculation.define.Context;
import info.sunjune.solve.calculation.define.Operator;
import info.sunjune.solve.calculation.error.CalculationException;
import info.sunjune.solve.calculation.error.StandardError;
import info.sunjune.solve.calculation.util.ValueUtil;

/**
 * Relational Operators
 * <br>
 * ------------------------------------------------------------------
 * <br>
 * 关系运算符
 * <br>
 * ------------------------------------------------------------------
 * <br>
 * 関係演算子
 */
public class RelationalOperator implements Operator<Boolean> {

    /**
     * Logical AND Operator: &&
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * and关系运算符: &&
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 論理積演算子: &&
     */
    public static final RelationalOperator AND = new RelationalOperator("&&");

    /**
     * Logical OR Operator: ||
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 或关系运算符: ||
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 論理和演算子：||
     */
    public static final RelationalOperator OR = new RelationalOperator("||");

    private final String symbol;

    public RelationalOperator(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String symbol() {
        return symbol;
    }

    @Override
    public int precedence() {
        return -1;
    }

    @Override
    public Boolean calculation(Object leftValue, Object rightValue, Context context) throws CalculationException {
        boolean left = ValueUtil.getBooleanValue(leftValue);
        boolean right = ValueUtil.getBooleanValue(rightValue);

        if (symbol.equals(AND.symbol)) {
            return left && right;
        } else if (symbol.equals(OR.symbol)) {
            return left || right;
        } else {
            throw new CalculationException("unknown operator: " + symbol, StandardError.UNKNOWN_OPERATOR);
        }
    }
}
