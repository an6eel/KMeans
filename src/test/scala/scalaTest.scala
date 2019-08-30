import java.io.{BufferedReader, File, FileNotFoundException, FileReader}
import kmeans.utils.CPoint
import kmeans.sec._
import kmeans.par
import org.scalameter.Bench
import org.scalameter.api._



object Data2 {

  def read(path: String,sep : Char, header : Int, clase : Int): List[CPoint] = {
    scala.io.Source
      .fromFile(new File(path))
      .getLines().drop(header).toList
      .map(_.split(sep))
      .map(_.dropRight(clase).toList.map(_.toDouble))
  }


  val sizes: Gen[Int] = Gen.range("iters")(1, 1, 1)
  val sizes2: Gen[Int] = Gen.range("iters")(1, 1, 1)

  val P1 = read("data/a1_raw.csv",',',1,1)
  val P2 = read("data/isolet5.data",',',0,0)
  val P3 = read("data/data_banknote_authentication.txt", ',',0,1)
  val P4 = read("data/movement_libras.data",',',0,0)

  val K1 = 5
  val K2 = 26
  val K3 = 2
  val K4 = 15

}


object KmeanTest extends Bench.LocalTime {

  performance of "Kmeans"  in {
    measure method "par train" in {
      using(Data2.sizes) curve "Par"  in {
        par.Kmeans(Data2.K3,_,Data2.P3).train
      }
    }

  }

 /* performance of "Kmeans" in {
    measure method "train" in {
      using(Data2.sizes2) curve "Secuencial" in {
        Kmeans(Data2.K3,_,Data2.P3).train
      }
    }
  }*/


}
