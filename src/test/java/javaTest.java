
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

    ArrayList<sec.Point> P11 = readSec("data/a1_raw.csv",1,",",true);
    ArrayList<fp.Point> P12 = readFP("data/a1_raw.csv",1,",",true);


    ArrayList<sec.Point> P21 = readSec("data/isolet5.data",0,",",true);
    ArrayList<fp.Point> P22 = readFP("data/isolet5.data",0,",",true);

    ArrayList<sec.Point> P31 = readSec("data/data_banknote_authentication.txt",0,",",true);
    ArrayList<fp.Point> P32 = readFP("data/data_banknote_authentication.txt",0,",",true);

    ArrayList<sec.Point> P41 = readSec("data/movement_libras.data",0,",",true);
    ArrayList<fp.Point> P42 = readFP("data/movement_libras.data",0,",",true);

    Integer K1= 5;
    Integer K2 = 26;
    Integer K3 = 2;
    Integer K4 = 15;


}

public class javaTest extends JBench.OfflineReport {



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

    public final JGen<Integer> sizes = JGen.range("iters", 1, 10, 1);


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
        fp.Kmeans kmeans = new  fp.Kmeans(Data.K3,size,Data.P32);

        kmeans.train();

        return v;
    }

    @gen("sizes")
    @benchmark("kmeans")
    @curve("Classic")
    public int classic(Integer v) {
        int size = v;
        sec.Kmeans kmeans = new  sec.Kmeans(Data.K3,size,Data.P31);

        try {
            kmeans.train();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return v;
    }

}
