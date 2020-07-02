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

   

