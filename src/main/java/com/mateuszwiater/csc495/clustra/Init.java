package com.mateuszwiater.csc495.clustra;

import com.mateuszwiater.csc495.clustra.latch.Latch;
import com.pi4j.io.gpio.OrangePiPin;
import com.pi4j.platform.Platform;
import com.pi4j.platform.PlatformAlreadyAssignedException;
import com.pi4j.platform.PlatformManager;
import spark.ModelAndView;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.HashMap;

import static spark.Spark.*;

public class Init {

    public static void main(String[] args) throws PlatformAlreadyAssignedException {
        // Make sure we are not using IPv6
        System.setProperty("java.net.preferIPv4Stack" , "true");

        // Set the Pi4J platform
        PlatformManager.setPlatform(Platform.ORANGEPI);

        // Set the web-server port
        port(80);

        // Enable static files from the resource directory
        staticFiles.location("/public");

        Latch left = new Latch("Left", OrangePiPin.GPIO_28, OrangePiPin.GPIO_29);
        Latch right = new Latch("Right", OrangePiPin.GPIO_30, OrangePiPin.GPIO_31);

        // Set the WebSocket
        webSocket("/ws", new StateWebSocket(left));

        // Mapping for the home page
        get("/", (req, res) -> new MustacheTemplateEngine().render(
                new ModelAndView(new HashMap<>(), "index.mustache")
        ));
    }
}
