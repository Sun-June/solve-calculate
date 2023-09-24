package info.sunjune.solve.calculation.define;

/**
 * The definition of a calculation function
 * <br>
 * ------------------------------------------------------------------
 * <br>
 * 计算函数的定义
 * <br>
 * ------------------------------------------------------------------
 * <br>
 * 計算関数の定義
 */
public interface Function<T> extends Define, FunctionCalculation<T> {

    /**
     * function name
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 计算函数的名称
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 計算関数の名前
     *
     * @return
     */
    String name();

    /**
     * The minimum value of parameters, when using fixed parameters, should be set the same as the maximum value.
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 参数个数的最小值，当使用固定参数时，最大值和最小值设置为一样的即可
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * パラメーターの最小値は、固定パラメーターを使用する場合、最大値と同じに設定します。
     *
     * @return
     */
    int minArgumentCount();

    /**
     * The maximum value of parameters can be set the same as the minimum value when using fixed parameters.
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 参数个数的最大值，当使用固定参数时，最大值和最小值设置为一样的即可
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * パラメーターの最大値は、固定パラメーターを使用する場合、最小値と同じに設定することができます。
     *
     * @return
     */
    int maxArgumentCount();

    /**
     * Retrieve the enclosing parentheses of a function definition, such as ().
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 获取函数定义的包裹符，比如()
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 関数定義の括弧を取得する、例えば（）。
     *
     * @return
     */
    BracketPair getPair();

}
