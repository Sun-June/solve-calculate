package info.sunjune.solve.calculation.define;

/**
 * The definition of the function or method wrapper operator usually uses (). For example, in max(1,3), max is the function, and the () that follows it is the wrapper.
 * <br>
 * ------------------------------------------------------------------
 * <br>
 * 函数方法的包裹运算符定义，通常使用(),比如`max(1,3)`中max为函数，对应的()为包裹体。
 * <br>
 * ------------------------------------------------------------------
 * <br>
 * 関数やメソッドの包裹演算子定義は、通常()を使用します。例えば、max(1,3)のmaxが関数で、それに続く()が包裹体です。
 */
public class BracketPair implements Define {

    /**
     * The parentheses pair: ()
     */
    public static final BracketPair PARENTHESES = new BracketPair('(', ')');

    /**
     * The square brackets pair: []
     */
    public static final BracketPair BRACKETS = new BracketPair('[', ']');

    /**
     * The braces pair: {}
     */
    public static final BracketPair BRACES = new BracketPair('{', '}');

    /**
     * The angle brackets pair
     */
    public static final BracketPair ANGLES = new BracketPair('<', '>');

    private final String open;
    private final String close;

    /**
     * The definition of the function or method wrapper operator usually uses (). For example, in max(1,3), max is the function, and the () that follows it is the wrapper.
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 函数方法的包裹运算符定义，通常使用(),比如`max(1,3)`中max为函数，对应的()为包裹体。
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 関数やメソッドの包裹演算子定義は、通常()を使用します。例えば、max(1,3)のmaxが関数で、それに続く()が包裹体です。
     *
     * @param open  wrapper operator open char
     * @param close wrapper operator close char
     */
    public BracketPair(char open, char close) {
        super();
        this.open = String.valueOf(open);
        this.close = String.valueOf(close);
    }

    /**
     * Gets the open bracket character.
     *
     * @return a char
     */
    public String getOpen() {
        return open;
    }

    /**
     * Gets the close bracket character.
     *
     * @return a char
     */
    public String getClose() {
        return close;
    }

    @Override
    public String toString() {
        return open + close;
    }

}
