package com.mateuszwiater.csc495.clustra;

import com.mateuszwiater.csc495.clustra.latch.LatchView;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class State {
    private final Map<UUID, LatchView> latchViews;

    public State() {
        latchViews = new HashMap<>();
    }

    public void setLatchView(final LatchView latchView) {
        latchViews.put(latchView.getId(), latchView);
    }

    public LatchView getLatchView(final UUID id) {
        return latchViews.get(id);
    }

    public void deleteLatchView(final UUID id) {
        System.out.println("REMOVING: " + id);
        latchViews.remove(id);
    }

    public Map<UUID, LatchView> getLatchViews() {
        return latchViews;
    }
}
