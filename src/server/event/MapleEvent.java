/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server.event;

import java.util.concurrent.ScheduledFuture;
import client.MapleCharacter;
import server.TimerManager;

/**
 *
 * @author Hugo
 */
public class MapleEvent {

    private ScheduledFuture<?> end;
    private int map;
    private int prize;

    public MapleEvent(int map, int prize, int time) {
        this.map = map;
        this.prize = prize;
        end = TimerManager.getInstance().schedule(new Runnable() {

            @Override
            public void run() {
                end(null);
            }
        }, time);
    }

    public int getMap() {
        return map;
    }

    public int getPrize() {
        return prize;
    }

    public void end(MapleCharacter winner) {
        if (!end.isDone()) {
            end.cancel(true);
        }
        if (winner != null) {
            winner.gainItem(prize);
        }
    }

    public void removePlayer(MapleCharacter victim) {
        victim.changeMap(109050001);
    }

    public void addPlayer(MapleCharacter victim) {
        victim.changeMap(map);
    }
}
