package net.login.handler;

import java.util.Calendar;
import client.MapleClient;
import net.AbstractMaplePacketHandler;
import tools.MaplePacketCreator;
import tools.data.input.SeekableLittleEndianAccessor;

public class DeleteCharHandler extends AbstractMaplePacketHandler {

    @Override
    public void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        int idate = slea.readInt();
        int cid = slea.readInt();
        int year = idate / 10000;
        int month = (idate - year * 10000) / 100;
        int day = idate - year * 10000 - month * 100;
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.set(year, month - 1, day);
        boolean shallDelete = true;
        int state = 0x12;
        if (shallDelete) {
            state = 0;
            if (!c.deleteCharacter(cid)) {
                state = 1;
            }
        }
        c.getSession().write(MaplePacketCreator.deleteCharResponse(cid, state));
    }
}