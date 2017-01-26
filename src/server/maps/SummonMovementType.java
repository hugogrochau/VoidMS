package server.maps;

import net.IntValueHolder;

public enum SummonMovementType implements IntValueHolder {
    STATIONARY(0), FOLLOW(1), CIRCLE_FOLLOW(3);

    private final int val;

    private SummonMovementType(int val) {
        this.val = val;
    }

    @Override
    public int getValue() {
        return val;
    }
}