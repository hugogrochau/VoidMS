package server.maps;

import java.awt.Point;
import java.awt.Rectangle;
import client.ISkill;
import client.MapleCharacter;
import client.MapleClient;
import client.SkillFactory;
import net.MaplePacket;
import server.MapleStatEffect;
import tools.MaplePacketCreator;

public class MapleMist extends AbstractMapleMapObject {
    private Rectangle mistPosition;
    private MapleCharacter owner;
    private MapleStatEffect source;

    public MapleMist(Rectangle mistPosition, MapleCharacter owner, MapleStatEffect source) {
        this.mistPosition = mistPosition;
        this.owner = owner;
        this.source = source;
    }

    @Override
    public MapleMapObjectType getType() {
        return MapleMapObjectType.MIST;
    }

    @Override
    public Point getPosition() {
        return mistPosition.getLocation();
    }

    public MapleCharacter getOwner() {
        return owner;
    }

    public ISkill getSourceSkill() {
        return SkillFactory.getSkill(source.getSourceId());
    }

    public Rectangle getBox() {
        return mistPosition;
    }

    @Override
    public void setPosition(Point position) {
        throw new UnsupportedOperationException();
    }

    public MaplePacket makeDestroyData() {
        return MaplePacketCreator.removeMist(getObjectId());
    }

    @Override
    public void sendDestroyData(MapleClient client) {
        client.getSession().write(makeDestroyData());
    }

    public MaplePacket makeSpawnData() {
        int level = owner.getSkillLevel(SkillFactory.getSkill(source.getSourceId()));
        return MaplePacketCreator.spawnMist(getObjectId(), owner.getId(), source.getSourceId(), mistPosition, level);
    }

    public MaplePacket makeFakeSpawnData(int level) {
        return MaplePacketCreator.spawnMist(getObjectId(), owner.getId(), source.getSourceId(), mistPosition, level);
    }

    @Override
    public void sendSpawnData(MapleClient client) {
        client.getSession().write(makeSpawnData());
    }

    public boolean makeChanceResult() {
        return source.makeChanceResult();
    }
}