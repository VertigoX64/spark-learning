package com.spark.sql

import org.apache.spark.SparkContext
import org.apache.spark.sql.SQLContext
import java.util.HashMap

object MsAccessImport {
  val MYSQL_DRIVER = "net.ucanaccess.jdbc.UcanaccessDriver";

  val MYSQL_CONNECTION_URL = "jdbc:ucanaccess:///Access2010DB/GarageMike.accdb";

  def main(args: Array[String]) {
    try {

      val sc = new SparkContext("local", "Import to spark")
      val sqlContext = new SQLContext(sc)

      val options = new HashMap[String, String];
      options.put("driver", MYSQL_DRIVER);
      options.put("url", MYSQL_CONNECTION_URL);
      /*options.put("dbtable",
        "(select emp_no, concat_ws(' ', first_name, last_name) as full_name from employees) as employees_name");*/
      options.put("dbtable",
        "(SELECT City from Employee) as City");
      //options.put("partitionColumn", "emp_no");
      //options.put("lowerBound", "10001");
      //options.put("upperBound", "499999");
      // options.put("numPartitions", "10");

      //Load MySQL query result as DataFrame
      val jdbcDF = sqlContext.load("jdbc", options);

      jdbcDF.show

    } catch {
      case e: Exception => e.printStackTrace()

    }
  }
}
