
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Aplicacion para la ejecucion del Algoritmo Kmeans
 */

public class main {

    /**
     * Lectura de los datos con programacion funcional
     * @param path Ruta relativa del archivo
     * @return Lista de puntos de datos
     */
    public static ArrayList<fp.Point> readFP(String path){
        try{
            BufferedReader in = new BufferedReader(new FileReader(path));
            return in.lines().map( line ->
                    Arrays.asList(line.split(" ")).stream().map(w -> Double.parseDouble(w))
                    ).map(value -> new fp.Point(value.collect(Collectors.toList())))
                    .collect(Collectors.toCollection(ArrayList::new));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Lectura de los datos con programacion imperativa
     * @param path Ruta relativa del archivo
     * @return Lista de puntos de datos
     */

    public static ArrayList<sec.Point> readSec(String path){
        try{
            BufferedReader in = new BufferedReader(new FileReader(path));
            return in.lines().map( line ->
                    Arrays.asList(line.split(" ")).stream().map(w -> Double.parseDouble(w))
            ).map(value -> new sec.Point(value.collect(Collectors.toCollection(ArrayList::new))))
                    .collect(Collectors.toCollection(ArrayList::new));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }


    public static void main(String... args) throws Exception {
        ArrayList<fp.Point> points = readFP("/home/angel/Escritorio/TFG/Kmeans/data/a1.txt");
        fp.Kmeans km= new fp.Kmeans(5,250,points);
        ArrayList<fp.Cluster> train=km.train();
        fp.Kmeans.elbowMethod(points,10).stream().forEach(s->System.out.println(s));

        ArrayList<sec.Point> points2 = readSec("/home/angel/Escritorio/TFG/Kmeans/data/a1.txt");
        sec.Kmeans kmsec = new sec.Kmeans(5,250,points2);
        ArrayList<sec.Cluster> train2=kmsec.train();
       // sec.Kmeans.elbowMethod(points2,10).stream().forEach(s->System.out.println(s));


    }
}
