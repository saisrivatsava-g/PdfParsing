package com.ssv.sodexo

import java.io.File
import java.io.FileInputStream

import org.apache.tika.metadata.Metadata
import org.apache.tika.parser.AutoDetectParser
import org.apache.tika.parser.ParseContext
import org.apache.tika.sax.BodyContentHandler

class TikaParsing(val inputFilePath: String) {
  def parse = {
    val file = new File(inputFilePath);
    val parser = new AutoDetectParser();
    val handler = new BodyContentHandler(-1);
    val metadata = new Metadata();
    val content = new FileInputStream(file);
    parser.parse(content, handler, metadata, new ParseContext());
    val data = handler.toString().split("\\n").toSeq
    data
  }
}