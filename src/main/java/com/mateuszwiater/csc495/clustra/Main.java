package com.mateuszwiater.csc495.clustra;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.platform.Platform;
import com.pi4j.platform.PlatformAlreadyAssignedException;
import com.pi4j.platform.PlatformManager;

import java.util.Arrays;

import static com.pi4j.io.gpio.OrangePiPin.*;

public class Main {

    public static void main(String[] args) throws InterruptedException, PlatformAlreadyAssignedException {
        PlatformManager.setPlatform(Platform.ORANGEPI);
//        GpioFactory.setDefaultProvider(new OrangePiGpioProvider());
        final GpioController gpio = GpioFactory.getInstance();

        Arrays.stream(OrangePiPin.allPins())
                .filter(p -> !Arrays.asList(8, 9).contains(p.getAddress()))
                .filter(p -> p.getAddress() <= 16 || Arrays.asList(21, 24,25).contains(p.getAddress()))
                .map(p -> gpio.provisionDigitalInputPin(p, PinPullResistance.PULL_DOWN))
                .forEach(p -> {
                            System.out.println("Adding: " + p.getName());
                    try {
                        p.addListener((GpioPinListenerDigital) event ->
                                System.out.println("GPIO Pin '" + event.getPin().getName() + "' is: " + event.getState())
                        );
                    } catch (Exception e) {
                        System.out.println("Exception on Pin: " + p.getName());
                        System.out.println(e.toString());
                    }
                }
                );

        System.out.println("Ready, waiting...");
        while(true) {
            Thread.sleep(500);
        }
    }
}
