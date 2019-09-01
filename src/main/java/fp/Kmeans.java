package fp;

import com.codepoetics.protonpack.StreamUtils;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Algoritmo K-means con programacion funcional
 */

public class Kmeans {

    /**
     * Numero de Clusters
     */

    private int K;

    /**
     * Numero maximo de iteraciones
     */

    private int iters;

    /**
     * Lista de puntos a clasificar
     */

    private ArrayList<Point> points;

    /**
     * Constructor de Kmeans
     * @param kvalue Numero de Clusters
     * @param it Numero maximo de iteraciones
     * @param pts Lista de puntos
     */

    public Kmeans(int kvalue,int it,ArrayList<Point> pts){
        K=kvalue;
        iters=it;
        points= new ArrayList<Point>(pts);
    }

    /**
     * Aplicación del algoritmo K-means
     * @return Lista de cluster con los puntos asociados a cada cluster
     */

    public ArrayList<Cluster> train(){
        ArrayList<Cluster> clusters = init(points);
        ArrayList<Cluster> old_clusters = assignToClusters(clusters,points);

        return update(old_clusters,points,0);
    }

    /**
     * Actualiza los clusters moviendo el centroide de cada cluster
     * @param old Clusters anteriores
     * @param pts Lista de puntos a clasificar
     * @param iter Numero de iteracion
     * @return Clusters actualizados
     */

    private ArrayList<Cluster> update(ArrayList<Cluster> old,
                                      ArrayList<Point> pts, int iter){
        ArrayList<Cluster> tmpold=old.stream()
                .map(c -> c.moveCenter())
                .collect(Collectors.toCollection(ArrayList::new));

        ArrayList<Cluster> news=assignToClusters(tmpold,pts);

        if(iter>=iters)
            return news;
        else
            return update(news,pts,iter+1);
    }

    /**
     * Genera K Clusters cuyo centroide es un punto aleatoriamente seleccionado
     * @param pts Lista de puntos
     * @return Clusters iniciales
     */

    private ArrayList<Cluster> init(ArrayList<Point> pts){
        ArrayList<Point> centroid=new Random()
                .ints(K,0,pts.size())
                .mapToObj(ind -> pts.get(ind))
                .collect(Collectors.toCollection(ArrayList::new));
        return centroid.stream()
                .map(cent -> new Cluster(cent))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Calcula el Cluster mas cercano para un punto
     * @param clusters Lista de clusters
     * @param p Punto
     * @return Cluster mas cercano al punto
     */

    private long getNearestNeighbour(ArrayList<Cluster> clusters,Point p){
        return StreamUtils.zipWithIndex(clusters.stream())
            .min(Comparator.comparing(j-> j.getValue()
                    .distanceToCentroid(p))).get().getIndex();
    }

    /**
     * Asignacion de los puntos a cluster mas cercano en cada caso
     * @param clusters Lista de clusters
     * @param pts Lista de puntos
     * @return Lista de clusters con los puntos asignados
     */

    private ArrayList<Cluster> assignToClusters(ArrayList<Cluster> clusters,
                                                ArrayList<Point> pts){
        Map<Long, List<Point>> cl= pts.stream()
                .collect(Collectors
                        .groupingBy(p -> getNearestNeighbour(clusters,p)));
        return cl.keySet().stream()
            .map( c ->  new Cluster(clusters.get(c.intValue())
                    .getCenter(),(ArrayList<Point>) cl.get(c)))
            .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Calcula los indices A de cada cluster, necesarios para calcular el valor silhouette
     * @param clusters Lista de clusters
     * @return Indices A de cada cluster
     */

    private static List<List<Double>> computeA(ArrayList<Cluster> clusters){
         return clusters.stream()
        .map( cl ->
                cl.getMembers().stream()
                        .map(u -> cl.getMembers().stream()
                                .filter(z -> z!=u)
                                .map(z->u.euclidean(z))
                                .mapToDouble(r->r)
                                .sum()/cl.getMembers().size()
                        ).collect(Collectors.toList())
        ).collect(Collectors.toList());
    }

    /**
     * Calcula los indices B de cada cluster, necesarios para calcular el valor silhouette
     * @param clusters Lista de clusters
     * @return Indices B de cada clusters
     */

    private static List<List<Double>> computeB(ArrayList<Cluster> clusters){
        return StreamUtils
        .zipWithIndex(clusters.stream())
        .map( cl ->
        cl.getValue().getMembers().stream().map(pt -> StreamUtils
                .zipWithIndex(clusters.stream()).filter(cl2 ->
                        cl2.getIndex()!=cl.getIndex())
                .map(pt2 -> pt2.getValue().getMembers().stream()
                        .map(y -> y.euclidean(pt)).mapToDouble(no->no)
                        .sum()/pt2.getValue().getMembers().size())
        .mapToDouble(no1->no1)
                .min().getAsDouble())
                .collect(Collectors.toList()))
                .collect(Collectors.toList());
    }

    /**
     * Calculo del valor Silhouette
     * @param clusters Lista de clusters
     * @return Valor silhouette
     */

    public static double silhouette(ArrayList<Cluster> clusters){
        List<List<Double>> a=computeA(clusters);
        Stream<Stream<Double>> as= a.stream().map(l->l.stream());
        List<List<Double>> b = computeB(clusters);
        Stream<Stream<Double>> bs= b.stream().map(l1->l1.stream());
        int npoints=clusters.stream()
                .map(cl -> cl.getMembers()
                        .size()).reduce(Integer::sum).get();

        return StreamUtils
                .zip(as.flatMap(Function.identity())
                        ,bs.flatMap(Function
                                .identity()),(x,y)-> x<y ? 1-(x/y) : (y/x)-1)
                .mapToDouble(s1->s1).sum()/npoints;

    }

    /**
     * Calculo del error medio de los puntos del cluster a su centroide
     * @param clusters Lista de clusters
     * @return Distorcion media
     */

    public static double distortion(ArrayList<Cluster> clusters){
       return clusters.stream()
               .map(x -> x.getMembers().stream()
                       .map(pt -> pt.error(x.getCenter()))
                       .reduce(Double::sum)
                       .get()).reduce(Double::sum).get();
    }

    /**
     * Calculo del indice Davies-Bouldin
     * @param clusters Lista de clusters
     * @return Indice Davies-Bouldin
     */

    public static double daviesBouldin(ArrayList<Cluster> clusters){
        return StreamUtils.zipWithIndex(clusters.stream())
        .map(cluster1 -> StreamUtils
                .zipWithIndex(clusters.stream())
            .filter(cluster2-> cluster2
                    .getIndex()!=cluster1.getIndex())
                .map(clcmp ->
                    (clcmp.getValue()
                            .avgDistanceToCentroid()+cluster1
                            .getValue()
                            .avgDistanceToCentroid())/cluster1
                            .getValue()
                            .getCenter()
                            .euclidean(clcmp.getValue().getCenter()))
                .max(Double::compareTo))
        .mapToDouble(id->id.get()).sum()/clusters.size();
    }

    /**
     * Calculo del mejor valor K de clusters para clasificar los
     * puntos dados en funcion de un indice dado
     * @param points Lista de puntos
     * @param oper Indice utilizado para comparar
     * @param maxk Maximo valor K a comparar
     * @return Mejor valor de K
     */

    private static ArrayList<Double> bestKValue(
            ArrayList<fp.Point> points,
            Function<ArrayList<fp.Cluster>,
                    Double> oper,int maxk){
        return IntStream.range(2,maxk)
        .mapToDouble( value -> oper
                .apply(new fp.Kmeans(value,250,points).train())).
        boxed().collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Calculo del mejor valor K utilizando el indice silhouette
     * @param points Lista de puntos
     * @param maxk Maximo valor de K a comparar
     * @return Mejor valor de K
     */

    public static ArrayList<Double> silhouetteMethod(ArrayList<Point> points,
                                                     int maxk){
        return bestKValue(points,fp.Kmeans::silhouette,maxk);
    }

    /**
     * Calculo del mejor valor K utilizando la distorcion como indice
     * @param points Lista de puntos
     * @param maxk Maximo valor de K a comparar
     * @return Mejor valor de K
     */

    public static ArrayList<Double> elbowMethod(ArrayList<Point> points,int maxk){
        return bestKValue(points,fp.Kmeans::distortion,maxk);
    }
}
