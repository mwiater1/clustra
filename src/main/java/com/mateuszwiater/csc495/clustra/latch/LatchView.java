package com.mateuszwiater.csc495.clustra.latch;

import com.mateuszwiater.csc495.clustra.DeviceView;

import java.util.UUID;

public class LatchView extends DeviceView {
    private LatchState state;

    public LatchView(final UUID id, final String name) {
        super(id, name);
        state = LatchState.OFF;
    }

    public LatchState getState() {
        return state;
    }

    public void setState(LatchState state) {
        this.state = state;
    }
}
