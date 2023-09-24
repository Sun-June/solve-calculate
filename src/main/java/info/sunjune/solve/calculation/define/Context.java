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
 *
 * @param <T>
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

    public T getLiteralValue(String literal) {
        if (topBracketValue.containsKey(literal)) {
            return topBracketValue.get(literal);
        }
        return null;
    }

    public void addRecord(SolveItem item, List values, Object result) {
        this.recordList.add(new CalculationRecord(item, values, result));
    }

}
