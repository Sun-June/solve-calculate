# solve-calculate

solve-calculate 是一个简单的公式解析及运算工具，主要面向于node code或需要用户自定义公式等业务场景。

- 简单的在线demo: [solve-calculate-example](https://solve-example.azurewebsites.net/)
- 最低支持java1.8及以上版本

> 本项目的公式解析及定义部分借鉴[javaluator](https://javaluator.sourceforge.net/en/home/) 的实现方式

> 你也可以看下关于[未来的计划](#未来计划)

## 如何使用

* maven引用

```xml

<dependency>
    <groupId>info.sun-june.solve</groupId>
    <artifactId>solve-calculate</artifactId>
    <version>0.8.3</version>
</dependency>
```

* 简单的例子：

> NumberCalculator为数值计算器，参与运算的对象都必须为数值。还提供混合计算实现MixedCalculator，可以进行判断式和字符串等计算，可看后续说明。
>
> assertEquals为断言方法，用于判断两边的结果相同

```java
public class NumberCalculatorTest {
    @Test
    void base_test() throws Exception {
        NumberCalculator calculator = new NumberCalculator();

        assertEquals(calculator.calculation("1 + 1"), 2);
        assertEquals(calculator.calculation("-1 + -100 + 11 * 10"), 9);
        assertEquals(calculator.calculation("-1 + -100 + 11 * 10 - 10 * 2 + sum(2, 3)"), -6);
        assertEquals(calculator.calculation("-1 + -100 + 11 * 10  + sum(1, 2, 5 * 2, min(5, 6, avg(8, 9 / 3 , 10 + 2 + (5 - 3))))"), 27);
        assertEquals(calculator.calculation("-1 + -100 + 11 * 10 / 2 + 5 / 2"), -43.5d);
        assertEquals(calculator.calculation("1 + round(3.15 * 2.45, 2, \"ROUND_UP\")"), 8.72d);
        assertEquals(calculator.calculation("1 + round(3.15 * 2.45, 2, \"ROUND_DOWN\")"), 8.71d);
        assertEquals(calculator.calculation("1 + 2 ^ 3 / 2 + 1"), 6);
        assertEquals(calculator.calculation("2 + 5 % 2000‰ + 1"), 4);
        assertEquals(calculator.calculation("2 + round( 2 * π * 7, 0) + 1"), 47);
    }
}        
```

### 特性

#### 简单的计算公式合法性校验

例子：

```java
public class NumberCalculatorTest {
    @Test
    void errorCheck() {
        NumberCalculator calculator = new NumberCalculator();
        String input = "π + sum(10, min(, 10)) - 10";

        FormulaException ex = assertThrows(FormulaException.class, () -> calculator.checkFormula(input));
        assertEquals(FormulaError.ARGUMENT_MISSING, ex.error);
        assertEquals(",", input.substring(ex.startIndex, ex.endIndex));

    }
}
```

* 使用checkFormula方法进行公式是否正确的检查
* 错误的情况下会抛出FormulaException异常
* 其中error为异常的code信息，各个code对应一个枚举，你可以通过这个做错误信息的国际化
* startIndex和endIndex为这个错误对应公式中字符的起始和截止位置
* 通过以上信息，你可以更好的提示和检查公式的正确性

#### 可获取计算过程记录

例子：

```java
public class NumberCalculatorTest {
    @Test
    void record_test() throws Exception {
        NumberCalculator calculator = new NumberCalculator();

        BothValue<Number, Context<Number>> bothValue = calculator.calculationBoth("-1 + -100 + 11 * 10  + sum(1, 2, 5 * 2, min(5, 6, avg(8, 9 / 3 , 10 + 2 + (5 - 3))))");

        assertEquals(bothValue.getLeft(), 27);
        Gson gson = new Gson();
        for (CalculationRecord record : bothValue.getRight().recordList) {
            if (record.kind != Kind.LITERAL) {
                System.out.println("record::" + gson.toJson(record));
            }
        }
    }
}
```

输出结果：

```shell
record::{"arithmetic":"-","index":0,"values":[1.0],"result":-1,"kind":"MONADIC_OPERATOR"}
record::{"arithmetic":"-","index":5,"values":[100.0],"result":-100,"kind":"MONADIC_OPERATOR"}
record::{"arithmetic":"+","index":3,"values":[-1,-100],"result":-101,"kind":"OPERATOR"}
record::{"arithmetic":"*","index":15,"values":[11.0,10.0],"result":110,"kind":"OPERATOR"}
record::{"arithmetic":"+","index":10,"values":[-101,110],"result":9,"kind":"OPERATOR"}
record::{"arithmetic":"*","index":35,"values":[5.0,2.0],"result":10,"kind":"OPERATOR"}
record::{"arithmetic":"/","index":59,"values":[9.0,3.0],"result":3,"kind":"OPERATOR"}
record::{"arithmetic":"+","index":68,"values":[10.0,2.0],"result":12,"kind":"OPERATOR"}
record::{"arithmetic":"-","index":77,"values":[5.0,3.0],"result":2,"kind":"OPERATOR"}
record::{"arithmetic":"+","index":72,"values":[12,2],"result":14,"kind":"OPERATOR"}
record::{"arithmetic":"avg","index":50,"values":[8.0,3,14],"result":8.333333333333332,"kind":"FUNCTION"}
record::{"arithmetic":"min","index":40,"values":[5.0,6.0,8.333333333333332],"result":5.0,"kind":"FUNCTION"}
record::{"arithmetic":"sum","index":23,"values":[1.0,2.0,10,5.0],"result":18,"kind":"FUNCTION"}
record::{"arithmetic":"+","index":21,"values":[9,18],"result":27,"kind":"OPERATOR"}
```

* 通过calculationBoth方法可以获取包含计算结果和上下文context的对象
* context中recordList则存储了整个计算记录
    * 计算记录的顺序为实际计算顺序
    * arithmetic为进行计算的原字符串
    * index为对应在公式中的位置
    * values为参与计算的值（顺序即为运算传入的顺序）
    * result为此次运算的结果
    * kind为计算类型

#### 精确的定位计算时产生的问题

例子：

```java
public class NumberCalculatorTest {
    @Test
    void calculationError() {
        MixedCalculator calculator = new MixedCalculator();
        String input = "100 - 50 / (2 - min(2, 2000)) + 1";
        CalculationException ex = assertThrows(CalculationException.class, () -> calculator.calculation(input));
        assertEquals(ex.getErrorInfo(), StandardError.DIVISION_BY_ZERO);
        assertEquals(ex.context.pendingItem.source, "/");
        Gson gson = new Gson();
        List<CalculationRecord> recordList = ex.context.recordList;
        for (CalculationRecord record : recordList) {
            if (record.kind != Kind.LITERAL) {
                System.out.println("record:" + gson.toJson(record));
            }
        }
    }
}
```

输出结果：

```shell
record:{"arithmetic":"min","index":16,"values":[2.0,2000.0],"result":2.0,"kind":"FUNCTION"}
record:{"arithmetic":"-","index":14,"values":[2.0,2.0],"result":0,"kind":"OPERATOR"}
record:{"arithmetic":"/","index":9,"values":[50.0,0],"kind":"OPERATOR"}
```

* 其中可以通过异常中绑定的context，访问出现问题的项目pendingItem
* 可以获取对应的错误信息以及对应的源字符串，以及对应的坐标
* 执行记录中依然存在已成功计算的记录

#### 简单的变量替换

例子：

```java
public class NumberContext extends Context<Number> {

    @Override
    public Number getLiteralValue(String literal) {
        Number value = super.getLiteralValue(literal);
        value = value == null ? getNumberValue(literal) : value;
        if (value != null) {
            return value;
        }
        return null;
    }

    public static Number getNumberValue(String literal) {
        if ("π".equals(literal)) {
            return Math.PI;
        }
        return ValueUtil.getNumberByString(literal);
    }

}
```

* 以上为目前数值运算的context定义（你可以通过继承NumberContext来重写需要返回的context）
* 其中有处理将`π`进行数值化转换的操作
* 借鉴此思路你可以定义哪些变量可进行代入公式进行计算

#### 简易的函数和运算符扩展

* 你也可以增加或调整支持的计算函数和运算符
    * 通过继承NumberCalculator或MixedCalculator重写对应的获取方法来调整参与计算的内容
    * 或者直接继承Calculator来设计自己的计算器工具类
* 计算函数的扩展请参考`info.sunjune.solve.calculation.function`包下的实现
    * 比如`info.sunjune.solve.calculation.function.NumberFunction`
* 运算符的扩展请参考`info.sunjune.solve.calculation.operator`包下的实现
    * 比如`info.sunjune.solve.calculation.operator.AdditionNumberOperator`

#### 混合计算器

例子：

```java
public class MixedCalculatorTest {
    @Test
    void baseTest() throws Exception {
        MixedCalculator calculator = new MixedCalculator();

        assertEquals(calculator.calculation("-1 + -100 + 11 * 10 + \"abc\""), "9abc");
        assertEquals(calculator.calculation("if(1 * 10 > 5, 10, \"abc\") + 2"), 12);

        assertEquals(calculator.calculation("if(1 * 10 < 5, 10, \"abc\") + 2"), "abc2");
        assertEquals(calculator.calculation("if(1 * 10 <= 5 * 2, 10, \"abc\") + 2"), 12);
        assertEquals(calculator.calculation("if(1 * 10 <= 5 * 2 || 10 < 3, 10, \"abc\") + 2"), 12);
        assertEquals(calculator.calculation("if(1 * 10 <= 5 * 2 && 1 == 1, 10, \"abc\") + 2"), 12);
    }
}
```

* 混合计算器的实现中，`+`可进行字符串运算（其他非数值对象也会转换为字符串进行运算）
* 支持比对运算符（`> >= < <= == !=`）和关系运算符(`&& ||`)

<a name="my-future"></a>

## 未来计划

* ~~0.8.0~~
    * 丰富代码注释
    * 增加常用计算函数支持
    * 发布到maven中央仓库
* 0.9.0
    * 增加链式计算支持，举例如下
        * 项目a，计算公式为`num + 100`，其中num为一个自定义变量
        * 项目b，计算公式为`项目a - 9`
        * 项目c，计算公式为`项目a + 项目b`
    * 追加链式计算的检测，防止成为环
    * 追加链式计算记录等功能
* 1.0.0
    * 继续增强链式计算
    * 追加链式计算中类似table这类多行数据的计算类型支持
        * 支持多行数据中每一行进行计算，其中也能引入多行数据之外的量来计算
        * 有限支持多行数据外的数据访问
* 1.0.0以后
    * 日常问题维护
    * 推出typescript版本，可以让nodejs或者前端完成同样的功能
    * 推出java21版本（另外新建项目），可能会利用虚拟线程等特性加快运算速度

