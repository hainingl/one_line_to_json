object People extends App {

  import org.apache.spark.sql.SparkSession

  val spark = SparkSession
    .builder()
    .appName("Spark SQL basic example")
    .config("spark.master", "local")
    .getOrCreate()


  showCsv()

  //  showJson()

  spark.stop();

  def showCsv(): Unit = {
    // For implicit conversions like converting RDDs to DataFrames
    import spark.implicits._
    val dfCsv = spark
      .read
      .option("inferSchema", "true")
      .option("header", "true")
      .option("delimiter", ";")
      .csv("src/main/resources/people.csv")

    // Displays the content of the DataFrame to stdout
    println("Original CSV ---- ")
    dfCsv.show()

    //    dfCsv.printSchema()

    // method 1 direct DF filtering
    //    println("JSON string=" + dfCsv.toJSON.toString())
    val length: Int = dfCsv.count().toInt
    println("JSON string=" + dfCsv.toJSON.take(length).mkString(","))

    // method 2 sql with table
    dfCsv.createGlobalTempView("people")
    spark.sql("SELECT name, age FROM global_temp.people WHERE age BETWEEN 19 AND 31").show()
  }

  def showJson(): Unit = {
    println("JSON ---- ")
    val dfJson = spark.read.json("src/main/resources/people.json")
    //     Displays the content of the DataFrame to stdout
    dfJson.show()

    dfJson.printSchema()
  }
}
