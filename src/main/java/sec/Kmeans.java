package sec;

import java.util.*;
import java.util.function.Function;

/**
 * Algoritmo K-means con programacion imperativa
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

    public ArrayList<Cluster> train() throws Exception {
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

    private ArrayList<Cluster> update(ArrayList<Cluster> old, ArrayList<Point> pts, int iter) throws Exception {

        ArrayList<Cluster> tmp=new ArrayList<>();

        for(Cluster c:old)
            tmp.add(c.moveCluster());

        ArrayList<Cluster> news=assignToClusters(tmp,pts);

        if(iter>=iters )
            return news;
        else
            return update(news,pts,iter+1);
    }

    /**
     * Genera K Clusters cuyo centroide es un punto aleatoriamente seleccionado
     * @param pts Lista de puntos
     * @return Clusters iniciales
     */

    public ArrayList<Cluster> init(ArrayList<Point> pts){

        ArrayList<Cluster> clusters = new ArrayList<Cluster>();
        ArrayList<Integer> index= new ArrayList<>();
        Random r= new Random();

        while(index.size()!=K){
            int n=r.nextInt(pts.size());
            if(!index.contains(n))
                index.add(n);
        }

        for(Integer n:index)
            clusters.add(new Cluster(pts.get(n)));

        return clusters;
    }

    /**
     * Calcula el Cluster mas cercano para un punto
     * @param clusters Lista de clusters
     * @param p Punto
     * @return Cluster mas cercano al punto
     */

    private int getNearestNeighbour(ArrayList<Cluster> clusters, Point p) throws Exception {

        int ind=-1;
        double dist=Double.MAX_VALUE;

        for(int i=0;i<clusters.size();++i){
            double d= clusters.get(i).distanceToCentroid(p);
            if(d<dist){
                dist=d;
                ind=i;
            }
        }

        return ind;
    }

    /**
     * Asignacion de los puntos a cluster mas cercano en cada caso
     * @param cl Lista de clusters
     * @param pts Lista de puntos
     * @return Lista de clusters con los puntos asignados
     */

    private ArrayList<Cluster> assignToClusters(ArrayList<Cluster> cl,ArrayList<Point> pts) throws Exception {

        HashMap<Integer,ArrayList<Point>> cluster=new HashMap<Integer, ArrayList<Point>>();
        ArrayList<Cluster> cls= new ArrayList<Cluster>();

        for(Point p:pts){
            int ind=getNearestNeighbour(cl,p);
            if(cluster.containsKey(ind))
                cluster.get(ind).add(p);
            else{
                ArrayList<Point> c= new ArrayList<>();
                c.add(p);
                cluster.put(ind,c);
            }

        }

        for(Integer k:cluster.keySet())
            cls.add(new Cluster(cl.get(k).getCenter(),cluster.get(k)));

        return cls;
    }

    /**
     * Calculo de la distancia media de un punto a un Cluster
     * @param p Punto
     * @param cl Cluster
     * @param ind Indice a ignorar
     * @return Distancia Media
     * @throws Exception Si existe alguna comparacion
     * entre puntos de diferente dimension
     */

    private static double avgDistanceToCluster(Point p, Cluster cl, int ind) throws Exception {

        double dist=0.0;

        for(Point p2:cl.getMembers()){
            if(ind==-1 || cl.getMembers().indexOf(p2)!=ind)
                dist+=p.euclidean(p2);
        }
        return dist/cl.getMembers().size();
    }

    /**
     * Calcula los indices A de cada cluster, necesarios
     * para calcular el valor silhouette
     * @param clusters Lista de Clusters
     * @return Indices A de los clusters
     * @throws Exception Si existe alguna comparacion
     * entre puntos de diferente dimension
     */
    private static List<List<Double>> computeA(ArrayList<Cluster> clusters) throws Exception {

        List<List<Double>> lista= new ArrayList<>();

        for(int i=0;i<clusters.size();++i){
            ArrayList<Double> tmp = new ArrayList<>();

            for(int j=0;j<clusters.get(i).getMembers().size();++j){
                tmp.add(avgDistanceToCluster(clusters
                        .get(i).getMembers().get(j), clusters.get(i),j));
            }
            lista.add(tmp);
        }

        return lista;
    }

    /**
     * Calcula los indices B de cada cluster, necesarios
     * para calcular el valor silhouette
     * @param clusters Lista de Clusters
     * @return Indice B de los clusters
     * @throws Exception Si existe alguna comparacion
     * entre puntos de diferente dimension
     */

    private static List<List<Double>> computeB(ArrayList<Cluster> clusters) throws Exception {

        List<List<Double>> lista= new ArrayList<>();
        for(int i=0;i<clusters.size();++i){
            ArrayList<Double> tmp = new ArrayList<>();
            for(int j=0;j<clusters.get(i).getMembers().size();++j){
              double dist=Double.MAX_VALUE;
              for(int k=0;k<clusters.size();++k){
                  if(i!=k){
                      double tmp1=avgDistanceToCluster(clusters.get(i)
                              .getMembers().get(j),clusters.get(k),-1);
                      dist= tmp1<dist ? tmp1 : dist;
                  }
              }
              tmp.add(dist);

            }
            lista.add(tmp);
        }

        return lista;
    }

    /**
     * Calculo del valor Silhouette
     * @param clusters Lista de clusters
     * @return Valor silhouette
     */

    public static double silhouette(ArrayList<Cluster> clusters){
        List<List<Double>> a= null;
        try {
            a = computeA(clusters);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<List<Double>> b= null;
        try {
            b = computeB(clusters);
        } catch (Exception e) {
            e.printStackTrace();
        }
        double result=0.0;
        int npoints=0;
        for(int i=0;i<a.size();++i){
            npoints+=a.get(i).size();
            for(int j=0;j<b.get(i).size();++j){
                double x=a.get(i).get(j);
                double y=b.get(i).get(j);
                double tmp= x<y ? 1-(x/y) : (y/x)-1;
                result+=tmp;
            }
        }
        return result/npoints;
    }

    /**
     * Calculo del error medio de los puntos del cluster a su centroide
     * @param clusters Lista de clusters
     * @return Distorcion media
     */

    public static double distortion(ArrayList<Cluster> clusters){

        double err=0.0;
        for(int i=0;i<clusters.size();++i){
            for(int j=0;j<clusters.get(i).getMembers().size();++j){
                try {
                    err+=clusters.get(i).getMembers().get(j)
                            .error(clusters.get(i).getCenter());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return err;
    }

    /**
     * Calculo del indice Davies-Bouldin
     * @param clusters Lista de clusters
     * @return Indice Davies-Bouldin
     * @throws Exception Si existe alguna comparacion
     * entre puntos de diferente dimension
     */

    public static double daviesBouldin(ArrayList<Cluster> clusters) throws Exception {

        double davis= 0.0;

        for (int i= 0; i < clusters.size(); i++){
            double sum_avgs= clusters.get(i).avgDistanceToCentroid() +
                    clusters.get((i+1) % clusters.size())
                            .avgDistanceToCentroid();

            double dist= clusters.get(i)
                    .getCenter()
                    .euclidean(clusters.get((i+1)%clusters.size())
                            .getCenter());
            double max_value= sum_avgs / dist;

            for (int j= (i+2) % clusters.size(); j != i; j= (j+1) % clusters.size()){
                sum_avgs= clusters.get(i).avgDistanceToCentroid() +
                        clusters.get(j).avgDistanceToCentroid();

                dist= clusters.get(i).getCenter()
                        .euclidean(clusters.get(j).getCenter());

                if (max_value < (sum_avgs / dist)) max_value= sum_avgs / dist;
            }

            davis += max_value;
        }

        return davis / clusters.size();
    }

    /**
     * Calculo del mejor valor K de clusters para clasificar los
     * puntos dados en funcion de un indice dado
     * @param points Lista de puntos
     * @param oper Indice utilizado para comparar
     * @param maxk Maximo valor K a comparar
     * @return Mejor valor de K
     */

    private static ArrayList<Double> bestKValue(ArrayList<Point> points, Function<ArrayList<Cluster>,Double> oper, int maxk) throws Exception {

        ArrayList<Double> list= new ArrayList<>();

        for(int i=2;i<maxk;++i){
            Kmeans km=new sec.Kmeans(i,250,points);
            ArrayList<Cluster> tr=km.train();
            list.add(oper.apply(tr));
        }
        return list;
    }

    /**
     * Calculo del mejor valor K utilizando el indice silhouette
     * @param points Lista de puntos
     * @param maxk Maximo valor de K a comparar
     * @return Mejor valor de K
     */

    public static ArrayList<Double> silhouetteMethod(ArrayList<Point> points,int maxk) {

        ArrayList<Double> value = null;
        try {
            value = bestKValue(points, Kmeans::silhouette, maxk);
        }catch (Exception e){
            e.printStackTrace();
        }
        return value;
    }

    /**
     * Calculo del mejor valor K utilizando la distorcion como indice
     * @param points Lista de puntos
     * @param maxk Maximo valor de K a comparar
     * @return Mejor valor de K
     */

    public static ArrayList<Double> elbowMethod(ArrayList<Point> points,int maxk){
        ArrayList<Double> value = null;
        try {
            value =bestKValue(points,Kmeans::distortion,maxk);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }
}
