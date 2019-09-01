

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
  val Points2 = readwoDrop("/home/angel/Escritorio/TFG/Kmeans/data/MINST.txt", ' ')

  // Lectura de datos

  val Points = read("/home/angel/Escritorio/TFG/Kmeans/data/a1_raw.csv",',')
  println(Points2.length)

  val kmeans = par.Kmeans(10,1,Points2).train

  kmeans.foreach((cl) => println(cl.members.length))


//  import java.io._
//  val pw = new PrintWriter(new File("elb" ))
//
//
//  // Metodo Elbow con valor maximo de K = 25
//  val elbow=sec.Kmeans.silhouetteMethod(Points,25)
//
//  // Guardamos los resultados en un fichero
//
//  elbow.foreach(u => pw.write(u.mkString(" ")+ "\n"))
//  pw.close
//
//  val pw2 = new PrintWriter(new File("sil" ))
//
//
//  // Metodo Silhouette con maximo valor de J = 25
//  val elb=sec.Kmeans.silhouetteMethod(Points,25)
//
//  // Guardamos los resultados en un fichero
//
//  elb.foreach(u => pw2.write(u.mkString(" ")+ "\n"))
//  pw2.close

}


