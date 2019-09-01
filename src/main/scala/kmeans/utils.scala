package kmeans

/**
  * Objeto que contiene la definicion del punto del Cluster y metodos
  * utiles para trabajar sobre los puntos de un cluster
  */
object utils {

  /**
    * Punto de un Cluster
    */

  type CPoint = List[Double]

  /**
    * Calculo de la distancia Minkowski entre dos puntos
    * @param x Primer punto
    * @param y Segundo punto
    * @param p Exponente
    * @return Distancia exponente
    */

  def minkowski(x:CPoint,y:CPoint,p:Int):Double = {
    require(x.length==y.length, "Puntos deben tener la misma dimensión")
    Math.pow((x,y).zipped.map((u,s)=> Math.pow(u-s,p)).sum,1.0/p)
  }

  /**
    * Calculo de la distancia Manhattan entre dos puntos
    * @param x Primer punto
    * @param y Segundo punto
    * @return Distancia Manhattan
    */
  def manhattan(x:CPoint,y:CPoint):Double = minkowski(x,y,1)

  /**
    * Calculo de la distancia Euclidea entre dos puntos
    * @param x Primer punto
    * @param y Segundo punto
    * @return Distancia Euclidea
    */

  def euclidean(x:CPoint,y:CPoint):Double = minkowski(x,y,2)

  /**
    * Calculo de la Similitud Coseno entre dos puntos
    * @param x Primer punto
    * @param y Segundo punto
    * @return Similitud Coseno
    */

  def cosine(x:CPoint,y:CPoint):Double = {
    dotProduct(x,y)/(Math.sqrt(dotProduct(x,x))*(Math.sqrt(dotProduct(y,y))))
  }

  /**
    * Calculo del Producto Escalar de dos puntos
    * @param x Primer punto
    * @param y Segundo punto
    * @return Producto Escalar
    */

  def dotProduct(x:CPoint,y:CPoint):Double = {
    (x,y).zipped.map(_*_).sum
  }

  /**
    * Calculo del error de un punto a otro
    * @param x Primer punto
    * @param y Segundo punto
    * @return Error
    */

  def error(x:CPoint,y:CPoint):Double = {
    require(x.length==y.length, "Puntos deben tener la misma dimensión")
    (x,y).zipped.map((u,s) => Math.pow(u-s,2)).sum
  }
}
