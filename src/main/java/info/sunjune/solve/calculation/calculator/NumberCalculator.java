package info.sunjune.solve.calculation.calculator;

import com.google.common.collect.Lists;
import info.sunjune.solve.calculation.calculator.context.NumberContext;
import info.sunjune.solve.calculation.define.*;
import info.sunjune.solve.calculation.function.NumberFunction;
import info.sunjune.solve.calculation.operator.*;
import info.sunjune.solve.calculation.operator.monadic.NegativeOperator;
import info.sunjune.solve.calculation.operator.monadic.ThousandthOperator;
import info.sunjune.solve.calculation.top.NumberTopBracket;

import java.util.List;

public class NumberCalculator extends Calculator<Number> {

    protected static final List<Function<Number>> functions = Lists.newArrayList(NumberFunction.ROUND, NumberFunction.MAX,
            NumberFunction.AVG, NumberFunction.MIN, NumberFunction.SUM, NumberFunction.LENGTH, NumberFunction.RANDOM);

    protected static final List<Operator<Number>> operators = Lists.newArrayList(AdditionNumberOperator.ADDITION, DivisionOperator.DIVISION,
            MultiplicationOperator.MULTIPLICATION, SubtractionOperator.SUBTRACTION, ModuloOperator.MODULO,
            ExponentOperator.EXPONENT);

    protected static final List<MonadicOperator<Number>> monadicOperators = Lists.newArrayList(NegativeOperator.NEGATIVE, ThousandthOperator.THOUSANDTH);

    protected static final List<TopBracket<Number>> tops = Lists.newArrayList(NumberTopBracket.NUMBER);

    public NumberCalculator() {
    }

    @Override
    protected List<Function<Number>> getFunctions() {
        return functions;
    }

    @Override
    protected List<Operator<Number>> getOperators() {
        return operators;
    }

    @Override
    protected List<MonadicOperator<Number>> getMonadicOperators() {
        return monadicOperators;
    }

    @Override
    protected List<TopBracket<Number>> topBrackets() {
        return tops;
    }

    @Override
    protected Context<Number> getInitContext(String formula) {
        return new NumberContext();
    }
}
