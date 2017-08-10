package com.ssv.sodexo

import org.apache.log4j.Level
import org.apache.log4j.Logger
import org.apache.spark.SparkContext
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import scala.collection.mutable.ListBuffer

object Main extends App {

  Logger.getLogger("org").setLevel(Level.ERROR)
  val sc = new SparkContext("local[*]", "SodexoParsing")

  val parserObj = new TikaParsing("../SudexoParsing/resources/sodexo.pdf")
  val filePath = parserObj.parse

  val rdd = sc.parallelize(filePath)
  val (querryRegex, querry) = askUserForQuerry
  println(querry +" , "+querryRegex)

  val res = rdd.filter { x => querryRegex.findAllIn(x).size > 0 }
  val resultArray = res.collect().mkString("\n")

  writeToFile(resultArray, "../SudexoParsing/resources/")

  def writeToFile(content: String, fileLoc: String) = {
    val file = new File(fileLoc + querry)
    val bw = new BufferedWriter(new FileWriter(file))
    if (content.size != 0) {
      bw.write(content)
      bw.close()
    } else {
      bw.write("No related content found for the given keyword " + querry)
      bw.close()
    }
    println("...")
    println("finished writing to the file at location : " + fileLoc + querry)
  }

  def askUserForQuerry = {
    println("Enter any keyword that you want to lookup in the file: ")
    val querry = readLine()
    (makeRegEx(querry), querry)
  }

  def makeRegEx(msg: String) = {
    val temp = msg.toLowerCase()
    val regex = ("(" + temp(0) + "|" + temp(0).toUpper + ")" + temp.tail).r
    regex
  }
}