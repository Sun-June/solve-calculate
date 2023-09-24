package info.sunjune.solve.calculation.function;

import info.sunjune.solve.calculation.define.BracketPair;
import info.sunjune.solve.calculation.define.Context;
import info.sunjune.solve.calculation.define.Function;
import info.sunjune.solve.calculation.define.FunctionCalculation;
import info.sunjune.solve.calculation.error.CalculationException;

import java.util.List;

/**
 * Simple method definition with the default enclosure as ().
 * <br>
 * ------------------------------------------------------------------
 * <br>
 * 简易方法定义，默认包裹符为()
 * <br>
 * ------------------------------------------------------------------
 * <br>
 * 簡易なメソッド定義、デフォルトの括弧は（）です。
 *
 * @param <T>
 */
public abstract class SimpleFunction<T> implements Function<T> {

    private final String name;

    private final int minArgumentCount;

    private final int maxArgumentCount;

    protected final FunctionCalculation<T> functionCalculation;

    public SimpleFunction(String name, int argumentCount, FunctionCalculation<T> functionCalculation) {
        this(name, argumentCount, argumentCount, functionCalculation);
    }

    public SimpleFunction(String name, int minArgumentCount, int maxArgumentCount, FunctionCalculation<T> functionCalculation) {
        this.name = name;
        this.minArgumentCount = minArgumentCount;
        this.maxArgumentCount = maxArgumentCount;
        this.functionCalculation = functionCalculation;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public int minArgumentCount() {
        return minArgumentCount;
    }

    @Override
    public int maxArgumentCount() {
        return maxArgumentCount;
    }

    @Override
    public BracketPair getPair() {
        return BracketPair.PARENTHESES;
    }

    @Override
    public T calculation(List<Object> values, Context context) throws CalculationException {
        return this.functionCalculation.calculation(values, context);
    }
}
