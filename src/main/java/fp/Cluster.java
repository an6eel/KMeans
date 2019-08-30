package fp;

import com.codepoetics.protonpack.StreamUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Clase Cluster con porgramacion funcional
 */

public class Cluster {

    /**
     * Centroide del Cluster
     */

    private Point center;

    /**
     * Lista de puntos pertenecientes al Cluster
     */

    private ArrayList<Point> mem;

    /**
     * Contructor del Cluster
     * @param center Centroide del Cluster
     * @param members Lista de puntos
     */

    public Cluster(Point center, ArrayList<Point> members){
        this.center = new Point(center.getValues().collect(Collectors.toList()));
        this.mem = new ArrayList<Point>(members);
    }

    /**
     * Constructor del Cluster
     * @param center Centroide del Cluster
     */

    public Cluster(Point center){
        this.center = new Point(center.getValues().collect(Collectors.toList()));
        this.mem = new ArrayList<Point>();
    }

    /**
     * Getter del centroide del Cluster
     * @return Centroide del Cluster
     */

    public Point getCenter(){
        return this.center;
    }

    /**
     * Getter de la lista de puntos pertenecientes al Cluster
     * @return Lista de puntos
     */

    public ArrayList<Point> getMembers(){
        return this.mem;
    }

    /**
     * Crea un nuevo cluster cuyo centroide es el
     * punto medio de los puntos pertenecientes al anterior cluster
     * @return Cluster cuyo centroide es el punto
     * medio de los puntos pertenecientes al cluster
     */

    public Cluster moveCenter(){
        List<Stream<Double>> list = mem.stream().map(s-> s.getValues()).collect(Collectors.toList());
        Stream<Double> st=StreamUtils.zip(list,l -> l.stream().reduce(Double::sum).get()).map(s-> s/(1.0*mem.size())).mapToDouble(i->i).boxed();
        return new Cluster(new Point(st.collect(Collectors.toList())));
    }

    /**
     * Calcula la distancia media de los puntos al centroide
     * @return Distancia media de los puntos al centroide
     */

    public double avgDistanceToCentroid(){
        double dist=mem.stream().map(p-> p.euclidean(center))
                .mapToDouble(o->o).average().getAsDouble();
        return dist;
    }

    /**
     * Calcula la distancia de un punto al centroide
     * @param p Punto al que se le calculará la distancia
     * @return Distancia del punto p al centroide
     */

    public double distanceToCentroid(Point p){
        return p.euclidean(center);
    }

    /**
     * Compara si dos clusters son iguales
     * @param obj Cluster con el que comparar
     * @return Comprobacion de si dos clusters son iguales
     */

    @Override
    public boolean equals(Object obj){
        if( obj instanceof sec.Cluster){
            if( center.equals(((sec.Cluster) obj).getCenter()) &&
                    mem.equals(((sec.Cluster) obj).getMembers()))
                return true;
            else
                return false;
        }
        else
            return false;
    }
}
