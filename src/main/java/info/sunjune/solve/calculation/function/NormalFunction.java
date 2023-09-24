package info.sunjune.solve.calculation.function;

import info.sunjune.solve.calculation.define.FunctionCalculation;
import info.sunjune.solve.calculation.error.CalculationException;
import info.sunjune.solve.calculation.util.ValueUtil;

import static info.sunjune.solve.calculation.error.StandardError.PARAMETERS_MUST_BE_NUMERIC;

public class NormalFunction extends SimpleFunction<Object> {

    public static final NormalFunction IF = new NormalFunction("if", 3, (values, context) -> {
        if (values.size() < 3) {
            throw new CalculationException(PARAMETERS_MUST_BE_NUMERIC);
        }
        boolean value = ValueUtil.getBooleanValue(values.get(0));
        System.out.println("if (" + values.get(0) + " , " + values.get(1) + ", " + values.get(2) + ") , the boolean:: " + value);
        if (value) {
            return values.get(1);
        } else {
            return values.get(2);
        }
    });


    public NormalFunction(String name, int argumentCount, FunctionCalculation<Object> functionCalculation) {
        super(name, argumentCount, functionCalculation);
    }

    public NormalFunction(String name, int minArgumentCount, int maxArgumentCount, FunctionCalculation<Object> functionCalculation) {
        super(name, minArgumentCount, maxArgumentCount, functionCalculation);
    }
}
