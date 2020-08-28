## Apache Spark

### 从Oracle读数据

通过`spark.read.jdbc()`的方式直接从Oracle读取时间字段时，默认只取年月日，而很多时候需要用到精确到秒的时间。解决方案，写一个MyOracleDialect：

```scala
package com.bmsoft.scala.utils

import java.sql.Types
import org.apache.spark.sql.jdbc.{JdbcDialect, JdbcDialects}
import org.apache.spark.sql.types.{DataType, DataTypes, MetadataBuilder}

object MyOracleDialect {
    val dialect = new JdbcDialect {
      override def canHandle(url: String): Boolean = {
        url.startsWith("jdbc:oracle")
      }
      override def getCatalystType(sqlType: Int, typeName: String, size: Int, metadataBuilder: MetadataBuilder): Option[DataType] = {
        if (sqlType.equals(Types.DATE) && typeName.equals("DATE") && size == 0)
          return Option.apply(org.apache.spark.sql.types.TimestampType)
        else if (sqlType.equals(Types.NUMERIC) && typeName.equals("NUMBER") ){
                val scale = metadataBuilder.build().getLong("scale")
                if(scale != 0l) {
                  return Option.apply(org.apache.spark.sql.types.FloatType)
                }else{
                  if(size > 9l ) {
                    return Option.apply(org.apache.spark.sql.types.LongType)
                  }else{
                    return Option.apply(org.apache.spark.sql.types.IntegerType)
                  }
                }
              }
        DataTypes.BooleanType.equals()
        Option.empty
      }
    }

    def registerMyDialect: Unit ={
      JdbcDialects.registerDialect(dialect)
    }
}

```

### column切分字符串

当要把一个字段切分成多个字段，例如`content->a:123@@b:234@@c:345`，对dataframe的操作可以如下：

```scala
val resData = sourceData.select($"*").
      withColumn("content_tmp", split($"content", "@@")).
      select(
        $"*" +:
        (0 until 3).
          map(
            i => $"content_tmp".getItem(i)
            .substr(lit(3), length($"content_tmp".getItem(i)) - 2).as(s"C${i+1}")
          ):_*
      )
```

### 执行shell命令

```scala
import sys.process._
"command".!!
```

### Dataframe写入csv

1. 分区方式

   ```scala
   // 分块文件会存在hdfs中
   nodes.repartition(16)
   			.write
   			.option("header", "true")
   			.option("sep", "|")
       	.mode("overwrite")
       	.csv("/tmp/vertexes")
   ```

2. 单机写入一个文件

   ```scala
   import java.io.{File, PrintWriter}
   val csv = new PrintWriter(
   		new File("/tmp/test.csv")
   )
   csv.write("uuid:ID(shebei)|oid|txid|sbmc|sbzlx|sstsl|modelalias|documentname|c1|c2|c3" + "\n")
   // 先collect确保在主节点上操作
   df.collect.map(_.mkString("|")).foreach(line => {
     csv.write(line + "\n")
   })
   csv.close()
   ```


### 对Dataframe某个字段进行分组计算操作

1. 对DF分组后根据字段`groupby`

   ```scala
   target.repartition(16) // 可以是自定义分区数也可以根据字段分区
         .groupByKey(r => r.getAs[String]("xxx")) // 根据字段groupby
         .mapGroups(
           (xxx, row) => {
             // row就是当前字段下的数据集
             // 可以转成单节点操作的数据结构进行计算
             val seq = row.toSeq
             // 需要返回一个结果
             res
           }
         )
   ```

2. 不用担心`groupby`前的分区会将数据集打乱，spark内部会自己操作

3. 结果集提取操作：

   在`mapGroups`后得到的是一个`Dataset[T]`，内部的泛型是根据结果集类型得出的，比如`Dataset[Seq[String]]`这样的类型，需要对DS解析转成DF

   ```scala
   resData.repartition(16)
   			 .mapPartitions(seq => {
            val s = mutable.ArrayBuffer[String]()
            seq.foreach(str => {
              s += str
            })
            s.toIterator
          }).toDF()
   ```

4. 注意事项：

   1) 在分组计算时不可以用DF这样分布式的数据存储结构，因为这些计算要在单机上执行

   2) 每个组别都需要用的数据可以在一开始广播出去，广播方法：`spark.sparkContext.broadcast()`，接收方法：`broadcast.value`获取数据

### 数据集持久化

1. 在进行DF操作的时候，我们需要在接下来多个行动中重用同一个DF，这个时候我们就可以将DF缓存起来，可以很大程度的节省计算和程序运行时间

2. 持久化有两种方法：`cache()`和`persist()`，如果`persist()`不传参的情况下与`cache()`等效，存储级别都是`MEMORY_AND_DISK`，源码如下：

   ```scala
   /**
      * Persist this Dataset with the default storage level (`MEMORY_AND_DISK`).
      *
      * @group basic
      * @since 1.6.0
      */
     def cache(): this.type = persist()
   ```

3. `persist(StorageLevel)`传参方式可以自行选择存储级别，有如下几种级别：

   ```scala
   val NONE = new StorageLevel(false, false, false, false)
   val DISK_ONLY = new StorageLevel(true, false, false, false)
   val DISK_ONLY_2 = new StorageLevel(true, false, false, false, 2)
   val MEMORY_ONLY = new StorageLevel(false, true, false, true)
   val MEMORY_ONLY_2 = new StorageLevel(false, true, false, true, 2)
   val MEMORY_ONLY_SER = new StorageLevel(false, true, false, false)
   val MEMORY_ONLY_SER_2 = new StorageLevel(false, true, false, false, 2)
   val MEMORY_AND_DISK = new StorageLevel(true, true, false, true)
   val MEMORY_AND_DISK_2 = new StorageLevel(true, true, false, true, 2)
   val MEMORY_AND_DISK_SER = new StorageLevel(true, true, false, false)
   val MEMORY_AND_DISK_SER_2 = new StorageLevel(true, true, false, false, 2)
   val OFF_HEAP = new StorageLevel(true, true, true, false, 1)
   ```

### dmp文件导入oracle

在工作中经常遇到Oracle整张表的数据导入的情况，而通过SQL脚本的方式写入十分笨重且缓慢，而使用dmp文件的方式写入更加高效。具体步骤如下：

1. 修改权限
```
chown oracle:dba xxx.dump
```
2. 切换到oracle 用户
3. 输入导入命令
```
imp db/db file=table.dmp log=table.log statistics=none full=y
```

### 诡异的`ClassNotFound: scala.Any`

在一次RDD转DataFrame的经历中，遇到了这样的问题，想了一天都没解决，无意中发现问题，以下是代码复现：

```scala
rdd.map(m => {
  // ......忽略逻辑代码
  (t1, t2, t3, t4, t5)
}).toDF("t1", "t2", "t3", "t4", "t5")
```

如此简单的转换过程为什么会出错呢，而且报的错也让人摸不着头脑，在对代码再三审核之后发现，返回值内包含了Double和String类型，而RDD转DataFrame的过程要求字段类型一致，所以解决方案也就很显然了，将全部类型转换成String。