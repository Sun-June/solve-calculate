package info.sunjune.solve.calculation.error;

import info.sunjune.solve.calculation.calculator.item.SolveItem;

/**
 * Errors in the formula are generated during formula validation.
 * <br>
 * ------------------------------------------------------------------
 * <br>
 * 公式中存在的错误，由公式检查时产生
 * <br>
 * ------------------------------------------------------------------
 * <br>
 * 式中の誤りは、式の検証時に生成されます。
 */
public class FormulaException extends Exception {

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
    public final int startIndex;

    public final int endIndex;

    public final ErrorInfo error;

    public final SolveItem item;

    public FormulaException(SolveItem item, ErrorInfo error) {
        super(error.name() + ", startIndex:" + item.startIndex + ", source:" + item.source);
        this.startIndex = item.startIndex;
        this.endIndex = item.startIndex + item.source.length();
        this.error = error;
        this.item = item;
    }

    public FormulaException(int startIndex, int endIndex, ErrorInfo error) {
        super(error.name() + ", startIndex:" + startIndex + ", endIndex:" + endIndex);
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.error = error;
        this.item = null;
    }
}
