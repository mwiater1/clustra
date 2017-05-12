package com.mateuszwiater.csc495.clustra;

import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.jgroups.*;
import org.jgroups.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public abstract class ClusterNode extends ReceiverAdapter {
    private static Logger log = LoggerFactory.getLogger(ClusterNode.class);

    protected JChannel channel;
    protected State state;
    protected final Gson gson;
    private final Queue<Consumer<State>> stateUpdateListener;

    public ClusterNode() {
        // Gson instance for converting state
        gson = new Gson();
        // New state for node
        state = new State();
        // New state update listener queue
        stateUpdateListener = new ConcurrentLinkedQueue<>();

        try {
            // Join the "CLUSTRA" cluster
            channel = new JChannel(this.getClass().getClassLoader().getResource("config.xml"));
            channel.setReceiver(this);
            channel.connect("CLUSTRA");
            // Get the state from the oldest member
            channel.getState(null, 10000);
            System.out.println("STATE: " + gson.toJson(state));
        } catch (Exception e) {
            log.error("JChannel Creation Failure!", e);
            System.exit(-1);
        }
    }

    public void send(final State state) {
        try {
            channel.send(new Message(null, Util.objectToByteBuffer(gson.toJson(state))));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void receive(Message msg) {
        try {
            state = gson.fromJson((String)Util.objectFromByteBuffer(msg.getBuffer()), State.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        updateState(state);
    }

    @Override
    public void getState(OutputStream output) throws Exception {
        IOUtils.write(gson.toJson(state), output, StandardCharsets.UTF_8);
    }

    @Override
    public void setState(InputStream input) throws Exception {
        state = gson.fromJson(IOUtils.toString(input, StandardCharsets.UTF_8), State.class);
    }

    @Override
    public void viewAccepted(View view) {
        updateView(view.getMembers().stream().map(Object::toString).collect(Collectors.toList()));
    }

    public State getState() {
        return state;
    }

    private void updateState(final State state) {
        stateUpdateListener.forEach(c -> c.accept(state));
        update(state);
    }

    public void addStateUpdateListener(final Consumer<State> listener) {
        stateUpdateListener.add(listener);
    }

    protected abstract void updateView(final List<String> channelIds);

    protected abstract void update(final State state);
}
