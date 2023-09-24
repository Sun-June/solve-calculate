package info.sunjune.solve.calculation.util;

import info.sunjune.solve.calculation.error.CalculationException;

public interface ThrowFunction<T, R> {

    R apply(T t) throws CalculationException;

}
