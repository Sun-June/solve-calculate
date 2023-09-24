package info.sunjune.solve.calculation.calculator.context;

import info.sunjune.solve.calculation.calculator.item.Kind;
import info.sunjune.solve.calculation.calculator.item.SolveItem;

import java.util.List;

/**
 * Recording of Calculation Process.
 * <br>
 * ------------------------------------------------------------------
 * <br>
 * 运算过程中的记录
 * <br>
 * ------------------------------------------------------------------
 * <br>
 * 計算過程の記録
 */
public class CalculationRecord {

    /**
     * Arithmetic string
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 运算的字符串
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 演算の文字列
     */
    public final String arithmetic;

    /**
     * Starting Position of the Operation in the Formula
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 运算在公式中的起始位置
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 数式内での演算の開始位置
     */
    public final int index;

    /**
     * Values involved in the calculation, such as 1 + 3, result in [1, 3].
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 参与运算的值，比如 1 + 3，则值为[1,3]
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 計算に参加する値、例えば 1 + 3 の場合、値は [1, 3] です。
     */
    public final List<Object> values;

    public final Object result;

    public final Kind kind;

    public CalculationRecord(SolveItem item, List<Object> values, Object result) {
        this.values = values;
        this.result = result;
        this.index = item.startIndex;
        this.arithmetic = item.source;
        this.kind = item.kind;
    }

}
