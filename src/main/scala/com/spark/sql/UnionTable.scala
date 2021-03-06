package com.spark.sql

import org.apache.spark.sql.types.StructType
import org.apache.spark.SparkContext
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.types.StringType
import java.util.LinkedList
import org.apache.spark.sql.SQLContext

object UnionTable {
  def main(args: Array[String]) {
    val sc = new SparkContext("local", "DropTable")
    val sqlContext = new SQLContext(sc)

    // Create an RDD
    val people = sc.textFile("src/main/resources/people.txt")

    // The schema is encoded in a string
    val schemaString = "name age"

    // Import Spark SQL data types and Row.
    import org.apache.spark.sql._

    // Generate the schema based on the string of schema
    val schema =
      StructType(
        schemaString.split(" ").map(fieldName => StructField(fieldName, StringType, true)))

    // Convert records of the RDD (people) to Rows.
    val rowRDD = people.map(_.split(",")).map(p => Row(p(0), p(1).trim))

    // Apply the schema to the RDD.
    val peopleSchemaRDD = sqlContext.applySchema(rowRDD, schema)

    // Register the SchemaRDD as a table.

    // SQL statements can be run by using the sql methods provided by sqlContext.

    // The results of SQL queries are SchemaRDDs and support all the normal RDD operations.
    // The columns of a row in the result can be accessed by ordinal.
    println("People Table")
    val unionSchemaRDD = peopleSchemaRDD.unionAll(peopleSchemaRDD)

    unionSchemaRDD.registerTempTable("people")

    val resultsPeople = sqlContext.sql("SELECT name FROM people ")
    resultsPeople.collect.foreach(println)

  }
}