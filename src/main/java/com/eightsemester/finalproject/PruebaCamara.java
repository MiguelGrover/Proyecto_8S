/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eightsemester.finalproject;

import com.github.sarxos.webcam.Webcam;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

/**
 *
 * @author micky
 */
public class PruebaCamara {
    
    private final String[] piezas = {"Pawn","Rook","Knight","Queen","King","Bishop"};
    private final String[] color = {"Black","White"};
    private final int[] valores = {-2655484,-4160173,-7104107,-7165095,-8021359,-8415602,-9204604,-9585920,-10326036,-11030382,-12857344,-14307674};
    
    
    public static void main(String[] args) throws IOException {
        Webcam webcam = getWebcam();
        BufferedImage capture = getImage(webcam);
        BufferedImage[] tablero = getChessTable(capture);
        
    }
    
    public static Webcam getWebcam(){
        Webcam webcam = Webcam.getDefault();
        List<Webcam> lista = Webcam.getWebcams();
        if(lista.size()>1){
            webcam = lista.get(1);
        }
        return webcam;
    }
    
    public static BufferedImage getImage(Webcam w){
        w.setViewSize(new Dimension(640,480));
        w.open();
        //ImageIO.write(webcam.getImage(), "PNG", new File("hello-world.png"));
        BufferedImage capture = w.getImage();
        w.close();
        return capture;
    }
    
    public static BufferedImage[] getChessTable(BufferedImage capture){
        int rows = 8; //You should decide the values for rows and cols variables  
        int cols = 8;  
        int chunks = rows * cols;  
        int offset = (capture.getWidth()-capture.getHeight())/2;
        int chunkWidth = capture.getHeight()/ cols; // determines the chunk width and height  
        int chunkHeight = capture.getHeight() / rows;  
        int count = 0;  
        BufferedImage imgs[] = new BufferedImage[chunks]; //Image array to hold image chunks  
        for (int x = 0; x < rows; x++) {  
            for (int y = 0; y < cols; y++) {  
                //Initialize the image array with image chunks  
                imgs[count] = new BufferedImage(chunkWidth, chunkHeight, 5);  
  
                // draws the image chunk  
                Graphics2D gr = imgs[count++].createGraphics();  
                gr.drawImage(capture, 
                        0, 0, chunkWidth, chunkHeight, 
                        chunkWidth * y + offset, chunkHeight * x, chunkWidth * y + chunkWidth + offset, chunkHeight * x + chunkHeight, 
                        null);  
                gr.dispose();  
            }  
        }  
        return imgs;
    }
    
    public void savetablero(BufferedImage[] tablero) throws IOException{
        //writing mini images into image files  
        for (int i = 0; i < tablero.length; i++) {  
            ImageIO.write(tablero[i], "jpg", new File("images/img" + i + ".jpg"));  
        } 
    }
    
    public void showTablero(BufferedImage[] tablero){
        JPanel panel = new JPanel();
        for(int x = 0; x < 64; x ++){
            JLabel picLabel = new JLabel(new ImageIcon(tablero[x]));
            panel.add(picLabel);
        }
        JFrame jf = new JFrame();
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jf.add(panel);
        jf.setSize(540,560);
        jf.setVisible(true);
    }
}
