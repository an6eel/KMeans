

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
    *
    * @param path  Ruta relativa del archivo de datos
    * @param header     Lineas iniciales a ignorar
    * @param sep   Separador
    * @param clase Columnas a la derecha a ignorar
    * @return Lista de puntos
    */

  def read(path: String,sep : Char, header : Int, clase : Int): List[CPoint] = scala.io.Source
    .fromFile(new File(path))
    .getLines().drop(header).toList
    .map(_.split(sep))
    .map(_.dropRight(clase).toList.map(_.toDouble))


  val Points = read("data/a1.txt",' ',0,0)

  val kmeansPar = par.Kmeans(5,250,Points).train
  val kmeans = sec.Kmeans(5,250,Points).train

  println("Sin paralelizacion")
  kmeans.foreach(cluster => {
    println("Centroide: "+ cluster.center.mkString("[",",","]") + " Miembros: " + cluster.members.length)
  })

  println("Con paralelizacion")
  kmeansPar.foreach(cluster => {
    println("Centroide: "+ cluster.center.mkString("[",",","]") + " Miembros: " + cluster.members.length)
  })



}


