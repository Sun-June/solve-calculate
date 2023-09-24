package info.sunjune.solve.calculation.calculator.item;

import info.sunjune.solve.calculation.define.Define;

/**
 * Intermediate processing items used for convenient computation parsing and processing.
 * <br>
 * ------------------------------------------------------------------
 * <br>
 * 运算中途处理项目，用于方便运算解析和处理
 * <br>
 * ------------------------------------------------------------------
 * <br>
 * 演算中間処理アイテム、演算解析および処理を容易にするために使用。
 */
public class SolveItem {

    public final String source;


    public final Kind kind;

    public final Define define;

    public final String literal;

    public int startIndex;

    public SolveItem(String source, Kind kind, Define define, String literal) {
        this.source = source;
        this.kind = kind;
        this.define = define;
        this.literal = literal;
    }

    public SolveItem(String source, Kind kind, Define define) {
        this(source, kind, define, null);
    }

    public SolveItem(String source, String literal) {
        this(source, Kind.LITERAL, null, literal);
    }

    public SolveItem(String source, Kind kind) {
        this(source, kind, null);
    }

}
