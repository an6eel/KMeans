package kmeans.par

import kmeans.utils.CPoint

import scala.annotation.tailrec
import scala.util.Random
import kmeans.utils._


/**
  * Algoritmo K-means paralelizado
  * @param K Numero de clusters
  * @param iters Numero máximo de iteraciones
  * @param points Lista de puntos a clasificar
  */

class Kmeans(K:Int,iters:Int,points:List[CPoint]){

  /**
    * Aplicación del algoritmo K-means
    * @return Lista de cluster con los puntos asociados a cada cluster
    */

  def train:List[Cluster] = {
    val clusters = init(points)
    val old_clusters = assignClusters(clusters,points)

    /**
      * Actualiza los clusters moviendo el centroide de cada cluster
      * @param oldClusters Clusters anteriores
      * @param pts Lista de puntos a clasificar
      * @param iter Numero de iteracion
      * @return Clusters actualizados
      */

    @tailrec
    def update(oldClusters:List[Cluster], pts:List[CPoint],iter:Int):List[Cluster] = {

      // Para cada cluster generamos uno nuevo cambiando el centroide
      // La accion de cambiar el centroide es una operacion costosa que en este caso se paraleliza

      val tmpcluster = old_clusters.par.map(_.moveCenter).toList
      val newClusters = assignClusters(tmpcluster,pts)  // Asignamos los puntos a los nuevos clusters calculados previamente

      if(iter>=iters )
        newClusters
      else
        update(newClusters,pts,iter+1)
    }
    update(old_clusters,points,0)
  }

  /**
    * Genera K Clusters cuyo centroide es un punto aleatoriamente seleccionado
    * @param points Lista de puntos
    * @return Clusters iniciales
    */

  def init(points:List[CPoint]):List[Cluster] = {

    val rand = new Random()
    val centroids = (0 until K)
      .map({ u => points(rand.nextInt(points.size))})
      .toList

    centroids.aggregate(List[Cluster]())( // Obtenemos una lista de K clusters con centroides aleatorios
      (u,t) => Cluster(t)::u, _:::_
    )
  }

  /**
    * Calcula el Cluster mas cercano para un punto
    * @param clusters Lista de clusters
    * @param x Punto
    * @return Cluster mas cercano al punto
    */

  private def getNearestCluster(clusters:List[Cluster],x:CPoint):Int = {

    // Dado un punto X obtenemos el indice del Cluster que esta mas cercano
    // Hacemos el zip de los clusters con los indices para obtenerlo posteriormente
    // y recorriendo la lista de clusters obtenemos el indice que queremos

    clusters
      .zipWithIndex
      ./:((Double.MaxValue, 0)) {
          case (p, (c, n)) =>
            val measure = c.distanceToCentroid(x)
            if (measure < p._1) (measure, n) else p
      }._2
  }

  /**
    * Asignacion de los puntos a cluster mas cercano en cada caso
    * @param clusters Lista de clusters
    * @param points Lista de puntos
    * @return Lista de clusters con los puntos asignados
    */

  private def assignClusters(clusters: List[Cluster], points: List[CPoint]):List[Cluster] = {

    // En este caso se paraleliza la agrupacion de los puntos en los diferentes clusters

    points
      .par
      .groupBy(getNearestCluster(clusters,_)) // Agrupamos todos los puntos por el indice del cluster mas cercano
      .map({
        case (id_cluster,pts) =>
          Cluster(clusters(id_cluster).center,pts.toList) // Para cada grupo creamos un cluster con la lista de puntos correspondiente
      })
      .toList
  }
}

/**
  * Factory object para crear [[Kmeans]] objetos
  */

object Kmeans {

  /**
    * Crea un objeto [[Kmeans]] dado un valor K de clusters, un valor iters máximo de iteraciones y una lista de puntos points.
    * @param K Numero de clusters
    * @param iters Numero maximo de iteraciones
    * @param points Lista de puntos
    * @return Objeto Kmeans
    */

  def apply(K: Int, iters: Int, points: List[CPoint]): Kmeans = new Kmeans(K, iters, points)

  /**
    * Calcula los indices A de cada cluster, necesarios para calcular el valor silhouette
    * @param cls Lista de clusters
    * @return Indices A de cada cluster
    */

  private def computeA(cls:List[Cluster]):List[List[Double]] = {

    // Para cada punto calculamos la distancia media al resto de puntos del mismo cluster

    cls.par
      .map(x=> x.members.par
        .map(u => x.members
          .filter(_!=u).par
          .map(euclidean(_,u))
          .sum/x.members.size)
        .toList)
      .toList
  }

  /**
    * Calcula los indices B de cada cluster, necesarios para calcular el valor silhouette
    * @param cls Lista de clusters
    * @return Indices B de cada clusters
    */

  private def computeB(cls:List[Cluster]):List[List[Double]] = {

    // Para cada cluster calculamos la minima distancia media a los otros clusters

    cls
      .zipWithIndex.par
      .map( x=> x._1.members.par
        .map(u => cls
          .zipWithIndex.filter(_._2!=x._2).par
          .map(y => y._1.members.par
            .map(euclidean(_,u))
            .sum/y._1.members.size)
        .min
      ).toList
    ).toList
  }

  /**
    * Calculo del valor Silhouette
    * @param cls Lista de clusters
    * @return Valor silhouette
    */

  def silhouette(cls:List[Cluster]):Double = {

    // Calculamos el valor silhouette usando los valores A y B calculados previamente

    val a=computeA(cls)
    val b=computeB(cls)
    val npoints = cls.par
      .map(_.members.size)
      .sum

    (a.flatten,b.flatten).zipped.par.map{
      case (x,y) if x<y => 1-(x/y)
      case (x,y) if x==y => 0
      case (x,y) if x>y => (y/x)-1
    }.sum/npoints
  }

  /**
    * Calculo del error medio de los puntos del cluster a su centroide
    * @param cls Lista de clusters
    * @return Distorcion media
    */

  def distortion(cls:List[Cluster]):Double = {

    // Error relativo de los puntos de cada cluster a su centroide

    cls.par.map(u => u.members.par.map(error(_,u.center)).sum).sum
  }

  /**
    * Calculo del indice Davies-Bouldin
    * @param cls Lista de clusters
    * @return Indice Davies-Bouldin
    */

  def daviesBouldin(cls:List[Cluster]):Double = {

    cls.zipWithIndex.par.map( u=> cls
      .zipWithIndex
      .filter(_._2!=u._2)
      .par.map { v =>
        val sum= v._1.avgDistanceCentroid + u._1.avgDistanceCentroid
        sum/euclidean(u._1.center,v._1.center)
      }.max
    ).sum/cls.size
  }

  /**
    * Calculo del indice Dunn
    * @param cls Lista de clusters
    * @return Indice Dunn
    */

  def dunnIndex(cls:List[Cluster]):Double = {

    val inter=cls
      .combinations(2)
      .collect{case List(a,b)=> euclidean(a.center,b.center)}
      .toList.min
    val intra=cls.par.map(u=>
      u.members
        .combinations(2)
        .collect{case List(c,d)=> euclidean(c,d)}.toList.max
    ).max
    inter/intra
  }

  /**
    * Calculo del mejor valor K de clusters para clasificar los puntos dados en funcion de un indice dado
    * @param pts Lista de puntos
    * @param op Indice utilizado para comparar
    * @param maxK Maximo valor K a comparar
    * @return Mejor valor de K
    */

  private def bestKvalue(pts:List[CPoint], op:List[Cluster]=> Double, maxK:Int=10):List[List[Double]]={

    // Se prueba desde todos los K desde 2 hasta maxK aplicandole una operacion al resultado que nos devuelva
    // un indice de evaluacion, de manera que obtengamos un lista de parejas [K,valoracion]

    (2 to maxK).par.map { u=>
      val model=Kmeans(u,250,pts)
      val cl = model.train
      List(u,op(cl))
    }.toList
  }

  /**
    * Calculo del mejor valor K utilizando el indice silhouette
    * @param pts Lista de puntos
    * @param maxK Maximo valor de K a comparar
    * @return Mejor valor de K
    */

  def silhouetteMethod(pts:List[CPoint],maxK:Int=10):List[List[Double]] = {
    bestKvalue(pts,Kmeans.silhouette,maxK)
  }

  /**
    * Calculo del mejor valor K utilizando la distorcion como indice
    * @param pts Lista de puntos
    * @param maxK Maximo valor de K a comparar
    * @return Mejor valor de K
    */

  def elbowMethod(pts:List[CPoint],maxK:Int=10):List[List[Double]] = {
    bestKvalue(pts,distortion,maxK)
  }
}
