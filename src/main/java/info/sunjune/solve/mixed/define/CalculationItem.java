package info.sunjune.solve.mixed.define;

public interface CalculationItem {

    /**
     * 是否需要计算，需要计算的项目，对应的formula则不能为空
     *
     * @return
     */
    boolean needCalculation();

    /**
     * 获取进行计算的formula
     *
     * @return
     */
    String getFormula();

    /**
     * 获取参与计算的名称
     *
     * @return
     */
    String getName();

    /**
     * 参与到了item对应的计算中去，比如当前项目名称为“cal1”, item对应的formula则为“cal1 + 100”。此方法可能被多次调用
     *
     * @param item
     */
    void participation(CalculationItem item);

}
