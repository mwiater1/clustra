package com.mateuszwiater.csc495.clustra;

import com.pi4j.component.lcd.LCDTextAlignment;
import com.pi4j.component.lcd.impl.GpioLcdDisplay;
import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.platform.Platform;
import com.pi4j.platform.PlatformAlreadyAssignedException;
import com.pi4j.platform.PlatformManager;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;

import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Main extends ReceiverAdapter {
//    public final static int LCD_ROWS = 2;
//    public final static int LCD_ROW_1 = 0;
//    public final static int LCD_ROW_2 = 1;
//    public final static int LCD_COLUMNS = 16;
//    public final static int LCD_BITS = 4;
//
//    public static void main(String[] args) throws InterruptedException, PlatformAlreadyAssignedException {
//        PlatformManager.setPlatform(Platform.ORANGEPI);
//        final GpioController gpio = GpioFactory.getInstance();
//
//
//
//        System.out.println("<--Pi4J--> GPIO 4 bit LCD example program");
//
//        // initialize LCD
//        final GpioLcdDisplay lcd = new GpioLcdDisplay(LCD_ROWS,    // number of row supported by LCD
//                LCD_COLUMNS,       // number of columns supported by LCD
//                OrangePiPin.GPIO_24,  // LCD RS pin
//                OrangePiPin.GPIO_15,  // LCD strobe pin
//                OrangePiPin.GPIO_06,  // LCD data bit 1
//                OrangePiPin.GPIO_21,  // LCD data bit 2
//                OrangePiPin.GPIO_29,  // LCD data bit 3
//                OrangePiPin.GPIO_28); // LCD data bit 4
//
//
//        // clear LCD
//        lcd.clear();
//        Thread.sleep(1000);
//
//        // write line 1 to LCD
//        lcd.write(LCD_ROW_1, "The Pi4J Project");
//
//        // write line 2 to LCD
//        lcd.write(LCD_ROW_2, "----------------");
//
//        // line data replacement
//        for(int index = 0; index < 5; index++)
//        {
//            lcd.write(LCD_ROW_2, "----------------");
//            Thread.sleep(500);
//            lcd.write(LCD_ROW_2, "****************");
//            Thread.sleep(500);
//        }
//        lcd.write(LCD_ROW_2, "----------------");
//
//        // single character data replacement
//        for(int index = 0; index < lcd.getColumnCount(); index++) {
//            lcd.write(LCD_ROW_2, index, ">");
//            if(index > 0)
//                lcd.write(LCD_ROW_2, index - 1, "-");
//            Thread.sleep(300);
//        }
//        for(int index = lcd.getColumnCount()-1; index >= 0 ; index--) {
//            lcd.write(LCD_ROW_2, index, "<");
//            if(index < lcd.getColumnCount()-1)
//                lcd.write(LCD_ROW_2, index + 1, "-");
//            Thread.sleep(300);
//        }
//
//        // left alignment, full line data
//        lcd.write(LCD_ROW_2, "----------------");
//        Thread.sleep(500);
//        lcd.writeln(LCD_ROW_2, "<< LEFT");
//        Thread.sleep(1000);
//
//        // right alignment, full line data
//        lcd.write(LCD_ROW_2, "----------------");
//        Thread.sleep(500);
//        lcd.writeln(LCD_ROW_2, "RIGHT >>", LCDTextAlignment.ALIGN_RIGHT);
//        Thread.sleep(1000);
//
//        // center alignment, full line data
//        lcd.write(LCD_ROW_2, "----------------");
//        Thread.sleep(500);
//        lcd.writeln(LCD_ROW_2, "<< CENTER >>", LCDTextAlignment.ALIGN_CENTER);
//        Thread.sleep(1000);
//
//        // mixed alignments, partial line data
//        lcd.write(LCD_ROW_2, "----------------");
//        Thread.sleep(500);
//        lcd.write(LCD_ROW_2, "<L>", LCDTextAlignment.ALIGN_LEFT);
//        lcd.write(LCD_ROW_2, "<R>", LCDTextAlignment.ALIGN_RIGHT);
//        lcd.write(LCD_ROW_2, "CC", LCDTextAlignment.ALIGN_CENTER);
//        Thread.sleep(3000);
//
//        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
//
//        // update time
//        while(true) {
//            Thread.sleep(1000);
//        }
//
//        // stop all GPIO activity/threads by shutting down the GPIO controller
//        // (this method will forcefully shutdown all GPIO monitoring threads and scheduled tasks)
//        // gpio.shutdown();   <--- implement this method call if you wish to terminate the Pi4J GPIO controller
//    }


    public static void main(String[] args) throws Exception {
		System.setProperty("java.net.preferIPv4Stack" , "true");

        Main m = new Main("A");

        Main g = new Main("B");

        Main x = new Main("C");


        Scanner s = new Scanner(System.in);
        String input = "";
        while(!input.equals("quit")) {
            input = s.nextLine();
            m.send(input);
        }

    }

    JChannel channel;
    String name;

    public Main(String name) throws Exception {
        channel = new JChannel();
        channel.connect("TEST");
        channel.setReceiver(this);
        //System.out.println(channel.getProtocolStack().getProtocols());
        this.name = name;
        System.out.println("Starting: " + name);
    }

    public void send(String s) throws Exception {
        channel.send(new Message(null, s));
    }

    public void viewAccepted(View new_view) {
        System.out.println("** view: " + new_view);
    }

    public void receive(Message msg) {

        System.out.println(name + ": " + msg.getSrc() + ": " + msg.getObject());
    }

    @Override
    public void getState(OutputStream output) throws Exception {
        super.getState(output);
    }

    @Override
    public void setState(InputStream input) throws Exception {
        super.setState(input);
    }
}
