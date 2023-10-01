# solve-calculate

- solve-calculateは、主にNode.jsコードやユーザー定義の数式が必要なビジネスシナリオ向けのシンプルな数式解析および計算ツールです。

> このプロジェクトの数式解析および定義の部分は、[javaluator](https://javaluator.sourceforge.net/en/home/)
> の実装方法にインスパイアされています。

> [未来の計画](#未来の計画)もご覧いただけます。

## 使い方

- Maven依存性

```xml

<dependency>
    <groupId>info.sun-june.solve</groupId>
    <artifactId>solve-calculate</artifactId>
    <version>0.8.3</version>
</dependency>
```

- シンプルな例：

> NumberCalculatorは数値計算機で、計算に関与するすべてのオブジェクトは数値である必要があります。MixedCalculatorを介して混合計算を提供し、条件式、文字列計算などを許可します。

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

### 特徴

#### 簡単な計算式の妥当性チェック

例：

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

- checkFormulaメソッドを使用して数式が正しいかどうかを確認します。
- エラーの場合、FormulaExceptionがスローされます。
- エラーは例外コードで、各コードが列挙型に対応し、エラーメッセージの国際化に使用できます。
- startIndexおよびendIndexは、このエラーに対応する数式内の文字の開始位置および終了位置を表します。
- この情報を使用すると、数式の正確性をより良くプロンプトおよびチェックできます。

#### 計算プロセスの記録へのアクセス

例：

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

出力結果：

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

- calculationBothメソッドを使用すると、計算結果とコンテキストを含むオブジェクトを取得できます。
- contextにはrecordListとして全体の計算記録が格納されています。
    - 計算記録の順序は実際の計算順序に従います。
    - arithmeticは計算に使用される元の文字列です。
    - indexは数式内の対応する位置です。
    - valuesは計算に関与する値です（計算のために渡された順序で表示）。
    - resultはこの計算の結果です。
    - kindは計算タイプを表します。

#### 計算時に発生する問題を正確に特定する

例：

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

出力結果：

```shell
record:{"arithmetic":"min","index":16,"values":[2.0,2000.0],"result":2.0,"kind":"FUNCTION"}
record:{"arithmetic":"-","index":14,"values":[2.0,2.0],"result":0,"kind":"OPERATOR"}
```

- 例外でバウンドされた`context`を使用して、問題のある`pendingItem`にアクセスできます。
- 対応するエラーメッセージ、ソース文字列、および座標を取得できます。
- 実行レコードにはまだ正常に計算されたエントリが含まれています。

#### シンプルな変数の置換

例：

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

- 上記は現在の数値計算のためのコンテキスト定義です（返すコンテキストをカスタマイズするにはNumberContextをオーバーライドできます）。
- `π`の数値変換を処理する要素が含まれています。
- このアプローチに従って、どの変数を数式に代入して計算に使用できるかを定義できます。

#### 関数と演算子の簡単な拡張

- サポートされる計算関数と演算子を追加または調整することもできます。
    - NumberCalculatorまたはMixedCalculatorで対応する取得メソッドをオーバーライドして計算に関与するコンテンツを調整できます。
    - または、Calculatorを直接拡張して独自の計算機ユーティリティクラスを設計できます。
- 計算関数を拡張するには、`info.sunjune.solve.calculation.function`
  パッケージの実装を参照してください。たとえば、`info.sunjune.solve.calculation.function.NumberFunction`です。
- 演算子の拡張については、`info.sunjune.solve.calculation.operator`
  パッケージの実装を参照してください。たとえば、`info.sunjune.solve.calculation.operator.AdditionNumberOperator`です。

#### ミキシング計算機

例：

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

- ミキシング計算機の実装では、`+`は文字列演算を実行できます（その他の非数値オブジェクトも演算のために文字列に変換されます）。
- 比較演算子（`> >= < <= == !=`）および論理演算子（`&& ||`）をサポートしています。

## 未来の計画

- ~~0.8.0~~
    - コードコメントの強化
    - 一般的な計算関数のサポートを追加
    - Maven Central Repository への公開
- 0.9.0
    - 連鎖計算のサポートを追加します。以下のようになります：
        - プロジェクトA、計算式：`num + 100`、ここで`num`はカスタム変数です
        - プロジェクトB、計算式：`プロジェクトA - 9`
        - プロジェクトC、計算式：`プロジェクトA + プロジェクトB`
    - 循環を防ぐための連鎖計算のチェックを追加
    - 連鎖計算の記録などの機能を追加
- 1.0.0
    - 連鎖計算をさらに強化
    - 連鎖計算内のテーブルのようなデータに対するサポートを追加します。具体的には以下のような内容です：
        - マルチローデータの各行に対する計算、外部のデータから変数を導入できる
        - マルチローデータ外部のデータへのアクセスの一部サポート
- 1.0.0以降
    - 通常のメンテナンス
    - TypeScriptバージョンを導入し、Node.jsまたはフロントエンドアプリケーションでも同様の機能を提供
    - Java 21バージョン（別プロジェクトとして）をリリース、仮想スレッドなどの機能を活用して計算を高速化することが考えられます