package com.mateuszwiater.csc495.clustra.latch;

import com.google.gson.Gson;
import com.mateuszwiater.csc495.clustra.ClusterNode;
import com.mateuszwiater.csc495.clustra.DeviceView;
import com.mateuszwiater.csc495.clustra.State;
import com.pi4j.io.gpio.*;

import java.io.*;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Latch extends ClusterNode {
    private final GpioPinDigitalOutput on, off;
    private final ExecutorService executorService;
    private LatchView view;
    private final String suffix;

    public Latch(final String suffix, final Pin onPin, final Pin offPin) {
        this.suffix = suffix;
        final GpioController gpio = GpioFactory.getInstance();
        executorService = Executors.newSingleThreadExecutor();

        File f = new File(suffix + ".json");

        // Check if there is a previous view
        if(f.exists()) {
            try (FileReader fw = new FileReader(f)) {
                view = new Gson().fromJson(fw, LatchView.class);

                final LatchView tmpView = getState().getLatchView(view.getId());

                if(tmpView == null) {
                    getState().setLatchView(view);
                } else {
                    view = tmpView;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            view = new LatchView(UUID.randomUUID(), suffix);
            getState().setLatchView(view);
        }

        view.setChannelId(channel.getName());

        send(getState());

        on = gpio.provisionDigitalOutputPin(onPin, PinState.LOW);
        off = gpio.provisionDigitalOutputPin(offPin, PinState.LOW);
    }

    public void setState(final LatchState state) {
        executorService.submit(() -> {
            if(state == LatchState.ON) {
                on.setState(PinState.HIGH);
            } else {
                off.setState(PinState.HIGH);
            }

            try {
                Thread.sleep(100);
                on.setState(PinState.LOW);
                off.setState(PinState.LOW);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    protected void updateView(List<String> channelIds) {
        final List<UUID> disconnectedLatchViews = state.getLatchViews().values().stream()
                .filter(l -> !channelIds.contains(l.getChannelId()))
                .map(DeviceView::getId).collect(Collectors.toList());

        if (!disconnectedLatchViews.isEmpty()) {
            disconnectedLatchViews.forEach(state::deleteLatchView);
            send(state);
        }
    }

    @Override
    protected void update(State state) {
        view = state.getLatchView(view.getId());
        final File f = new File(suffix + ".json");
        try (final FileWriter fw = new FileWriter(f)) {
            // Save the view
            fw.write(new Gson().toJson(view));
        } catch (IOException e) {
            e.printStackTrace();
        }
        setState(view.getState());
    }
}
