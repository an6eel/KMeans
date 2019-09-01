package sec;

import java.io.Serializable;
import java.util.ArrayList;
import java.lang.*;

/**
 * Clase que representa un punto del Cluster
 */

public class Point implements Serializable {

    /**
     * Valores del punto
     */

    private ArrayList<Double> values;

    /**
     * Contructor del punto
     * @param val Lista de valores
     */

    public Point(ArrayList<Double> val){
        values= (ArrayList<Double>) val.clone();
    }

    /**
     * Getter de los valores del punto
     * @return Valores del punto
     */

    public ArrayList<Double> getValues(){
        return values;
    }

    /**
     * Calculo de la distancia Euclidea a otro punto
     * @param other Otro punto
     * @return Distancia Euclidea
     */

    public double euclidean(Point other) throws Exception {
        if(other.getValues().size() != values.size())
            throw new Exception(
                    "Puntos deben tener el mismo numero de valores"
            );

        double dist=0;
        for(int i=0;i<values.size();++i)
            dist+=(Math.pow(values.get(i)-
                    other.getValues().get(i),2));

        return Math.pow(dist,0.5);
    }

    /**
     * Calculo del error a otro punto
     * @param other Otro punto
     * @return Error
     */

    public double error(Point other) throws Exception {
        return Math.pow(euclidean(other),2);
    }

}
