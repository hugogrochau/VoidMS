/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server.PQ;

import net.world.MapleParty;

/**
 *
 * @author Hugo
 */
public class KerningPQ implements MaplePQ {

    private MapleParty party;
    private int currentStage;
    private static final int prizes[] = {133, 134424};
    private static final int mapid = 35235235;

    public KerningPQ(MapleParty party) {
        this.party = party;
        currentStage = 0;
    }

    public void init() {
    }

    public void advance() {
    }

    public void end() {
    }
}
