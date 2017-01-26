package client;

import server.MapleStatEffect;
import server.life.Element;

public interface ISkill {

    int getId();

    MapleStatEffect getEffect(int level);

    int getMaxLevel();

    int getAnimationTime();

    public boolean canBeLearnedBy(MapleJob job);

    public boolean isFourthJob();

    public boolean isBeginnerSkill();

    public boolean isGMSkill();

    public Element getElement();
}
