package info.sunjune.solve.calculation.function;

import info.sunjune.solve.calculation.define.FunctionCalculation;
import info.sunjune.solve.calculation.error.CalculationException;
import info.sunjune.solve.calculation.top.NumberTopBracket;
import info.sunjune.solve.calculation.util.ValueUtil;

import java.math.BigDecimal;
import java.util.Random;

import static info.sunjune.solve.calculation.error.StandardError.INCORRECT_NUMBER_PARAMETERS;
import static info.sunjune.solve.calculation.error.StandardError.PARAMETERS_MUST_BE_NUMERIC;

public class NumberFunction extends SimpleFunction<Number> {

    public static final NumberFunction MAX = new NumberFunction("max", 2, Integer.MAX_VALUE, (list, context) -> {
        if (list.size() < 2) {
            throw new CalculationException(INCORRECT_NUMBER_PARAMETERS);
        }
        Number value = Double.MIN_VALUE;
        for (Object object : list) {
            if (object instanceof Number) {
                Number number = (Number) object;
                if (number.doubleValue() > value.doubleValue()) {
                    value = number;
                }
            } else {
                throw new CalculationException(PARAMETERS_MUST_BE_NUMERIC);
            }
        }

        return value;
    });

    public static final NumberFunction MIN = new NumberFunction("min", 2, Integer.MAX_VALUE, (list, context) -> {
        if (list.size() < 2) {
            throw new CalculationException(INCORRECT_NUMBER_PARAMETERS);
        }
        Number value = Double.MAX_VALUE;
        for (Object object : list) {
            if (object instanceof Number) {
                Number number = (Number) object;
                if (number.doubleValue() < value.doubleValue()) {
                    value = number;
                }
            } else {
                throw new CalculationException(PARAMETERS_MUST_BE_NUMERIC);
            }
        }

        return value;
    });

    public static final NumberFunction SUM = new NumberFunction("sum", 2, Integer.MAX_VALUE, (list, context) -> {
        if (list.size() < 2) {
            throw new CalculationException(INCORRECT_NUMBER_PARAMETERS);
        }
        BigDecimal sum = BigDecimal.valueOf(0);
        for (Object object : list) {
            if (object instanceof Number) {
                Number number = (Number) object;
                sum = sum.add(BigDecimal.valueOf(number.doubleValue()));
            } else {
                throw new CalculationException(PARAMETERS_MUST_BE_NUMERIC);
            }
        }

        return ValueUtil.getReasonableNumber(sum);
    });

    public static final NumberFunction AVG = new NumberFunction("avg", 2, Integer.MAX_VALUE, (list, context) -> {
        if (list.size() < 2) {
            throw new CalculationException(INCORRECT_NUMBER_PARAMETERS);
        }

        BigDecimal sum = BigDecimal.valueOf(0);
        for (Object object : list) {
            if (object instanceof Number) {
                Number number = (Number) object;
                sum = sum.add(BigDecimal.valueOf(number.doubleValue()));
            } else {
                throw new CalculationException(PARAMETERS_MUST_BE_NUMERIC);
            }
        }
        BigDecimal result = ValueUtil.divide(sum.doubleValue(), list.size());

        return ValueUtil.getReasonableNumber(result);
    });

    public static final NumberFunction ROUND = new NumberFunction("round", 1, 3, (list, context) -> {
        if (list.isEmpty()) {
            throw new CalculationException(INCORRECT_NUMBER_PARAMETERS);
        }

        double value = 0;
        int scale = 0;
        int roundType = BigDecimal.ROUND_HALF_UP;

        for (int i = 0; i < list.size(); i++) {
            Object object = list.get(i);
            if (object instanceof String) {
                object = NumberTopBracket.convert(object.toString());
            }
            if (!(object instanceof Number)) {
                throw new CalculationException(PARAMETERS_MUST_BE_NUMERIC);
            }
            Number number = (Number) object;
            if (i == 0) {
                value = number.doubleValue();
            } else if (i == 1) {
                scale = number.intValue();
            } else if (i == 2) {
                roundType = number.intValue();
                break;
            }
        }

        BigDecimal result = BigDecimal.valueOf(value).setScale(scale, roundType);

        return ValueUtil.getReasonableNumber(result);
    });

    public static final NumberFunction LENGTH = new NumberFunction("length", 1, (list, context) -> {
        if (list.isEmpty()) {
            throw new CalculationException(INCORRECT_NUMBER_PARAMETERS);
        }
        return list.get(0).toString().length();
    });

    public static final NumberFunction RANDOM = new NumberFunction("random", 0, (list, context) -> new Random().nextInt());


    public NumberFunction(String name, int argumentCount, FunctionCalculation<Number> functionCalculation) {
        super(name, argumentCount, functionCalculation);
    }

    public NumberFunction(String name, int minArgumentCount, int maxArgumentCount, FunctionCalculation<Number> functionCalculation) {
        super(name, minArgumentCount, maxArgumentCount, functionCalculation);
    }
}
