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

