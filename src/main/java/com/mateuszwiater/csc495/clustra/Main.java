package com.mateuszwiater.csc495.clustra;

import com.pi4j.io.gpio.*;

import static com.pi4j.io.gpio.PinState.HIGH;
import static com.pi4j.io.gpio.PinState.LOW;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Hello World!");

        GpioFactory.setDefaultProvider(new OrangePiGpioProvider());
        final GpioController gpio = GpioFactory.getInstance();

        final GpioPinDigitalOutput pin1 = gpio.provisionDigitalOutputPin(OrangePiPin.GPIO_23, LOW);
        final GpioPinDigitalOutput pin2 = gpio.provisionDigitalOutputPin(OrangePiPin.GPIO_24, HIGH);

        while (true) {
            pin1.toggle();
            pin2.toggle();
            System.out.println("Toggle");
            Thread.sleep(1000);
        }
    }
}
