package sec;

import java.util.ArrayList;

/**
 * Clase Cluster con porgramacion imperativa
 */

public class Cluster {

    /**
     * Centroide del Cluster
     */

    private Point center;

    /**
     * Lista de puntos pertenecientes al Cluster
     */

    private ArrayList<Point> members;

    /**
     * Constructor del Cluster
     * @param point Centroide del Cluster
     */

    public Cluster(Point point){
        center=new Point(point.getValues());
        members=new ArrayList<Point>();
    }

    /**
     * Contructor del Cluster
     * @param pt Centroide del Cluster
     * @param mbrs Lista de puntos
     */

    public Cluster(Point pt,ArrayList<Point> mbrs){
        center= new Point(pt.getValues());
        members = new ArrayList<Point>(mbrs);
    }

    /**
     * Getter del centroide del Cluster
     * @return Centroide del Cluster
     */

    public Point getCenter() {
        return center;
    }

    /**
     * Getter de la lista de puntos pertenecientes al Cluster
     * @return Lista de puntos
     */

    public ArrayList<Point> getMembers() {
        return members;
    }

    /**
     * Crea un nuevo cluster cuyo centroide es el punto medio
     * de los puntos pertenecientes al anterior cluster
     * @return Cluster cuyo centroide es el punto medio
     * de los puntos pertenecientes al cluster
     */

    public Cluster moveCluster(){
        if(members.size()==0)
            return this;

        ArrayList<Double> mean=new ArrayList<Double>();

        for(int i=0;i<center.getValues().size();++i){
            double m= 0.0;
            for(Point p:members)
                m+=(p.getValues().get(i));

            mean.add(m/members.size());
        }

        return new Cluster(new Point(mean));
    }

    /**
     * Calcula la distancia de un punto al centroide
     * @param p Punto al que se le calculará la distancia
     * @return Distancia del punto p al centroide
     * @throws Exception Si los puntos tienen diferente dimension
     */

    public double distanceToCentroid(Point p) throws Exception {
        return p.euclidean(center);
    }

    /**
     * Calcula la distancia media de los puntos al centroide
     * @return Distancia media de los puntos al centroide
     */

    public double avgDistanceToCentroid() throws Exception {
        double dist=0.0;
        for(Point p:members)
            dist+=p.euclidean(center);
        return dist/members.size();
    }

    /**
     * Compara si dos clusters son iguales
     * @param obj Cluster con el que comparar
     * @return Comprobacion de si dos clusters son iguales
     */

    @Override
    public boolean equals(Object obj){
        if( obj instanceof Cluster){
            if( center.equals(((Cluster) obj).getCenter())
                    && members.equals(((Cluster) obj).getMembers()))
                return true;
            else
                return false;
        }
        else
            return false;
    }
}
