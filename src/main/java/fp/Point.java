package fp;

import com.codepoetics.protonpack.StreamUtils;
import scala.Char;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Clase que representa un punto del Cluster
 */

public class Point implements Serializable {

    /**
     * Valores del punto
     */

    private Supplier<Stream<Double>> values;

    /**
     * Contructor del punto
     * @param val Lista de valores
     */

    public Point(List<Double> val){
        values= () -> val.stream();
    }

    /**
     * Getter de los valores del punto
     * @return Valores del punto
     */

    public Stream<Double> getValues(){return values.get();}

    /**
     * Calculo de la distancia Euclidea a otro punto
     * @param b Otro punto
     * @return Distancia Euclidea
     */

    public double euclidean(Point b){
        double value=StreamUtils
                .zip(values.get(),b.values.get(),(x,y)->Math.pow(x-y,2))
                .reduce(Double::sum).get();
        return Math.pow(value,0.5);
    }

    /**
     * Calculo del error a otro punto
     * @param b Otro punto
     * @return Error
     */

    public double error(Point b){
        double value=StreamUtils
                .zip(values.get(),b.values.get(),(x,y) -> Math.pow(x-y,2))
                .reduce(Double::sum).get();
        return value;
    }

    @Override
    public String toString() {
        ArrayList<Double> values = getValues()
                        .collect(Collectors.toCollection(ArrayList::new));
        return Arrays.toString(values.toArray());
    }
}
