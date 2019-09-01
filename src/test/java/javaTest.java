
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;

import org.scalameter.Aggregator;
import org.scalameter.Context;
import org.scalameter.Measurer;
import org.scalameter.Reporter;
import org.scalameter.japi.ContextBuilder;
import org.scalameter.japi.JBench;
import org.scalameter.japi.JGen;
import org.scalameter.japi.annotation.*;



interface Data {

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

    // Lectura de los conjuntos de datos
    // La variable PX1 contiene las instancias del conjunto de datos X (imperativa)
    // La variable PX2 contiene las instancias del conjunto de datos X (funcional)

    /*
    * Conjuntos de datos:
    *       1 -> a1_raw.csv
    *       2 -> isolet5.data
    *       3 -> data_banknote_authentication.txt
    *       4 -> movement_libras.data
    * */

    ArrayList<sec.Point> P11 = readSec("data/a1_raw.csv",1,",",true);
    ArrayList<fp.Point> P12 = readFP("data/a1_raw.csv",1,",",true);


    ArrayList<sec.Point> P21 = readSec("data/isolet5.data",0,",",true);
    ArrayList<fp.Point> P22 = readFP("data/isolet5.data",0,",",true);

    ArrayList<sec.Point> P31 = readSec("data/data_banknote_authentication.txt",0,",",true);
    ArrayList<fp.Point> P32 = readFP("data/data_banknote_authentication.txt",0,",",true);

    ArrayList<sec.Point> P41 = readSec("data/movement_libras.data",0,",",true);
    ArrayList<fp.Point> P42 = readFP("data/movement_libras.data",0,",",true);

    // Valores de K para cada conjunto de datos

    Integer K1= 5;
    Integer K2 = 26;
    Integer K3 = 2;
    Integer K4 = 15;

    static Integer getK(int idDataset) {
        switch (idDataset) {
            case 0:
                return K1;
            case 1:
                return K2;
            case 2:
                return K3;
            case 3:
                return K4;
        }
        return null;
    }

    static ArrayList<fp.Point> getInstancesFP(int idDataset) {
        switch (idDataset) {
            case 0:
                return P12;
            case 1:
                return P22;
            case 2:
                return P32;
            case 3:
                return P42;
        }
        return null;
    }

    static ArrayList<sec.Point> getInstancesSec(int idDataset) {
        switch (idDataset) {
            case 0:
                return P11;
            case 1:
                return P21;
            case 2:
                return P31;
            case 3:
                return P41;
        }
        return null;
    }

}

public class javaTest extends JBench.OfflineReport {

    // Indice del conjunto de datos a usar
    // Cambiar esta variable para usar cualquier otro
    /*
    * Indices:
    *       - 0 -> a1_raw.csv
    *       - 1 -> isolet5.data
    *       - 2 -> data_banknote_authentication.txt
    *       - 3 -> movement_libras.data
    * */

    public final int DATASET = 1;

    @Override
    public Aggregator<Object> aggregator() {
        return super.aggregator();
    }

    @Override
    public Measurer<Object> measurer() {
        return super.measurer();
    }

    @Override
    public Reporter<Object> reporter() {
        return super.reporter();
    }

    // Generador de iteraciones
    // Configuración actual -> De 1 hasta 20 con saltos de 1

    public final JGen<Integer> sizes = JGen.range("iters", 1, 20, 1);


    @Override
    public Context defaultConfig() {
        return new ContextBuilder()
                .build();
    }


    @gen("sizes")
    @benchmark("kmeans")
    @curve("Functional")
    public int functional(Integer v) {
        int size = v;
        fp.Kmeans kmeans = new  fp.Kmeans(Data.getK(DATASET),size,Data.getInstancesFP(DATASET));

        kmeans.train();

        return v;
    }

    @gen("sizes")
    @benchmark("kmeans")
    @curve("Classic")
    public int classic(Integer v) {
        int size = v;
        sec.Kmeans kmeans = new  sec.Kmeans(Data.getK(DATASET),size,Data.getInstancesSec(DATASET));

        try {
            kmeans.train();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return v;
    }

}
