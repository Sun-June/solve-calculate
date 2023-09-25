package info.sunjune.solve.calculation.function;

import info.sunjune.solve.calculation.define.FunctionCalculation;
import info.sunjune.solve.calculation.error.CalculationException;
import info.sunjune.solve.calculation.util.ValueUtil;

import java.math.BigDecimal;
import java.util.regex.Pattern;

import static info.sunjune.solve.calculation.error.StandardError.INCORRECT_NUMBER_PARAMETERS;

public class NormalFunction extends SimpleFunction<Object> {

    public static final NormalFunction IF = new NormalFunction("if", 3, (values, context) -> {
        if (values.size() < 3) {
            throw new CalculationException(INCORRECT_NUMBER_PARAMETERS);
        }
        boolean value = ValueUtil.getBooleanValue(values.get(0));
        if (value) {
            return values.get(1);
        } else {
            return values.get(2);
        }
    });

    public static final NormalFunction TO_NUMBER = new NormalFunction("toNumber", 1, (values, context) -> {
        if (values.isEmpty()) {
            throw new CalculationException(INCORRECT_NUMBER_PARAMETERS);
        }
        Number number = ValueUtil.getNumberByString(values.get(0).toString());
        if (number != null) {
            return ValueUtil.getReasonableNumber(BigDecimal.valueOf(number.doubleValue()));
        }
        return 0;
    });

    public static final NormalFunction REGULAR_MATCH = new NormalFunction("regularMatch", 2, (values, context) -> {
        if (values.size() < 2) {
            throw new CalculationException(INCORRECT_NUMBER_PARAMETERS);
        }
        String value1 = values.get(0).toString();
        String value2 = values.get(1).toString();
        return Pattern.matches(value1, value2);
    });

    public NormalFunction(String name, int argumentCount, FunctionCalculation<Object> functionCalculation) {
        super(name, argumentCount, functionCalculation);
    }

    public NormalFunction(String name, int minArgumentCount, int maxArgumentCount, FunctionCalculation<Object> functionCalculation) {
        super(name, minArgumentCount, maxArgumentCount, functionCalculation);
    }
}
