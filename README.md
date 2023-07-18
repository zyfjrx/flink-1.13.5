# Apache Flink

Apache Flink is an open source stream processing framework with powerful stream- and batch-processing capabilities.

Learn more about Flink at [https://flink.apache.org/](https://flink.apache.org/)

# Dynamic Window 、Dynamic CEP



### Streaming Dynamic Window Example
```scala
case class WordWithCount(word: String, count: Long)

val text = env.socketTextStream(host, port, '\n')

val windowCounts = text.flatMap { w => w.split("\\s") }
  .map { w => WordWithCount(w, 1) }
  .keyBy("word")
  .window(DynamicSlidingEventTimeWindows.of(
    new TimeAdjustExtractor<T>() {
      @Override
      public long extract(T element) {
        return element.getWinSize();
      }
    },
    new TimeAdjustExtractor<T>() {
      @Override
      public long extract(T element) {
        return element.getWinSlide();
      }
    }
  ))
  .sum("count")

windowCounts.print()
```
### 基于Flink1.13.5源码 为窗口模块添加 从数据或者配置流动态注入开窗时间功能
功能描述: 使用窗口进行计算时候,当逻辑频繁发生修改调整时
              整个程序需要停止后,修改代码,重新打包程序然后给集群提交，无法实现逻辑
              动态修改和外部动态注入,目前已经实现了滑动窗口逻辑动态注入，基于消息驱动逻
              辑修改，可以手动往source端注入特定消息实现细腻度控制逻辑注入感知。
              
DynamicSlidingEventTimeWindows 实现 TimeAdjustExtractor<T> 接口可从数据中获取窗口信息，每来一条数据就获取一次，从而触发窗口的分配，由于flink底层相同的窗口不会重复创建，从而也就天然实现了只有数据中窗口信息改变时才会触发新的窗口分配。

### 基于Flink1.13.5源码 为CEP模块添加 逻辑动态注入功能

    功能描述: 使用CEP作为复杂事件处理引擎时,当逻辑频繁发生修改,以及阈值频繁调整时
              整个程序需要停止后,修改代码,重新打包程序然后给集群提交，无法实现逻辑
              动态修改和外部动态注入,目前已经实现了CEP逻辑动态注入，基于消息驱动逻
              辑修改，可以手动往source端注入特定消息实现细腻度控制逻辑注入感知     

为Client端API中PatternStream添加方法registerListener(CepListener<T> cepListener)  注意必须在select方法之前调用

cepListener对象需要实现接口CepListener

接口方法

        Boolean needChange()      每条数据会调用这个方法，用于确定这条数据是否会触发规则更新
        Pattern returnPattern()   触发更新时调用，用于返回新的pattern作为新规则
