package info.sunjune.solve.calculation.calculator;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import info.sunjune.solve.calculation.calculator.item.*;
import info.sunjune.solve.calculation.define.*;
import info.sunjune.solve.calculation.error.CalculationException;
import info.sunjune.solve.calculation.error.DefineException;
import info.sunjune.solve.calculation.error.FormulaError;
import info.sunjune.solve.calculation.error.FormulaException;
import info.sunjune.solve.calculation.util.BothValue;
import info.sunjune.solve.calculation.util.ThrowFunction;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static info.sunjune.solve.calculation.error.FormulaError.VALUE_DOES_NOT_EXIST;
import static info.sunjune.solve.calculation.error.StandardError.UNKNOWN_OPERATOR;
import static info.sunjune.solve.calculation.util.PatternUtil.delimitersToRegexp;
import static info.sunjune.solve.calculation.util.PatternUtil.topBracketsRegexp;

/**
 * Calculating Processing Calculator
 * <br>
 * ------------------------------------------------------------------
 * <br>
 * 计算处理计算器
 * <br>
 * ------------------------------------------------------------------
 * <br>
 * 計算処理計算機
 *
 * @param <T> Used to restrict the operators, functions, and output result types that can participate in calculations.
 *            <br>
 *            ------------------------------------------------------------------
 *            <br>
 *            用以限制可以参与计算的运算符和函数及输出的结果类型
 *            <br>
 *            ------------------------------------------------------------------
 *            <br>
 *            計算に参加できる演算子、関数、および出力結果のタイプを制限するために使用されます。
 */
public abstract class Calculator<T> {

    protected static final String DEFAULT_FUNCTION_SEPARATOR = ",";

    protected final Set<String> openBrackets = Sets.newHashSet();

    protected final Set<String> closeBrackets = Sets.newHashSet();

    protected final Map<String, Function<T>> functionMap = Maps.newHashMap();

    protected final Map<String, Operator<T>> operatorMap = Maps.newHashMap();

    protected final Map<String, MonadicOperator<T>> monadicOperatorMap = Maps.newHashMap();

    protected Pattern formulaPattern;

    protected Pattern topBracketPattern;

    public Calculator() {
        BracketPair expressionBracket = this.expressionBracket();
        Set<String> delimiters = Sets.newLinkedHashSet();
        delimiters.add(expressionBracket.getOpen());
        delimiters.add(expressionBracket.getClose());

        for (Function<T> function : this.getFunctions()) {
            delimiters.add(function.name());
            this.functionMap.put(function.name(), function);
            delimiters.add(function.getPair().getOpen());
            delimiters.add(function.getPair().getClose());
            this.openBrackets.add(function.getPair().getOpen());
            this.closeBrackets.add(function.getPair().getClose());
        }

        if (!this.functionMap.isEmpty()) {
            delimiters.add(this.functionSeparator());
        }

        List<Operator<T>> operators = this.getOperators();
        operators.sort((o1, o2) -> o2.symbol().length() - o1.symbol().length()); // Longer characters must be parsed first.
        for (Operator<T> operator : this.getOperators()) {
            if (this.operatorMap.containsKey(operator.symbol()) || functionMap.containsKey(operator.symbol())) {
                throw new DefineException("no duplicate operators can exist");
            }
            delimiters.add(operator.symbol());
            this.operatorMap.put(operator.symbol(), operator);
        }

        for (MonadicOperator<T> monadicOperator : this.getMonadicOperators()) {
            if (this.monadicOperatorMap.containsKey(monadicOperator.symbol())) {
                throw new DefineException("no duplicate monadic-operators can exist");
            }
            if (!monadicOperator.right() && this.operatorMap.containsKey(monadicOperator.symbol())) {
                throw new DefineException("Left-matching monadic operators cannot duplicate standard operators.");
            }
            delimiters.add(monadicOperator.symbol());
            this.monadicOperatorMap.put(monadicOperator.symbol(), monadicOperator);
        }

        this.formulaPattern = delimitersToRegexp(delimiters);

        if (!this.topBrackets().isEmpty()) {
            List<BracketPair> topBrackets = Lists.newArrayList();
            Set<String> checkPairs = Sets.newHashSet();
            for (TopBracket<T> top : this.topBrackets()) {
                if (checkPairs.contains(top.getPair().getOpen()) || checkPairs.contains(top.getPair().getClose())) {
                    throw new DefineException("Top-level operators cannot have duplicate definitions:" + top.getPair());
                }
                topBrackets.add(top.getPair());
                checkPairs.add(top.getPair().getOpen());
                checkPairs.add(top.getPair().getClose());
            }

            this.topBracketPattern = topBracketsRegexp(topBrackets);
        }

    }

    /**
     * Calculate and obtain the result using a formula.
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 通过公式计算并获取结果
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 式を使って計算し、結果を取得します。
     *
     * @param formula formula
     * @return the result
     * @throws FormulaException     formula validation
     * @throws CalculationException Exceptions occurring during calculations
     */
    public T calculation(String formula) throws FormulaException, CalculationException {
        BothValue<T, Context<T>> result = this.calculationBoth(formula);
        return result.getLeft();
    }

    /**
     * Calculate and obtain the result using a formula.
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 通过公式计算并获取结果
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 式を使って計算し、結果を取得します。
     *
     * @param formula formula
     * @param context customer context
     * @return the result
     * @throws FormulaException     formula validation
     * @throws CalculationException Exceptions occurring during calculations
     */
    public T calculation(String formula, Context<T> context) throws FormulaException, CalculationException {
        BothValue<T, Context<T>> result = this.calculationBoth(formula, context);
        return result.getLeft();
    }

    /**
     * Calculate the formula and retrieve the result and context, where the left side is the result, and the right side is the context (which can be used to retrieve execution records and more).
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 通过公式计算并获取结果及context，其中左边为结果，右边为context（可以通过context获取执行记录等操作）
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 式を計算して結果とコンテキストを取得し、左側が結果で右側がコンテキストです（コンテキストを使用して実行履歴などを取得できます）。
     *
     * @param formula formula
     * @return the result and context
     * @throws FormulaException     formula validation
     * @throws CalculationException Exceptions occurring during calculations
     */
    public BothValue<T, Context<T>> calculationBoth(String formula) throws FormulaException, CalculationException {
        return this.calculationBoth(formula, null);
    }

    /**
     * Calculate the formula and retrieve the result and context, where the left side is the result, and the right side is the context (which can be used to retrieve execution records and more).
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 通过公式计算并获取结果及context，其中左边为结果，右边为context（可以通过context获取执行记录等操作）
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 式を計算して結果とコンテキストを取得し、左側が結果で右側がコンテキストです（コンテキストを使用して実行履歴などを取得できます）。
     *
     * @param formula formula
     * @param context customer context
     * @return the result
     * @throws FormulaException     formula validation
     * @throws CalculationException Exceptions occurring during calculations
     */
    public BothValue<T, Context<T>> calculationBoth(String formula, Context<T> context) throws FormulaException, CalculationException {
        CalculatorContext<T> calculatorContext = this.checkFormula(formula, context);
        SolveItem item = calculatorContext.items.get(0);

        T value;
        try {
            value = this.leftToRight(calculatorContext, item);
        } catch (CalculationException e) {
            e.context = calculatorContext.context;
            throw e;
        }

        return new BothValue<>(value, calculatorContext.context);
    }

    /**
     * Validate the calculation formula (throw an exception if there is an error, return normally if there is no error), and return the corresponding object as the calculation context.
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 校验计算公式（如果错误会抛出异常，无错误则正常返回），返回对应对象为计算用context
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 計算式を検証し、エラーがあれば例外をスローし、エラーがなければ正常に返し、対応するオブジェクトを計算用のコンテキストとして返します。
     *
     * @param formula formula
     * @return CalculatorContext
     * @throws FormulaException formula validation
     */
    public CalculatorContext<T> checkFormula(String formula) throws FormulaException {
        return this.checkFormula(formula, null);
    }

    /**
     * Validate the calculation formula (throw an exception if there is an error, return normally if there is no error), and return the corresponding object as the calculation context.
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 校验计算公式（如果错误会抛出异常，无错误则正常返回），返回对应对象为计算用context
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 計算式を検証し、エラーがあれば例外をスローし、エラーがなければ正常に返し、対応するオブジェクトを計算用のコンテキストとして返します。
     *
     * @param formula formula
     * @param context customer context
     * @return the result
     * @throws FormulaException formula validation
     */
    public CalculatorContext<T> checkFormula(String formula, Context<T> context) throws FormulaException {
        if (context == null) {
            context = this.getDefaultContext(formula);
        }
        CalculatorContext<T> calculatorContext = new CalculatorContext<T>(context, this.expressionBracket());

        formula = this.formatTop(formula, calculatorContext.context);
        this.initAndCheck(formula, calculatorContext);
        return calculatorContext;
    }

    protected String formatTop(String formula, Context<T> context) {
        if (topBracketPattern != null) {
            List<TopBracket<T>> topBrackets = this.topBrackets();
            Matcher matcher = topBracketPattern.matcher(formula);
            int drift = 0;
            int length = formula.length();
            while (matcher.find()) {
                for (int i = 0; i < topBrackets.size(); i++) {
                    String str = matcher.group(i + 1);
                    if (str != null) {
                        String key = "S" + UUID.randomUUID().toString().substring(0, 8);
                        T value = topBrackets.get(i).convertString(str);
                        context.topBracketValue.put(key, value);

                        formula = formula.substring(0, matcher.start() + drift) + key + formula.substring(matcher.end() + drift);

                        drift = formula.length() - length;
                        length = formula.length();
                    }
                }
            }
        }

        return formula;
    }

    protected void initAndCheck(String formula, CalculatorContext<T> calculatorContext) throws FormulaException {
        Matcher matcher = formulaPattern.matcher(formula);

        int pos = 0;
        while (matcher.find()) {
            if (pos != matcher.start()) {
                String source = formula.substring(pos, matcher.start());
                this.addLiteral(calculatorContext, source);
            }
            String spec = matcher.group();
            SolveItem item = this.specToItem(spec, calculatorContext);
            if (item == null) {
                throw new FormulaException(pos, pos + spec.length(), FormulaError.UNKNOWN);
            }
            calculatorContext.addItem(item);

            pos = matcher.end(); // Remember end of delimiter
        }

        if (pos != formula.length()) {
            // If it remains some characters in the string after last delimiter
            addLiteral(calculatorContext, formula.substring(pos));
        }
    }

    protected void addLiteral(CalculatorContext<T> calculatorContext, String source) throws FormulaException {
        SolveItem pre = calculatorContext.getPrevious();
        if (pre != null && pre.kind == Kind.FUNCTION) {
            FunctionSolveItem functionItem = (FunctionSolveItem) pre;
            Function function = (Function) functionItem.define;
            if (!function.getPair().getOpen().equals(source)) { // not like "max("
                throw calculatorContext.buildException(new LiteralSolveItem<T>(source, source, null), FormulaError.INVALID_BRACKET_AFTER_FUNCTION);
            }
        }
        String literal = source.trim().replace("　", ""); // remove half-width spaces and full-width spaces
        if (literal.isEmpty()) {
            // TODO
            calculatorContext.addIndex(source);
        } else {
            T value = calculatorContext.context.getLiteralValue(literal);
            calculatorContext.addItem(new LiteralSolveItem<T>(source, literal, value));
        }
    }

    protected SolveItem specToItem(String spec, CalculatorContext<T> calculatorContext) {
        if (spec.equals(this.functionSeparator())) {
            return new SolveItem(spec, Kind.FUNCTION_SEPARATOR);
        } else if (this.openBrackets.contains(spec)) {
            return new OpenBracketSolveItem(spec);
        } else if (this.closeBrackets.contains(spec)) {
            return new CloseBracketSolveItem(spec);
        } else if (this.functionMap.containsKey(spec)) {
            return new FunctionSolveItem(spec, this.functionMap.get(spec));
        } else {
            Operator operator = this.operatorMap.get(spec);
            MonadicOperator monadicOperator = this.monadicOperatorMap.get(spec);
            SolveItem previous = calculatorContext.getPrevious();

            if (monadicOperator != null) {
                if (monadicOperator.right() && (previous == null || previous.kind == Kind.OPEN_BRACKET || previous.kind == Kind.OPERATOR)) {
                    return new SolveItem(spec, Kind.MONADIC_OPERATOR, monadicOperator);
                } else if (!monadicOperator.right() && previous != null && (previous.kind == Kind.LITERAL || previous.kind == Kind.CLOSE_BRACKET)) {
                    return new SolveItem(spec, Kind.MONADIC_OPERATOR, monadicOperator);
                }
            }
            if (operator != null) {
                return new SolveItem(spec, Kind.OPERATOR, operator);
            }
            throw new DefineException("Unparsable operators:" + spec);
        }

    }

    /**
     * Retrieve all supported function definitions.
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 获取所有支持的函数定义
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * サポートされているすべての関数定義を取得する。
     *
     * @return functions
     */
    protected abstract List<Function<T>> getFunctions();

    /**
     * Retrieve all supported operators.
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 获取所有支持的运算符
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * サポートされているすべての演算子を取得します。
     *
     * @return operators
     */
    protected abstract List<Operator<T>> getOperators();

    /**
     * Retrieve all supported monadic operators.
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 获取所有支持的单体运算符
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * すべてのサポートされている単項演算子を取得します。
     *
     * @return monadic operators
     */
    protected abstract List<MonadicOperator<T>> getMonadicOperators();

    /**
     * Retrieve the default context (i.e., this value is used when no context is provided during calculations).
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 获取默认的context（即计算时不传入context时采用此值）
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * デフォルトのコンテキストを取得します（つまり、コンテキストが指定されていない計算時にこの値が使用されます）。
     *
     * @param formula formula
     * @return Context
     */
    protected abstract Context<T> getDefaultContext(String formula);

    /**
     * High-priority enclosure, default is ().
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 高优先级包裹符，默认为 ()
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 高優先度の括弧、デフォルトは（）です。
     *
     * @return expressionBracket
     */
    protected BracketPair expressionBracket() {
        return BracketPair.PARENTHESES;
    }

    /**
     * Retrieve the definition of the top-level enclosing symbol.
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 获取顶级包裹符定义
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * トップレベルの包括記号の定義を取得する。
     *
     * @return TopBrackets
     */
    protected List<TopBracket<T>> topBrackets() {
        return Lists.newArrayList();
    }

    /**
     * Parameter delimiter within the function, default is ",".
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 函数中的参数分割符，默认为: ,
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 関数内のパラメーターの区切り記号、デフォルトは「,」です。
     *
     * @return functionSeparator
     */
    protected String functionSeparator() {
        return DEFAULT_FUNCTION_SEPARATOR;
    }

    /**
     * Find the item to the left of the current item.
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 寻找当前item的左侧的item。
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 現在のアイテムの左側のアイテムを検索します。
     *
     * @param calculatorContext context
     * @param item              Find the item
     * @return current item
     */
    protected SolveItem left(CalculatorContext<T> calculatorContext, SolveItem item) {
        int index = calculatorContext.items.indexOf(item);
        if (index > 0 && index < calculatorContext.items.size()) {
            return calculatorContext.items.get(index - 1);
        }
        return null;
    }

    /**
     * Find the item to the right of the current item.
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 寻找当前item的右侧的item。
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 現在のアイテムの右側のアイテムを検索します。
     *
     * @param calculatorContext context
     * @param item              Find the item
     * @return current item
     */
    protected SolveItem right(CalculatorContext<T> calculatorContext, SolveItem item) {
        int index = calculatorContext.items.indexOf(item);
        if (index > -1 && index < calculatorContext.items.size() - 1) {
            return calculatorContext.items.get(index + 1);
        }
        return null;
    }

    /**
     * Find the operator item to the right of the current item.
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 寻找当前item的右侧的运算符item。
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 現在のアイテムの右隣の演算子アイテムを探します。
     *
     * @param calculatorContext context
     * @param item              Find the item
     * @return current item
     */
    protected SolveItem rightOperator(CalculatorContext<T> calculatorContext, SolveItem item) {
        int index = calculatorContext.items.indexOf(item) + 1;
        while (index < calculatorContext.items.size()) {
            SolveItem find = calculatorContext.items.get(index);
            if (find.kind == Kind.OPERATOR) {
                return find;
            } else if (find.kind == Kind.FUNCTION_SEPARATOR || find.kind == Kind.CLOSE_BRACKET) {
                return null;
            } else if (find.kind == Kind.OPEN_BRACKET) {
                OpenBracketSolveItem openBracketSolveItem = (OpenBracketSolveItem) find;
                CloseBracketSolveItem closeBracketSolveItem = openBracketSolveItem.getClose();
                index = calculatorContext.items.indexOf(closeBracketSolveItem);
            }
            index++;
        }
        return null;
    }

    /**
     * Find the operator item to the left of the current item.
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 寻找当前item的左侧的运算符item。
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 現在のアイテムの左側にある演算子のアイテムを探します。
     *
     * @param calculatorContext context
     * @param item              Find the item
     * @return current item
     */
    protected SolveItem leftOperator(CalculatorContext<T> calculatorContext, SolveItem item) {
        int index = calculatorContext.items.indexOf(item) - 1;
        while (index > -1) {
            SolveItem find = calculatorContext.items.get(index);
            if (find.kind == Kind.OPERATOR) {
                return find;
            } else if (find.kind == Kind.FUNCTION_SEPARATOR || find.kind == Kind.OPEN_BRACKET) {
                return null;
            } else if (find.kind == Kind.CLOSE_BRACKET) {
                CloseBracketSolveItem closeBracketSolveItem = (CloseBracketSolveItem) find;
                OpenBracketSolveItem openBracketSolveItem = closeBracketSolveItem.getOpen();
                index = calculatorContext.items.indexOf(openBracketSolveItem);
            }
            index--;
        }
        return null;
    }

    /**
     * Calculate from left to right.
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 从左到右进行计算。
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 左から右へ計算を行います。
     *
     * @param calculatorContext context
     * @param item              process item
     * @return result
     * @throws CalculationException Exceptions occurring during calculations
     */
    protected T leftToRight(CalculatorContext<T> calculatorContext, SolveItem item) throws CalculationException {
        SolveItem leftItem;
        SolveItem rightOperator;
        Context<T> context = calculatorContext.context;

        switch (item.kind) {
            case OPEN_BRACKET: // (
                OpenBracketSolveItem open = (OpenBracketSolveItem) item;
                CloseBracketSolveItem close = open.getClose();
                T value = leftToRight(calculatorContext, right(calculatorContext, item));
                SolveItem closeAfter = right(calculatorContext, close);
                leftItem = left(calculatorContext, item);

                if (closeAfter != null && closeAfter.kind == Kind.OPERATOR) {
                    if (leftItem != null && leftItem.kind == Kind.OPERATOR) {
                        Operator left = (Operator) leftItem.define;
                        Operator right = (Operator) closeAfter.define;
                        if (right.precedence() <= left.precedence()) {
                            return value;
                        }
                    }
                    calculatorContext.leftValue = value;
                    return leftToRight(calculatorContext, closeAfter);
                }
                return value;
            case LITERAL: // param
                SolveItem next = right(calculatorContext, item);
                leftItem = left(calculatorContext, item);
                LiteralSolveItem<T> literalSolveItem = (LiteralSolveItem<T>) item;
                T literalValue = this.processCalculation(context, item, Lists.newArrayList(literalSolveItem.getValue()),
                        values -> (T) values.get(0));
                if (next != null && calculatorContext.isMonadicOperatorLeft(next)) {
                    calculatorContext.leftValue = literalValue;
                    return leftToRight(calculatorContext, next);
                }
                if ((leftItem == null || leftItem.kind == Kind.FUNCTION_SEPARATOR || leftItem.kind == Kind.OPEN_BRACKET) &&
                        (next != null && next.kind != Kind.FUNCTION_SEPARATOR && next.kind != Kind.CLOSE_BRACKET)) {
                    calculatorContext.leftValue = literalValue;
                    return leftToRight(calculatorContext, next);
                }
                return literalValue;
            case MONADIC_OPERATOR:
                MonadicOperator<T> monadicOperator = (MonadicOperator<T>) item.define;
                rightOperator = this.rightOperator(calculatorContext, item);
                if (monadicOperator.right()) {
                    T rightValue = leftToRight(calculatorContext, right(calculatorContext, item));
                    T resultValue = this.processCalculation(context, item, Lists.newArrayList(rightValue),
                            values -> monadicOperator.calculation(values.get(0), context));
                    leftItem = left(calculatorContext, item);
                    if ((leftItem == null || leftItem.kind == Kind.FUNCTION_SEPARATOR || leftItem.kind == Kind.OPEN_BRACKET) && rightOperator != null) {
                        calculatorContext.leftValue = resultValue;
                        return leftToRight(calculatorContext, rightOperator);
                    } else {
                        return resultValue;
                    }
                } else {
                    T resultValue = this.processCalculation(context, item, Lists.newArrayList(calculatorContext.leftValue),
                            values -> monadicOperator.calculation(values.get(0), context));
                    SolveItem leftOperator = this.leftOperator(calculatorContext, item);
                    if (rightOperator != null && leftOperator == null) {
                        calculatorContext.leftValue = resultValue;
                        return leftToRight(calculatorContext, rightOperator);
                    } else {
                        return resultValue;
                    }
                }
            case OPERATOR:
                Operator<T> operator = (Operator<T>) item.define;
                rightOperator = this.rightOperator(calculatorContext, item);

                T leftValue = calculatorContext.leftValue;
                T rightValue = this.leftToRight(calculatorContext, right(calculatorContext, item));
                if (rightOperator != null) {
                    Operator<T> rightOp = (Operator<T>) rightOperator.define;
                    if (rightOp.precedence() > operator.precedence()) {
                        calculatorContext.leftValue = rightValue;
                        calculatorContext.stacks.computeIfAbsent(operator.precedence(), key ->
                                obj -> this.processCalculation(context, item, Lists.newArrayList(leftValue, obj),
                                        values -> operator.calculation(values.get(0), values.get(1), context)));
                        return this.leftToRight(calculatorContext, rightOperator);
                    } else {
                        T stepValue = this.processCalculation(context, item, Lists.newArrayList(leftValue, rightValue),
                                values -> operator.calculation(values.get(0), values.get(1), context));
                        if (calculatorContext.stacks.containsKey(rightOp.precedence())) {
                            stepValue = calculatorContext.stacks.get(rightOp.precedence()).apply(stepValue);
                            calculatorContext.stacks.remove(rightOp.precedence());
                        } else {
                            for (int i = rightOp.precedence(); i < operator.precedence(); i++) {
                                if (calculatorContext.stacks.containsKey(i)) {
                                    stepValue = calculatorContext.stacks.get(i).apply(stepValue);
                                    calculatorContext.stacks.remove(i);
                                }
                            }
                        }
                        calculatorContext.leftValue = stepValue;
                        return this.leftToRight(calculatorContext, rightOperator);
                    }
                } else {
                    T stepValue = this.processCalculation(context, item, Lists.newArrayList(leftValue, rightValue),
                            values -> operator.calculation(values.get(0), values.get(1), context));
                    SolveItem leftOperator = this.leftOperator(calculatorContext, item);
                    if (leftOperator == null) {
                        return stepValue;
                    }
                    if (!calculatorContext.stacks.isEmpty()) {
                        stepValue = calculatorContext.stacks.values().stream().findFirst().get().apply(stepValue);
                        calculatorContext.stacks.clear();
                    }

                    return stepValue;
                }
            case FUNCTION:
                FunctionSolveItem functionSolveItem = (FunctionSolveItem) item;
                Function<T> function = (Function<T>) item.define;
                List<Object> values = Lists.newArrayList();
                SolveItem indexItem = functionSolveItem.getOpen();
                for (int i = 0; i < functionSolveItem.getParamsCount(); i++) {
                    SolveItem nextItem = this.right(calculatorContext, indexItem);
                    values.add(this.leftToRight(calculatorContext, nextItem));
                    indexItem = functionSolveItem.getSeparator(i);
                }
                T functionResult = this.processCalculation(context, item, values, list -> function.calculation(list, context));
                SolveItem functionOpen = this.left(calculatorContext, item);
                SolveItem leftOperation = this.leftOperator(calculatorContext, item);
                SolveItem rightOperation = this.rightOperator(calculatorContext, functionOpen);
                if (rightOperation != null) {
                    if (leftOperation == null) {
                        calculatorContext.leftValue = functionResult;
                        return this.leftToRight(calculatorContext, rightOperation);
                    } else {
                        Operator operatorLeft = (Operator) leftOperation.define;
                        Operator operatorRight = (Operator) rightOperation.define;
                        if (operatorRight.precedence() > operatorLeft.precedence()) {
                            calculatorContext.leftValue = functionResult;
                            return this.leftToRight(calculatorContext, rightOperation);
                        }
                    }
                }
                return functionResult;
            default:
                throw new CalculationException(UNKNOWN_OPERATOR);
        }
    }

    /**
     * Process the calculation, record the objects to be processed, and make records.
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 处理计算过程，并记录将要处理的对象以及进行记录。
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 計算プロセスを処理し、処理対象のオブジェクトを記録し、記録を行います。
     *
     * @param context context
     * @param item    process item
     * @param values  process values
     * @param process process function
     * @return result
     * @throws CalculationException Exceptions occurring during calculations
     */
    protected T processCalculation(Context<T> context, SolveItem item, List<Object> values, ThrowFunction<List<Object>, T> process) throws CalculationException {
        context.pendingItem = item;
        T functionResult = null;
        try {
            functionResult = process.apply(values);
        } finally {
            context.addRecord(item, values, functionResult);
        }
        return functionResult;
    }

}
