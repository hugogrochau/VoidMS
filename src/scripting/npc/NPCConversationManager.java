package scripting.npc;

import java.awt.Point;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import client.IItem;
import client.Item;
import client.MapleCharacter;
import client.MapleClient;
import client.MapleInventoryType;
import client.MapleJob;
import client.SkillFactory;
import scripting.AbstractPlayerInteraction;
import scripting.event.EventManager;
import server.MapleItemInformationProvider;
import server.MapleShopFactory;
import server.quest.MapleQuest;
import tools.MaplePacketCreator;
import client.MapleStat;
import net.world.guild.MapleGuild;
import server.MapleSquad;
import server.MapleSquadType;
import server.maps.MapleMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import client.Equip;
import client.ISkill;
import client.MapleInventory;
import client.MapleKeyBinding;
import client.MapleRing;
import client.achievement.Achievement;
import database.DatabaseConnection;
import net.MaplePacket;
import net.channel.ChannelServer;
import net.world.MaplePartyCharacter;
import net.world.WorldServer;
import server.ClanHolder;
import server.MapleInventoryManipulator;
import server.MaplePortal;
import server.MapleStatEffect;
import server.TimerManager;
import server.life.MapleLifeFactory;
import server.life.MapleMonster;
import server.life.MapleMonsterStats;
import server.life.MobSkill;
import server.maps.MapleJQ;
import server.maps.MapleMapObject;
import server.maps.MapleMapObjectType;
import tools.Pair;

public class NPCConversationManager extends AbstractPlayerInteraction {

    private MapleClient c;
    private int npc;
    private String fileName = null;
    private String getText;
    private MapleCharacter chr;
    private int diff;
    final WorldServer wserv = WorldServer.getInstance();

    public NPCConversationManager(MapleClient c, int npc, MapleCharacter chr, String fileName) {
        super(c);
        this.c = c;
        this.npc = npc;
        this.chr = chr;
        this.fileName = fileName;
    }

    public int GetRareCount()
    {
        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM rares order by id desc limit 1");
            ResultSet rs = ps.executeQuery();
            rs.first();
            return rs.getInt("id");
            
        }catch(SQLException se) {}
        return 0;
    }
    
    public int GetRareId(int index)
    {
        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT itemid FROM rares WHERE id =" + index);
            ResultSet rs = ps.executeQuery();
            if(!rs.first())
                return 0;
            else
                return rs.getInt("itemid");
            
        }catch(SQLException se) {}
        return 0;
    }
    
    public void dispose() {
        NPCScriptManager.getInstance().dispose(this);
    }

    public void sendNext(String text) {
        getClient().getSession().write(MaplePacketCreator.getNPCTalk(npc, (byte) 0, text, "00 01"));
    }

    public void sendPrev(String text) {
        getClient().getSession().write(MaplePacketCreator.getNPCTalk(npc, (byte) 0, text, "01 00"));
    }

    public void sendNextPrev(String text) {
        getClient().getSession().write(MaplePacketCreator.getNPCTalk(npc, (byte) 0, text, "01 01"));
    }

    public void sendOk(String text) {
        getClient().getSession().write(MaplePacketCreator.getNPCTalk(npc, (byte) 0, text, "00 00"));
    }

    public void sendYesNo(String text) {
        getClient().getSession().write(MaplePacketCreator.getNPCTalk(npc, (byte) 1, text, ""));
    }

    public void sendAcceptDecline(String text) {
        getClient().getSession().write(MaplePacketCreator.getNPCTalk(npc, (byte) 0x0C, text, ""));
    }

    public void sendSimple(String text) {
        getClient().getSession().write(MaplePacketCreator.getNPCTalk(npc, (byte) 4, text, ""));
    }

    public void sendStyle(String text, int styles[]) {
        getClient().getSession().write(MaplePacketCreator.getNPCTalkStyle(npc, text, styles));
    }

    public void sendGetNumber(String text, int def, int min, int max) {
        getClient().getSession().write(MaplePacketCreator.getNPCTalkNum(npc, text, def, min, max));
    }

    public void sendGetText(String text) {
        getClient().getSession().write(MaplePacketCreator.getNPCTalkText(npc, text));
    }

    public void setGetText(String text) {
        this.getText = text;
    }

    public String getText() {
        return this.getText;
    }

    public boolean isFull() {
        return getP().getInventory(MapleInventoryType.EQUIP).isFull();
    }

    public MapleCharacter getVictim(String name) {
        return ChannelServer.getInstance(getPlayer().getClient().getChannel()).getPlayerStorage().getCharacterByName(name);
    }

    public short integerToShort(int i) {
        return (short) i;
    }

    public String getBuff() {
        return getPlayer().getBuff();
    }

    public void addBuff(int buff) {
        String sbuff = Integer.toString(buff);
        String playerBuffs = getPlayer().getBuff();
        if (playerBuffs.equals("none")) {
            getPlayer().setBuff(sbuff + ",");
        } else {
            String newBuff;
            if (playerBuffs.length() < 200) {
                if (playerBuffs.equals("none")) {
                    getPlayer().setBuff(sbuff + ",");
                } else {
                    newBuff = playerBuffs + sbuff;
                    getPlayer().setBuff(newBuff + ",");
                }
            } else {
                getPlayer().dropMessage("You have reached the limit of buffs you can add. Please delete some");
            }
        }
    }

    public void deleteAllBuffs() {
        getPlayer().setBuff("none");
    }

    public void deleteBuff(int buff) {
        String sbuff = Integer.toString(buff);
        CharSequence cs = (CharSequence) sbuff + ",";
        if (getBuff().contains((CharSequence) sbuff)) {
            String playerBuffs = getBuff();
            String newBuff = playerBuffs.replace(cs, (CharSequence) "");
            if (playerBuffs.length() > 8) {
                getPlayer().setBuff(newBuff);
            } else {
                deleteAllBuffs();
            }
        }
    }

    public void openShop(int id) {
        MapleShopFactory.getInstance().getShop(id).sendShop(getClient());
    }

    public void changeJob(MapleJob job) {
        getPlayer().changeJob(job);
    }

    public int getMonsterCount() {
        return getPlayer().getMap().getMonsterCount();
    }

    public void changeJobById(int jobid) {
        getPlayer().changeJob(MapleJob.getById(jobid));
    }

    public MapleJob getJob() {
        return getPlayer().getJob();
    }

    public void startQuest(int id) {
        MapleQuest.getInstance(id).start(getPlayer(), npc);
    }

    public boolean createRing(String name, int itemid) {
        MapleCharacter player = getP();
        MapleCharacter partner = getClient().getChannelServer().getPlayerStorage().getCharacterByName(name); // get character by name
        if (partner != null) { // if player is in the same channel
            int[] ringid = MapleRing.createRing(itemid, player, partner); // create ring (SQL ONLY) returns int[ringid1, ringid2]
            MapleInventoryManipulator.addRing(player, itemid, ringid[0]); // adds to inventory + sends packet to client about new item
            MapleInventoryManipulator.addRing(partner, itemid, ringid[1]);
            if (itemid > 1112015) {
                player.addFriendshipRing(MapleRing.loadFromDb(ringid[0])); // adds to the list, loadfromDb returns a MapleRing from info in the db.
                partner.addFriendshipRing(MapleRing.loadFromDb(ringid[1]));
            } else {
                player.addCrushRing(MapleRing.loadFromDb(ringid[0]));
                partner.addCrushRing(MapleRing.loadFromDb(ringid[1]));
            }
            player.saveToDB(true, true);
            partner.saveToDB(true, true);
            player.getClient().getSession().write(MaplePacketCreator.getCharInfo(player));
            player.getMap().removePlayer(player);
            player.getMap().addPlayer(player);
            partner.getClient().getSession().write(MaplePacketCreator.getCharInfo(partner));
            partner.getMap().removePlayer(partner);
            partner.getMap().addPlayer(partner);
            return true;
        }
        return false;
    }
    // public boolean makeRing(int itemId, String name) {
    //    return MapleRing.createRing(c, itemId, name);
    // }

    public void completeQuest(int id) {
        MapleQuest.getInstance(id).complete(getPlayer(), npc);
    }

    public void forfeitQuest(int id) {
        MapleQuest.getInstance(id).forfeit(getPlayer());
    }

    public int getTime(int map) {
        return MapleJQ.getTime(map);
    }

    public int getJQPoints(int map) {
        return MapleJQ.getPoints(map);
    }

    public void buffMonsters(int id) {
        MapleMap map = getPlayer().getMap();
        List<MapleMapObject> monsters = map.getMapObjectsInRange(getPlayer().getPosition(), Double.POSITIVE_INFINITY, Arrays.asList(MapleMapObjectType.MONSTER));
        for (MapleMapObject monstermo : monsters) {
            MapleMonster monster = (MapleMonster) monstermo;
            MobSkill ms = new MobSkill(id, 20);
            ms.applyEffect(getPlayer(), monster, true);
        }
    }

    public void scheduleJQ(int map) {
        getP().scheduleJQ(map);
    }

    /**
     * use getPlayer().getMeso() instead
     * @return
     */
    @Deprecated
    public int getMeso() {
        return getPlayer().getMeso();
    }

    public void gainMeso(int gain) {
        getPlayer().gainMeso(gain, true, false, true);
    }

    public void gainExp(int gain) {
        getPlayer().gainExp(gain, true, true);
    }

    public int getNpc() {
        return npc;
    }

    public void togglePractice() {
        getP().togglePractice();
    }

    public boolean isPractice() {
        return getP().isPractice();
    }

    public String getFileName() {
        return fileName;
    }

    /**
     * use getPlayer().getLevel() instead
     * @return
     */
    @Deprecated
    public int getLevel() {
        return getPlayer().getLevel();
    }

    public int length(String str) {
        return str.length();
    }

    public String showAchievements(int dif) {
        String out = "";
        for (Achievement a : Achievement.values()) {
            if (a.getDifficulty() == dif) {
                if (getPlayer().hasAchievement(a)) {
                    out += "\r\n#g" + a.getName() + "#k";
                } else {
                    out += "\r\n#r" + a.getName() + "#k";
                }
            }
        }
        return out;
    }

    public void unequipEverything() {
        getPlayer().unequipEverything();
    }

    public void teachSkill(int id, int level, int masterlevel) {
        getPlayer().changeSkillLevel(SkillFactory.getSkill(id), level, masterlevel);
    }

    public void clearSkills() {
        Map<ISkill, MapleCharacter.SkillEntry> skills = getPlayer().getSkills();
        for (Entry<ISkill, MapleCharacter.SkillEntry> skill : skills.entrySet()) {
            getPlayer().changeSkillLevel(skill.getKey(), 0, 0);
        }
    }

    /**
     * Use getPlayer() instead (for consistency with MapleClient)
     * @return
     */
    @Deprecated
    public MapleCharacter getChar() {
        return getPlayer();
    }

    public MapleClient getC() {
        return getClient();
    }

    public void rechargeStars() {
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        IItem stars = getChar().getInventory(MapleInventoryType.USE).getItem((byte) 1);
        if (ii.isThrowingStar(stars.getItemId()) || ii.isBullet(stars.getItemId())) {
            stars.setQuantity(ii.getSlotMax(getClient(), stars.getItemId()));
            getC().getSession().write(MaplePacketCreator.updateInventorySlot(MapleInventoryType.USE, (Item) stars));
        }
    }

    public int getPetMaxLevel() {
        return getP().getPetMaxLevel();
    }

    public EventManager getEventManager(String event) {
        return getClient().getChannelServer().getEventSM().getEventManager(event);
    }

    public void showEffect(String effect) {
        getPlayer().getMap().broadcastMessage(MaplePacketCreator.showEffect(effect));
    }

    public void playSound(String sound) {
        getPlayer().getMap().broadcastMessage(MaplePacketCreator.playSound(sound));
    }

    @Override
    public String toString() {
        return "Conversation with NPC: " + npc;
    }

    public void updateBuddyCapacity(int capacity) {
        getPlayer().setBuddyCapacity(capacity);
    }

    public int getBuddyCapacity() {
        return getPlayer().getBuddyCapacity();
    }

    public void setHair(int hair) {
        getPlayer().setHair(hair);
        getPlayer().updateSingleStat(MapleStat.HAIR, hair);
        getPlayer().equipChanged();
    }

    public void setFace(int face) {
        getPlayer().setFace(face);
        getPlayer().updateSingleStat(MapleStat.FACE, face);
        getPlayer().equipChanged();
    }

    @SuppressWarnings("static-access")
    public void setSkin(int color) {
        getPlayer().setSkinColor(c.getPlayer().getSkinColor().getById(color));
        getPlayer().updateSingleStat(MapleStat.SKIN, color);
        getPlayer().equipChanged();
    }

    public void sendHint(String msg, int width, int height) {
        getPlayer().sendHint(msg, width, height);
    }

    public void warpParty(int mapId) {
        warpParty(mapId, 0, 0);
    }

    public void warpParty(int mapId, int exp, int meso) {
        for (MaplePartyCharacter chr_ : getPlayer().getParty().getMembers()) {
            MapleCharacter curChar = c.getChannelServer().getPlayerStorage().getCharacterByName(chr_.getName());
            if ((curChar.getEventInstance() == null && c.getPlayer().getEventInstance() == null) || curChar.getEventInstance() == getPlayer().getEventInstance()) {
                curChar.changeMap(mapId);
                if (exp > 0) {
                    curChar.gainExp(exp, true, false, true);
                }
                if (meso > 0) {
                    curChar.gainMeso(meso, true);
                }
            }
        }
    }

    private void killMapMobs() {
        MapleMap map = c.getPlayer().getMap();
        double range = Double.POSITIVE_INFINITY;
        try {
            List<MapleMapObject> monsters = map.getMapObjectsInRange(c.getPlayer().getPosition(), range, Arrays.asList(MapleMapObjectType.MONSTER));
            for (MapleMapObject monstermo : monsters) {
                MapleMonster monster = (MapleMonster) monstermo;
                map.killMonster(monster, c.getPlayer(), false);
            }
        } catch (NullPointerException e) {
        }
    }

    public boolean killMonsters() {
        if (c.getPlayer().getMap().getCharacters().size() < 2) {
            killMapMobs();
            return true;
        }
        if (c.getPlayer().getPartyId() == -1) {
            return false;
        }
        for (MapleCharacter chars : c.getPlayer().getMap().getCharacters()) {
            if (chars.getPartyId() != c.getPlayer().getPartyId() && !chars.isHidden()) {
                return false;
            }
        }
        killMapMobs();
        return true;
    }

    public void warpRandom(int mapid) {
        MapleMap target = c.getChannelServer().getMapFactory().getMap(mapid);
        Random rand = new Random();
        MaplePortal portal = target.getPortal(rand.nextInt(target.getPortals().size())); //generate random portal
        getPlayer().changeMap(target, portal);
    }

    public int random(int num) {
        Random rand = new Random();
        return (int) Math.round(Math.ceil((double) rand.nextInt(num)));
    }

    public int itemQuantity(int itemid) {
        int possesed = getPlayer().getItemQuantity(itemid, false);
        return possesed;
    }

    public MapleSquad createMapleSquad(MapleSquadType type) {
        MapleSquad squad = new MapleSquad(c.getChannel(), getPlayer());
        if (getSquadState(type) == 0) {
            c.getChannelServer().addMapleSquad(squad, type);
        } else {
            return null;
        }
        return squad;
    }

    public MapleCharacter getSquadMember(MapleSquadType type, int index) {
        MapleSquad squad = c.getChannelServer().getMapleSquad(type);
        MapleCharacter ret = null;
        if (squad != null) {
            ret = squad.getMembers().get(index);
        }
        return ret;
    }

    public int getSquadState(MapleSquadType type) {
        MapleSquad squad = c.getChannelServer().getMapleSquad(type);
        if (squad != null) {
            return squad.getStatus();
        } else {
            return 0;
        }
    }

    public void setSquadState(MapleSquadType type, int state) {
        MapleSquad squad = c.getChannelServer().getMapleSquad(type);
        if (squad != null) {
            squad.setStatus(state);
        }
    }

    public boolean checkSquadLeader(MapleSquadType type) {
        MapleSquad squad = c.getChannelServer().getMapleSquad(type);
        if (squad != null) {
            if (squad.getLeader().getId() == getPlayer().getId()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public void removeMapleSquad(MapleSquadType type) {
        MapleSquad squad = c.getChannelServer().getMapleSquad(type);
        if (squad != null) {
            if (squad.getLeader().getId() == getPlayer().getId()) {
                squad.clear();
                c.getChannelServer().removeMapleSquad(squad, type);
            }
        }
    }

    public int numSquadMembers(MapleSquadType type) {
        MapleSquad squad = c.getChannelServer().getMapleSquad(type);
        int ret = 0;
        if (squad != null) {
            ret = squad.getSquadSize();
        }
        return ret;
    }

    public boolean isSquadMember(MapleSquadType type) {
        MapleSquad squad = c.getChannelServer().getMapleSquad(type);
        boolean ret = false;
        if (squad.containsMember(getPlayer())) {
            ret = true;
        }
        return ret;
    }

    public void addSquadMember(MapleSquadType type) {
        MapleSquad squad = c.getChannelServer().getMapleSquad(type);
        if (squad != null) {
            squad.addMember(getPlayer());
        }
    }

    public void removeSquadMember(MapleSquadType type, MapleCharacter chr, boolean ban) {
        MapleSquad squad = c.getChannelServer().getMapleSquad(type);
        if (squad != null) {
            squad.banMember(chr, ban);
        }
    }

    public void removeSquadMember(MapleSquadType type, int index, boolean ban) {
        MapleSquad squad = c.getChannelServer().getMapleSquad(type);
        if (squad != null) {
            MapleCharacter squadChar = squad.getMembers().get(index);
            squad.banMember(squadChar, ban);
        }
    }

    public boolean canAddSquadMember(MapleSquadType type) {
        MapleSquad squad = c.getChannelServer().getMapleSquad(type);
        if (squad != null) {
            if (squad.isBanned(getPlayer())) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    public void warpSquadMembers(MapleSquadType type, int mapId) {
        MapleSquad squad = c.getChannelServer().getMapleSquad(type);
        MapleMap map = c.getChannelServer().getMapFactory().getMap(mapId);
        if (squad != null) {
            if (checkSquadLeader(type)) {
                for (MapleCharacter squadChar : squad.getMembers()) {
                    squadChar.changeMap(map, map.getPortal(0));
                }
            }
        }
    }

    public String searchItem(String item) {
        StringBuilder message = new StringBuilder("Choose the item you want:");
        getPlayer().getMap().broadcastMessage(getPlayer(), MaplePacketCreator.showJobChange(getPlayer().getId()), false);
        for (Pair<Integer, String> itemPair : MapleItemInformationProvider.getInstance().getAllItems()) {
            if (itemPair.getRight().toLowerCase().contains(item.toLowerCase())) {
                message.append("\r\n#L" + itemPair.getLeft() + "##i" + itemPair.getLeft() + "# - #b" + itemPair.getRight() + "#k");
            }
        }
        if (!message.toString().contains("#L")) {
            return "No Items Found";
        }
        return message.toString();
    }

    public boolean deleteRings(String partner) {
        final int[] rings = {1112800, 1112801, 1112802};
        boolean b = false;
        for (int ring : rings) {
            if (getP().getItemQuantity(ring, true) > 0) {
                b = true;
            }
        }
        if (!b) {
            return false;
        }
        for (int ring : rings) {
            MapleInventoryManipulator.removeAllById(c, ring, true);
        }
        MapleCharacter partnerChar = c.getChannelServer().getPlayerStorage().getCharacterByName(partner);
        for (int ring : rings) {
            try {
                if (partnerChar == null) {
                    Connection con = DatabaseConnection.getConnection();
                    PreparedStatement ps = con.prepareStatement("DELETE FROM inventoryitems WHERE itemid = ? and characterid = (select id from characters where name = ?)");
                    ps.setInt(1, ring);
                    ps.setString(2, partner);
                    ps.executeUpdate();
                    ps = con.prepareStatement("DELETE FROM rings WHERE partnerChrId = (select id from characters where name = ?)");
                    ps.setString(1, partner);
                    ps.executeUpdate();
                } else {
                    MapleInventoryManipulator.removeAllById(partnerChar.getClient(), ring, true);
                    Connection con = DatabaseConnection.getConnection();
                    PreparedStatement ps = con.prepareStatement("DELETE FROM rings WHERE partnerChrId = ?");
                    ps.setInt(1, partnerChar.getId());
                    ps.executeUpdate();
                }
            } catch (SQLException se) {
                return false;
            }
        }
        return true;
    }

    public void resetReactors() {
        getPlayer().getMap().resetReactors();
    }

    public void displayGuildRanks() {
        MapleGuild.displayGuildRanks(getClient(), npc);
    }

    public boolean sendMessage(String recipient, String message) {
        MapleCharacter chr_ = getCharByName(recipient);
        if (chr_ != null) {
            chr_.dropMessage(6, getPlayer().getName() + ": " + message);
            return true;
        }
        return false;
    }

    public void gainFame(int amount) {
        getPlayer().addFame(amount);
        if (amount > 0) {
            getPlayer().dropMessage(1, "You have gained " + amount + " fame.");
        } else {
            getPlayer().dropMessage(1, "You have lost " + amount + " fame.");
        }
    }

    public void maxSkills() {
        getPlayer().maxAllSkills();
    }

    public int getSkillLevel(int skillid) {
        int skilllevel = getPlayer().getSkillLevel(SkillFactory.getSkill(skillid));
        return skilllevel;
    }

    public void giveBuff(int skillid) {
        MapleItemInformationProvider mii = MapleItemInformationProvider.getInstance();
        MapleStatEffect statEffect = mii.getItemEffect(skillid);
        statEffect.applyTo(getPlayer());
    }

    public int partyMembersInMap() {
        int inMap = 0;
        for (MapleCharacter char2 : getPlayer().getMap().getCharacters()) {
            if (char2.getParty() == getPlayer().getParty()) {
                inMap++;
            }
        }
        return inMap;
    }

    public void modifyNx(int amount) {
        getPlayer().modifyCSPoints(1, amount);
        if (amount > 0) {
            getPlayer().dropMessage(5, "You have gained " + amount + " NX points.");
        } else {
            getPlayer().dropMessage(5, "You have lost " + amount + " NX points.");
        }
    }

    public int getTime(String type) {
        Calendar cal = Calendar.getInstance();
        if (type.startsWith("d")) {
            return cal.get(Calendar.DAY_OF_WEEK);
        } else if (type.startsWith("h")) {
            return cal.get(Calendar.HOUR_OF_DAY);
        } else if (type.startsWith("m")) {
            return cal.get(Calendar.MINUTE);
        } else if (type.startsWith("s")) {
            return cal.get(Calendar.SECOND);
        }
        return -1; // wrong input
    }

    public void addBuddyCapacity(int capacity) {
        getPlayer().addBuddyCapacity(capacity);
    }

    public void clearKeys() {
        getPlayer().setDefaultKeyMap();
    }

    public void scheduleWarp(int delay, int mapid) {
        final int fmapid = mapid;
        TimerManager.getInstance().schedule(new Runnable() {

            public void run() {
                getPlayer().changeMap(fmapid);
            }
        }, delay * 1000);
    }

    public void startClock(int limit, int endMap) {
        getPlayer().getMap().addMapTimer(limit, endMap);
    }

    public MapleCharacter getCharByName(String name) {
        try {
            return c.getChannelServer().getPlayerStorage().getCharacterByName(name);
        } catch (Exception e) {
            return null;
        }
    }
// start of drop event functions

    public boolean hasDropEvent() {
        return wserv.hasDropEvent();
    }

    public void endDropEvent() {
        wserv.endDropEvent();
    }

    public void setDropEvent(byte slot, int map, int inventory) {
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        MapleInventoryType inv = MapleInventoryType.UNDEFINED;
        switch (inventory) {
            case 0:
                inv = inv.CASH;
                break;
            case 1:
                inv = inv.EQUIP;
                break;
            case 2:
                inv = inv.ETC;
                break;
            case 3:
                inv = inv.SETUP;
                break;
            case 4:
                inv = inv.USE;
                break;
        }
        IItem item = c.getPlayer().getInventory(inv).getItem(slot);
        c.getPlayer().getInventory(inv).removeItem(slot);
        MapleMap mmap = c.getChannelServer().getMapFactory().getMap(map);
        Random rand = new Random();
        MaplePortal portal = mmap.getPortal(rand.nextInt(mmap.getPortals().size()));
        if (inv == MapleInventoryType.EQUIP) {
            wserv.setDropEvent((Equip) item, map, getPlayer().getName());
            mmap.spawnItemDrop(getPlayer(), getPlayer(), (Equip) item, portal.getPosition(), true, false);
        } else {
            wserv.setDropEvent(item, map, getPlayer().getName());
            mmap.spawnItemDrop(getPlayer(), getPlayer(), item, portal.getPosition(), true, false);
        }
        worldMessage(6, "[Drop Event] " + getPlayer().getName() + " has started a drop event. Check out the drop event NPC in Henesys for more information.");
    }

    public int getDropEventItem() {
        return wserv.getDropEventItem().getItemId();
    }

    public int getDropEventHints() {
        return wserv.getHints();
    }

    public void useDropEventHint() {
        wserv.useHint();
    }

    public int getDropEventMap() {
        return wserv.getDropEventMap();
    }

    public String getDropEventHost() {
        return wserv.getDropEventHost();
    }

    public void warpAllInMap(int mapid) {
        warpAllInMap(mapid, 0);
    }

    public void warpAllInMap(int mapid, int portal) {
        for (MapleCharacter mch : getPlayer().getMap().getCharacters()) {
            if (mch.getEventInstance() != null) {
                mch.getEventInstance().unregisterPlayer(mch);
            }
            mch.changeMap(mapid, portal);
        }
    }

    public boolean createMarriage(String partner_) {
        MapleCharacter partner = getCharByName(partner_);
        if (partner == null) {
            return false;
        }
        partner.setMarried(true);
        getPlayer().setMarried(true);
        partner.setPartnerId(getPlayer().getId());
        getPlayer().setPartnerId(partner.getId());
        if (partner.getGender() > 0) {
            Marriage.createMarriage(getPlayer(), partner);
        } else {
            Marriage.createMarriage(partner, getPlayer());
        }
        return true;
    }

    public boolean createEngagement(String partner_) {
        MapleCharacter partner = getCharByName(partner_);
        if (partner == null) {
            return false;
        }
        if (partner.getGender() > 0) {
            Marriage.createEngagement(getPlayer(), partner);
        } else {
            Marriage.createEngagement(partner, getPlayer());
        }
        return true;
    }

    public void divorceMarriage() {
        getPlayer().setPartnerId(0);
        getPlayer().setMarried(false);
        Marriage.divorceMarriage(getPlayer());
    }

    public void changeKeyBinding(int key, byte type, int action) {
        MapleKeyBinding newbinding = new MapleKeyBinding(type, action);
        getPlayer().changeKeybinding(key, newbinding);
    }

    public Equip getEquipById(int id) { // we can do getEquipById(2349823).setStr(545); etc.
        MapleInventoryType type = MapleItemInformationProvider.getInstance().getInventoryType(id);
        return (Equip) getPlayer().getInventory(type).findById(id);
    }

    public int getNpcTalkTimes() {
        return NPCScriptManager.getInstance().getNpcTalkTimes(getPlayer().getId(), npc);
    }

    public void setNpcTalkTimes(int amount) {
        NPCScriptManager.getInstance().setNpcTalkTimes(getPlayer().getId(), npc, amount);
    }

    public int talkedTimesByNpc() {
        return NPCScriptManager.getInstance().talkedTimesByNpc(npc);
    }

    public boolean makeProItem(int id, int hardcore) {
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        IItem item = ii.getEquipById(id);
        MapleInventoryType type = ii.getInventoryType(id);
        if (type.equals(MapleInventoryType.EQUIP)) {
            MapleInventoryManipulator.addFromDrop(c, ii.hardcoreItem((Equip) item, (short) hardcore), true, true);
            return true;
        } else {
            return false;
        }
    }

    public boolean isGuest() {
        if (c.isGuest()) {
            return true;
        }
        return false;
    }

    public void broadcastMessage(int type, String message) {
        try {
            getPlayer().getClient().getChannelServer().getWorldInterface().broadcastMessage(null, MaplePacketCreator.serverNotice(type, message).getBytes());
        } catch (RemoteException e) {
            c.getChannelServer().reconnectWorld();
        }
    }

    public void setClan(int set) {
        getPlayer().setClan(set);
        try {
            getPlayer().getClient().getChannelServer().getWorldInterface().broadcastToClan((getPlayer().getName() + " has entered the clan ! Give him a nice welcome.").getBytes(), set);
        } catch (RemoteException e) {
            c.getChannelServer().reconnectWorld();
        }
        c.getChannelServer().addToClan(getPlayer());
    }

    public String getAllOnlineNamesFromClan(int set) {
        StringBuilder sb = new StringBuilder();
        for (MapleCharacter names : c.getChannelServer().getClanHolder().getAllOnlinePlayersFromClan(set)) {
            sb.append(names.getName() + "\r\n");
        }
        return sb.toString();
    }

    public String getAllOfflineNamesFromClan(int set) {
        StringBuilder sb = new StringBuilder();
        for (String names : c.getChannelServer().getClanHolder().getAllOfflinePlayersFromClan(set)) {
            sb.append(names + "\r\n");
        }
        return sb.toString();
    }

    public int getOfflineClanCount(int clan) {
        return ClanHolder.countOfflineByClan(clan);
    }

    public int getOnlineClanCount(int clan) {
        try {
            return c.getChannelServer().getWorldInterface().onlineClanMembers(clan);
        } catch (RemoteException re) {
            c.getChannelServer().reconnectWorld();
        }
        return -1;
    }

    public String getJobById(int id) {
        return MapleJob.getJobName(id);
    }

    public List<MapleCharacter> getPartyMembers() {
        if (getPlayer().getParty() == null) {
            return null;
        }
        List<MapleCharacter> chars = new LinkedList<MapleCharacter>();
        for (ChannelServer channel : ChannelServer.getAllInstances()) {
            for (MapleCharacter Partychr : channel.getPartyMembers(getPlayer().getParty())) {
                if (Partychr != null) {
                    chars.add(Partychr);
                }
            }
        }
        return chars;
    }

    public MapleCharacter getSender() {
        return this.chr;
    }

    public boolean hasTemp() {
        if (!getPlayer().hasMerchant() && getPlayer().tempHasItems()) {
            return true;
        } else {
            return false;
        }
    }

    public int getHiredMerchantMesos() {
        Connection con = DatabaseConnection.getConnection();
        int mesos;
        try {
            PreparedStatement ps = con.prepareStatement("SELECT MerchantMesos FROM characters WHERE id = ?");
            ps.setInt(1, getPlayer().getId());
            ResultSet rs = ps.executeQuery();
            rs.next();
            mesos = rs.getInt("MerchantMesos");
            rs.close();
            ps.close();
        } catch (SQLException se) {
            return 0;
        }
        return mesos;
    }

    public int getHiredMerchantStamps() {
        Connection con = DatabaseConnection.getConnection();
        int mesos;
        try {
            PreparedStatement ps = con.prepareStatement("SELECT MerchantStamps FROM characters WHERE id = ?");
            ps.setInt(1, getPlayer().getId());
            ResultSet rs = ps.executeQuery();
            rs.next();
            mesos = rs.getInt("MerchantStamps");
            rs.close();
            ps.close();
        } catch (SQLException se) {
            return 0;
        }
        return mesos;
    }

    public void setHiredMerchantMesos(int set) {
        Connection con = DatabaseConnection.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("UPDATE characters SET MerchantMesos = ? WHERE id = ?");
            ps.setInt(1, set);
            ps.setInt(2, getPlayer().getId());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException se) {
        }
    }

    public void setHiredMerchantStamps(int set) {
        Connection con = DatabaseConnection.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("UPDATE characters SET MerchantStamps = ? WHERE id = ?");
            ps.setInt(1, set);
            ps.setInt(2, getPlayer().getId());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException se) {
        }
    }

    public void removeHiredMerchantItem(boolean tempItem, int itemId) {
        String Table = "";
        if (tempItem) {
            Table = "temp";
        }
        Connection con = DatabaseConnection.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("DELETE FROM hiredmerchant" + Table + " WHERE itemid = ? AND ownerid = ? LIMIT 1");
            ps.setInt(1, itemId);
            ps.setInt(2, getPlayer().getId());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException se) {
        }
    }

    public boolean isOnline(String player) {
        try {
            return c.getChannelServer().getWorldInterface().isConnected(player);
        } catch (RemoteException e) {
            c.getChannelServer().reconnectWorld();
        }
        return false;
    }

    public String getLatestPlayers() {
        String str = "10 Latest Players:\r\n";
        for (String pl : getPlayer().getLatestPlayers()) {
            str += (isOnline(pl) ? "#g" : "#r") + "\r\n-" + pl + "#k";
        }
        str += "\r\n\r\n #gOnline#k | #rOffline#k";
        return str;
    }

    public boolean getHiredMerchantItems(boolean tempTable) {
        boolean temp = false, completed = false;
        String Table = "";
        if (tempTable) {
            Table = "temp";
            temp = true;
        }
        Connection con = DatabaseConnection.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM hiredmerchant" + Table + " WHERE ownerid = ?");
            ps.setInt(1, getPlayer().getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getInt("type") == 1) {
                    Equip spItem = new Equip(rs.getInt("itemid"), (byte) 0, -1);
                    spItem.setUpgradeSlots((byte) rs.getInt("upgradeslots"));
                    spItem.setLevel((byte) rs.getInt("level"));
                    spItem.setStr((short) rs.getInt("str"));
                    spItem.setDex((short) rs.getInt("dex"));
                    spItem.setInt((short) rs.getInt("int"));
                    spItem.setLuk((short) rs.getInt("luk"));
                    spItem.setHp((short) rs.getInt("hp"));
                    spItem.setMp((short) rs.getInt("mp"));
                    spItem.setWatk((short) rs.getInt("watk"));
                    spItem.setMatk((short) rs.getInt("matk"));
                    spItem.setWdef((short) rs.getInt("wdef"));
                    spItem.setMdef((short) rs.getInt("mdef"));
                    spItem.setAcc((short) rs.getInt("acc"));
                    spItem.setAvoid((short) rs.getInt("avoid"));
                    spItem.setHands((short) rs.getInt("hands"));
                    spItem.setSpeed((short) rs.getInt("speed"));
                    spItem.setJump((short) rs.getInt("jump"));
                    spItem.setOwner(rs.getString("owner"));
                    if (!getPlayer().getInventory(MapleInventoryType.EQUIP).isFull()) {
                        MapleInventoryManipulator.addFromDrop(c, spItem, true, true);
                        removeHiredMerchantItem(temp, spItem.getItemId());
                    } else {
                        rs.close();
                        ps.close();
                        return false;
                    }
                } else {
                    Item spItem = new Item(rs.getInt("itemid"), (byte) 0, (short) rs.getInt("quantity"));
                    spItem.setOwner(rs.getString("owner"));
                    MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
                    MapleInventoryType type = ii.getInventoryType(spItem.getItemId());
                    if (!getPlayer().getInventory(type).isFull()) {
                        MapleInventoryManipulator.addFromDrop(c, spItem, true, true);
                        removeHiredMerchantItem(temp, spItem.getItemId());
                    } else {
                        rs.close();
                        ps.close();
                        return false;
                    }
                }
            }
            rs.last();
            if (rs.getRow() < 0) {
                completed = false;
            } else {
                completed = true;
            }
            rs.close();
            ps.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return completed;
    }

    /** SirPali's Additions **/
    //Start the training timer. Credits to Ace from RaGEZONE
    public void startTraining(int time) {
        c.getPlayer().startTraining(time);
    }

    public void summonMob(int mobid, int customHP, int customEXP, int amount) {
        spawnMonster(mobid, customHP, -1, -1, customEXP, 0, 0, amount, getPlayer().getPosition().x, getPlayer().getPosition().y);
    }

    public void spawnMonster(int mobid, int HP, int MP, int level, int EXP, int boss, int undead, int amount, int x, int y) {
        MapleMonsterStats newStats = new MapleMonsterStats();
        if (HP >= 0) {
            newStats.setHp(HP);
        }
        if (MP >= 0) {
            newStats.setMp(MP);
        }
        if (level >= 0) {
            newStats.setLevel(level);
        }
        if (EXP >= 0) {
            newStats.setExp(EXP);
        }
        newStats.setBoss(boss == 1);
        newStats.setUndead(undead == 1);
        for (int i = 0; i < amount; i++) {
            MapleMonster npcmob = MapleLifeFactory.getMonster(mobid);
            npcmob.setOverrideStats(newStats);
            npcmob.setHp(npcmob.getMaxHp());
            npcmob.setMp(npcmob.getMaxMp());
            c.getPlayer().getMap().spawnMonsterOnGroundBelow(npcmob, new Point(x, y));
        }
    }

    @SuppressWarnings("static-access")
    public String getNameById(int id) {
        return c.getPlayer().getNameById(id, 0);
    }

    public void gainAp(int gain) {
        getPlayer().gainAp(gain);
    }

    public MapleCharacter getP() {
        return getPlayer();
    }

    public void tagItem(byte slot) {
        MapleInventory equip = getP().getInventory(MapleInventoryType.EQUIP);
        Equip eq = (Equip) equip.getItem(slot);
        eq.setOwner(getP().getName());
        c.getSession().write(MaplePacketCreator.updateEquipSlot(eq));
    }

    public void makeGMItem2(byte slot, MapleCharacter player) {
        MapleInventory equip = player.getInventory(MapleInventoryType.EQUIP);
        Equip eu = (Equip) equip.getItem(slot);
        int item = equip.getItem(slot).getItemId();
        MapleJob job = eu.getJob();
        short hand = eu.getHands();
        byte level = eu.getLevel();
        Equip nItem = new Equip(item, equip.getNextFreeSlot());
        nItem.setStr((short) 30000); // STR
        nItem.setDex((short) 30000); // DEX
        nItem.setInt((short) 30000); // INT
        nItem.setLuk((short) 30000); //LUK
        nItem.setJob(job);
        nItem.setOwner(player.getName());
        nItem.setUpgradeSlots((byte) 5);
        nItem.setWatk((short) 250);
        nItem.setHands(hand);
        nItem.setLevel(level);
        nItem.setRingId(-1);
        equip.removeItem(slot);
        player.getInventory(MapleInventoryType.EQUIP).addFromDB(nItem);
    }

    public void makeSItem(int item, MapleCharacter player, int str, int dex, int inl, int luk, int att, int slots) {
        MapleInventory equip = player.getInventory(MapleInventoryType.EQUIP);
        Equip nItem = new Equip(item, equip.getNextFreeSlot());
        nItem.setStr((short) str); // STR
        nItem.setDex((short) dex); // DEX
        nItem.setInt((short) inl); // INT
        nItem.setLuk((short) luk); //LUK
        nItem.setUpgradeSlots((byte) slots);
        nItem.setWatk((short) att);
        player.getInventory(MapleInventoryType.EQUIP).addFromDB(nItem);
    }

    public void makeItem(int item, MapleCharacter player, int str, int dex, int inl, int luk, int att) {
        MapleInventory equip = player.getInventory(MapleInventoryType.EQUIP);
        Equip nItem = new Equip(item, equip.getNextFreeSlot());
        nItem.setStr((short) str); // STR
        nItem.setDex((short) dex); // DEX
        nItem.setInt((short) inl); // INT
        nItem.setLuk((short) luk); //LUK
        nItem.setUpgradeSlots((byte) 5);
        nItem.setWatk((short) att);
        player.getInventory(MapleInventoryType.EQUIP).addFromDB(nItem);
        c.getSession().write(MaplePacketCreator.addInventorySlot(MapleInventoryType.EQUIP, nItem));
    }

    public void makeMItem(int item, MapleCharacter player, int str, int dex, int inl, int luk, int att) {
        MapleInventory equip = player.getInventory(MapleInventoryType.EQUIP);
        Equip nItem = new Equip(item, equip.getNextFreeSlot());
        nItem.setStr((short) str); // STR
        nItem.setDex((short) dex); // DEX
        nItem.setInt((short) inl); // INT
        nItem.setLuk((short) luk); //LUK
        nItem.setUpgradeSlots((byte) 5);
        nItem.setMatk((short) att);
        player.getInventory(MapleInventoryType.EQUIP).addFromDB(nItem);
        c.getSession().write(MaplePacketCreator.addInventorySlot(MapleInventoryType.EQUIP, nItem));
    }

    public void makeCustomItem(int item, MapleCharacter player, int str, int dex, int inl, int luk, int jump, int speed, int acc, int avd, int mdef, int wdef, int hp, int mp, int att, int matt, int slots) {
        MapleInventory equip = player.getInventory(MapleInventoryType.EQUIP);
        Equip nItem = new Equip(item, equip.getNextFreeSlot());
        nItem.setStr((short) str); // STR
        nItem.setDex((short) dex); // DEX
        nItem.setInt((short) inl); // INT
        nItem.setLuk((short) luk); //LUK
        nItem.setJump((short) jump); // JUMP
        nItem.setSpeed((short) speed); // SPEED
        nItem.setAcc((short) acc); // ACCURACY
        nItem.setAvoid((short) avd); // AVOID
        nItem.setMdef((short) mdef); // MAGIC DEFENSE
        nItem.setWdef((short) wdef); // WEAPON DEFENSE
        nItem.setHp((short) hp); // HP
        nItem.setMp((short) mp); // MP
        nItem.setUpgradeSlots((byte) slots);
        nItem.setWatk((short) att);
        nItem.setMatk((short) matt);
        player.getInventory(MapleInventoryType.EQUIP).addFromDB(nItem);
    }

    public void upgradeSlots(byte slot, int add) {
        MapleInventory equip = getP().getInventory(MapleInventoryType.EQUIP);
        Equip eu = (Equip) equip.getItem(slot);
        int USlots = (int) (eu.getUpgradeSlots() + add);
        eu.setUpgradeSlots((byte) USlots);
        equip.removeItem(slot, (short) 1, true);
        equip.addFromDB(eu);
    }

    public void MakeGMItemPlayer(byte slot, MapleCharacter player) {
        MapleInventory equip = player.getInventory(MapleInventoryType.EQUIP);
        Equip eu = (Equip) equip.getItem(slot);
        int item = equip.getItem(slot).getItemId();
        MapleJob job = eu.getJob();
        short hand = eu.getHands();
        byte level = eu.getLevel();
        Equip nItem = new Equip(item, equip.getNextFreeSlot());
        nItem.setStr((short) 30000); // STR
        nItem.setDex((short) 30000); // DEX
        nItem.setInt((short) 30000); // INT
        nItem.setLuk((short) 30000); //LUK
        nItem.setOwner(player.getName());
        nItem.setUpgradeSlots((byte) 5);
        nItem.setWatk((short) 250);
        nItem.setJob(job);
        nItem.setHands(hand);
        nItem.setLevel(level);
        nItem.setRingId(-1);
        equip.removeItem(slot);
        player.getInventory(MapleInventoryType.EQUIP).addFromDB(nItem);
    }

    public String EquippedList(MapleClient c) {
        StringBuilder str = new StringBuilder();
        MapleInventory equipped = c.getPlayer().getInventory(MapleInventoryType.EQUIPPED);
        List<String> stra = new LinkedList<String>();
        for (IItem item : equipped.list()) {
            stra.add("#L" + item.getPosition() + "##v" + item.getItemId() + "##l");
        }
        for (String strb : stra) {
            str.append(strb);
        }
        return str.toString();
    }

    public String EquipList(MapleClient c) {
        StringBuilder str = new StringBuilder();
        MapleInventory equip = c.getPlayer().getInventory(MapleInventoryType.EQUIP);
        List<String> stra = new LinkedList<String>();
        for (IItem item : equip.list()) {
            stra.add("#L" + item.getPosition() + "##v" + item.getItemId() + "##l");
        }
        for (String strb : stra) {
            str.append(strb);
        }
        return str.toString();
    }

    public String UseList(MapleClient c) {
        StringBuilder str = new StringBuilder();
        MapleInventory use = c.getPlayer().getInventory(MapleInventoryType.USE);
        List<String> stra = new LinkedList<String>();
        for (IItem item : use.list()) {
            stra.add("#L" + item.getPosition() + "##v" + item.getItemId() + "##l");
        }
        for (String strb : stra) {
            str.append(strb);
        }
        return str.toString();
    }

    public String CashList(MapleClient c) {
        StringBuilder str = new StringBuilder();
        MapleInventory cash = c.getPlayer().getInventory(MapleInventoryType.CASH);
        List<String> stra = new LinkedList<String>();
        for (IItem item : cash.list()) {
            stra.add("#L" + item.getPosition() + "##v" + item.getItemId() + "##l");
        }
        for (String strb : stra) {
            str.append(strb);
        }
        return str.toString();
    }

    public String ETCList(MapleClient c) {
        StringBuilder str = new StringBuilder();
        MapleInventory etc = c.getPlayer().getInventory(MapleInventoryType.ETC);
        List<String> stra = new LinkedList<String>();
        for (IItem item : etc.list()) {
            stra.add("#L" + item.getPosition() + "##v" + item.getItemId() + "##l");
        }
        for (String strb : stra) {
            str.append(strb);
        }
        return str.toString();
    }

    public String SetupList(MapleClient c) {
        StringBuilder str = new StringBuilder();
        MapleInventory setup = c.getPlayer().getInventory(MapleInventoryType.SETUP);
        List<String> stra = new LinkedList<String>();
        for (IItem item : setup.list()) {
            stra.add("#L" + item.getPosition() + "##v" + item.getItemId() + "##l");
        }
        for (String strb : stra) {
            str.append(strb);
        }
        return str.toString();
    }

    public int getEquipId(byte slot) {
        MapleInventory equip = c.getPlayer().getInventory(MapleInventoryType.EQUIP);
        Equip eu = (Equip) equip.getItem(slot);
        return equip.getItem(slot).getItemId();
    }

    public int getUseId(byte slot) {
        MapleInventory use = c.getPlayer().getInventory(MapleInventoryType.USE);
        return use.getItem(slot).getItemId();
    }

    public int getSetupId(byte slot) {
        MapleInventory setup = c.getPlayer().getInventory(MapleInventoryType.SETUP);
        return setup.getItem(slot).getItemId();
    }

    public int getCashId(byte slot) {
        MapleInventory cash = c.getPlayer().getInventory(MapleInventoryType.CASH);
        return cash.getItem(slot).getItemId();
    }

    public int getETCId(byte slot) {
        MapleInventory etc = c.getPlayer().getInventory(MapleInventoryType.ETC);
        return etc.getItem(slot).getItemId();
    }

    public void worldMessage(int type, String msg) {
        MaplePacket packet = MaplePacketCreator.serverNotice(type, msg);
        try {
            ChannelServer.getInstance(c.getChannel()).getWorldInterface().broadcastMessage(getPlayer().getName(), packet.getBytes());
        } catch (RemoteException e) {
            c.getChannelServer().reconnectWorld();
        }
    }

    public String getItemName(int id) {
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        return ii.getName(id);
    }

    public void reloadChar() {
        getPlayer().getClient().getSession().write(MaplePacketCreator.getCharInfo(getPlayer()));
        getPlayer().getMap().removePlayer(getPlayer());
        getPlayer().getMap().addPlayer(getPlayer());
        if (getPlayer().getMap().isJQ()) {
            getPlayer().changeMap(100000000);
        }
    }

    public void fixExp() {
        getPlayer().setExp(0);
        getPlayer().updateSingleStat(MapleStat.EXP, getPlayer().getExp());
    }

    public boolean gainOneItem(int ID, int quantity) {
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        return MapleInventoryManipulator.addFromDrop(c, ii.randomizeStats(c, (Equip) ii.getEquipById(ID)), true, true);
    }

    public boolean isUseFull() {
        return getP().getInventory(MapleInventoryType.USE).isFull();
    }

    public boolean isEtcFull() {
        return getP().getInventory(MapleInventoryType.ETC).isFull();
    }

    public boolean isSetupFull() {
        return getP().getInventory(MapleInventoryType.SETUP).isFull();
    }

    public boolean isCashFull() {
        return getP().getInventory(MapleInventoryType.CASH).isFull();
    }

    public void loseItem(int ID, int quantity) {
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        MapleInventoryType type = ii.getInventoryType(ID);
        MapleInventoryManipulator.removeById(c, type, ID, quantity, true, false);
    }

    public int getKarma() {
        return getPlayer().getKarma();
    }

    public void gainKarma(int gain) {
        getPlayer().setKarma(getKarma() + gain);
    }

    public void setHp(int hp) {
        c.getPlayer().setHp(hp);
    }

    public void setMp(int mp) {
        c.getPlayer().setMp(mp);
    }

    public void setMP(int MP) {
        getPlayer().setMP(MP);
        getPlayer().updateSingleStat(MapleStat.MP, Integer.valueOf(MP));
    }

    public void setHP(int hp) {
        getPlayer().sethp(hp);
        getPlayer().updateSingleStat(MapleStat.HP, Integer.valueOf(hp));
    }

    public boolean giveHideoutNpc(int npcid) {
        if (itemQuantity(4002001) == 0) {
            getP().setHideoutNpc(npcid);
            gainItem(4002001);
            return true;
        } else {
            return false;
        }
    }

    public boolean hasHideoutNpc(int npcid) {
        return getPlayer().getGuild().hasHideoutNpc(npcid);
    }

    public boolean createHideout(int map) throws RemoteException {
        if (c.getChannelServer().getWorldInterface().createHideout(map, c.getPlayer().getGuildId())) {
            for (ChannelServer cs : c.getChannelServer().getAllInstances()) {
                for (MapleCharacter chars : cs.getMapFactory().getMap(map).getCharacters()) {
                    chars.changeMap(100000000);
                }
                cs.getMapFactory().disposeMap(map);
            }
            return true;
        }
        return false;
    }

    public boolean hideoutExpired() {
        return getP().getGuild().hideoutExpired();
    }

    public boolean extendHideoutTime() throws RemoteException {
        return c.getChannelServer().getWorldInterface().extendHideoutTime(c.getPlayer().getGuildId());
    }

    public long getHideoutTimeLeft() {
        return getP().getGuild().getHideoutTimeLeft();
    }

    public int getHideout() throws RemoteException {
        return getP().getGuild().getHideout();
    }

    public String listHideoutNpcs() {
        return getGuild().listHideoutNpcs();
    }

    public void removeHideoutNpc(int npcid) {
        int hideout = getGuild().getHideout();
        MapleGuild.removeHideoutNpc(c.getPlayer().getGuildId(), npcid);
        for (ChannelServer cs : c.getChannelServer().getAllInstances()) {
            for (MapleCharacter chars : cs.getMapFactory().getMap(hideout).getCharacters()) {
                chars.changeMap(100000000);
            }
            cs.getMapFactory().disposeMap(hideout);
        }
    }

    public void removeHideout() throws RemoteException {
        int hideout = getGuild().getHideout();
        c.getChannelServer().getWorldInterface().removeHideout(c.getPlayer().getGuildId());
        for (ChannelServer cs : c.getChannelServer().getAllInstances()) {
            for (MapleCharacter chars : cs.getMapFactory().getMap(hideout).getCharacters()) {
                chars.changeMap(100000000);
            }
            cs.getMapFactory().disposeMap(hideout);
        }
    }

    /*** TIC TAC TOE STUFF ***/
    public String tTTGameOver(int[] board, int player, int cp, int empty) {
        if (board[0] != 0 && ((board[0] == board[1] && board[1] == board[2]) || (board[0] == board[3] && board[3] == board[6]) || (board[0] == board[4] && board[4] == board[8]))) {
            return tTTGenerateBoard(board, player, cp, empty, true, board[0]);
        } else if (board[4] != 0 && ((board[3] == board[4] && board[4] == board[5]) || (board[1] == board[4] && board[4] == board[7]) || (board[2] == board[4] && board[4] == board[6]))) {
            return tTTGenerateBoard(board, player, cp, empty, true, board[4]);
        } else if (board[8] != 0 && ((board[6] == board[7] && board[7] == board[8]) || (board[2] == board[5] && board[5] == board[8]))) {
            return tTTGenerateBoard(board, player, cp, empty, true, board[8]);

        } else if ((board[1] != 0) && (board[2] != 0) && (board[3] != 0) && (board[4] != 0) && (board[5] != 0) && (board[6] != 0) && (board[7] != 0) && (board[8] != 0) && (board[0] != 0)) {
            return tTTGenerateBoard(board, player, cp, empty, true, -1);
        } else {
            return "0";
        }
    }

    public int[] tTTLogicMain(int[] board, boolean goodlogic) {
        ArrayList<Integer> newboard = new ArrayList();
        int playerturns = 0;
        int cpturns = 0;
        for (int j = 0; j < board.length; j++) {
            newboard.add(board[j]); //Generate NewBoard
            if (board[j] == 1) {
                playerturns++;
            } else if (board[j] == 2) {
                cpturns++;
            }
        }
        //GAMELOGIC START

        //CHECK IF CP CAN WIN (24 Possibilities[8x3])

        if (board[0] == 2 && board[1] == 2 && board[2] == 0) {
            newboard.set(2, 2);
        } else if (board[0] == 2 && board[1] == 0 && board[2] == 2) {
            newboard.set(1, 2);
        } else if (board[0] == 0 && board[1] == 2 && board[2] == 2) {
            newboard.set(0, 2);

        } else if (board[0] == 2 && board[3] == 2 && board[6] == 0) {
            newboard.set(6, 2);
        } else if (board[0] == 2 && board[3] == 0 && board[6] == 2) {
            newboard.set(3, 2);
        } else if (board[0] == 0 && board[3] == 2 && board[6] == 2) {
            newboard.set(0, 2);

        } else if (board[3] == 2 && board[4] == 2 && board[5] == 0) {
            newboard.set(5, 2);
        } else if (board[3] == 2 && board[4] == 0 && board[5] == 2) {
            newboard.set(4, 2);
        } else if (board[3] == 0 && board[4] == 2 && board[5] == 2) {
            newboard.set(3, 2);

        } else if (board[1] == 2 && board[4] == 2 && board[7] == 0) {
            newboard.set(7, 2);
        } else if (board[1] == 2 && board[4] == 0 && board[7] == 2) {
            newboard.set(4, 2);
        } else if (board[1] == 0 && board[4] == 2 && board[7] == 2) {
            newboard.set(1, 2);

        } else if (board[2] == 2 && board[5] == 2 && board[8] == 0) {
            newboard.set(8, 2);
        } else if (board[2] == 2 && board[5] == 0 && board[8] == 2) {
            newboard.set(5, 2);
        } else if (board[2] == 0 && board[5] == 2 && board[8] == 2) {
            newboard.set(2, 2);

        } else if (board[6] == 2 && board[7] == 2 && board[8] == 0) {
            newboard.set(8, 2);
        } else if (board[6] == 2 && board[7] == 0 && board[8] == 2) {
            newboard.set(7, 2);
        } else if (board[6] == 0 && board[7] == 2 && board[8] == 2) {
            newboard.set(6, 2);

        } else if (board[0] == 2 && board[4] == 2 && board[8] == 0) {
            newboard.set(8, 2);
        } else if (board[0] == 2 && board[4] == 0 && board[8] == 2) {
            newboard.set(4, 2);
        } else if (board[0] == 0 && board[4] == 2 && board[8] == 2) {
            newboard.set(0, 2);

        } else if (board[2] == 2 && board[4] == 2 && board[6] == 0) {
            newboard.set(6, 2);
        } else if (board[2] == 2 && board[4] == 0 && board[6] == 2) {
            newboard.set(4, 2);
        } else if (board[2] == 0 && board[4] == 2 && board[6] == 2) {
            newboard.set(2, 2);
        } else if (board[0] == 2 && board[1] == 2 && board[2] == 0) {
            newboard.set(2, 2);
        } else if (board[0] == 2 && board[1] == 0 && board[2] == 2) {
            newboard.set(1, 2);
        } else if (board[0] == 0 && board[1] == 2 && board[2] == 2) {
            newboard.set(0, 2);

        } else if (board[0] == 2 && board[3] == 2 && board[6] == 0) {
            newboard.set(6, 2);
        } else if (board[0] == 2 && board[3] == 0 && board[6] == 2) {
            newboard.set(3, 2);
        } else if (board[0] == 0 && board[3] == 2 && board[6] == 2) {
            newboard.set(0, 2);

        } else if (board[3] == 2 && board[4] == 2 && board[5] == 0) {
            newboard.set(5, 2);
        } else if (board[3] == 2 && board[4] == 0 && board[5] == 2) {
            newboard.set(4, 2);
        } else if (board[3] == 0 && board[4] == 2 && board[5] == 2) {
            newboard.set(3, 2);

        } else if (board[1] == 2 && board[4] == 2 && board[7] == 0) {
            newboard.set(7, 2);
        } else if (board[1] == 2 && board[4] == 0 && board[7] == 2) {
            newboard.set(4, 2);
        } else if (board[1] == 0 && board[4] == 2 && board[7] == 2) {
            newboard.set(1, 2);

        } else if (board[2] == 2 && board[5] == 2 && board[8] == 0) {
            newboard.set(8, 2);
        } else if (board[2] == 2 && board[5] == 0 && board[8] == 2) {
            newboard.set(5, 2);
        } else if (board[2] == 0 && board[5] == 2 && board[8] == 2) {
            newboard.set(2, 2);

        } else if (board[6] == 2 && board[7] == 2 && board[8] == 0) {
            newboard.set(8, 2);
        } else if (board[6] == 2 && board[7] == 0 && board[8] == 2) {
            newboard.set(7, 2);
        } else if (board[6] == 0 && board[7] == 2 && board[8] == 2) {
            newboard.set(6, 2);

        } else if (board[0] == 2 && board[4] == 2 && board[8] == 0) {
            newboard.set(8, 2);
        } else if (board[0] == 2 && board[4] == 0 && board[8] == 2) {
            newboard.set(4, 2);
        } else if (board[0] == 0 && board[4] == 2 && board[8] == 2) {
            newboard.set(0, 2);

        } else if (board[2] == 2 && board[4] == 2 && board[6] == 0) {
            newboard.set(6, 2);
        } else if (board[2] == 2 && board[4] == 0 && board[6] == 2) {
            newboard.set(4, 2);
        } else if (board[2] == 0 && board[4] == 2 && board[6] == 2) {
            newboard.set(2, 2);


            //CHECK IF CP SHOULD BLOCK (24 Possibilities[8x3])

        } else if (board[0] == 1 && board[1] == 1 && board[2] == 0) {
            newboard.set(2, 2);
        } else if (board[0] == 1 && board[1] == 0 && board[2] == 1) {
            newboard.set(1, 2);
        } else if (board[0] == 0 && board[1] == 1 && board[2] == 1) {
            newboard.set(0, 2);

        } else if (board[0] == 1 && board[3] == 1 && board[6] == 0) {
            newboard.set(6, 2);
        } else if (board[0] == 1 && board[3] == 0 && board[6] == 1) {
            newboard.set(3, 2);
        } else if (board[0] == 0 && board[3] == 1 && board[6] == 1) {
            newboard.set(0, 2);

        } else if (board[3] == 1 && board[4] == 1 && board[5] == 0) {
            newboard.set(5, 2);
        } else if (board[3] == 1 && board[4] == 0 && board[5] == 1) {
            newboard.set(4, 2);
        } else if (board[3] == 0 && board[4] == 1 && board[5] == 1) {
            newboard.set(3, 2);

        } else if (board[1] == 1 && board[4] == 1 && board[7] == 0) {
            newboard.set(7, 2);
        } else if (board[1] == 1 && board[4] == 0 && board[7] == 1) {
            newboard.set(4, 2);
        } else if (board[1] == 0 && board[4] == 1 && board[7] == 1) {
            newboard.set(1, 2);

        } else if (board[2] == 1 && board[5] == 1 && board[8] == 0) {
            newboard.set(8, 2);
        } else if (board[2] == 1 && board[5] == 0 && board[8] == 1) {
            newboard.set(5, 2);
        } else if (board[2] == 0 && board[5] == 1 && board[8] == 1) {
            newboard.set(2, 2);

        } else if (board[6] == 1 && board[7] == 1 && board[8] == 0) {
            newboard.set(8, 2);
        } else if (board[6] == 1 && board[7] == 0 && board[8] == 1) {
            newboard.set(7, 2);
        } else if (board[6] == 0 && board[7] == 1 && board[8] == 1) {
            newboard.set(6, 2);

        } else if (board[0] == 1 && board[4] == 1 && board[8] == 0) {
            newboard.set(8, 2);
        } else if (board[0] == 1 && board[4] == 0 && board[8] == 1) {
            newboard.set(4, 2);
        } else if (board[0] == 0 && board[4] == 1 && board[8] == 1) {
            newboard.set(0, 2);

        } else if (board[2] == 1 && board[4] == 1 && board[6] == 0) {
            newboard.set(6, 2);
        } else if (board[2] == 1 && board[4] == 0 && board[6] == 1) {
            newboard.set(4, 2);
        } else if (board[2] == 0 && board[4] == 1 && board[6] == 1) {
            newboard.set(2, 2);




            //START LOGIC //Note: Many cases covered by above conditionals.
        } else {

            if (goodlogic) {

                //Bandaid for some bad logic?
                if ((board[0] == 1 && board[8] == 1 && board[4] == 2 && board[1] == 0 && board[2] == 0 && board[3] == 0 && board[5] == 0 && board[6] == 0 && board[7] == 0)
                        || (board[2] == 1 && board[6] == 1 && board[4] == 2 && board[0] == 0 && board[1] == 0 && board[3] == 0 && board[5] == 0 && board[7] == 0 && board[8] == 0)) {
                    if (Math.random() < .5) {
                        newboard.set(tTTRandom2Move(1, 3), 2);
                    } else {
                        newboard.set(tTTRandom2Move(5, 7), 2);
                    }
                } else //Extra Winning Conditions. (Currently 88 combinations)
                if (board[2] == 2 && board[7] == 2 && board[5] == 0 && board[6] == 0 & board[8] == 0) {
                    newboard.set(8, 2);
                } else if (board[3] == 2 && board[8] == 2 && board[0] == 0 && board[6] == 0 & board[7] == 0) {
                    newboard.set(6, 2);
                } else if (board[1] == 2 && board[6] == 2 && board[0] == 0 && board[2] == 0 & board[3] == 0) {
                    newboard.set(0, 2);
                } else if (board[0] == 2 && board[5] == 2 && board[1] == 0 && board[2] == 0 & board[8] == 0) {
                    newboard.set(2, 2);

                } else if (board[0] == 2 && board[7] == 2 && board[3] == 0 && board[6] == 0 & board[8] == 0) {
                    newboard.set(6, 2);
                } else if (board[2] == 2 && board[3] == 2 && board[0] == 0 && board[1] == 0 & board[6] == 0) {
                    newboard.set(0, 2);
                } else if (board[1] == 2 && board[8] == 2 && board[0] == 0 && board[2] == 0 & board[5] == 0) {
                    newboard.set(2, 2);
                } else if (board[5] == 2 && board[6] == 2 && board[2] == 0 && board[7] == 0 & board[8] == 0) {
                    newboard.set(8, 2);

                    /*3*/                } else if (board[0] == 2 && board[8] == 2 && board[1] == 0 && board[2] == 0 && board[5] == 0) {
                    newboard.set(2, 2);
                } else if (board[0] == 2 && board[8] == 2 && board[3] == 0 && board[6] == 0 && board[7] == 0) {
                    newboard.set(6, 2);
                } else if (board[2] == 2 && board[6] == 2 && board[0] == 0 && board[1] == 0 && board[3] == 0) {
                    newboard.set(0, 2);
                } else if (board[2] == 2 && board[6] == 2 && board[5] == 0 && board[7] == 0 && board[8] == 0) {
                    newboard.set(8, 2);

                    /*4*/                } else if (board[1] == 2 && board[6] == 2 && board[4] == 0 && board[7] == 0 & board[8] == 0) {
                    newboard.set(7, 2);
                } else if (board[0] == 2 && board[5] == 2 && board[3] == 0 && board[4] == 0 & board[6] == 0) {
                    newboard.set(3, 2);
                } else if (board[2] == 2 && board[7] == 2 && board[0] == 0 && board[1] == 0 & board[4] == 0) {
                    newboard.set(1, 2);
                } else if (board[3] == 2 && board[8] == 2 && board[2] == 0 && board[4] == 0 & board[5] == 0) {
                    newboard.set(5, 2);

                } else if (board[1] == 2 && board[8] == 2 && board[4] == 0 && board[6] == 0 & board[7] == 0) {
                    newboard.set(7, 2);
                } else if (board[5] == 2 && board[6] == 2 && board[0] == 0 && board[3] == 0 & board[4] == 0) {
                    newboard.set(3, 2);
                } else if (board[0] == 2 && board[7] == 2 && board[1] == 0 && board[2] == 0 & board[4] == 0) {
                    newboard.set(1, 2);
                } else if (board[2] == 2 && board[3] == 2 && board[4] == 0 && board[5] == 0 & board[8] == 0) {
                    newboard.set(5, 2);

                    /*6*/                } else if (board[1] == 2 && board[3] == 2 && board[0] == 0 && board[2] == 0 && board[6] == 0) {
                    newboard.set(0, 2);
                } else if (board[1] == 2 && board[5] == 2 && board[0] == 0 && board[2] == 0 && board[8] == 0) {
                    newboard.set(2, 2);
                } else if (board[5] == 2 && board[7] == 2 && board[2] == 0 && board[6] == 0 && board[8] == 0) {
                    newboard.set(8, 2);
                } else if (board[3] == 2 && board[7] == 2 && board[0] == 0 && board[6] == 0 && board[8] == 0) {
                    newboard.set(6, 2);

                    /*7*/                } else if (board[1] == 2 && board[4] == 2 && board[0] == 0 && board[2] == 0 && board[6] == 0) {
                    newboard.set(0, 2);
                } else if (board[4] == 2 && board[5] == 2 && board[0] == 0 && board[2] == 0 && board[8] == 0) {
                    newboard.set(2, 2);
                } else if (board[4] == 2 && board[7] == 2 && board[2] == 0 && board[6] == 0 && board[8] == 0) {
                    newboard.set(8, 2);
                } else if (board[3] == 2 && board[4] == 2 && board[0] == 0 && board[6] == 0 && board[8] == 0) {
                    newboard.set(6, 2);

                } else if (board[1] == 2 && board[4] == 2 && board[0] == 0 && board[2] == 0 && board[8] == 0) {
                    newboard.set(2, 2);
                } else if (board[4] == 2 && board[5] == 2 && board[2] == 0 && board[6] == 0 && board[8] == 0) {
                    newboard.set(8, 2);
                } else if (board[4] == 2 && board[7] == 2 && board[0] == 0 && board[6] == 0 && board[8] == 0) {
                    newboard.set(6, 2);
                } else if (board[3] == 2 && board[4] == 2 && board[0] == 0 && board[2] == 0 && board[6] == 0) {
                    newboard.set(0, 2);

                    /*9*/                } else if (board[2] == 2 && board[4] == 2 && board[0] == 0 && board[1] == 0 && board[7] == 0) {
                    newboard.set(1, 2);
                } else if (board[4] == 2 && board[8] == 2 && board[2] == 0 && board[3] == 0 && board[5] == 0) {
                    newboard.set(5, 2);
                } else if (board[4] == 2 && board[6] == 2 && board[1] == 0 && board[7] == 0 && board[8] == 0) {
                    newboard.set(7, 2);
                } else if (board[0] == 2 && board[4] == 2 && board[3] == 0 && board[5] == 0 && board[6] == 0) {
                    newboard.set(3, 2);

                } else if (board[0] == 2 && board[4] == 2 && board[1] == 0 && board[2] == 0 && board[7] == 0) {
                    newboard.set(1, 2);
                } else if (board[2] == 2 && board[4] == 2 && board[3] == 0 && board[5] == 0 && board[8] == 0) {
                    newboard.set(5, 2);
                } else if (board[4] == 2 && board[8] == 2 && board[1] == 0 && board[6] == 0 && board[7] == 0) {
                    newboard.set(7, 2);
                } else if (board[4] == 2 && board[6] == 2 && board[3] == 0 && board[6] == 0 && board[8] == 0) {
                    newboard.set(3, 2);

                    /*11*/                } else if (board[0] == 2 && board[4] == 2 && board[1] == 0 && board[2] == 0 && board[6] == 0) {
                    newboard.set(2, 2);
                } else if (board[2] == 2 && board[4] == 2 && board[3] == 0 && board[5] == 0 && board[8] == 0) {
                    newboard.set(8, 2);
                } else if (board[4] == 2 && board[8] == 2 && board[1] == 0 && board[6] == 0 && board[7] == 0) {
                    newboard.set(6, 2);
                } else if (board[4] == 2 && board[6] == 2 && board[0] == 0 && board[3] == 0 && board[5] == 0) {
                    newboard.set(0, 2);

                } else if (board[2] == 2 && board[4] == 2 && board[0] == 0 && board[1] == 0 && board[8] == 0) {
                    newboard.set(0, 2);
                } else if (board[4] == 2 && board[8] == 2 && board[2] == 0 && board[5] == 0 && board[6] == 0) {
                    newboard.set(2, 2);
                } else if (board[4] == 2 && board[6] == 2 && board[0] == 0 && board[7] == 0 && board[8] == 0) {
                    newboard.set(8, 2);
                } else if (board[0] == 2 && board[4] == 2 && board[2] == 0 && board[3] == 0 && board[6] == 0) {
                    newboard.set(6, 2);

                    /*13*/                } else if (board[1] == 2 && board[6] == 2 && board[0] == 0 && board[2] == 0 && board[4] == 0) {
                    newboard.set(2, 2);
                } else if (board[0] == 2 && board[5] == 2 && board[2] == 0 && board[4] == 0 && board[8] == 0) {
                    newboard.set(8, 2);
                } else if (board[2] == 2 && board[7] == 2 && board[4] == 0 && board[6] == 0 && board[8] == 0) {
                    newboard.set(6, 2);
                } else if (board[3] == 2 && board[8] == 2 && board[0] == 0 && board[4] == 0 && board[6] == 0) {
                    newboard.set(0, 2);

                } else if (board[1] == 2 && board[8] == 2 && board[0] == 0 && board[2] == 0 && board[4] == 0) {
                    newboard.set(0, 2);
                } else if (board[5] == 2 && board[6] == 2 && board[2] == 0 && board[4] == 0 && board[8] == 0) {
                    newboard.set(2, 2);
                } else if (board[0] == 2 && board[7] == 2 && board[4] == 0 && board[6] == 0 && board[8] == 0) {
                    newboard.set(8, 2);
                } else if (board[2] == 2 && board[3] == 2 && board[0] == 0 && board[4] == 0 && board[6] == 0) {
                    newboard.set(6, 2);

                    /*15*/                } else if (board[0] == 2 && board[6] == 2 && board[1] == 0 && board[2] == 0 && board[4] == 0) {
                    newboard.set(2, 2);
                } else if (board[0] == 2 && board[2] == 2 && board[4] == 0 && board[5] == 0 && board[8] == 0) {
                    newboard.set(8, 2);
                } else if (board[2] == 2 && board[8] == 2 && board[4] == 0 && board[6] == 0 && board[7] == 0) {
                    newboard.set(6, 2);
                } else if (board[6] == 2 && board[8] == 2 && board[0] == 0 && board[3] == 0 && board[4] == 0) {
                    newboard.set(0, 2);

                } else if (board[2] == 2 && board[8] == 2 && board[0] == 0 && board[1] == 0 && board[4] == 0) {
                    newboard.set(0, 2);
                } else if (board[6] == 2 && board[8] == 2 && board[2] == 0 && board[4] == 0 && board[5] == 0) {
                    newboard.set(2, 2);
                } else if (board[0] == 2 && board[6] == 2 && board[4] == 0 && board[7] == 0 && board[8] == 0) {
                    newboard.set(8, 2);
                } else if (board[0] == 2 && board[2] == 2 && board[3] == 0 && board[4] == 0 && board[6] == 0) {
                    newboard.set(6, 2);

                    /*17*/                } else if (board[1] == 2 && board[5] == 2 && board[3] == 0 && board[4] == 0 && board[7] == 0) {
                    newboard.set(4, 2);
                } else if (board[5] == 2 && board[7] == 2 && board[1] == 0 && board[3] == 0 && board[4] == 0) {
                    newboard.set(4, 2);
                } else if (board[3] == 2 && board[7] == 2 && board[1] == 0 && board[4] == 0 && board[5] == 0) {
                    newboard.set(4, 2);
                } else if (board[1] == 2 && board[3] == 2 && board[4] == 0 && board[5] == 0 && board[7] == 0) {
                    newboard.set(4, 2);

                    /*18*/                } else if (board[0] == 2 && board[2] == 2 && board[4] == 0 && board[6] == 0 && board[8] == 0) {
                    newboard.set(4, 2);
                } else if (board[2] == 2 && board[8] == 2 && board[0] == 0 && board[4] == 0 && board[6] == 0) {
                    newboard.set(4, 2);
                } else if (board[6] == 2 && board[8] == 2 && board[0] == 0 && board[2] == 0 && board[4] == 0) {
                    newboard.set(4, 2);
                } else if (board[0] == 2 && board[6] == 2 && board[2] == 0 && board[4] == 0 && board[8] == 0) {
                    newboard.set(4, 2);

                    /*19*/                } else if (board[0] == 2 && board[1] == 2 && board[4] == 0 && board[7] == 0 && board[8] == 0) {
                    newboard.set(4, 2);
                } else if (board[2] == 2 && board[5] == 2 && board[3] == 0 && board[4] == 0 && board[6] == 0) {
                    newboard.set(4, 2);
                } else if (board[7] == 2 && board[8] == 2 && board[0] == 0 && board[1] == 0 && board[4] == 0) {
                    newboard.set(4, 2);
                } else if (board[3] == 2 && board[6] == 2 && board[2] == 0 && board[4] == 0 && board[5] == 0) {
                    newboard.set(4, 2);

                } else if (board[1] == 2 && board[2] == 2 && board[4] == 0 && board[6] == 0 && board[7] == 0) {
                    newboard.set(4, 2);
                } else if (board[5] == 2 && board[8] == 2 && board[0] == 0 && board[3] == 0 && board[4] == 0) {
                    newboard.set(4, 2);
                } else if (board[6] == 2 && board[7] == 2 && board[1] == 0 && board[2] == 0 && board[4] == 0) {
                    newboard.set(4, 2);
                } else if (board[0] == 2 && board[3] == 2 && board[4] == 0 && board[5] == 0 && board[8] == 0) {
                    newboard.set(4, 2);

                    /*21*/                } else if (board[1] == 2 && board[8] == 2 && board[0] == 0 && board[4] == 0 && board[7] == 0) {
                    newboard.set(4, 2);
                } else if (board[5] == 2 && board[6] == 2 && board[2] == 0 && board[3] == 0 && board[4] == 0) {
                    newboard.set(4, 2);
                } else if (board[0] == 2 && board[7] == 2 && board[1] == 0 && board[4] == 0 && board[8] == 0) {
                    newboard.set(4, 2);
                } else if (board[2] == 2 && board[3] == 2 && board[4] == 0 && board[5] == 0 && board[6] == 0) {
                    newboard.set(4, 2);

                } else if (board[1] == 2 && board[6] == 2 && board[2] == 0 && board[4] == 0 && board[7] == 0) {
                    newboard.set(4, 2);
                } else if (board[0] == 2 && board[5] == 2 && board[3] == 0 && board[4] == 0 && board[8] == 0) {
                    newboard.set(4, 2);
                } else if (board[2] == 2 && board[7] == 2 && board[1] == 0 && board[4] == 0 && board[6] == 0) {
                    newboard.set(4, 2);
                } else if (board[3] == 2 && board[8] == 2 && board[0] == 0 && board[4] == 0 && board[5] == 0) {
                    newboard.set(4, 2);


                    //      \\      //      \\      //      \\

                    //Specific Blocking Conditions (Currently 88 combinations)
                } else if (board[2] == 1 && board[7] == 1 && board[5] == 0 && board[6] == 0 & board[8] == 0) {
                    newboard.set(8, 2);
                } else if (board[3] == 1 && board[8] == 1 && board[0] == 0 && board[6] == 0 & board[7] == 0) {
                    newboard.set(6, 2);
                } else if (board[1] == 1 && board[6] == 1 && board[0] == 0 && board[2] == 0 & board[3] == 0) {
                    newboard.set(0, 2);
                } else if (board[0] == 1 && board[5] == 1 && board[1] == 0 && board[2] == 0 & board[8] == 0) {
                    newboard.set(2, 2);

                } else if (board[0] == 1 && board[7] == 1 && board[3] == 0 && board[6] == 0 & board[8] == 0) {
                    newboard.set(6, 2);
                } else if (board[2] == 1 && board[3] == 1 && board[0] == 0 && board[1] == 0 & board[6] == 0) {
                    newboard.set(0, 2);
                } else if (board[1] == 1 && board[8] == 1 && board[0] == 0 && board[2] == 0 & board[5] == 0) {
                    newboard.set(2, 2);
                } else if (board[5] == 1 && board[6] == 1 && board[2] == 0 && board[7] == 0 & board[8] == 0) {
                    newboard.set(8, 2);

                    /*3*/                } else if (board[0] == 1 && board[8] == 1 && board[1] == 0 && board[2] == 0 && board[5] == 0) {
                    newboard.set(2, 2);
                } else if (board[0] == 1 && board[8] == 1 && board[3] == 0 && board[6] == 0 && board[7] == 0) {
                    newboard.set(6, 2);
                } else if (board[2] == 1 && board[6] == 1 && board[0] == 0 && board[1] == 0 && board[3] == 0) {
                    newboard.set(0, 2);
                } else if (board[2] == 1 && board[6] == 1 && board[5] == 0 && board[7] == 0 && board[8] == 0) {
                    newboard.set(8, 2);

                    /*4*/                } else if (board[1] == 1 && board[6] == 1 && board[4] == 0 && board[7] == 0 & board[8] == 0) {
                    newboard.set(7, 2);
                } else if (board[0] == 1 && board[5] == 1 && board[3] == 0 && board[4] == 0 & board[6] == 0) {
                    newboard.set(3, 2);
                } else if (board[2] == 1 && board[7] == 1 && board[0] == 0 && board[1] == 0 & board[4] == 0) {
                    newboard.set(1, 2);
                } else if (board[3] == 1 && board[8] == 1 && board[2] == 0 && board[4] == 0 & board[5] == 0) {
                    newboard.set(5, 2);

                } else if (board[1] == 1 && board[8] == 1 && board[4] == 0 && board[6] == 0 & board[7] == 0) {
                    newboard.set(7, 2);
                } else if (board[5] == 1 && board[6] == 1 && board[0] == 0 && board[3] == 0 & board[4] == 0) {
                    newboard.set(3, 2);
                } else if (board[0] == 1 && board[7] == 1 && board[1] == 0 && board[2] == 0 & board[4] == 0) {
                    newboard.set(1, 2);
                } else if (board[2] == 1 && board[3] == 1 && board[4] == 0 && board[5] == 0 & board[8] == 0) {
                    newboard.set(5, 2);


                    /*6*/                } else if (board[1] == 1 && board[3] == 1 && board[0] == 0 && board[2] == 0 && board[6] == 0) {
                    newboard.set(0, 2);
                } else if (board[1] == 1 && board[5] == 1 && board[0] == 0 && board[2] == 0 && board[8] == 0) {
                    newboard.set(2, 2);
                } else if (board[5] == 1 && board[7] == 1 && board[2] == 0 && board[6] == 0 && board[8] == 0) {
                    newboard.set(8, 2);
                } else if (board[3] == 1 && board[7] == 1 && board[0] == 0 && board[6] == 0 && board[8] == 0) {
                    newboard.set(6, 2);

                    /*7*/                } else if (board[1] == 1 && board[4] == 1 && board[0] == 0 && board[2] == 0 && board[6] == 0) {
                    newboard.set(0, 2);
                } else if (board[4] == 1 && board[5] == 1 && board[0] == 0 && board[2] == 0 && board[8] == 0) {
                    newboard.set(2, 2);
                } else if (board[4] == 1 && board[7] == 1 && board[2] == 0 && board[6] == 0 && board[8] == 0) {
                    newboard.set(8, 2);
                } else if (board[3] == 1 && board[4] == 1 && board[0] == 0 && board[6] == 0 && board[8] == 0) {
                    newboard.set(6, 2);

                } else if (board[1] == 1 && board[4] == 1 && board[0] == 0 && board[2] == 0 && board[8] == 0) {
                    newboard.set(2, 2);
                } else if (board[4] == 1 && board[5] == 1 && board[2] == 0 && board[6] == 0 && board[8] == 0) {
                    newboard.set(8, 2);
                } else if (board[4] == 1 && board[7] == 1 && board[0] == 0 && board[6] == 0 && board[8] == 0) {
                    newboard.set(6, 2);
                } else if (board[3] == 1 && board[4] == 1 && board[0] == 0 && board[2] == 0 && board[6] == 0) {
                    newboard.set(0, 2);

                    /*9*/                } else if (board[2] == 1 && board[4] == 1 && board[0] == 0 && board[1] == 0 && board[7] == 0) {
                    newboard.set(1, 2);
                } else if (board[4] == 1 && board[8] == 1 && board[2] == 0 && board[3] == 0 && board[5] == 0) {
                    newboard.set(5, 2);
                } else if (board[4] == 1 && board[6] == 1 && board[1] == 0 && board[7] == 0 && board[8] == 0) {
                    newboard.set(7, 2);
                } else if (board[0] == 1 && board[4] == 1 && board[3] == 0 && board[5] == 0 && board[6] == 0) {
                    newboard.set(3, 2);

                } else if (board[0] == 1 && board[4] == 1 && board[1] == 0 && board[2] == 0 && board[7] == 0) {
                    newboard.set(1, 2);
                } else if (board[2] == 1 && board[4] == 1 && board[3] == 0 && board[5] == 0 && board[8] == 0) {
                    newboard.set(5, 2);
                } else if (board[4] == 1 && board[8] == 1 && board[1] == 0 && board[6] == 0 && board[7] == 0) {
                    newboard.set(7, 2);
                } else if (board[4] == 1 && board[6] == 1 && board[3] == 0 && board[6] == 0 && board[8] == 0) {
                    newboard.set(3, 2);


                    /*11*/                } else if (board[0] == 1 && board[4] == 1 && board[1] == 0 && board[2] == 0 && board[6] == 0) {
                    newboard.set(2, 2);
                } else if (board[2] == 1 && board[4] == 1 && board[3] == 0 && board[5] == 0 && board[8] == 0) {
                    newboard.set(8, 2);
                } else if (board[4] == 1 && board[8] == 1 && board[1] == 0 && board[6] == 0 && board[7] == 0) {
                    newboard.set(6, 2);
                } else if (board[4] == 1 && board[6] == 1 && board[0] == 0 && board[3] == 0 && board[5] == 0) {
                    newboard.set(0, 2);

                } else if (board[2] == 1 && board[4] == 1 && board[0] == 0 && board[1] == 0 && board[8] == 0) {
                    newboard.set(0, 2);
                } else if (board[4] == 1 && board[8] == 1 && board[2] == 0 && board[5] == 0 && board[6] == 0) {
                    newboard.set(2, 2);
                } else if (board[4] == 1 && board[6] == 1 && board[0] == 0 && board[7] == 0 && board[8] == 0) {
                    newboard.set(8, 2);
                } else if (board[0] == 1 && board[4] == 1 && board[2] == 0 && board[3] == 0 && board[6] == 0) {
                    newboard.set(6, 2);

                    /*13*/                } else if (board[1] == 1 && board[6] == 1 && board[0] == 0 && board[2] == 0 && board[4] == 0) {
                    newboard.set(2, 2);
                } else if (board[0] == 1 && board[5] == 1 && board[2] == 0 && board[4] == 0 && board[8] == 0) {
                    newboard.set(8, 2);
                } else if (board[2] == 1 && board[7] == 1 && board[4] == 0 && board[6] == 0 && board[8] == 0) {
                    newboard.set(6, 2);
                } else if (board[3] == 1 && board[8] == 1 && board[0] == 0 && board[4] == 0 && board[6] == 0) {
                    newboard.set(0, 2);

                } else if (board[1] == 1 && board[8] == 1 && board[0] == 0 && board[2] == 0 && board[4] == 0) {
                    newboard.set(0, 2);
                } else if (board[5] == 1 && board[6] == 1 && board[2] == 0 && board[4] == 0 && board[8] == 0) {
                    newboard.set(2, 2);
                } else if (board[0] == 1 && board[7] == 1 && board[4] == 0 && board[6] == 0 && board[8] == 0) {
                    newboard.set(8, 2);
                } else if (board[2] == 1 && board[3] == 1 && board[0] == 0 && board[4] == 0 && board[6] == 0) {
                    newboard.set(6, 2);

                    /*15*/                } else if (board[0] == 1 && board[6] == 1 && board[1] == 0 && board[2] == 0 && board[4] == 0) {
                    newboard.set(2, 2);
                } else if (board[0] == 1 && board[2] == 1 && board[4] == 0 && board[5] == 0 && board[8] == 0) {
                    newboard.set(8, 2);
                } else if (board[2] == 1 && board[8] == 1 && board[4] == 0 && board[6] == 0 && board[7] == 0) {
                    newboard.set(6, 2);
                } else if (board[6] == 1 && board[8] == 1 && board[0] == 0 && board[3] == 0 && board[4] == 0) {
                    newboard.set(0, 2);

                } else if (board[2] == 1 && board[8] == 1 && board[0] == 0 && board[1] == 0 && board[4] == 0) {
                    newboard.set(0, 2);
                } else if (board[6] == 1 && board[8] == 1 && board[2] == 0 && board[4] == 0 && board[5] == 0) {
                    newboard.set(2, 2);
                } else if (board[0] == 1 && board[6] == 1 && board[4] == 0 && board[7] == 0 && board[8] == 0) {
                    newboard.set(8, 2);
                } else if (board[0] == 1 && board[2] == 1 && board[3] == 0 && board[4] == 0 && board[6] == 0) {
                    newboard.set(6, 2);

                    /*17*/                } else if (board[1] == 1 && board[5] == 1 && board[3] == 0 && board[4] == 0 && board[7] == 0) {
                    newboard.set(4, 2);
                } else if (board[5] == 1 && board[7] == 1 && board[1] == 0 && board[3] == 0 && board[4] == 0) {
                    newboard.set(4, 2);
                } else if (board[3] == 1 && board[7] == 1 && board[1] == 0 && board[4] == 0 && board[5] == 0) {
                    newboard.set(4, 2);
                } else if (board[1] == 1 && board[3] == 1 && board[4] == 0 && board[5] == 0 && board[7] == 0) {
                    newboard.set(4, 2);

                    /*18*/                } else if (board[0] == 1 && board[2] == 1 && board[4] == 0 && board[6] == 0 && board[8] == 0) {
                    newboard.set(4, 2);
                } else if (board[2] == 1 && board[8] == 1 && board[0] == 0 && board[4] == 0 && board[6] == 0) {
                    newboard.set(4, 2);
                } else if (board[6] == 1 && board[8] == 1 && board[0] == 0 && board[2] == 0 && board[4] == 0) {
                    newboard.set(4, 2);
                } else if (board[0] == 1 && board[6] == 1 && board[2] == 0 && board[4] == 0 && board[8] == 0) {
                    newboard.set(4, 2);

                    /*19*/                } else if (board[0] == 1 && board[1] == 1 && board[4] == 0 && board[7] == 0 && board[8] == 0) {
                    newboard.set(4, 2);
                } else if (board[2] == 1 && board[5] == 1 && board[3] == 0 && board[4] == 0 && board[6] == 0) {
                    newboard.set(4, 2);
                } else if (board[7] == 1 && board[8] == 1 && board[0] == 0 && board[1] == 0 && board[4] == 0) {
                    newboard.set(4, 2);
                } else if (board[3] == 1 && board[6] == 1 && board[2] == 0 && board[4] == 0 && board[5] == 0) {
                    newboard.set(4, 2);

                } else if (board[1] == 1 && board[2] == 1 && board[4] == 0 && board[6] == 0 && board[7] == 0) {
                    newboard.set(4, 2);
                } else if (board[5] == 1 && board[8] == 1 && board[0] == 0 && board[3] == 0 && board[4] == 0) {
                    newboard.set(4, 2);
                } else if (board[6] == 1 && board[7] == 1 && board[1] == 0 && board[2] == 0 && board[4] == 0) {
                    newboard.set(4, 2);
                } else if (board[0] == 1 && board[3] == 1 && board[4] == 0 && board[5] == 0 && board[8] == 0) {
                    newboard.set(4, 2);

                    /*21*/                } else if (board[1] == 1 && board[8] == 1 && board[0] == 0 && board[4] == 0 && board[7] == 0) {
                    newboard.set(4, 2);
                } else if (board[5] == 1 && board[6] == 1 && board[2] == 0 && board[3] == 0 && board[4] == 0) {
                    newboard.set(4, 2);
                } else if (board[0] == 1 && board[7] == 1 && board[1] == 0 && board[4] == 0 && board[8] == 0) {
                    newboard.set(4, 2);
                } else if (board[2] == 1 && board[3] == 1 && board[4] == 0 && board[5] == 0 && board[6] == 0) {
                    newboard.set(4, 2);

                } else if (board[1] == 1 && board[6] == 1 && board[2] == 0 && board[4] == 0 && board[7] == 0) {
                    newboard.set(4, 2);
                } else if (board[0] == 1 && board[5] == 1 && board[3] == 0 && board[4] == 0 && board[8] == 0) {
                    newboard.set(4, 2);
                } else if (board[2] == 1 && board[7] == 1 && board[1] == 0 && board[4] == 0 && board[6] == 0) {
                    newboard.set(4, 2);
                } else if (board[3] == 1 && board[8] == 1 && board[0] == 0 && board[4] == 0 && board[5] == 0) {
                    newboard.set(4, 2);
                } //cpturns should NEVER be greater than player turns.
                else if (playerturns > cpturns) { //Player Went First.
                    if (cpturns == 0) { //Turn 1 for CP.
                        //Player Took a corner, CP takes Middle.
                        if (board[0] == 1 || board[2] == 1 || board[6] == 1 || board[8] == 1) {
                            newboard.set(4, 2);

                            //Player took a side space. CP takes Middle or adjacent corner or opposite edge. ??? Verify that CP won't lose.
                        } else if (board[1] == 1 || board[3] == 1 || board[5] == 1 || board[7] == 1) {
                            if (board[1] == 1) {
                                if (Math.random() < .5) {
                                    newboard.set(tTTRandom2Move(4, 7), 2);
                                } else {
                                    newboard.set(tTTRandom2Move(0, 2), 2);
                                }
                            } else if (board[3] == 1) {
                                if (Math.random() < .5) {
                                    newboard.set(tTTRandom2Move(4, 5), 2);
                                } else {
                                    newboard.set(tTTRandom2Move(0, 6), 2);
                                }
                            } else if (board[5] == 1) {
                                if (Math.random() < .5) {
                                    newboard.set(tTTRandom2Move(4, 3), 2);
                                } else {
                                    newboard.set(tTTRandom2Move(2, 8), 2);
                                }
                            } else if (board[7] == 1) {
                                if (Math.random() < .5) {
                                    newboard.set(tTTRandom2Move(1, 4), 2);
                                } else {
                                    newboard.set(tTTRandom2Move(6, 8), 2);
                                }
                            }

                            //Player took the middle, CP takes a corner.
                        } else {
                            if (Math.random() < .5) {
                                newboard.set(tTTRandom2Move(0, 2), 2);
                            } else {
                                newboard.set(tTTRandom2Move(6, 8), 2);
                            }
                        }
                    } else if (cpturns == 1) { //CP Turn 2
                        //Most possibilities should be handled by above conditionals.
                        //In case not. Leave this.

                        //IF Player has two opposite corners, take a side space!
                        if ((board[0] == 1 && board[8] == 1) || (board[2] == 1 && board[6] == 1)) {
                            if (Math.random() < .5) {
                                newboard.set(tTTRandom2Move(1, 3), 2);
                            } else {
                                newboard.set(tTTRandom2Move(5, 7), 2);
                            }
                        } else {
                            newboard.set(tTTRandom8Move(board), 2);
                        }
                    } else if (cpturns > 1) { //CP Turn 3
                        newboard.set(tTTRandom8Move(board), 2);
                    }


                } else if (playerturns == cpturns) { //Player Went Second.
                    if (cpturns == 0) { //Turn 1 for CP.
                        if (Math.random() > 0.5) {
                            newboard.set(4, 2); //Take Center
                        } else {
                            newboard.set(tTTRandomCorner(), 2);//Take a Corner
                        }
                    } else if (cpturns == 1) { //Turn 2 for CP.
                        if (board[4] == 2) { //CP took center on first turn.

                            if (board[1] == 1) {//Player Loses,Took a side space.
                                newboard.set(tTTRandom2Move(0, 2), 2);
                            } else if (board[3] == 1) {
                                newboard.set(tTTRandom2Move(0, 6), 2);
                            } else if (board[5] == 1) {
                                newboard.set(tTTRandom2Move(2, 8), 2);
                            } else if (board[7] == 1) {
                                newboard.set(tTTRandom2Move(6, 8), 2);

                            } else if (board[0] == 1) {//Player Took a Corner.
                                newboard.set(tTTRandom8Move(board), 2);
                            } else if (board[2] == 1) {
                                newboard.set(tTTRandom8Move(board), 2);
                            } else if (board[6] == 1) {
                                newboard.set(tTTRandom8Move(board), 2);
                            } else if (board[8] == 1) {
                                newboard.set(tTTRandom8Move(board), 2);
                            }

                        } else { //CP Took a corner on the first turn.
                            if (board[4] == 0 && (board[1] == 1 || board[3] == 1 || board[5] == 1 || board[7] == 1)) {
                                newboard.set(4, 2); //Take Center if not taken already AND player took a side. Player Loses.
                            } else if (board[4] == 0 && (board[0] == 1 || board[2] == 1 || board[6] == 1 || board[8] == 1)) {
                                //If player took an adjacent corner, player loses.
                                //If player took opposite corner, player loses.
                                if (board[0] == 2 && board[8] == 0) {
                                    if (board[2] == 1) {
                                        newboard.set(3, 2);
                                    } else if (board[6] == 1) {
                                        newboard.set(1, 2);
                                    }
                                } else if (board[2] == 2 && board[6] == 0) {
                                    if (board[0] == 1) {
                                        newboard.set(5, 2);
                                    } else if (board[8] == 1) {
                                        newboard.set(1, 2);
                                    }
                                } else if (board[6] == 2 && board[2] == 0) {
                                    if (board[0] == 1) {
                                        newboard.set(7, 2);
                                    } else if (board[8] == 1) {
                                        newboard.set(3, 2);
                                    }
                                } else if (board[8] == 2 && board[0] == 0) {
                                    if (board[2] == 1) {
                                        newboard.set(7, 2);
                                    } else if (board[6] == 1) {
                                        newboard.set(5, 2);
                                    }


                                } else if (board[0] == 2 && board[8] == 1) {
                                    newboard.set(tTTRandom2Move(2, 6), 2);
                                } else if (board[2] == 2 && board[6] == 1) {
                                    newboard.set(tTTRandom2Move(0, 8), 2);
                                } else if (board[6] == 2 && board[2] == 1) {
                                    newboard.set(tTTRandom2Move(0, 8), 2);
                                } else if (board[8] == 2 && board[0] == 1) {
                                    newboard.set(tTTRandom2Move(2, 6), 2);
                                }
                            } else { //If Player Took Center
                                //Weighted Randomize a move based on which corner you originally took.
                                if (board[0] == 2) {
                                    if (Math.random() < 0.5) {
                                        if (Math.random() < 0.5) {
                                            newboard.set(tTTRandom2Move(1, 3), 2);
                                        } else {
                                            newboard.set(tTTRandom2Move(2, 6), 2);
                                        }
                                    } else {
                                        if (Math.random() < 0.5) {
                                            newboard.set(tTTRandom2Move(5, 7), 2);
                                        } else {
                                            newboard.set(8, 2);
                                        }
                                    }
                                } else if (board[2] == 2) {
                                    if (Math.random() < 0.5) {
                                        if (Math.random() < 0.5) {
                                            newboard.set(tTTRandom2Move(1, 5), 2);
                                        } else {
                                            newboard.set(tTTRandom2Move(0, 8), 2);
                                        }
                                    } else {
                                        if (Math.random() < 0.5) {
                                            newboard.set(tTTRandom2Move(3, 7), 2);
                                        } else {
                                            newboard.set(6, 2);
                                        }
                                    }
                                } else if (board[6] == 2) {
                                    if (Math.random() < 0.5) {
                                        if (Math.random() < 0.5) {
                                            newboard.set(tTTRandom2Move(3, 7), 2);
                                        } else {
                                            newboard.set(tTTRandom2Move(0, 8), 2);
                                        }
                                    } else {
                                        if (Math.random() < 0.5) {
                                            newboard.set(tTTRandom2Move(1, 5), 2);
                                        } else {
                                            newboard.set(2, 2);
                                        }
                                    }
                                } else if (board[8] == 2) {
                                    if (Math.random() < 0.5) {
                                        if (Math.random() < 0.5) {
                                            newboard.set(tTTRandom2Move(5, 7), 2);
                                        } else {
                                            newboard.set(tTTRandom2Move(2, 6), 2);
                                        }
                                    } else {
                                        if (Math.random() < 0.5) {
                                            newboard.set(tTTRandom2Move(1, 3), 2);
                                        } else {
                                            newboard.set(0, 2);
                                        }
                                    }
                                }
                            }
                        }
                    } else if (cpturns == 2) { //Turn 3 for CP.
                        if (board[4] == 2 && (board[0] == 2 || board[2] == 2 || board[6] == 2 || board[8] == 2) && (board[1] == 1 || board[3] == 1 || board[5] == 1 || board[7] == 1)) {
                            //CP has center and a corner and player took side.
                            if (board[1] == 1) {
                                if (board[0] == 2) {
                                    newboard.set(3, 2);
                                } else {
                                    newboard.set(5, 2);
                                }
                            } else if (board[3] == 1) {
                                if (board[0] == 2) {
                                    newboard.set(1, 2);
                                } else {
                                    newboard.set(7, 2);
                                }
                            } else if (board[5] == 1) {
                                if (board[8] == 2) {
                                    newboard.set(7, 2);
                                } else {
                                    newboard.set(1, 2);
                                }
                            } else if (board[7] == 1) {
                                if (board[8] == 2) {
                                    newboard.set(5, 2);
                                } else {
                                    newboard.set(3, 2);
                                }
                            }
                        } else if (board[4] == 2 && (board[0] == 2 || board[2] == 2 || board[6] == 2 || board[8] == 2) && (board[1] == 1 || board[3] == 1 || board[5] == 1 || board[7] == 1)) {
                            //CP has middle and corner. Player has a corner.
                            ////ID corner, then ID other move.
                            if (board[0] == 1) {
                                if (board[5] == 1) {
                                    newboard.set(8, 2);
                                } else if (board[8] == 1) {
                                    newboard.set(5, 2);
                                }
                            } else if (board[2] == 1) {
                                if (board[3] == 1) {
                                    newboard.set(7, 2);
                                } else if (board[7] == 1) {
                                    newboard.set(3, 2);
                                }
                            } else if (board[6] == 1) {
                                if (board[1] == 1) {
                                    newboard.set(5, 2);
                                } else if (board[5] == 1) {
                                    newboard.set(1, 2);
                                }
                            } else if (board[8] == 1) {
                                if (board[1] == 1) {
                                    newboard.set(3, 2);
                                } else if (board[3] == 1) {
                                    newboard.set(1, 2);
                                }
                            }
                        } else {
                            newboard.set(tTTRandom8Move(board), 2);
                        }
                    } else if (cpturns > 2) { // Game should be decided by CP's Turn 4.
                        newboard.set(tTTRandom8Move(board), 2);
                    }
                }
            } else {
                //"Easy" Logic - Random Move.
                newboard.set(tTTRandom8Move(board), 2);
            }
        }

        //CONVERT BACK
        int[] newboardint = new int[newboard.size()];
        for (int i = 0; i < newboard.size(); i++) {
            newboardint[i] = newboard.get(i).intValue();
        }
        return newboardint;
    }

    public int tTTRandom8Move(int[] board) {
        int move = (int) Math.floor(Math.random() * board.length);
        while (board[move] != 0) {
            move = (int) Math.floor(Math.random() * board.length);
        }
        return move;
    }

    public int tTTRandom2Move(int move1, int move2) {
        if (Math.random() < .5) {
            return move1;
        } else {
            return move2;
        }
    }

    public int tTTRandom5Move(int move1, int move2, int move3, int move4, int move5) {
        if (Math.random() < .2) {
            return move1;
        } else if (Math.random() < .4) {
            return move2;
        } else if (Math.random() < .6) {
            return move3;
        } else if (Math.random() < .8) {
            return move4;
        } else {
            return move5;
        }
    }

    public int tTTRandomCorner() {
        double rand = Math.random();
        if (rand >= 0 && rand < .25) {
            return 0;
        } else if (rand >= .25 && rand < .5) {
            return 2;
        } else if (rand >= .5 && rand < .75) {
            return 6;
        } else {
            return 8;
        }
    }

    public String tTTGenerateBoard(int[] board, int player, int cp, int empty, boolean gameover, int winner) {
        StringBuilder sb = new StringBuilder(50);
        sb.append("\r\n");
        int sel = 0;
        for (int i = 0; i < board.length; i++) {
            if (board[i] == 1) {
                sb.append("#L1000##v" + player + "##l");
            } else if (board[i] == 2) {
                sb.append("#L1000##v" + cp + "##l");
            } else if (board[i] == 0) {
                sb.append("#L" + sel + "##v" + empty + "##l");
            }
            sel++;
            if ((i + 1) % 3 == 0) {
                sb.append("\r\n");
            }
        }
        if (gameover) {
            if (winner == -1) {
                sb.append("It's a Tie!");
            } else if (winner == 1) {
                sb.append("You Win!");
            } else if (winner == 2) {
                sb.append("You Lose!");
            }
        } else {
            sb.append("Your turn.");
        }
        sb.trimToSize();
        return sb.toString();
    }
}
