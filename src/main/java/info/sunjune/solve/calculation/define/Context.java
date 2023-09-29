package info.sunjune.solve.calculation.define;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import info.sunjune.solve.calculation.calculator.context.CalculationRecord;
import info.sunjune.solve.calculation.calculator.item.SolveItem;

import java.util.List;
import java.util.Map;

/**
 * Context object during a single expression evaluation
 * <br>
 * ------------------------------------------------------------------
 * <br>
 * 一次计算式运算途中的上下文对象
 * <br>
 * ------------------------------------------------------------------
 * <br>
 * 計算式の実行中のコンテキストオブジェクト
 */
public abstract class Context<T> {

    /**
     * Used to store the content corresponding to the top-level operator for easy parsing and conversion.
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 用于存储顶级运算符对应的内容，方便解析转换
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * トップレベル演算子に対応する内容を格納し、解析と変換を容易にします。
     */
    public final Map<String, T> topBracketValue = Maps.newHashMap();

    /**
     * Calculate execution records
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 计算执行记录
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 計算実行の記録
     */
    public final List<CalculationRecord> recordList = Lists.newArrayList();

    /**
     * Objects to be processed, used for easy identification of the object causing calculation errors.
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 将要处理的计算对象，用以在出现计算异常时，方便定位出错的计算对象。
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 計算対象オブジェクト、計算エラーの特定を容易にするためのオブジェクト
     */
    public SolveItem pendingItem;

    public T getLiteralValue(String literal) {
        if (topBracketValue.containsKey(literal)) {
            return topBracketValue.get(literal);
        }
        return this.getCustomerLiteralValue(literal);
    }

    public void addRecord(SolveItem item, List values, Object result) {
        this.recordList.add(new CalculationRecord(item, values, result));
    }

    /**
     * Customizing the retrieval of values corresponding to other variables, with a note that if null is returned, it may result in an error when checking the corresponding formula for the absence of the value.
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 用以自定义获取其他变量对应的值，注意返回为null时对应的计算式检查会报找不到对应值的错。
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 他の変数に対応する値をカスタマイズして取得し、nullが返された場合に対応する計算式のチェックで値が見つからないエラーが発生することに注意してください。
     *
     * @param literal literal
     * @return customer value
     */
    public abstract T getCustomerLiteralValue(String literal);

}
