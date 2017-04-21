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

        Arrays.asList(GPIO_00, GPIO_01, GPIO_02, GPIO_03, GPIO_04, GPIO_05, GPIO_11, GPIO_12, GPIO_13, GPIO_15, GPIO_16, GPIO_07).stream()
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
