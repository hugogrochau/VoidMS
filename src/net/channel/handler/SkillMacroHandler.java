package net.channel.handler;

import client.MapleClient;
import client.SkillMacro;
import tools.data.input.SeekableLittleEndianAccessor;
import net.AbstractMaplePacketHandler;


public class SkillMacroHandler extends AbstractMaplePacketHandler {

    // Create a new instance
    public SkillMacroHandler() {
    }

    @Override
    public void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        c.getPlayer().resetAfkTime();
        int num = slea.readByte();
        for (int i = 0; i < num; i++) {
            String name = slea.readMapleAsciiString();
            int shout = slea.readByte();
            int skill1 = slea.readInt();
            int skill2 = slea.readInt();
            int skill3 = slea.readInt();
            SkillMacro macro = new SkillMacro(skill1, skill2, skill3, name, shout, i);
            c.getPlayer().updateMacros(i, macro);
        }
    }
}