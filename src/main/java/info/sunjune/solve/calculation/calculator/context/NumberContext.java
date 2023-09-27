package info.sunjune.solve.calculation.calculator.context;

import info.sunjune.solve.calculation.define.Context;
import info.sunjune.solve.calculation.util.ValueUtil;

public class NumberContext extends Context<Number> {

    @Override
    public Number getCustomerLiteralValue(String literal) {
        return getNumberValue(literal);
    }

    public static Number getNumberValue(String literal) {
        if ("π".equals(literal)) {
            return Math.PI;
        }
        return ValueUtil.getNumberByString(literal);
    }

}
