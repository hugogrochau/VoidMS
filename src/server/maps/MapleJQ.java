/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server.maps;

/**
 *
 * @author Hugo
 */
public class MapleJQ {

    private static final int maps[] = {220000006, 100000202, 610020000, 610020001, 682000200, 280020000, 280020001, 109040001, 109040002, 109040003, 109040004, 101000100, 101000101, 101000102, 101000103, 101000104, 105040310, 105040311, 105040312, 105040313, 105040314, 105040315, 105040316, 103000900, 103000901, 103000903, 103000904, 103000906, 103000907, 103000908};
    private static final int points[] = {1, 1, 2, 2, 8, 5, 10, 3, 4, 4, 5, 2, 5, 4, 5, 10, 1, 3, 3, 4, 5, 5, 10, 4, 1, 3, 5, 3, 3, 10};
    private static final int times[] = {50, 55, 80, 55, 65, 85, 125, 70, 50, 115, 85, 45, 95, 90, 55, 60, 50, 80, 60, 85, 85, 80, 140, 125, 25, 70, 140, 65, 55, 80};

    public static int getPoints(int map) {
        for (int i = 0; i < maps.length; i++) {
            if (maps[i] == map) {
                return points[i];
            }
        }
        return -1;
    }

    public static int getTime(int map) {
        for (int i = 0; i < maps.length; i++) {
            if (maps[i] == map) {
                return times[i];
            }
        }
        return -1;
    }

    public static boolean isJQ(int id) {
        for (int i : maps) {
            if (i == id) {
                return true;
            }
        }
        return false;
    }
}
