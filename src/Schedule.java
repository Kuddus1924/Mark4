import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;

import java.util.ArrayList;
import java.util.List;

public class Schedule {
    public static void print (ArrayList<Double> array, ArrayList<Double> array1,String name, int k) {

        ArrayList<Integer> list = new ArrayList<>();
        for(int i = 0; i < k;i++)
        {
            list.add(i);
        }

        XYChart chart = new XYChartBuilder().width(800).height(600).title(name).xAxisTitle("x").yAxisTitle("y").build();

        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
        chart.getStyler().setHasAnnotations(false);
        chart.getStyler().setPlotGridLinesVisible(false);

        chart.addSeries("теоретическая", list, array);
        chart.addSeries("Практическая", list,array1);
        List<XYChart> charts = new ArrayList<XYChart>();
        charts.add(chart);
        new SwingWrapper<XYChart>(charts).displayChartMatrix();

    }
    public static void print4 (ArrayList<Double> array, ArrayList<Double> array1,ArrayList<Double> array2, ArrayList<Double> array3,String name, int k) {

        ArrayList<Integer> list = new ArrayList<>();
        for(int i = 0; i < k;i++)
        {
            list.add(i);
        }

        XYChart chart = new XYChartBuilder().width(800).height(600).title(name).xAxisTitle("x").yAxisTitle("y").build();

        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
        chart.getStyler().setHasAnnotations(false);
        chart.getStyler().setPlotGridLinesVisible(false);

        chart.addSeries("Практическая", list, array);
        chart.addSeries("Верхняя", list,array1);
        chart.addSeries("Нижняя1", list, array2);
        chart.addSeries("Нижняя2", list,array3);
        List<XYChart> charts = new ArrayList<XYChart>();
        charts.add(chart);
        new SwingWrapper<XYChart>(charts).displayChartMatrix();

    }
}
