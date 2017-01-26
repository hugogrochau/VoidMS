package net.channel.handler;

import client.MapleClient;
import net.AbstractMaplePacketHandler;
import net.SendPacketOpcode;
import tools.data.input.SeekableLittleEndianAccessor;
import tools.data.output.MaplePacketLittleEndianWriter;

public class NPCAnimation extends AbstractMaplePacketHandler {

    @Override
    public void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        int length = (int) slea.available();
        if (length == 6) { // NPC Talk
            mplew.writeShort(SendPacketOpcode.NPC_ACTION.getValue());
            mplew.writeInt(slea.readInt());
            mplew.writeShort(slea.readShort());
            c.getSession().write(mplew.getPacket());
        } else if (length > 6) { // NPC Move
            byte[] bytes = slea.read(length - 9);
            mplew.writeShort(SendPacketOpcode.NPC_ACTION.getValue());
            mplew.write(bytes);
            c.getSession().write(mplew.getPacket());
        }
    }
}