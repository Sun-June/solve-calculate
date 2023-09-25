package info.sunjune.solve.calculation.define;

import info.sunjune.solve.calculation.error.CalculationException;

/**
 * The monadic operator Definition
 * <br>
 * ------------------------------------------------------------------
 * <br>
 * 单体操作符定义
 * <br>
 * ------------------------------------------------------------------
 * <br>
 * モナド演算子
 */
public interface MonadicOperator<T> extends Define {

    /**
     * The monadic operator name
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 单体操作符名称
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * モナド演算子名
     *
     * @return monadic operator name
     */
    String symbol();

    /**
     * Whether it's in right-match mode, such as -10, or in left-match mode when returning false, such as 20% = 0.2. (Note that within the same operator system, only one instance of the same operator can exist.)
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 是否为右侧匹配模式, 比如 -10， 返回false时则为左侧匹配，比如 20% = 0.2。（注意在一个运算体系下，同一个单体运算符只能存在一个）
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 右側マッチングモードであるか、例えば -10、false を返す場合は左側マッチングモードである、例えば 20% = 0.2。（同一の演算子は 1 つしか存在できない、同じ演算体系内であることに注意してください）。
     *
     * @return it's in right-match mode
     */
    boolean right();

    /**
     * Perform calculations using parameters.
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 通过参数进行运算
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * パラメーターを使用して計算する。
     *
     * @param value   value
     * @param context context
     * @return result
     * @throws CalculationException Exceptions occurring during calculations
     */
    T calculation(Object value, Context context) throws CalculationException;
}
