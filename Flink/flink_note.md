# Apache Flink

## Sketch

### Definition

- 框架：开发人员只关注业务
- 分布式计算引擎
- 有状态计算
- 有界无界数据流

### Application

1. Streams

   - Bounded
   - Unbounded

2. State

   - Stateless

     无状态的计算，类似于传统Java应用，数据不存储在系统中，而是存储在各种数据库中

   - Stateful

     可以将数据存储在系统中，例如存储最近1小时的数据在系统中，*这样有状态的计算对于流处理引擎至关重要*

3. Time

   - Event Time：事件发生的时间 *
   - Ingestion Time：数据进入Flink的时间 *
   - Processing Time：每个算子处理数据的时间
   - 可以判断出某个业务的时间滞后性

4. API

   - SQL API
   - Table API
   - DataStream API
   - ProcessFunction API
   - 越往下越灵活，越往上抽象越好

### Architecture

1. 有界无界数据流
2. 部署灵活：Yarn、K8S
3. 极高可伸缩性：峰值17亿条/S
4. 极致流式处理性能：本地状态存储，以前存在Redis、HBASE这样的缓存中
5. 容灾：状态同步到远程 

### Scenario

1. Data Pipeline

   数据搬运中做一些数据处理

2. Data Analytics

   数据分析，实时大屏

3. Data Driven

   适合用DataStream、ProcessFunction API编写，复杂场景

## Stateful

### Traditional Patch Process

- 通过时间划分出块，再对每个块执行处理方法。这样的操作看似很合理，但如果出现了某种操作需要记录开始和结束时间，开始时间在A块，结束时间在B块，为了应对这种场景需要记录一个中间状态传入下一个块处理，但这样的方式是很不符合流处理原则的

- 并且人为划分的时间段并不能保证该任务所需数据都能收到

### Stateful Stream Process

- 确保任务所需数据全部收到后再执行处理
- 能产生准确的数据
- 在这样的设计下，收到准确及时的结果是一件自然而然的事

### Four must things

- State Fault Tolerance（状态容错）

  **在分布式场景下精确一次**的容错保证，全域一致的快照，并且不中断计算

  Flink是如何做的？

  **CheckPoint**，Flink会在一定的间隔中打上检查点，每条检查点记录了数据从进入到计算的流程，并且同时支持多条CheckPoint记录，从而不会影响到计算过程

- State Management（状态维护）

  1. JVM Heap 状态后端

     存储在内存中进行状态维护，适合数据量不太大的计算

  2. RocketsDB 状态后端

     存储在磁盘中，适合大数据量计算

- Event-time Processing（事件发生时间处理）

  用真实事件发生的时间作为window移动的要求

- Savepoints and Job Migration（状态保存于迁移）

  **Savepoint**：可以理解为手动产生的一个Checkpoint，便于Flink的版本升级或者计算逻辑的修改

## DataStream API

### Basic Model

- 有向无环图DAG，每一个节点表示一个算子
- DataStream API面向数据操作，不同于Storm面向图的操作，更高层，符合工业界需求

### Steps

- 获取数据源
- 运算
- 输出

### Operations

- 基于单条数据：filter、map
- 基于窗口：window
- 合并多条流：union、join、connect
- 拆分流：split

### Distributed Computing

- 对一条数据流进行AllWindow操作体现不出分布式
- 先keyby再window得到WindowedStream，提高计算效率

### Physical Grouping

```
dataStream.global() -> 全部发往第一个task
dataStream.broadcast() -> 广播
dataStream.forward() -> 上下游并发度一样时一对一发送
dataStream.shuffle() -> 随机均匀分配
dataStream.rebalance() -> round-robin（轮流分配）
dataStream.recale() -> 本地轮流分配
dataStream.partitionCustom() -> 自定义单播
```

