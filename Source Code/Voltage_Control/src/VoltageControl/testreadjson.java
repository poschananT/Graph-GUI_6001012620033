package VoltageControl;
import com.fazecast.jSerialComm.SerialPort;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import java.util.Scanner;
import org.jfree.chart.plot.XYPlot;
import javax.sound.sampled.*;
import org.jfree.chart.plot.PlotOrientation;

public class testreadjson {
    static SerialPort chosenPort;
    static int x = 0;
    public static void main(String[] arge) throws LineUnavailableException, UnsupportedAudioFileException, IOException{
        
        //window
        JFrame window = new JFrame();
        window.setTitle("Graph");
        window.setSize(600,400);
        window.setLayout(new BorderLayout());
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //drop-down box
        JComboBox<String> portList = new JComboBox<>();
        JButton connectBT = new JButton("Connect");
        JPanel topPanel = new JPanel();
        topPanel.add(portList);
        topPanel.add(connectBT);
        window.add(topPanel,BorderLayout.NORTH );
        
        

        

        SerialPort[] portNames = SerialPort.getCommPorts();
        for(int i = 0; i < portNames.length; i ++)
            portList.addItem(portNames[i].getSystemPortName());
        
        
        
        // create the line graph
        XYSeries series = new XYSeries("Reading");
        XYSeries series2 = new XYSeries("Second");
        XYSeriesCollection dataset = new XYSeriesCollection();
        
        
        JFreeChart chart = ChartFactory.createXYLineChart("", "Time", "DC Reading", dataset, PlotOrientation.VERTICAL, true, true, false);
        
        window.add(new ChartPanel(chart),BorderLayout.CENTER);
        
        ChartPanel ch = new ChartPanel(chart);
        XYPlot xyPlot = chart.getXYPlot();
        //System.out.println(xyPlot);
        org.jfree.chart.axis.ValueAxis domainAxis = xyPlot.getDomainAxis();
        org.jfree.chart.axis.ValueAxis rangeAxis = xyPlot.getRangeAxis();
        

        
        rangeAxis.setRange(0.0, 1100.0);
        rangeAxis.setAutoRangeMinimumSize(0.1);        
        
        //configure the connect button and use another thread to listen for data
        connectBT.addActionListener(new ActionListener(){
            @Override public void actionPerformed(ActionEvent arg0){
                
                if (connectBT.getText().equals("Connect")){
                //attemp to connect to the serial port    
                chosenPort = SerialPort.getCommPort(portList.getSelectedItem().toString());
                String s = portList.getSelectedItem().toString();
                
                
                
                //System.out.println(s);
                chosenPort.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);
                if(chosenPort.openPort()){
                    connectBT.setText("Disconnect");
                    portList.setEnabled(false);
                }
                //create a new thread that listens for incoming text and populates the graph
                Thread thread = new Thread(){
                   @Override public void run()
                   {
                       Scanner scanner;
                      try {
                      scanner = new Scanner(chosenPort.getInputStream());
                      int x = 0;
                      int count = 0;
                      int n = 1000;
                      int l = 0;
                      int flush = 0;
                        while(scanner.hasNextLine()){
                            
                            try{
                               
                                String line = scanner.nextLine();
                                int number = Integer.parseInt(line);
                                flush = flush + 1;
                                System.out.println(flush);
                                
                                x++;
                                System.out.println(number);
                                if (l == 0){
                                    series.add(x, 1023 - number);
                                    l = 1;
                                }else
                                    System.out.println(l);
                                    series2.add(x, number);
                                    l = 0;
                                //System.out.println(series.getItemCount());
                                
                                
                                
                                
                                
                                
                                
                                ch.repaint();
                                    if (n < x){
                                    n = n+1000;
                                    domainAxis.setRange(x, n);
                                    //domainAxis.setAutoRangeMinimumSize(0.1);
                                    }
                                       
                                    
                                
                                
                               
                            } catch(NumberFormatException e){}
                        }
                        scanner.close();
                         } catch(Exception e){
                         System.err.println(e);}
                    }
                        
                };
                thread.start();
                }
                   else{
                    //disconnect form the serial port
                    chosenPort.closePort();
                    portList.setEnabled(true);
                    connectBT.setText("Connect");
                    series.clear();
                    x = 0;
        
                    }
        dataset.addSeries(series);
        dataset.addSeries(series2);      
     }

        });

        // show the window 
        window.setVisible(true);
       
        
        
        
    }
    
    
}