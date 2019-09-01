
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
     * Lectura de los datos (implementacion imperativa)
     * @param path Ruta relativa del archivo de datos
     * @param n Lineas iniciales a ignorar
     * @param sep Separador
     * @param clase Indica si el conjunto de datos contiene la columna de etiquetas
     * @return Lista de puntos
     */

    static ArrayList<sec.Point> readSec(String path,int n, String sep,boolean clase){
        try{
            BufferedReader in = new BufferedReader(new FileReader(path));
            return in.lines().skip(n).map((line) -> {
                ArrayList<String> splitline = new ArrayList<String>(Arrays.asList(line.split(sep)));
                if(clase)
                    splitline.remove(splitline.size()-1);
                return new sec.Point(splitline.stream().map((elem) -> Double.parseDouble(elem)).collect(Collectors.toCollection(ArrayList::new)));

            }).collect(Collectors.toCollection(ArrayList::new));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Lectura de los datos (implementacion funcional)
     * @param path Ruta relativa del archivo de datos
     * @param n Lineas iniciales a ignorar
     * @param sep Separador
     * @param clase Indica si el conjunto de datos contiene la columna de etiquetas
     * @return Lista de puntos
     */

    static ArrayList<fp.Point> readFP(String path,int n,String sep, boolean clase){
        try{
            BufferedReader in = new BufferedReader(new FileReader(path));
            return in.lines().skip(n).map((line) -> {
                ArrayList<String> splitline = new ArrayList<String>(Arrays.asList(line.split(sep)));
                if(clase)
                    splitline.remove(splitline.size()-1);
                return new fp.Point(splitline.stream().map((elem) -> Double.parseDouble(elem)).collect(Collectors.toCollection(ArrayList::new)));

            }).collect(Collectors.toCollection(ArrayList::new));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }


    public static void main(String... args) throws Exception {
        ArrayList<fp.Point> points = readFP("data/a1.txt",0," ",false);
        fp.Kmeans km= new fp.Kmeans(5,250,points);
        ArrayList<fp.Cluster> train=km.train();
        System.out.println("Funcional");
        for(fp.Cluster cluster: train) {
            System.out.println("Centroide: " + cluster.getCenter().toString() + " Miembros: " + cluster.getMembers().size());
        }

        ArrayList<sec.Point> points2 = readSec("data/a1.txt",0," ",false);
        sec.Kmeans kmsec = new sec.Kmeans(5,250,points2);
        ArrayList<sec.Cluster> train2=kmsec.train();

        System.out.println("Imperativa");
        for(sec.Cluster cluster: train2) {
            System.out.println("Centroide: " + cluster.getCenter().toString() + " Miembros: " + cluster.getMembers().size());
        }


    }
}
