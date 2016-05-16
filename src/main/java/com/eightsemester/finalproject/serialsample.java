/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eightsemester.finalproject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import gnu.io.CommPortIdentifier; 
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent; 
import gnu.io.SerialPortEventListener; 
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import pieces.Piece;
import pieces.Rook;


/**
 *
 * @author micky
 */
public class serialsample implements SerialPortEventListener {
    SerialPort serialPort;
    static volatile String response = null;
    
        /** The port we're normally going to use. */
	private static final String PORT_NAMES[] = { 
			"/dev/tty.usbserial-A9007UX1", // Mac OS X
                        "/dev/ttyACM0", // Raspberry Pi
			"/dev/ttyUSB0", // Linux
			"COM4", // Windows
                        "COM5", // Windows
                        "COM3", // Windows
	};
	/**
	* A BufferedReader which will be fed by a InputStreamReader 
	* converting the bytes into characters 
	* making the displayed results codepage independent
	*/
	private BufferedReader input;
	/** The output stream to the port */
	private OutputStream output;
	/** Milliseconds to block while waiting for port open */
	private static final int TIME_OUT = 2000;
	/** Default bits per second for COM port. */
	private static final int DATA_RATE = 9600;

	public void initialize() {
                // the next line is for Raspberry Pi and 
                // gets us into the while loop and was suggested here was suggested http://www.raspberrypi.org/phpBB3/viewtopic.php?f=81&t=32186
                //System.setProperty("gnu.io.rxtx.SerialPorts", "/dev/ttyACM0");
                
                
		CommPortIdentifier portId = null;
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

		//First, Find an instance of serial port as set in PORT_NAMES.
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
			for (String portName : PORT_NAMES) {
				if (currPortId.getName().equals(portName)) {
					portId = currPortId;
					break;
				}
			}
		}
		if (portId == null) {
			System.out.println("Could not find COM port.");
			return;
		}

		try {
			// open serial port, and use class name for the appName.
			serialPort = (SerialPort) portId.open(this.getClass().getName(),
					TIME_OUT);

			// set port parameters
			serialPort.setSerialPortParams(DATA_RATE,
					SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);

			// open the streams
			input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
			output = serialPort.getOutputStream();

			// add event listeners
			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);
		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}

	/**
	 * This should be called when you stop using the port.
	 * This will prevent port locking on platforms like Linux.
	 */
	public synchronized void close() {
		if (serialPort != null) {
			serialPort.removeEventListener();
			serialPort.close();
		}
	}

	/**
	 * Handle an event on the serial port. Read the data and print it.
	 */
	public synchronized void serialEvent(SerialPortEvent oEvent) {
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
                                //output.write("hola".getBytes());
                                //output.flush();
				String inputLine=input.readLine();
				System.out.println(inputLine);
                                response = inputLine;
                                
			} catch (Exception e) {
				System.err.println(e.toString());
			}
                }
                
		// Ignore all the other eventTypes, but you should consider the other ones.
	}
        
        public void escribir(String mensaje) throws IOException, InterruptedException{
            output.write(mensaje.getBytes("UTF-8"));
            output.flush();
            
        }
        
    public static String[] difference(Piece[][] photo, Piece[][] move){
        int width = 8;
        int height = 8;
        boolean[][] checked = new boolean[width][height];
        ArrayList<String> commands = new ArrayList<>();
        
        //Detectar que piezas se mantuvieron igual
        for(int x = width - 1 ; x > -1 ; x--){
            for(int y = height - 1 ; y > -1 ; y--){
                Piece pp = photo[x][y];
                Piece pm = move[x][y];
                if(pp!=null && pm!= null){
                    if(pp.getTeam().equals(pm.getTeam())){
                        if(pp.getType().equals(pm.getType())){
                            checked[x][y] = true;
                        }
                    }
                }else if(pp == null && pm == null){
                    checked[x][y] = true;
                }
            }
        }
        /* A este punto el checked tiene los espacios que quedaron iguales como 
         * true por lo que solo 2 casillas quedaran en false
         * las casillas en false indicaran donde se presento un movimiento
         * se debera verificar si habia una pieza para poder determinar si se
         * retira o se conserva.
        */
        int nxpos = 0;
        int nypos = 0;
        int oxpos = 0;
        int oypos = 0;
        for(int x = width - 1 ; x > -1 ; x--){
            for(int y = height - 1 ; y > -1 ; y--){
                if(checked[x][y] == false){
                    Piece pp = photo[x][y];
                    Piece pm = move[x][y];
//                    if(pm!=null){
//                        commands.add(""+x+","+y+","+-1+","+-1);
//                        nxpos = x;
//                        nypos = y;
//                    }else{
//                        oxpos = x;
//                        oypos = y;
//                    }
                    if(pp != null && pm == null){
                        oxpos = x;
                        oypos = y;
                        checked[x][y] = true;
                    }else {
                        nxpos = x;
                        nypos = y;
                        if(pp!=null){
                            commands.add(""+x+","+y+","+-1+","+-1);
                        }
                    }
                    
                }
            }
        }
        commands.add(""+oxpos+","+oypos+","+nxpos+","+nypos);
        
        return (String[]) commands.toArray(new String[0]);
    }
    
    
        //Esto es lo que tiene que ir en el codigo principal.
    public static void main(String[] args) throws Exception {
        serialsample main = new serialsample();
        main.initialize();
        Piece[][] pp = new Piece[8][8];
        pp[0][0] = new pieces.Rook("src/pieces_images/Brook.png", "black", new int[]{0, 0});
        pp[7][1] = new pieces.Rook("src/pieces_images/Brook.png", "white", new int[]{7, 1});
        Piece[][] pm = new Piece[8][8];
        pm[1][1] = new pieces.Rook("src/pieces_images/Brook.png", "black", new int[]{1, 1});
        String[] palabras = difference(pp, pm);
        Thread.sleep(2000);
        //String[] palabras = {"Hola","Adios"};
        for (String palabra : palabras) {
            main.escribir(palabra);
        while(response==null){}
        response = null;
        }

        System.out.println("Started");
        main.close();
    }
        

}
