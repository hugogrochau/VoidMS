package client;

public enum MapleSkinColor {

    NORMAL(0), DARK(1), BLACK(2), PALE(3), BLUE(4), GREEN(5), YELLOW(6), SILVER(7), BRONZE(8), WHITE(9), ALBINO(10), PURPLE(11);
    final int id;

    private MapleSkinColor(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static MapleSkinColor getById(int id) {
        for (MapleSkinColor l : MapleSkinColor.values()) {
            if (l.getId() == id) {
                return l;
            }
        }
        return null;
    }
}