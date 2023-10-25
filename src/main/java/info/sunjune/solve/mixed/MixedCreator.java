package info.sunjune.solve.mixed;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.graph.MutableNetwork;
import com.google.common.graph.NetworkBuilder;
import info.sunjune.solve.calculation.calculator.Calculator;
import info.sunjune.solve.calculation.define.Context;
import info.sunjune.solve.calculation.error.FormulaException;
import info.sunjune.solve.mixed.define.CalculationItem;

import java.util.List;
import java.util.Map;

public abstract class MixedCreator<T> {

    private List<CalculationItem> items;

    private List<CalculationItem> needCalItems;

    private Map<String, CalculationItem> itemMap;

    public MixedCreator(List<CalculationItem> items) {
        this.items = items;
    }

    protected abstract Calculator<T> getCalculator();

    protected abstract Context<T> getContext();

    protected void init() {
        MutableNetwork<CalculationItem, String> network = NetworkBuilder.directed().build();

        itemMap = Maps.newHashMap();
        needCalItems = Lists.newArrayList();
        for (CalculationItem item : items) {
            if (itemMap.containsKey(item.getName())) {
                // TODO: add throw
            }
            itemMap.put(item.getName(), item);
            if (item.needCalculation()) {
                needCalItems.add(item);
            } else {
                network.addNode(item);
            }
        }

        Calculator<T> calculator = this.getCalculator();
        Context<T> context = this.getContext();

        for (CalculationItem item : needCalItems) {
            try {
                calculator.checkFormula(item.getFormula(), new Context<T>() {
                    @Override
                    public T getCustomerLiteralValue(String literal) {
                        T value = context.getLiteralValue(literal);
                        if (value == null) {
                            CalculationItem find = itemMap.get(literal);
                            if (find != null) {
                                if (find == item) {
                                    // TODO: throw self ex
                                }
                                find.participation(item);
                                network.addEdge(find, item, find.getName() + "->" + item.getName());
                                return this.getExampleValue();
                            } else {
                                // TODO: throw ex
                            }
                        }
                        return null;
                    }

                    @Override
                    public T getExampleValue() {
                        return context.getExampleValue();
                    }
                });
            } catch (FormulaException e) {
                throw new RuntimeException(e);
            }
        }


    }
}
