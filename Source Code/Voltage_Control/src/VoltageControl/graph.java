/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VoltageControl;



import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class graph {
    public static void main(String[] arge){
        
        //window
        JFrame window = new JFrame();
        window.setTitle("Graph");
        window.setSize(600,400);
        //window.setLayout(new BorderLayout());
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //drop-down box
        JComboBox<String> portList = new JComboBox<String>();
        JButton connectBT = new JButton("Connect");
        JPanel topPanel = new JPanel();
        topPanel.add(portList);
        topPanel.add(connectBT);
        window.add(topPanel,BorderLayout.NORTH );
        
        // create the line graph
        XYSeries series = new XYSeries("Reading");
        XYSeriesCollection dataset = new XYSeriesCollection(series);
        JFreeChart chart = ChartFactory.createXYAreaChart("", "Time", "DC Reading", dataset, PlotOrientation.VERTICAL, true, true, false);
        window.add(new ChartPanel(chart),BorderLayout.BEFORE_LINE_BEGINS);
        
        window.setVisible(true);
        
    }

}