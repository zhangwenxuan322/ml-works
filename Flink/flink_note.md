# Apache Flink

## Definition

- 框架：开发人员只关注业务
- 分布式计算引擎
- 有状态计算
- 有界无界数据流

## Application

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

## Architecture

1. 有界无界数据流
2. 部署灵活：Yarn、K8S
3. 极高可伸缩性：峰值17亿条/S
4. 极致流式处理性能：本地状态存储，以前存在Redis、HBASE这样的缓存中
5. 容灾：状态同步到远程 

## Scenario

1. Data Pipeline

   数据搬运中做一些数据处理

2. Data Analytics

   数据分析，实时大屏

3. Data Driven

   适合用DataStream、ProcessFunction API编写，复杂场景