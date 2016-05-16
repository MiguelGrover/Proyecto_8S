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

import graphics.*;
import static graphics.StartGame.board;
import pieces.*;

/**
 *
 * @author micky
 */
public class PruebaCamara {
    
    private static String[][] piezas;
    private final int[] valores = {-2655484,-4160173,-7104107,-7165095,-8021359,-8415602,-9204604,-9585920,-10326036,-11030382,-12857344,-14307674};    
    
    public static void main(String[] args) throws IOException {
        Webcam webcam = getWebcam();
        BufferedImage capture = getImage(webcam);
        BufferedImage[] tablero = getChessTable(capture);
        tablero[0].getRGB(30,30);
        System.out.println(tablero[0].getRGB(30,30));
        
        imagesToPieces(tablero);

        showTablero(tablero);
        savetablero(tablero);
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
    
    public static void savetablero(BufferedImage[] tablero) throws IOException{
        //writing mini images into image files  
        for (int i = 0; i < tablero.length; i++) {  
            ImageIO.write(tablero[i], "jpg", new File("images/img" + i + ".jpg"));  
        }
        
    }
    
    public static void showTablero(BufferedImage[] tablero){
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
    
    public static void imagesToPieces(BufferedImage[] images){
        int color;
        board.createSquares();

        for (int i = 0; i < 64; i++) {

            color = images[i].getRGB(30,30);
            
            if(color <=  -2655484 || color >= -2355484){
               board.addPiece(new Pawn("src/pieces_images/Wpawn.png", "white", new int[]{6, i}), 6, i);
               //piezas[i][0] = "white";
               //piezas[i][1] = "pawn";
               
            } else if(color <=  -4160173 || color >= -4060173){
               board.addPiece(new Rook("src/pieces_images/Wrook.png", "white", new int[]{7, 7}), 7, 7);
               board.addPiece(new Rook("src/pieces_images/Wrook.png", "white", new int[]{7, 0}), 7, 0);
               
               //piezas[i][0] = "white";
               //piezas[i][1] = "rook";
               
            } else if(color <=  -7104107 || color >= -7102107) {
               board.addPiece(new pieces.King("src/pieces_images/Wking.png", "white", new int[]{7, 3}), 7, 3);

               //piezas[i][0] = "white";
               //piezas[i][1] = "king";
               
            } else if(color <=  -7165095 || color >= -7145095) {
               board.addPiece(new Queen("src/pieces_images/Wqueen.png", "white", new int[]{7, 4}), 7, 4);

               //piezas[i][0] = "white";
               //piezas[i][1] = "queen";
               
            } else if(color <=  -8021359 || color >= -8001359) {
               board.addPiece(new Bishop("src/pieces_images/Wbishop.png", "white", new int[]{7, 5}), 7, 5);
               board.addPiece(new Bishop("src/pieces_images/Wbishop.png", "white", new int[]{7, 2}), 7, 2);
               
               //piezas[i][0] = "white";
               //piezas[i][1] = "bishop";
               
            } else if(color <=  -8415602 || color >= -8315602) {
               board.addPiece(new Knight("src/pieces_images/Wknight.png", "white", new int[]{7, 6}), 7, 6);
               board.addPiece(new Knight("src/pieces_images/Wknight.png", "white", new int[]{7, 1}), 7, 1);

               //piezas[i][0] = "white";
               //piezas[i][1] = "knight";
               
            } else if(color <=  -9204604 || color >= -9004604) {
               board.addPiece(new Pawn("src/pieces_images/Bpawn.png", "black", new int[]{1, i}), 1, i);

               //piezas[i][0] = "black";
               //piezas[i][1] = "pawn";
               
            } else if(color <=  -9585920 || color >= -9485920) {
               board.addPiece(new Rook("src/pieces_images/Brook.png", "black", new int[]{0, 7}), 0, 7);
               board.addPiece(new Rook("src/pieces_images/Brook.png", "black", new int[]{0, 0}), 0, 0);
               
               //piezas[i][0] = "black";
               //piezas[i][1] = "rook";
               
            } else if(color <=  -10326036 || color >= -10126036) {
               board.addPiece(new King("src/pieces_images/Bking.png", "black", new int[]{0, 3}), 0, 3);
               //piezas[i][0] = "black";
               //piezas[i][1] = "king";
               
            } else if(color <=  -11030382 || color >= -10530382) {
               board.addPiece(new Queen("src/pieces_images/Bqueen.png", "black", new int[]{0, 4}), 0, 4);

               //piezas[i][0] = "black";
               //piezas[i][1] = "queen";
               
            } else if(color <=  -12857344 || color >= -12557344) {
                board.addPiece(new Bishop("src/pieces_images/Bbishop.png", "black", new int[]{0, 5}), 0, 5);
                board.addPiece(new Bishop("src/pieces_images/Bbishop.png", "black", new int[]{0, 2}), 0, 2);
        
               //piezas[i][0] = "black";
               //piezas[i][1] = "bishop";
               
            } else if(color <=  -14307674 || color >= -13307674) {
                board.addPiece(new Knight("src/pieces_images/Bknight.png", "black", new int[]{0, 6}), 0, 6);
                board.addPiece(new Knight("src/pieces_images/Bknight.png", "black", new int[]{0, 1}), 0, 1);
                
               //piezas[i][0] = "black";
               //piezas[i][1] = "knight";
               
            } else {
               piezas[i][0] = "";
               piezas[i][1] = "";
            }
        }
    }
}
