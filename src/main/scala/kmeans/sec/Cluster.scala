package kmeans.sec

import kmeans.utils._

/**
  * Cluster con un centroide y una lista de puntos asignados
  * @param center Centroide del cluster
  * @param members Lista de puntos del cluster
  */

class Cluster(val center:CPoint,val members:List[CPoint]) {

  /**
    * Crea un nuevo cluster cuyo centroide es el punto medio de los puntos pertenecientes al anterior cluster
    * @return Cluster cuyo centroide es el punto medio de los puntos pertenecientes al cluster
    */

  def moveCenter:Cluster = {
    val point:CPoint = members
      .transpose // Obtenemos la traspuesta de la matriz
      .map(_.sum) // Para cada fila obtenemos la suma de sus valores
      .map(_/members.size) // Dividiendo entre el numero de puntos obtenemos el centroide
    Cluster(point)
  }

  /**
    * Calcula la distancia media de los puntos al centroide
    * @return Distancia media de los puntos al centroide
    */

  def avgDistanceCentroid:Double = {
    members
      .map(euclidean(center,_)) // Para cada punto obtenemos la distancia al centroide
      .sum/members.size // Obtenemos la distancia media
  }

  /**
    * Calcula la distancia de un punto al centroide
    * @param x Punto al que se le calculará la distancia
    * @return Distancia del punto x al centroide
    */

  def distanceToCentroid(x:CPoint):Double = euclidean(center,x)

  /**
    * Compara si dos clusters son iguales
    * @param obj Cluster con el que comparar
    * @return Comprobacion de si dos clusters son iguales
    */

  override def equals(obj: Any): Boolean = obj match {
    case (obj:Cluster) =>
      this.center == obj.center && this.members == obj.members
    case _ => false
  }
}

/** Factory Object para crear [[Cluster]] objetos */

object Cluster {

  /**
    * Crea un nuevo cluster dado un centroide
    * @param center Centroide del cluster
    * @return Cluster con un centroide asignado
    */

  def apply(center:CPoint) = new Cluster(center,Nil)

  /**
    * Crea un nuevo cluster dado un centroide y una lista de puntos
    * @param center Centroide del cluster
    * @param members Lista de puntos
    * @return Cluster con un centroide y una lista de puntos asignados
    */

  def apply(center:CPoint,
            members:List[CPoint])  = new Cluster(center,members)
}
