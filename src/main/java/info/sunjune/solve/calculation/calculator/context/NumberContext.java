package info.sunjune.solve.calculation.calculator.context;

import info.sunjune.solve.calculation.define.Context;
import info.sunjune.solve.calculation.util.ValueUtil;

public class NumberContext extends Context<Number> {

    @Override
    public Number getLiteralValue(String literal) {
        Number value = super.getLiteralValue(literal);
        value = value == null ? getNumberValue(literal) : value;
        if (value != null) {
            return value;
        }
        return null;
    }

    public static Number getNumberValue(String literal) {
        if ("π".equals(literal)) {
            return Math.PI;
        }
        return ValueUtil.getNumberByString(literal);
    }

}
