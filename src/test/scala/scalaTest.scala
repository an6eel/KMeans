import java.io.{BufferedReader, File, FileNotFoundException, FileReader}
import kmeans.utils.CPoint
import kmeans.sec._
import kmeans.par
import org.scalameter.Bench
import org.scalameter.api._

object Data2 {

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

  // Generador de iteraciones
  // Configuración actual -> De 1 hasta 20 con saltos de 1


  val sizes: Gen[Int] = Gen.range("iters")(1, 20, 1)
  val sizes2: Gen[Int] = Gen.range("iters")(1, 20, 1)

  // Lectura de los conjuntos de datos
  // La variable PX contiene las instancias del conjunto de datos X

  /*
  * Conjuntos de datos:
  *       1 -> a1_raw.csv
  *       2 -> isolet5.data
  *       3 -> data_banknote_authentication.txt
  *       4 -> movement_libras.data
  * */


  val P1 = read("data/a1_raw.csv",',',1,1)
  val P2 = read("data/isolet5.data",',',0,0)
  val P3 = read("data/data_banknote_authentication.txt", ',',0,1)
  val P4 = read("data/movement_libras.data",',',0,0)


  // Valores de K para cada conjunto de datos

  val K1 = 5
  val K2 = 26
  val K3 = 2
  val K4 = 15

  def getK(idDataset: Int) = idDataset match {
    case 0 => K1
    case 1 => K2
    case 2 => K3
    case 3 => K4
  }

  def getInstances(idDataset: Int) = idDataset match {
    case 0 => P1
    case 1 => P2
    case 2 => P3
    case 3 => P4
  }

}


object KmeanTest extends Bench.OfflineReport {

  // Indice del conjunto de datos a usar
  // Cambiar esta variable para usar cualquier otro
  /*
  * Indices:
  *       - 0 -> a1_raw.csv
  *       - 1 -> isolet5.data
  *       - 2 -> data_banknote_authentication.txt
  *       - 3 -> movement_libras.data
  * */

  val DATASET = 3

  performance of "Kmeans"  in {
    measure method "par train" in {
      using(Data2.sizes) curve "Par"  in {
        par.Kmeans(Data2.getK(DATASET),_,Data2.getInstances(DATASET)).train
      }
    }

  }

  performance of "Kmeans" in {
    measure method "train" in {
      using(Data2.sizes2) curve "Secuencial" in {
        Kmeans(Data2.getK(DATASET),_,Data2.getInstances(DATASET)).train
      }
    }
  }


}
