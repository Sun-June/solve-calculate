package info.sunjune.solve.calculation.define;

/**
 * Define top-level operators, define the top-level operator, such as "123," which will be operated at the top level and preserve the content within the enclosure.
 * <br>
 * ------------------------------------------------------------------
 * <br>
 * 顶级运算符定义，定义最高级运算符，比如"123"，会以最高级进行运算并且保持包裹体内的内容
 * <br>
 * ------------------------------------------------------------------
 * <br>
 * トップレベル演算子の定義、最上位演算子を定義し、例えば "123" のようなものは、最上位演算子で演算され、かつ包括体内の内容が保持されます。
 */
public interface TopBracket<T> extends Define {

    /**
     * Retrieve the definition of the enclosing symbol.
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 获取包裹符定义
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 包括記号の定義を取得する。
     *
     * @return definition of the enclosing symbol
     */
    BracketPair getPair();

    /**
     * Convert the value inside the parentheses to a value of type T.
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 将包裹符内的值转换为对应T类型的值
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 括弧内の値を対応するT型の値に変換します。
     *
     * @param str the value inside the parentheses
     * @return value of type T
     */
    T convertString(String str);

}
