package info.sunjune.solve.calculation.define;

import info.sunjune.solve.calculation.error.CalculationException;

import java.util.List;

public interface FunctionCalculation<T> {

    /**
     * Perform calculations based on the passed parameters and output the calculation result.
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 根据传入的参数进行计算，输出计算结果。
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 渡されたパラメーターに基づいて計算し、計算結果を出力します。
     *
     * @param values  values
     * @param context context
     * @return result
     * @throws CalculationException Exceptions occurring during calculations
     */
    T calculation(List<Object> values, Context context) throws CalculationException;

}
