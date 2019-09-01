

import java.io.{File, PrintWriter}

import kmeans.sec.Kmeans
import kmeans.{par, sec}
import kmeans.utils.CPoint
import org.scalameter._

/**
  * Aplicacion para ejecutar el Algoritmo K-means
  */

object Main extends App {

  /**
    * Lectura de los datos
    * @param path Ruta relativa del archivo de datos
    * @return Lista de puntos
    */

  def read(path: String,sep : Char): List[CPoint] = {
    scala.io.Source
      .fromFile(new File(path))
      .getLines().drop(1).toList
      .map(_.split(sep))
      .map(_.dropRight(1).toList.map(_.toDouble))
  }

  def readwoDrop(path: String,sep : Char): List[CPoint] = {
    scala.io.Source
      .fromFile(new File(path))
      .getLines().drop(1).toList
      .map(_.split(sep))
      .map(_.toList.map(_.toDouble))
  }
  val Points2 = readwoDrop("data/MINST.txt", ' ')

  // Lectura de datos

  val Points = read("data/a1_raw.csv",',')
  println(Points2.length)

  val kmeans = par.Kmeans(10,1,Points2).train



}


