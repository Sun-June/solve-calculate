package info.sunjune.solve.calculation.define;

import info.sunjune.solve.calculation.error.CalculationException;

/**
 * Operator Definition
 * <br>
 * ------------------------------------------------------------------
 * <br>
 * 运算符定义
 * <br>
 * ------------------------------------------------------------------
 * <br>
 * 演算子の定義
 */
public interface Operator<T> extends Define {

    /**
     * The operator name
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 操作符名称
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 演算子名
     *
     * @return operator name
     */
    String symbol();

    /**
     * Operator Precedence: For example, in the expression 1 + 5 * 3, the * operator has a higher precedence than +, so if + equals 1, * should be greater than or equal to 2. You can refer to the <a href="http://en.wikipedia.org/wiki/Order_of_operations">link</a> for more information.
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 运算符的优先级，比如 1 + 5 * 3中，* 的优先级要高于 +， 则如果+为1的情况下，*则要大于等于2。可参考：<a href="http://en.wikipedia.org/wiki/Order_of_operations">link</a>
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 演算子の優先順位: 例えば、式 1 + 5 * 3 の場合、* 演算子は + よりも高い優先順位を持っています。したがって、+ が1である場合、* は2以上である必要があります。詳細については<a href="http://en.wikipedia.org/wiki/Order_of_operations">リンク</a>を参照してください。
     *
     * @return Operator Precedence
     */
    int precedence();

    /**
     * Perform the calculation using the parameters on the left and right.
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 通过左右两边的参数进行运算
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 左右のパラメーターを使用して計算を行います。
     *
     * @param leftValue  leftValue
     * @param rightValue rightValue
     * @param context    context
     * @return result
     * @throws CalculationException Exceptions occurring during calculations
     */
    T calculation(Object leftValue, Object rightValue, Context<T> context) throws CalculationException;

}
