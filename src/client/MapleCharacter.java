package client;

import java.awt.Point;
import java.lang.ref.WeakReference;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.text.NumberFormat;
import java.text.DecimalFormat;
import java.util.HashMap;
import client.achievement.Achievement;
import client.anticheat.CheatTracker;
import database.DatabaseException;
import net.MaplePacket;
import net.channel.ChannelServer;
import net.world.MapleMessengerCharacter;
import net.world.MaplePartyCharacter;
import net.world.PlayerBuffValueHolder;
import scripting.event.EventInstanceManager;
import server.MapleInventoryManipulator;
import server.MapleItemInformationProvider;
import server.MaplePortal;
import server.MapleShop;
import server.MapleStatEffect;
import server.MapleStorage;
import server.MapleTrade;
import server.TimerManager;
import server.maps.AbstractAnimatedMapleMapObject;
import server.maps.MapleDoor;
import server.maps.MapleMapObjectType;
import server.maps.SavedLocationType;
import server.maps.SummonMovementType;
import server.quest.MapleCustomQuest;
import server.quest.MapleQuest;
import tools.MaplePacketCreator;
import tools.Pair;
import database.DatabaseConnection;
import java.util.*;
import net.world.MapleMessenger;
import net.world.MapleParty;
import net.world.PartyOperation;
import net.world.PlayerCoolDownValueHolder;
import net.world.WorldServer;
import net.world.guild.MapleGuild;
import net.world.guild.MapleGuildCharacter;
import net.world.remote.WorldChannelInterface;
import server.PlayerInteraction.HiredMerchant;
import server.life.MapleMonster;
import server.life.MobSkill;
import server.maps.FakeCharacter;
import server.maps.MapleMap;
import server.maps.MapleMapFactory;
import server.maps.MapleMapObject;
import server.PlayerInteraction.IPlayerInteractionManager;
import server.PlayerInteraction.MaplePlayerShop;
import server.life.MapleLifeFactory;
import server.life.MapleNPC;
import server.maps.MapleJQ;
import server.maps.MapleSummon;

public class MapleCharacter extends AbstractAnimatedMapleMapObject implements InventoryContainer {

    public static final double MAX_VIEW_RANGE_SQ = 850 * 850;
    private int world;
    private int accountid;
    private int rank;
    private int rankMove;
    private int jobRank;
    private int jobRankMove;
    private String name;
    private String hoster = null;
    private int level, reborns;
    private int str, dex, luk, int_;
    private AtomicInteger exp = new AtomicInteger();
    private int hp, maxhp;
    private int mp, maxmp;
    private int mpApUsed, hpApUsed;
    private int hair, face;
    private AtomicInteger meso = new AtomicInteger();
    private int remainingAp, remainingSp;
    private int savedLocations[];
    private int fame;
    private long lastfametime;
    private List<Integer> lastmonthfameids;
    private transient int localmaxhp, localmaxmp;
    private transient int localstr, localdex, localluk, localint;
    private transient int magic, watk;
    private transient double speedMod, jumpMod;
    private transient int localmaxbasedamage;
    private int id;
    private MapleClient client;
    private MapleMap map;
    private int initialSpawnPoint;
    private int mapid;
    private boolean allowed = false;
    private int JQPToGain = 0;
    private MapleShop shop = null;
    private IPlayerInteractionManager interaction = null;
    private MapleStorage storage = null;
    private MaplePet[] pets = new MaplePet[3];
    private boolean isJQing = false;
    private ScheduledFuture<?> jqTimer;
    private ScheduledFuture<?> fullnessSchedule;
    private ScheduledFuture<?> fullnessSchedule_1;
    private ScheduledFuture<?> fullnessSchedule_2;
    private SkillMacro[] skillMacros = new SkillMacro[5];
    private MapleTrade trade = null;
    private MapleSkinColor skinColor = MapleSkinColor.NORMAL;
    private MapleJob job = MapleJob.BEGINNER;
    private int gender;
    private int gmLevel;
    private int achievementp;
    private int donatorLevel;
    private boolean hidden;
    private boolean canDoor = true;
    private int chair;
    private int itemEffect;
    private int APQScore;
    private boolean chatBan = false;
    private List<Achievement> achievements = new ArrayList<Achievement>();
    private List<Achievement> tempAchievements = new ArrayList<Achievement>();
    private List<MapleCharacter> watcher = new ArrayList<MapleCharacter>();
    private MapleParty party;
    private EventInstanceManager eventInstance = null;
    private MapleInventory[] inventory;
    private Map<MapleQuest, MapleQuestStatus> quests;
    private Set<MapleMonster> controlled = new LinkedHashSet<MapleMonster>();
    private Set<MapleMapObject> visibleMapObjects = new LinkedHashSet<MapleMapObject>();
    private Map<ISkill, SkillEntry> skills = new LinkedHashMap<ISkill, SkillEntry>();
    private Map<MapleBuffStat, MapleBuffStatValueHolder> effects = new LinkedHashMap<MapleBuffStat, MapleBuffStatValueHolder>();
    private HashMap<Integer, MapleKeyBinding> keymap = new LinkedHashMap<Integer, MapleKeyBinding>();
    private List<MapleDoor> doors = new ArrayList<MapleDoor>();
    private Map<Integer, MapleSummon> summons = new LinkedHashMap<Integer, MapleSummon>();
    private BuddyList buddylist;
    private Map<Integer, MapleCoolDownValueHolder> coolDowns = new LinkedHashMap<Integer, MapleCoolDownValueHolder>();
    private CheatTracker anticheat;
    private ScheduledFuture<?> dragonBloodSchedule;
    private ScheduledFuture<?> mapTimeLimitTask = null;
    private int guildid;
    private int guildrank, allianceRank;
    private boolean practice = false;
    private MapleGuildCharacter mgc = null;
    private int paypalnx = 0, maplepoints = 0, cardnx = 0;
    private boolean incs, inmts;
    private int currentPage = 0, currentType = 0, currentTab = 1;
    private MapleMessenger messenger = null;
    int messengerposition = 4;
    private String buff = "none";
    private ScheduledFuture<?> hpDecreaseTask;
    private List<MapleDisease> diseases = new ArrayList<MapleDisease>();
    private ScheduledFuture<?> beholderHealingSchedule;
    private ScheduledFuture<?> beholderBuffSchedule;
    private ScheduledFuture<?> BerserkSchedule;
    private ScheduledFuture<?> expMultiplier;
    private double expRate = 1;
    private boolean Berserk = false;
    public SummonMovementType getMovementType;
    private String chalktext;
    private int CP;
    private int totalCP;
    private int team;
    private boolean married = false;
    private int partnerid;
    private int marriageQuestLevel;
    private boolean canSmega = true;
    private boolean smegaEnabled = true;
    private boolean canTalk = true;
    private int zakumLvl;
    private List<FakeCharacter> fakes = new ArrayList<FakeCharacter>();
    private List<String> ignore = new LinkedList<String>();
    private boolean isfake = false;
    private int clan;
    private int bombpoints;
    private int pvpkills;
    private int pvpdeaths;
    private int donatePoints = 0;
    private int reminds = 0;
    private int jqMap = -1;
    private MapleMount maplemount;
    private int gmtext = 0;
    private boolean hideChat = true;
    private double sword;
    private double blunt;
    private double axe;
    private double spear;
    private double polearm;
    private double claw;
    private double dagger;
    private double staffwand = 0.1;
    private double crossbow;
    private double bow;
    private int skill = 0;
    private ISkill skil;
    private int maxDis;
    public int mpoints = 0;
    private transient int wdef, mdef;
    private int energybar = 0;
    private long afkTime;
    private long lastLogin = 0;
    private int votepoints;
    //find_change
    private int ringRequest;
    private boolean hasMerchant;
    private boolean pvp = false;
    private boolean tagged = false;
    private boolean warned = false;
    private boolean cannpc = true;
    private boolean hasflag = false;
    private boolean redteam = false;
    private boolean blueteam = false;
    private boolean justentered = false;
    private boolean flagon = false;
    private boolean spyMode = true;
    private boolean banned = false;
    private int blueteamwins = 0;
    private int redteamwins = 0;
    private int timeshit = 0;
    private String flagPromptName = "no";
    private int karma;
    private int stampJob;
    private boolean chaser = false;
    private int chaseyCount = 0;
    private int originalSkin = 0;
    private int falseItemCount = 0;
    private boolean stampOn = true;
    private boolean canSay = true;
    private boolean canJail = true;
    private boolean exempted = false;
    private boolean staffChat = false;
    private int hideoutNpc = -1;
    private int follower = -1;
    private List<MapleRing> crushRings = new ArrayList<MapleRing>();
    private List<MapleRing> friendshipRings = new ArrayList<MapleRing>();
    private MapleRing marriageRing;
    private WorldServer wserv = WorldServer.getInstance();
    private MapleClient c;

    public MapleCharacter() {
        setStance(0);
        inventory = new MapleInventory[MapleInventoryType.values().length];
        for (MapleInventoryType type : MapleInventoryType.values()) {
            inventory[type.ordinal()] = new MapleInventory(type, (byte) 100);
        }
        savedLocations = new int[SavedLocationType.values().length];
        for (int i = 0; i < SavedLocationType.values().length; i++) {
            savedLocations[i] = -1;
        }
        quests = new LinkedHashMap<MapleQuest, MapleQuestStatus>();
        anticheat = new CheatTracker(this);
        afkTime = System.currentTimeMillis();
        setPosition(new Point(0, 0));
    }

    public MapleCharacter getThis() {
        return this;
    }

    public long timeSinceLastVote() { //in seconds
        long date = 0;
        try {
            int day = (1000 * 60 * 60) * 24;
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("select `timestamp` from gtopvotes where ip = (select lastknownip from accounts where id = (select accountid from characters where name = \"" + this.name + "\")) order by id desc limit 1");
            ResultSet rs = ps.executeQuery();
            if (!rs.first()) {
                ps.close();
                rs.close();
                return 999999999;
            }



            date = (new Date().getTime() / 1000) - rs.getLong("timestamp");
            date -= (60 * 60);
        } catch (SQLException se) {
            //
        }
        return date;
    }

    public static MapleCharacter loadCharFromDB(int charid, MapleClient client, boolean channelserver) throws SQLException {
        MapleCharacter ret = new MapleCharacter();
        ret.client = client;
        ret.id = charid;
        Connection con = DatabaseConnection.getConnection();
        PreparedStatement ps = con.prepareStatement("SELECT * FROM characters WHERE id = ?");
        ps.setInt(1, charid);
        ResultSet rs = ps.executeQuery();
        if (!rs.next()) {
            rs.close();
            ps.close();
            throw new RuntimeException("Loading the Char Failed (char not found)");
        }
        ret.name = rs.getString("name");
        ret.level = rs.getInt("level");
        ret.pvpdeaths = rs.getInt("pvpdeaths");
        ret.pvpkills = rs.getInt("pvpkills");
        ret.reborns = rs.getInt("reborns");
        ret.fame = rs.getInt("fame");
        ret.str = rs.getInt("str");
        ret.dex = rs.getInt("dex");
        ret.int_ = rs.getInt("int");
        ret.luk = rs.getInt("luk");
        ret.canTalk = rs.getBoolean("canTalk");
        ret.exp.set(rs.getInt("exp"));
        if (ret.exp.get() < 0) {
            ret.exp.set(0);
        }
        ret.hp = rs.getInt("hp");
        if (ret.hp < 50) {
            ret.hp = 50;
        }
        ret.maxhp = rs.getInt("maxhp");
        ret.mp = rs.getInt("mp");
        if (ret.mp < 50) {
            ret.mp = 50;
        }
        ret.maxmp = rs.getInt("maxmp");
        ret.hpApUsed = rs.getInt("hpApUsed");
        ret.mpApUsed = rs.getInt("mpApUsed");
        ret.hasMerchant = rs.getInt("HasMerchant") == 1;
        ret.remainingSp = rs.getInt("sp");
        ret.remainingAp = rs.getInt("ap");
        ret.meso.set(rs.getInt("meso"));
        ret.gmLevel = rs.getInt("gm");
        ret.clan = rs.getInt("clan");
        int mountexp = rs.getInt("mountexp");
        int mountlevel = rs.getInt("mountlevel");
        int mounttiredness = rs.getInt("mounttiredness");
        ret.married = rs.getInt("married") == 0 ? false : true;
        ret.partnerid = rs.getInt("partnerid");
        ret.marriageQuestLevel = rs.getInt("marriagequest");
        ret.zakumLvl = rs.getInt("zakumLvl");
        ret.skinColor = MapleSkinColor.getById(rs.getInt("skincolor"));
        ret.gender = rs.getInt("gender");
        ret.job = MapleJob.getById(rs.getInt("job"));
        ret.hair = rs.getInt("hair");
        ret.face = rs.getInt("face");
        ret.accountid = rs.getInt("accountid");
        ret.mapid = rs.getInt("map");
        ret.initialSpawnPoint = rs.getInt("spawnpoint");
        ret.world = rs.getInt("world");
        ret.karma = rs.getInt("karma");
        ret.rank = rs.getInt("rank");
        ret.rankMove = rs.getInt("rankMove");
        ret.jobRank = rs.getInt("jobRank");
        ret.jobRankMove = rs.getInt("jobRankMove");
        ret.guildid = rs.getInt("guildid");
        ret.guildrank = rs.getInt("guildrank");
        ret.allianceRank = rs.getInt("allianceRank");
        ret.buff = rs.getString("buff");
        ret.achievementp = rs.getInt("achievements");
        if (ret.guildid > 0) {
            ret.mgc = new MapleGuildCharacter(ret);
        }
        int buddyCapacity = rs.getInt("buddyCapacity");
        ret.buddylist = new BuddyList(buddyCapacity);
        ret.gmtext = rs.getInt("gmtext");
        if (channelserver) {
            MapleMapFactory mapFactory = ChannelServer.getInstance(client.getChannel()).getMapFactory();
            ret.map = mapFactory.getMap(ret.mapid);
            if (ret.map == null) {
                ret.map = mapFactory.getMap(100000000);
            }
            MaplePortal portal = ret.map.getPortal(ret.initialSpawnPoint);
            if (portal == null) {
                portal = ret.map.getPortal(0);
                ret.initialSpawnPoint = 0;
            }
            ret.setPosition(portal.getPosition());
            int partyid = rs.getInt("party");
            if (partyid >= 0) {
                try {
                    MapleParty party = client.getChannelServer().getWorldInterface().getParty(partyid);
                    if (party != null && party.getMemberById(ret.id) != null) {
                        ret.party = party;
                    }
                } catch (RemoteException e) {
                    client.getChannelServer().reconnectWorld();
                }
            }
            int messengerid = rs.getInt("messengerid");
            int position = rs.getInt("messengerposition");
            if (messengerid > 0 && position < 4 && position > -1) {
                try {
                    WorldChannelInterface wci = ChannelServer.getInstance(client.getChannel()).getWorldInterface();
                    MapleMessenger messenger = wci.getMessenger(messengerid);
                    if (messenger != null) {
                        ret.messenger = messenger;
                        ret.messengerposition = position;
                    }
                } catch (RemoteException e) {
                    client.getChannelServer().reconnectWorld();
                }
            }
        }
        rs.close();
        ps.close();
        ps = con.prepareStatement("SELECT * FROM accounts WHERE id = ?");
        ps.setInt(1, ret.accountid);
        rs = ps.executeQuery();
        if (rs.next()) {
            ret.getClient().setAccountName(rs.getString("name"));
            ret.getClient().setAccountPass(rs.getString("password"));
            ret.getClient().setGuest(rs.getInt("guest") > 0);
            ret.donatePoints = rs.getInt("donorPoints");
            ret.donatorLevel = rs.getInt("donator");
            ret.lastLogin = rs.getLong("LastLoginInMilliseconds");
            ret.votepoints = rs.getInt("votepoints");
            if (!ret.isGM()) {
                if (ret.donatorLevel == 1) {
                    ret.gmLevel = 1;
                } else if (ret.donatorLevel == 2) {
                    ret.gmLevel = 1;
                }
            }
            ret.paypalnx = rs.getInt("paypalNX");
            ret.maplepoints = rs.getInt("mPoints");
            ret.cardnx = rs.getInt("cardNX");
        }
        rs.close();
        ps.close();
        String sql = "SELECT * FROM inventoryitems LEFT JOIN inventoryequipment USING (inventoryitemid) WHERE characterid = ?";
        if (!channelserver) {
            sql += " AND inventorytype = " + MapleInventoryType.EQUIPPED.getType();
        }
        ps = con.prepareStatement(sql);
        ps.setInt(1, charid);
        rs = ps.executeQuery();
        while (rs.next()) {
            MapleInventoryType type = MapleInventoryType.getByType((byte) rs.getInt("inventorytype"));
            if (type.equals(MapleInventoryType.EQUIP) || type.equals(MapleInventoryType.EQUIPPED)) {
                int itemid = rs.getInt("itemid");
                Equip equip = new Equip(itemid, (byte) rs.getInt("position"), rs.getInt("ringid"));
                equip.setOwner(rs.getString("owner"));
                equip.setQuantity((short) rs.getInt("quantity"));
                equip.setAcc((short) rs.getInt("acc"));
                equip.setAvoid((short) rs.getInt("avoid"));
                equip.setDex((short) rs.getInt("dex"));
                equip.setHands((short) rs.getInt("hands"));
                equip.setHp((short) rs.getInt("hp"));
                equip.setInt((short) rs.getInt("int"));
                equip.setJump((short) rs.getInt("jump"));
                equip.setLuk((short) rs.getInt("luk"));
                equip.setMatk((short) rs.getInt("matk"));
                equip.setMdef((short) rs.getInt("mdef"));
                equip.setMp((short) rs.getInt("mp"));
                equip.setSpeed((short) rs.getInt("speed"));
                equip.setStr((short) rs.getInt("str"));
                equip.setWatk((short) rs.getInt("watk"));
                equip.setWdef((short) rs.getInt("wdef"));
                equip.setUpgradeSlots((byte) rs.getInt("upgradeslots"));
                equip.setLocked((byte) rs.getInt("locked"));
                equip.setLevel((byte) rs.getInt("level"));
                if (equip.getRingId() > -1) {
                    MapleRing ring = MapleRing.loadFromDb(equip.getRingId());
                    if (ring != null) {
                        if (type.equals(MapleInventoryType.EQUIPPED)) {
                            ring.equip();
                        }
                        if (ring.getItemId() > 1112013) {
                            ret.addFriendshipRing(ring);
                        } else {
                            ret.addCrushRing(ring);
                        }
                        ret.getInventory(type).addFromDB(equip);
                    }
                } else {
                    ret.getInventory(type).addFromDB(equip);
                }
            } else {
                Item item = new Item(rs.getInt("itemid"), (byte) rs.getInt("position"), (short) rs.getInt("quantity"), rs.getInt("petid"));
                item.setOwner(rs.getString("owner"));
                ret.getInventory(type).addFromDB(item);
            }
        }
        rs.close();
        ps.close();

        if (channelserver) {
            ps = con.prepareStatement("SELECT aid FROM achievements WHERE cid = ?"); // loads on login and change channel
            ps.setInt(1, charid);
            rs = ps.executeQuery();
            if (rs.first()) {
                do {
                    ret.achievements.add(Achievement.getById(rs.getInt("aid")));
                } while (rs.next());
            }
            rs.close();
            ps.close();
            ps = con.prepareStatement("SELECT * FROM queststatus WHERE characterid = ?");
            ps.setInt(1, charid);
            rs = ps.executeQuery();
            PreparedStatement pse = con.prepareStatement("SELECT * FROM queststatusmobs WHERE queststatusid = ?");
            while (rs.next()) {
                MapleQuest q = MapleQuest.getInstance(rs.getInt("quest"));
                MapleQuestStatus status = new MapleQuestStatus(q, MapleQuestStatus.Status.getById(rs.getInt("status")));
                long cTime = rs.getLong("time");
                if (cTime > -1) {
                    status.setCompletionTime(cTime * 1000);
                }
                status.setForfeited(rs.getInt("forfeited"));
                ret.quests.put(q, status);
                pse.setInt(1, rs.getInt("queststatusid"));
                ResultSet rsMobs = pse.executeQuery();
                while (rsMobs.next()) {
                    status.setMobKills(rsMobs.getInt("mob"), rsMobs.getInt("count"));
                }
                rsMobs.close();
            }
            rs.close();
            ps.close();
            pse.close();
            for (int s : SkillFactory.getSkillIds()) {
                ISkill skill = SkillFactory.getSkill(s);
                ret.skills.put(skill, new SkillEntry(skill.getMaxLevel(), skill.getMaxLevel()));
            }
            if (ret.gmLevel > 0) {
                for (int s : SkillFactory.getGMSkillIds()) {
                    ISkill skill = SkillFactory.getSkill(s);
                    ret.skills.put(skill, new SkillEntry(skill.getMaxLevel(), skill.getMaxLevel()));
                }
            }
            ps = con.prepareStatement("SELECT * FROM skillmacros WHERE characterid = ?");
            ps.setInt(1, charid);
            rs = ps.executeQuery();
            while (rs.next()) {
                int skill1 = rs.getInt("skill1");
                int skill2 = rs.getInt("skill2");
                int skill3 = rs.getInt("skill3");
                String name = rs.getString("name");
                int shout = rs.getInt("shout");
                int position = rs.getInt("position");
                SkillMacro macro = new SkillMacro(skill1, skill2, skill3, name, shout, position);
                ret.skillMacros[position] = macro;
            }
            rs.close();
            ps.close();
            ps = con.prepareStatement("SELECT `key`,`type`,`action` FROM keymap WHERE characterid = ?");
            ps.setInt(1, charid);
            rs = ps.executeQuery();
            while (rs.next()) {
                int key = rs.getInt("key");
                int type = rs.getInt("type");
                int action = rs.getInt("action");
                ret.keymap.put(Integer.valueOf(key), new MapleKeyBinding(type, action));
            }
            rs.close();
            ps.close();
            ps = con.prepareStatement("SELECT `locationtype`,`map` FROM savedlocations WHERE characterid = ?");
            ps.setInt(1, charid);
            rs = ps.executeQuery();
            while (rs.next()) {
                String locationType = rs.getString("locationtype");
                int mapid = rs.getInt("map");
                ret.savedLocations[SavedLocationType.valueOf(locationType).ordinal()] = mapid;
            }
            rs.close();
            ps.close();
            ps = con.prepareStatement("SELECT `characterid_to`,`when` FROM famelog WHERE characterid = ? AND DATEDIFF(NOW(),`when`) < 30");
            ps.setInt(1, charid);
            rs = ps.executeQuery();
            ret.lastfametime = 0;
            ret.lastmonthfameids = new ArrayList<Integer>(31);
            while (rs.next()) {
                ret.lastfametime = Math.max(ret.lastfametime, rs.getTimestamp("when").getTime());
                ret.lastmonthfameids.add(Integer.valueOf(rs.getInt("characterid_to")));
            }
            rs.close();
            ps.close();
            ret.buddylist.loadFromDb(charid);
            ret.storage = MapleStorage.loadOrCreateFromDB(ret.accountid);
        }
        if (ret.getInventory(MapleInventoryType.EQUIPPED).getItem((byte) -18) != null) {
            ret.maplemount = new MapleMount(ret, ret.getInventory(MapleInventoryType.EQUIPPED).getItem((byte) -18).getItemId(), 1004);
            ret.maplemount.setExp(mountexp);
            ret.maplemount.setLevel(mountlevel);
            ret.maplemount.setTiredness(mounttiredness);
            ret.maplemount.setActive(false);
        } else {
            ret.maplemount = new MapleMount(ret, 0, 1004);
            ret.maplemount.setExp(mountexp);
            ret.maplemount.setLevel(mountlevel);
            ret.maplemount.setTiredness(mounttiredness);
            ret.maplemount.setActive(false);
        }
        ret.recalcLocalStats();
        ret.silentEnforceMaxHpMp();
        return ret;
    }

    public static MapleCharacter getDefault(MapleClient client, int chrid) {
        MapleCharacter ret = getDefault(client);
        ret.id = chrid;
        return ret;
    }

    public static MapleCharacter getDefault(MapleClient client) {
        MapleCharacter ret = new MapleCharacter();
        ret.client = client;
        ret.hp = 50;
        ret.maxhp = 50;
        ret.mp = 5;
        ret.maxmp = 5;
        ret.map = null;
        ret.exp.set(0);
        ret.gmLevel = 0;
        ret.clan = -1;
        ret.job = MapleJob.BEGINNER;
        ret.meso.set(0);
        ret.level = 1;
        ret.reborns = 0;
        ret.pvpdeaths = 0;
        ret.pvpkills = 0;
        ret.bombpoints = 0;
        ret.accountid = client.getAccID();
        ret.buddylist = new BuddyList(100);
        ret.CP = 0;
        ret.totalCP = 0;
        ret.team = -1;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM accounts WHERE id = ?");
            ps.setInt(1, ret.accountid);
            ResultSet rs = ps.executeQuery();
            rs = ps.executeQuery();
            if (rs.next()) {
                ret.getClient().setAccountName(rs.getString("name"));
                ret.getClient().setAccountPass(rs.getString("password"));
                ret.getClient().setGuest(rs.getInt("guest") > 0);
                ret.donatePoints = rs.getInt("donorPoints");
                ret.donatorLevel = rs.getInt("donator");
                ret.votepoints = rs.getInt("votepoints");
                ret.paypalnx = rs.getInt("paypalNX");
                ret.maplepoints = rs.getInt("mPoints");
                ret.cardnx = rs.getInt("cardNX");
                ret.lastLogin = rs.getLong("LastLoginInMilliseconds");
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Error Getting Default: " + e);
        }
        ret.incs = false;
        ret.inmts = false;
        ret.APQScore = 0;
        ret.maplemount = null;
        ret.setDefaultKeyMap();
        ret.recalcLocalStats();
        return ret;
    }

    public void saveToDB(boolean update, boolean full) { // saves on @save
        Connection con = DatabaseConnection.getConnection();
        try {
            con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
            con.setAutoCommit(false);
            PreparedStatement ps;
            if (update) {
                ps = con.prepareStatement("UPDATE characters SET level = ?, fame = ?, str = ?, dex = ?, luk = ?, `int` = ?, exp = ?, hp = ?, mp = ?, maxhp = ?, maxmp = ?, sp = ?, ap = ?, gm = ?, skincolor = ?, gender = ?, job = ?, hair = ?, face = ?, map = ?, meso = ?, hpApUsed = ?, mpApUsed = ?, spawnpoint = ?, party = ?, buddyCapacity = ?, messengerid = ?, messengerposition = ?, reborns = ?, pvpkills = ?, pvpdeaths = ?, clan = ?, mountlevel = ?, mountexp = ?, mounttiredness = ?, married = ?, partnerid = ?, zakumlvl = ?, marriagequest = ?, gmtext = ?, canTalk = ?, karma = ?, buff = ?, achievements = ? WHERE id = ?");
            } else {
                ps = con.prepareStatement("INSERT INTO characters (level, fame, str, dex, luk, `int`, exp, hp, mp, maxhp, maxmp, sp, ap, gm, skincolor, gender, job, hair, face, map, meso, hpApUsed, mpApUsed, spawnpoint, party, buddyCapacity, messengerid, messengerposition, reborns, pvpkills, pvpdeaths, clan, mountlevel, mountexp, mounttiredness, married, partnerid, zakumlvl, marriagequest, accountid, name, world) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            }
            ps.setInt(1, level);
            ps.setInt(2, fame);
            ps.setInt(3, str);
            ps.setInt(4, dex);
            ps.setInt(5, luk);
            ps.setInt(6, int_);
            ps.setInt(7, exp.get());
            ps.setInt(8, hp);
            ps.setInt(9, mp);
            ps.setInt(10, maxhp);
            ps.setInt(11, maxmp);
            ps.setInt(12, remainingSp);
            ps.setInt(13, remainingAp);
            ps.setInt(14, gmLevel);
            ps.setInt(15, skinColor.getId());
            ps.setInt(16, gender);
            ps.setInt(17, job.getId());
            ps.setInt(18, hair);
            ps.setInt(19, face);
            if (map == null) {
                ps.setInt(20, 0);
            } else if (map.getId() == 980000603) {
                ps.setInt(20, 100000000);
            } else if (map.getId() == 980000404) {
                ps.setInt(20, 980000404);
            } else {
                ps.setInt(20, map.getId());
            }
            ps.setInt(21, meso.get());
            ps.setInt(22, hpApUsed);
            ps.setInt(23, mpApUsed);
            if (map == null || map.getId() == 610020000 || map.getId() == 610020001) {
                ps.setInt(24, 0);
            } else {
                MaplePortal closest = map.findClosestSpawnpoint(getPosition());
                if (closest != null) {
                    ps.setInt(24, closest.getId());
                } else {
                    ps.setInt(24, 0);
                }
            }
            if (party != null) {
                ps.setInt(25, party.getId());
            } else {
                ps.setInt(25, -1);
            }
            ps.setInt(26, buddylist.getCapacity());
            if (messenger != null) {
                ps.setInt(27, messenger.getId());
                ps.setInt(28, messengerposition);
            } else {
                ps.setInt(27, 0);
                ps.setInt(28, 4);
            }
            ps.setInt(29, reborns);
            ps.setInt(30, pvpkills);
            ps.setInt(31, pvpdeaths);
            ps.setInt(32, clan);
            if (maplemount != null) {
                ps.setInt(33, maplemount.getLevel());
                ps.setInt(34, maplemount.getExp());
                ps.setInt(35, maplemount.getTiredness());
            } else {
                ps.setInt(33, 1);
                ps.setInt(34, 0);
                ps.setInt(35, 0);
            }
            ps.setInt(36, married ? 1 : 0);
            ps.setInt(37, partnerid);
            ps.setInt(38, zakumLvl > 2 ? 2 : zakumLvl);
            ps.setInt(39, marriageQuestLevel);
            if (update) {
                ps.setInt(40, gmtext);
                ps.setBoolean(41, canTalk);
                ps.setInt(42, karma);
                ps.setString(43, buff);
                ps.setInt(44, achievementp);
                ps.setInt(45, id);
            } else {
                ps.setInt(40, accountid);
                ps.setString(41, name);
                ps.setInt(42, world);
            }
            if (!full) {
                ps.executeUpdate();
                ps.close();
            } else {
                int updateRows = ps.executeUpdate();
                if (!update) {
                    ResultSet rs = ps.getGeneratedKeys();
                    if (rs.next()) {
                        this.id = rs.getInt(1);
                    } else {
                        throw new DatabaseException("Inserting char failed.");
                    }
                    rs.close();
                } else if (updateRows < 1) {
                    throw new DatabaseException("Character not in database (" + id + ")");
                }
                ps.close();
                for (int i = 0; i < 3; i++) {
                    if (pets[i] != null) {
                        pets[i].saveToDb();
                    } else {
                        break;
                    }
                }
                deleteWhereCharacterId(con, "DELETE FROM skillmacros WHERE characterid = ?");
                for (int i = 0; i < 5; i++) {
                    SkillMacro macro = skillMacros[i];
                    if (macro != null) {
                        ps = con.prepareStatement("INSERT INTO skillmacros (characterid, skill1, skill2, skill3, name, shout, position) VALUES (?, ?, ?, ?, ?, ?, ?)");
                        ps.setInt(1, id);
                        ps.setInt(2, macro.getSkill1());
                        ps.setInt(3, macro.getSkill2());
                        ps.setInt(4, macro.getSkill3());
                        ps.setString(5, macro.getName());
                        ps.setInt(6, macro.getShout());
                        ps.setInt(7, i);
                        ps.executeUpdate();
                        ps.close();
                    }
                }
                deleteWhereCharacterId(con, "DELETE FROM inventoryitems WHERE characterid = ?");
                ps = con.prepareStatement("INSERT INTO inventoryitems (characterid, itemid, inventorytype, position, quantity, owner, petid) VALUES (?, ?, ?, ?, ?, ?, ?)");
                PreparedStatement pse = con.prepareStatement("INSERT INTO inventoryequipment VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                for (MapleInventory iv : inventory) {
                    ps.setInt(3, iv.getType().getType());
                    for (IItem item : iv.list()) {
                        ps.setInt(1, id);
                        ps.setInt(2, item.getItemId());
                        ps.setInt(4, item.getPosition());
                        ps.setInt(5, item.getQuantity());
                        ps.setString(6, item.getOwner());
                        ps.setInt(7, item.getPetId());
                        ps.executeUpdate();
                        ResultSet rs = ps.getGeneratedKeys();
                        int itemid;
                        if (rs.next()) {
                            itemid = rs.getInt(1);
                        } else {
                            rs.close();
                            ps.close();
                            throw new DatabaseException("Inserting char failed.");
                        }
                        rs.close();
                        if (iv.getType().equals(MapleInventoryType.EQUIP) || iv.getType().equals(MapleInventoryType.EQUIPPED)) {
                            pse.setInt(1, itemid);
                            IEquip equip = (IEquip) item;
                            pse.setInt(2, equip.getUpgradeSlots());
                            pse.setInt(3, equip.getLevel());
                            pse.setInt(4, equip.getStr());
                            pse.setInt(5, equip.getDex());
                            pse.setInt(6, equip.getInt());
                            pse.setInt(7, equip.getLuk());
                            pse.setInt(8, equip.getHp());
                            pse.setInt(9, equip.getMp());
                            pse.setInt(10, equip.getWatk());
                            pse.setInt(11, equip.getMatk());
                            pse.setInt(12, equip.getWdef());
                            pse.setInt(13, equip.getMdef());
                            pse.setInt(14, equip.getAcc());
                            pse.setInt(15, equip.getAvoid());
                            pse.setInt(16, equip.getHands());
                            pse.setInt(17, equip.getSpeed());
                            pse.setInt(18, equip.getJump());
                            pse.setInt(19, equip.getRingId());
                            pse.setInt(20, equip.getLocked());
                            pse.executeUpdate();
                        }
                    }
                }
                ps.close();
                pse.close();
                deleteWhereCharacterId(con, "DELETE FROM queststatus WHERE characterid = ?");
                ps = con.prepareStatement("INSERT INTO queststatus (`queststatusid`, `characterid`, `quest`, `status`, `time`, `forfeited`) VALUES (DEFAULT, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                pse = con.prepareStatement("INSERT INTO queststatusmobs VALUES (DEFAULT, ?, ?, ?)");
                ps.setInt(1, id);
                for (MapleQuestStatus q : quests.values()) {
                    ps.setInt(2, q.getQuest().getId());
                    ps.setInt(3, q.getStatus().getId());
                    ps.setInt(4, (int) (q.getCompletionTime() / 1000));
                    ps.setInt(5, q.getForfeited());
                    ps.executeUpdate();
                    ResultSet rs = ps.getGeneratedKeys();
                    rs.next();
                    for (int mob : q.getMobKills().keySet()) {
                        pse.setInt(1, rs.getInt(1));
                        pse.setInt(2, mob);
                        pse.setInt(3, q.getMobKills(mob));
                        pse.executeUpdate();
                    }
                    rs.close();
                }
                ps.close();
                pse.close();
                deleteWhereCharacterId(con, "DELETE FROM keymap WHERE characterid = ?");
                ps = con.prepareStatement("INSERT INTO keymap (characterid, `key`, `type`, `action`) VALUES (?, ?, ?, ?)");
                ps.setInt(1, id);
                for (Entry<Integer, MapleKeyBinding> keybinding : keymap.entrySet()) {
                    ps.setInt(2, keybinding.getKey().intValue());
                    ps.setInt(3, keybinding.getValue().getType());
                    ps.setInt(4, keybinding.getValue().getAction());
                    ps.executeUpdate();
                }
                ps.close();
                deleteWhereCharacterId(con, "DELETE FROM savedlocations WHERE characterid = ?");
                ps = con.prepareStatement("INSERT INTO savedlocations (characterid, `locationtype`, `map`) VALUES (?, ?, ?)");
                ps.setInt(1, id);
                for (SavedLocationType savedLocationType : SavedLocationType.values()) {
                    if (savedLocations[savedLocationType.ordinal()] != -1) {
                        ps.setString(2, savedLocationType.name());
                        ps.setInt(3, savedLocations[savedLocationType.ordinal()]);
                        ps.executeUpdate();
                    }
                }
                ps.close();
                if (tempAchievements.size() > 0) {
                    ps = con.prepareStatement("INSERT INTO achievements (aid, cid) VALUES (?,?)");
                    for (Achievement a : tempAchievements) {
                        ps.setInt(1, a.getId());
                        ps.setInt(2, id);
                        ps.executeUpdate();
                    }
                    ps.close();
                }
                tempAchievements.clear();
                deleteWhereCharacterId(con, "DELETE FROM buddies WHERE characterid = ? AND pending = 0");
                ps = con.prepareStatement("INSERT INTO buddies (characterid, `buddyid`, `pending`) VALUES (?, ?, 0)");
                ps.setInt(1, id);
                for (BuddylistEntry entry : buddylist.getBuddies()) {
                    if (entry.isVisible()) {
                        ps.setInt(2, entry.getCharacterId());
                        ps.executeUpdate();
                    }
                }
                ps.close();
                this.updateVotePoints();
                ps = con.prepareStatement("UPDATE accounts SET `paypalNX` = ?, `mPoints` = ?, `cardNX` = ?, `donorPoints` = ?, `votepoints` = ?, `donator` = ? WHERE id = ?");
                ps.setInt(1, paypalnx);
                ps.setInt(2, maplepoints);
                ps.setInt(3, cardnx);
                ps.setInt(4, donatePoints);
                ps.setInt(6, donatorLevel);
                ps.setInt(5, votepoints);

                ps.setInt(7, client.getAccID());
                ps.executeUpdate();
                ps.close();
                if (storage != null) {
                    storage.saveToDB();
                }
            }
            con.commit();
        } catch (Exception e) {
            System.out.println("[Saving] Error saving character data: " + e);
            try {
                con.rollback();
            } catch (SQLException e1) {
                System.out.println("[Saving] Error rolling back: " + e1);
            }
        } finally {
            try {
                con.setAutoCommit(true);
                con.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
            } catch (SQLException e) {
                System.out.println("[Saving] Error going back to autocommit mode: " + e);
            }
        }
    }

    private void deleteWhereCharacterId(Connection con, String sql) throws SQLException {
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, id);
        ps.executeUpdate();
        ps.close();
    }

    private static void sqlException(SQLException e) {
        System.out.println("SQL Error: " + e);
    }

    private static void sqlException(RemoteException e) {
        System.out.println("SQL Error: " + e);
    }

    public MapleQuestStatus getQuest(MapleQuest quest) {
        if (!quests.containsKey(quest)) {
            return new MapleQuestStatus(quest, MapleQuestStatus.Status.NOT_STARTED);
        }
        return quests.get(quest);
    }

    public void updateQuest(MapleQuestStatus quest) {
        quests.put(quest.getQuest(), quest);
        if (!(quest.getQuest() instanceof MapleCustomQuest)) {
            if (quest.getStatus().equals(MapleQuestStatus.Status.STARTED)) {
                client.getSession().write(MaplePacketCreator.startQuest(this, (short) quest.getQuest().getId()));
                client.getSession().write(MaplePacketCreator.updateQuestInfo(this, (short) quest.getQuest().getId(), quest.getNpc(), (byte) 8));
            } else if (quest.getStatus().equals(MapleQuestStatus.Status.COMPLETED)) {
                client.getSession().write(MaplePacketCreator.completeQuest(this, (short) quest.getQuest().getId()));
            } else if (quest.getStatus().equals(MapleQuestStatus.Status.NOT_STARTED)) {
                client.getSession().write(MaplePacketCreator.forfeitQuest(this, (short) quest.getQuest().getId()));
            }
        }
    }

    public String getBuff() {
        return buff;
    }

    public void setBuff(String buff) {
        this.buff = buff;
        //  this.saveToDB(true, true);
    }
    /*
     * public void sendMail(String Reciever, String message) throws SQLException
     * { Connection con = DatabaseConnection.getConnection(); PreparedStatement
     * ps = con.prepareStatement("INSERT INTO mail (`MailSender`,
     * `MailReciever`, `Message`) VALUES (?, ?, ?)"); ps.setString(1,
     * getName()); ps.setString(2, Reciever); ps.setString(3, message);
     * ps.executeUpdate(); ps.close();
     *
     *
     * }
     */
    /*
     * public void loadBuff() throws SQLException { Connection con =
     * DatabaseConnection.getConnection(); PreparedStatement ps =
     * con.prepareStatement("SELECT buff FROM characters WHERE id = ?");
     * ps.setInt(1, getId()); ResultSet rs = ps.executeQuery(); while
     * (rs.next()) { buff = rs.getString("buff"); } ps.close(); }
     *
     * public void saveBuff() { Connection con =
     * DatabaseConnection.getConnection(); PreparedStatement ps; try { ps =
     * con.prepareStatement("UPDATE characters SET buff = ? WHERE id = ?");
     * ps.setString(1, getBuff()); ps.setInt(2, getId()); ps.executeUpdate();
     * ps.close(); } catch (SQLException e) { sqlException(e); } }
     */

    public static int getIdByName(String name, int world) {
        Connection con = DatabaseConnection.getConnection();
        PreparedStatement ps;
        try {
            ps = con.prepareStatement("SELECT id FROM characters WHERE name = ? AND world = ?");
            ps.setString(1, name);
            ps.setInt(2, world);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                rs.close();
                ps.close();
                return -1;
            }
            int id = rs.getInt("id");
            rs.close();
            ps.close();
            return id;
        } catch (SQLException e) {
            sqlException(e);
        }
        return -1;
    }

    public static String getNameById(int id, int world) {
        Connection con = DatabaseConnection.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT name FROM characters WHERE id = ? AND world = ?");
            ps.setInt(1, id);
            ps.setInt(2, world);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                rs.close();
                ps.close();
                return null;
            }
            String name = rs.getString("name");
            rs.close();
            ps.close();
            return name;
        } catch (SQLException e) {
            sqlException(e);
        }
        return null;
    }

    public Integer getBuffedValue(MapleBuffStat effect) {
        MapleBuffStatValueHolder mbsvh = effects.get(effect);
        if (mbsvh == null) {
            return null;
        }
        return Integer.valueOf(mbsvh.value);
    }

    public boolean isBuffFrom(MapleBuffStat stat, ISkill skill) {
        MapleBuffStatValueHolder mbsvh = effects.get(stat);
        if (mbsvh == null) {
            return false;
        }
        return mbsvh.effect.isSkill() && mbsvh.effect.getSourceId() == skill.getId();
    }

    public int getBuffSource(MapleBuffStat stat) {
        MapleBuffStatValueHolder mbsvh = effects.get(stat);
        if (mbsvh == null) {
            return -1;
        }
        return mbsvh.effect.getSourceId();
    }

    public int getItemQuantity(int itemid, boolean checkEquipped) {
        MapleInventoryType type = MapleItemInformationProvider.getInstance().getInventoryType(itemid);
        MapleInventory iv = inventory[type.ordinal()];
        int possesed = iv.countById(itemid);
        if (checkEquipped) {
            possesed += inventory[MapleInventoryType.EQUIPPED.ordinal()].countById(itemid);
        }
        return possesed;
    }

    public void setBuffedValue(MapleBuffStat effect, int value) {
        MapleBuffStatValueHolder mbsvh = effects.get(effect);
        if (mbsvh == null) {
            return;
        }
        mbsvh.value = value;
    }

    public Long getBuffedStarttime(MapleBuffStat effect) {
        MapleBuffStatValueHolder mbsvh = effects.get(effect);
        if (mbsvh == null) {
            return null;
        }
        return Long.valueOf(mbsvh.startTime);
    }

    public MapleStatEffect getStatForBuff(MapleBuffStat effect) {
        MapleBuffStatValueHolder mbsvh = effects.get(effect);
        if (mbsvh == null) {
            return null;
        }
        return mbsvh.effect;
    }

    private void prepareDragonBlood(final MapleStatEffect bloodEffect) {
        if (dragonBloodSchedule != null) {
            dragonBloodSchedule.cancel(false);
        }
        dragonBloodSchedule = TimerManager.getInstance().register(new Runnable() {

            @Override
            public void run() {
                addHP(-bloodEffect.getX());
                getClient().getSession().write(MaplePacketCreator.showOwnBuffEffect(bloodEffect.getSourceId(), 5));
                getMap().broadcastMessage(MapleCharacter.this, MaplePacketCreator.showBuffeffect(getId(), bloodEffect.getSourceId(), 5, (byte) 3), false);
            }
        }, 4000, 4000);
    }

    public void togglePractice() {
        if (jqTimer != null) {
            jqTimer.cancel(true);
        }
        practice = !practice;
    }

    public void setJQChampionshipTZ(int tz) {
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("UPDATE `jqchampionship` SET `timezone` = ? WHERE `charid` = ?");
            ps.setInt(1, tz);
            ps.setInt(2, id);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public String getJQChampionshipParticipants() {
        String out = "";
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT `name` FROM `jqchampionship` ORDER BY `timezone`;");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (((rs.getRow() - 1) % 8) == 0) {
                    out += "\r\n\r\n#bGroup " + ((rs.getRow() / 8) + 1) + "#n";
                }
                out += "\r\n" + rs.getString("name");
            }
            rs.close();
            ps.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return out;
    }

    public void joinJQChampionship() {
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("INSERT INTO `jqchampionship` (`charid`, `name`) VALUES (?, ?)");
            ps.setInt(1, id);
            ps.setString(2, name);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public void leaveJQChampionship() {
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("DELETE FROM `jqchampionship` WHERE `charid` = ?");
            ps.setInt(1, id);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public boolean inJQChampionship() {
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT `id` FROM `jqchampionship` WHERE `charid` = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            boolean b = rs.next();
            rs.close();
            ps.close();
            return b;
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return false;
    }

    public void practiceOff() {
        if (jqTimer != null) {
            jqTimer.cancel(true);
        }
        practice = false;
    }

    public void practiceOn() {
        if (jqTimer != null) {
            jqTimer.cancel(true);
        }
        practice = true;
    }

    public boolean isPractice() {
        return practice;
    }

    public boolean isJQing() {
        return isJQing;
    }

    public void scheduleJQ(int map) {
        int points = MapleJQ.getPoints(map);
        int time = MapleJQ.getTime(map);
        changeMap(map, 0);
        if (!practice) {
            jqMap = map;
            JQPToGain = points;
            isJQing = true;
            jqTimer = TimerManager.getInstance().schedule(new Runnable() {

                public void run() {
                    if (getMapId() == jqMap) {
                        changeMap(100000000, 24);
                        getClient().getSession().write(MaplePacketCreator.showEffect("event/coconut/lose"));
                        getClient().getSession().write(MaplePacketCreator.playSound("Party1/Failed"));
                        getClient().getSession().write(MaplePacketCreator.getNPCTalk(9300006, (byte) 0, "#e#rTough luck.#k You didn't finish the JQ in time!", "00 00"));
                    }
                    isJQing = false;
                    JQPToGain = 0;
                }
            }, time * 1000);
            getClient().getSession().write(MaplePacketCreator.getClock(time - 1));
        }
    }

    public void giveUp() {
        if (practice) {
            changeMap(100000000, 24);
            dropMessage("You have left the JQ.");
        } else {
            if (isJQing || getMap().isJQ()) {
                changeMap(100000000, 24);
                JQPToGain = 0;
                isJQing = false;
                if (jqTimer != null) {
                    jqTimer.cancel(true);
                }
                getClient().getSession().write(MaplePacketCreator.showEffect("event/coconut/lose"));
                getClient().getSession().write(MaplePacketCreator.playSound("Party1/Failed"));
                getClient().getSession().write(MaplePacketCreator.getNPCTalk(9300006, (byte) 0, "#eToo hard huh? Maybe you should try something a little #reasier#k next time.", "00 00"));
            } else {
                dropMessage("You're not in a JQ.");
            }
        }
    }

    public void finishJQ() {
        if (isJQing && jqMap == getMapId() && !practice) {
            gainKarma(JQPToGain);
        } else {
            JQPToGain = 0;
        }
        if (jqTimer != null) {
            jqTimer.cancel(true);
        }
        String msg = "#e#gCongratulations!#k You have finshed this stage of the JQ. You received #r" + JQPToGain + "#k JQ Points!\r\n\r\nKeep going to win even more #rJQ Points!";
        switch (getMapId()) {
            case 610020000:
                scheduleJQ(610020001);
                break;
            case 101000100:
                scheduleJQ(101000101);
                break;
            case 101000101:
                scheduleJQ(101000102);
                break;
            case 101000102:
                scheduleJQ(101000103);
                break;
            case 101000103:
                scheduleJQ(101000104);
                break;
            case 101000104:
                scheduleJQ(105040310);
                break;
            case 105040310:
                scheduleJQ(105040311);
                break;
            case 105040311:
                scheduleJQ(105040312);
                break;
            case 105040312:
                scheduleJQ(105040313);
                break;
            case 105040313:
                scheduleJQ(105040314);
                break;
            case 105040314:
                scheduleJQ(105040315);
                break;
            case 105040315:
                scheduleJQ(105040316);
                break;
            case 109040001:
                scheduleJQ(109040002);
                break;
            case 109040002:
                scheduleJQ(109040003);
                break;
            case 109040003:
                scheduleJQ(109040004);
                break;
            case 103000900:
                scheduleJQ(103000901);
                break;
            case 103000901:
                scheduleJQ(103000903);
                break;
            case 103000903:
                scheduleJQ(103000904);
                break;
            case 103000904:
                scheduleJQ(103000906);
                break;
            case 103000906:
                scheduleJQ(103000907);
                break;
            case 103000907:
                scheduleJQ(103000908);
                break;
            case 280020000:
                scheduleJQ(280020001);
                break;
            case 220000006:
                scheduleJQ(220000006);
                break;
            case 100000202:
                scheduleJQ(100000202);
                break;
            default:
                changeMap(100000000, 24);
                isJQing = false;
                msg = "#e#gCongratulations!#k You have finshed the JQ. You received #r" + JQPToGain + "#k JQ Points!";
                JQPToGain = 0;
                break;
        }
        getClient().getSession().write(MaplePacketCreator.showEffect("quest/party/clear"));
        getClient().getSession().write(MaplePacketCreator.playSound("Party1/Clear"));
        getClient().getSession().write(MaplePacketCreator.getNPCTalk(9300006, (byte) 0, msg, "00 00"));
        achieve(Achievement.FJQ);
    }

    public void voteReminder() {
        long time = getLastVoteTime();
        if (time + 60 * 60 * 24 < System.currentTimeMillis() / 1000) {
            dropMessage(7, "The last time you voted was over 24 hours ago, which means you can now vote again!");
        } else {
            TimerManager.getInstance().schedule(new Runnable() {

                @Override
                public void run() {
                    if (getLastVoteTime() + 60 * 60 * 24 < System.currentTimeMillis() / 1000) {
                        voteReminder();
                    }
                }
            }, time + 60 * 60 * 24 - System.currentTimeMillis() / 1000);
        }
    }

    public long getLastVoteTime() {
        long res = 0;
        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT `timestamp` FROM `gtopvotes` WHERE `ip` = ? ORDER BY `timestamp` DESC LIMIT 1");
            ps.setString(1, getClient().getSession().getRemoteAddress().toString().split(":")[0]);
            ResultSet rs = ps.executeQuery();
            rs.next();
            res = rs.getLong(1);
            rs.close();
            ps.close();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        return res;
    }

    public boolean isChatBanned() {
        return chatBan;
    }

    public void chatBan() {
        chatBan = true;
    }

    public void chatUnban() {
        chatBan = false;
    }

    public double getExpRate() {
        return expRate;
    }

    public void cancelExpMultiplier() {
        expMultiplier.cancel(true);
        expRate = 1;
    }

    public void startExpMultiplier() {
        boolean dwb = haveItem(4031558, 1, false, true);
        if (dwb || haveItem(4002002, 1, false, true)) {
            expRate += 0.05;
            this.dropMessage(7, "The " + (dwb ? "DwB" : "Stump") + " Stamp starts giving you extra exp. It's currently at 5% extra");
            expMultiplier = TimerManager.getInstance().register(new Runnable() {

                public void run() {
                    double hrs = (expRate + 0.05) * 20;
                    if (hrs < 6) {
                        expRate += 0.05;
                        dropMessage(7, "After " + hrs + " hour" + (hrs > 1 ? "s" : "") + " your " + (haveItem(4031558, 1, false, true) ? "DwB" : "Stump") + " Stamp's extra exp rate is at " + hrs * 5 + "%");
                    } else {
                        cancelExpMultiplier();
                    }
                }
            }, 3600000, 3600000);
        }
    }

    public void startFullnessSchedule(final int decrease, final MaplePet pet, int petSlot) {
        ScheduledFuture<?> schedule = TimerManager.getInstance().register(new Runnable() {

            @Override
            public void run() {
                if (pet != null) {
                    int newFullness = pet.getFullness() - decrease;
                    if (newFullness <= 5) {
                        pet.setFullness(15);
                        unequipPet(pet, true, true);
                    } else {
                        pet.setFullness(newFullness);
                        getClient().getSession().write(MaplePacketCreator.updatePet(pet, true));
                    }
                }
            }
        }, 60000, 60000);
        switch (petSlot) {
            case 0:
                fullnessSchedule = schedule;
                break;
            case 1:
                fullnessSchedule_1 = schedule;
                break;
            case 2:
                fullnessSchedule_2 = schedule;
                break;
            default:
                break;
        }
    }

    public void cancelFullnessSchedule(int petSlot) {
        switch (petSlot) {
            case 0:
                if (fullnessSchedule != null) {
                    fullnessSchedule.cancel(false);
                }
            case 1:
                if (fullnessSchedule_1 != null) {
                    fullnessSchedule_1.cancel(false);
                }
            case 2:
                if (fullnessSchedule_2 != null) {
                    fullnessSchedule_2.cancel(false);
                }
            default:
                break;
        }
    }

    public void broadcastWorldNotice(int type, String message) {
        try {
            getClient().getChannelServer().getWorldInterface().broadcastMessage(null, MaplePacketCreator.serverNotice(type, message).getBytes());
        } catch (RemoteException e) {
            c.getChannelServer().reconnectWorld();
        }
    }

    public void achieve(int aid) {
        achieve(Achievement.getById(aid));
    }

    public int getAchievementPoints() {
        return achievementp;
    }

    public void achieve(Achievement a) {
        if (!achievements.contains(a) && a != null && Achievement.getById(a.getId()) != null) {
            achievements.add(a);
            tempAchievements.add(a);
            achievementp += a.getDifficulty();
            if (getMeso() < (Integer.MAX_VALUE - (10000000 * a.getDifficulty()))) {
                gainMeso(10000000 * a.getDifficulty(), true);
            } else {
                gainMeso(Integer.MAX_VALUE - getMeso(), true);
            }
            getClient().getSession().write(MaplePacketCreator.playSound("Party1/Clear"));
            getClient().getSession().write(MaplePacketCreator.sendYellowTip("[Achievement] You have just gained the " + a.getName() + " achievement. Congratulations on " + a.getDesc() + "."));
            if (Achievement.getDifficulty(a.getId()) == 5) {
                broadcastWorldNotice(6, "[Achievement] " + getName() + " has just unlocked the " + a.getName() + " achievement!");
            }
            switch (achievements.size()) {
                case 10:
                    achieve(Achievement.ACH10);
                    break;
                case 20:
                    achieve(Achievement.ACH20);
                    break;
                case 30:
                    achieve(Achievement.ACH30);
                    break;

            }
        }
    }

    public void reset() {
        int all = Achievement.values().length;
        if (achievements.size() == all) {
            achievements.clear();
            tempAchievements.clear();
            // need to delete all from db.
        } else {
            dropMessage("[Achievement] You have not yet finished all " + all + " achievements. You still have " + (all - achievements.size()) + " achievements to achieve.");
        }
    }

    public List<Achievement> getAllAchievements() {
        return achievements;
    }

    public boolean hasAchievement(int aid) {
        return hasAchievement(Achievement.getById(aid));
    }

    public boolean hasAchievement(Achievement a) {
        return achievements.contains(a);
    }

    public boolean isRedTeam() {
        return redteam == true;
    }

    public boolean isExempted() {
        return exempted == true;
    }

    public void exempt() {
        exempted = true;
    }

    public void unExempt() {
        exempted = false;
    }

    public void setRedTeam(boolean ass) {
        redteam = ass;
    }

    public void setStampOn() {
        stampOn = true;
    }

    public void setStampOff() {
        stampOn = false;
    }

    public boolean stampOn() {
        return stampOn;
    }

    public int getStampJob() {
        return stampJob;
    }

    public void setStampJob(int job) {
        stampJob = job;
    }

    public boolean canSay() {
        return canSay == true;
    }

    public void setCanSay(boolean bol) {
        canSay = bol;
    }

    public boolean inStaffChat() {
        return staffChat == true;
    }

    public void setStaffChat(boolean b) {
        staffChat = b;
    }

    public boolean canJail() {
        return canJail == true;
    }

    public void setCanJail(boolean bol) {
        canJail = bol;
    }

    public boolean isBlueTeam() {
        return blueteam == true;
    }

    public void setBlueTeam(boolean vag) {
        blueteam = vag;
    }

    public boolean hasFlag() {
        return hasflag == true;
    }

    public void setHasFlag(boolean penis) {
        hasflag = penis;
    }

    public boolean isWarned() {
        return warned == true;
    }

    public void setWarned(boolean bewbs) {
        warned = bewbs;
    }

    public boolean isFlagOn() {
        return flagon == true;
    }

    public void setFlagOn(boolean rofl) {
        flagon = rofl;
    }

    public boolean isChaser() {
        return chaser == true;
    }

    public void setChaser() {
        achieve(Achievement.ZOMBIE);
        chaser = true;
    }

    public void unsetChaser() {
        chaser = false;
    }

    public boolean isJustEntered() {
        return justentered == true;
    }

    public void setJustEntered(boolean flowers) {
        justentered = flowers;
    }

    public void toggleSpyMode() {
        spyMode = !spyMode;
    }

    public boolean inSpyMode() {
        return spyMode;
    }

    public void setBanned() {
        banned = true;
    }

    public boolean banned() {
        return banned;
    }

    public void warpParty(int mapId, int portal) {
        for (MapleCharacter member : c.getChannelServer().getPartyMembers(getParty())) {
            member.changeMap(mapId, portal);
        }
    }

    public void warpPartySpecial(int mapId) {
        for (MapleCharacter member : c.getChannelServer().getPartyMembers(getParty())) {
            member.changeMapSpecial(mapId);
        }
    }

    public void gainBlueTeamWin() {
        this.blueteamwins = this.getBlueTeamWins() + 1;
    }

    public void setBlueTeamWins(int dick) {
        blueteamwins = dick;
    }

    public int getBlueTeamWins() {
        return blueteamwins;
    }

    public void setTimeShit(int shit) {
        timeshit = shit;
    }

    public int getTimeShit() {
        return timeshit;
    }

    public void setRedTeamWins(int dick) {
        redteamwins = dick;
    }

    public int getRedTeamWins() {
        return redteamwins;
    }

    public void setRedTeam() {
        for (MaplePartyCharacter partychar : party.getMembers()) {
            partychar.setRedTeam(this);
        }
    }

    public void setBlueTeam() {
        for (MaplePartyCharacter partychar : party.getMembers()) {
            partychar.setBlueTeam(this);
        }
    }

    public void gainRedTeamWin() {
        this.redteamwins = this.getRedTeamWins() + 1;
    }

    public void resetFlag() {
        this.setBlueTeam(false);
        this.setRedTeam(false);
        if (this.haveItem(4001025, 1, false, false)) {
            MapleInventoryManipulator.removeAllById(getClient(), 4001025, true);
        }
        this.setRedTeamWins(0);
        this.setBlueTeamWins(0);
        this.setFlagPromptName("no");
        this.setFlagOn(false);
        this.setHasFlag(false);
        this.setJustEntered(false);
    }

    public void timedTimer(int amount) {
        TimerManager.getInstance().schedule(new Runnable() {

            public void run() {
                getClient().getSession().write(MaplePacketCreator.getClock(600));
                startFlagTimer();


            }
        }, 5000);
    }

    public void startFlagTimer() {
        TimerManager.getInstance().schedule(new Runnable() {

            public void run() {
                for (MapleCharacter chr : getMap().getCharacters()) {
                    if (getMapId() == 1010000) {
                        chr.dropMessage("Time is up.");
                        chr.changeMap(100000000, 0);
                    }
                }
            }
        }, 600000);
    }

    public void setFlagPromptName(String gotnothingmang) {
        flagPromptName = gotnothingmang;
    }

    public String getflagPromptName() {
        return this.flagPromptName;
    }

    public boolean getHideChat() {
        return hideChat;
    }

    public boolean hideChat() {
        hideChat = !hideChat;
        return hideChat;
    }

    public void startMapTimeLimitTask(final MapleMap from, final MapleMap to) {
        if (to.getTimeLimit() > 0 && from != null) {
            final MapleCharacter chr = this;
            mapTimeLimitTask = TimerManager.getInstance().register(new Runnable() {

                @Override
                public void run() {
                    MaplePortal pfrom = null;
                    if (from.isMiniDungeonMap()) {
                        pfrom = from.getPortal("MD00");
                    } else {
                        pfrom = from.getPortal(0);
                    }
                    if (pfrom != null) {
                        chr.changeMap(from, pfrom);
                    }
                }
            }, from.getTimeLimit() * 1000, from.getTimeLimit() * 1000);
        }
    }

    public void cancelMapTimeLimitTask() {
        if (mapTimeLimitTask != null) {
            mapTimeLimitTask.cancel(false);
        }
    }

    public void registerEffect(MapleStatEffect effect, long starttime, ScheduledFuture<?> schedule) {
        if (effect.isHide() && getGMLevel() > 0) {
            this.hidden = true;
            getMap().broadcastNONGMMessage(this, MaplePacketCreator.removePlayerFromMap(getId()), false);
        } else if (effect.isDragonBlood()) {
            prepareDragonBlood(effect);
        } else if (effect.isBerserk()) {
            checkBerserk();
        } else if (effect.isBeholder()) {
            prepareBeholderEffect();
        }
        for (Pair<MapleBuffStat, Integer> statup : effect.getStatups()) {
            effects.put(statup.getLeft(), new MapleBuffStatValueHolder(effect, starttime, schedule, statup.getRight().intValue()));
        }
        recalcLocalStats();
    }

    private List<MapleBuffStat> getBuffStats(MapleStatEffect effect, long startTime) {
        List<MapleBuffStat> stats = new ArrayList<MapleBuffStat>();
        for (Entry<MapleBuffStat, MapleBuffStatValueHolder> stateffect : effects.entrySet()) {
            MapleBuffStatValueHolder mbsvh = stateffect.getValue();
            if (mbsvh.effect.sameSource(effect) && (startTime == -1 || startTime == mbsvh.startTime)) {
                stats.add(stateffect.getKey());
            }
        }
        return stats;
    }

    private void deregisterBuffStats(List<MapleBuffStat> stats) {
        List<MapleBuffStatValueHolder> effectsToCancel = new ArrayList<MapleBuffStatValueHolder>(stats.size());
        for (MapleBuffStat stat : stats) {
            MapleBuffStatValueHolder mbsvh = effects.get(stat);
            if (mbsvh != null) {
                effects.remove(stat);
                boolean addMbsvh = true;
                for (MapleBuffStatValueHolder contained : effectsToCancel) {
                    if (mbsvh.startTime == contained.startTime && contained.effect == mbsvh.effect) {
                        addMbsvh = false;
                    }
                }
                if (addMbsvh) {
                    effectsToCancel.add(mbsvh);
                }
                if (stat == MapleBuffStat.SUMMON || stat == MapleBuffStat.PUPPET) {
                    int summonId = mbsvh.effect.getSourceId();
                    MapleSummon summon = summons.get(summonId);
                    if (summon != null) {
                        getMap().broadcastMessage(MaplePacketCreator.removeSpecialMapObject(summon, true));
                        getMap().removeMapObject(summon);
                        removeVisibleMapObject(summon);
                        summons.remove(summonId);
                    }
                    if (summon.getSkill() == 1321007) {
                        if (beholderHealingSchedule != null) {
                            beholderHealingSchedule.cancel(false);
                            beholderHealingSchedule = null;
                        }
                        if (beholderBuffSchedule != null) {
                            beholderBuffSchedule.cancel(false);
                            beholderBuffSchedule = null;
                        }
                    }
                } else if (stat == MapleBuffStat.DRAGONBLOOD) {
                    dragonBloodSchedule.cancel(false);
                    dragonBloodSchedule = null;
                }
            }
        }
        for (MapleBuffStatValueHolder cancelEffectCancelTasks : effectsToCancel) {
            if (getBuffStats(cancelEffectCancelTasks.effect, cancelEffectCancelTasks.startTime).size() == 0) {
                cancelEffectCancelTasks.schedule.cancel(false);
            }
        }
    }

    public void cancelEffect(MapleStatEffect effect, boolean overwrite, long startTime) {
        List<MapleBuffStat> buffstats;
        if (!overwrite) {
            buffstats = getBuffStats(effect, startTime);
        } else {
            List<Pair<MapleBuffStat, Integer>> statups = effect.getStatups();
            buffstats = new ArrayList<MapleBuffStat>(statups.size());
            for (Pair<MapleBuffStat, Integer> statup : statups) {
                buffstats.add(statup.getLeft());
            }
        }
        deregisterBuffStats(buffstats);
        if (effect.isMagicDoor()) {
            if (!getDoors().isEmpty()) {
                MapleDoor door = getDoors().iterator().next();
                for (MapleCharacter chr : door.getTarget().getCharacters()) {
                    door.sendDestroyData(chr.getClient());
                }
                for (MapleCharacter chr : door.getTown().getCharacters()) {
                    door.sendDestroyData(chr.getClient());
                }
                for (MapleDoor destroyDoor : getDoors()) {
                    door.getTarget().removeMapObject(destroyDoor);
                    door.getTown().removeMapObject(destroyDoor);
                }
                clearDoors();
                silentPartyUpdate();
            }
        }
        if (effect.isMonsterRiding()) {
            if (effect.getSourceId() != 5221006) {
                this.getMount().cancelSchedule();
                this.getMount().setActive(false);
            }
        }
        if (!overwrite) {
            cancelPlayerBuffs(buffstats);
            if (effect.isHide() && (MapleCharacter) getMap().getMapObject(getObjectId()) != null) {
                this.hidden = false;
                getMap().broadcastNONGMMessage(this, MaplePacketCreator.spawnPlayerMapobject(this), false);
                setOffOnline(true);
                for (int i = 0; i < 3; i++) {
                    if (pets[i] != null) {
                        getMap().broadcastNONGMMessage(this, MaplePacketCreator.showPet(this, pets[i], false, false), false);
                    } else {
                        break;
                    }
                }
            }
        }
    }

    public void cancelBuffStats(MapleBuffStat stat) {
        List<MapleBuffStat> buffStatList = Arrays.asList(stat);
        deregisterBuffStats(buffStatList);
        cancelPlayerBuffs(buffStatList);
    }

    public void cancelEffectFromBuffStat(MapleBuffStat stat) {
        cancelEffect(effects.get(stat).effect, false, -1);
    }

    private void cancelPlayerBuffs(List<MapleBuffStat> buffstats) {
        if (getClient().getChannelServer().getPlayerStorage().getCharacterById(getId()) != null) {
            recalcLocalStats();
            enforceMaxHpMp();
            getClient().getSession().write(MaplePacketCreator.cancelBuff(buffstats));
            getMap().broadcastMessage(this, MaplePacketCreator.cancelForeignBuff(getId(), buffstats), false);
        }
    }

    public void dispel() {
        LinkedList<MapleBuffStatValueHolder> allBuffs = new LinkedList<MapleBuffStatValueHolder>(effects.values());
        for (MapleBuffStatValueHolder mbsvh : allBuffs) {
            if (mbsvh.effect.isSkill()) {
                cancelEffect(mbsvh.effect, false, mbsvh.startTime);
            }
        }
    }

    public void cancelAllBuffs() {
        LinkedList<MapleBuffStatValueHolder> allBuffs = new LinkedList<MapleBuffStatValueHolder>(effects.values());
        for (MapleBuffStatValueHolder mbsvh : allBuffs) {
            cancelEffect(mbsvh.effect, false, mbsvh.startTime);
        }
    }

    public void cancelMorphs() {
        LinkedList<MapleBuffStatValueHolder> allBuffs = new LinkedList<MapleBuffStatValueHolder>(effects.values());
        for (MapleBuffStatValueHolder mbsvh : allBuffs) {
            if (mbsvh.effect.isMorph() && mbsvh.effect.getSourceId() != 5111005 && mbsvh.effect.getSourceId() != 5121003) {
                cancelEffect(mbsvh.effect, false, mbsvh.startTime);
            }
        }
    }

    public boolean hasMorph() {
        LinkedList<MapleBuffStatValueHolder> allBuffs = new LinkedList<MapleBuffStatValueHolder>(effects.values());
        for (MapleBuffStatValueHolder mbsvh : allBuffs) {
            if (mbsvh.effect.isMorph()) {
                return true;
            }
        }
        return false;
    }

    public void silentGiveBuffs(List<PlayerBuffValueHolder> buffs) {
        for (PlayerBuffValueHolder mbsvh : buffs) {
            mbsvh.effect.silentApplyBuff(this, mbsvh.startTime);
        }
    }

    public List<PlayerBuffValueHolder> getAllBuffs() {
        List<PlayerBuffValueHolder> ret = new ArrayList<PlayerBuffValueHolder>();
        for (MapleBuffStatValueHolder mbsvh : effects.values()) {
            ret.add(new PlayerBuffValueHolder(mbsvh.startTime, mbsvh.effect));
        }
        return ret;
    }

    public void allow() {
        allowed = true;
    }

    public void scheduleAllowed() {
        TimerManager.getInstance().schedule(new Runnable() {

            public void run() {
                if (!allowed) {
                    getClient().disconnect();
                }
            }
        }, 20 * 1000);
    }

    public void cancelMagicDoor() {
        LinkedList<MapleBuffStatValueHolder> allBuffs = new LinkedList<MapleBuffStatValueHolder>(effects.values());
        for (MapleBuffStatValueHolder mbsvh : allBuffs) {
            if (mbsvh.effect.isMagicDoor()) {
                cancelEffect(mbsvh.effect, false, mbsvh.startTime);
            }
        }
    }

    public void handleOrbgain() {
        MapleStatEffect ceffect = null;
        int advComboSkillLevel = getSkillLevel(SkillFactory.getSkill(1120003));
        if (advComboSkillLevel > 0) {
            ceffect = SkillFactory.getSkill(1120003).getEffect(advComboSkillLevel);
        } else {
            ceffect = SkillFactory.getSkill(1111002).getEffect(getSkillLevel(SkillFactory.getSkill(1111002)));
        }
        if (getBuffedValue(MapleBuffStat.COMBO) < ceffect.getX() + 1) {
            int neworbcount = getBuffedValue(MapleBuffStat.COMBO) + 1;
            if (advComboSkillLevel > 0 && ceffect.makeChanceResult()) {
                if (neworbcount < ceffect.getX() + 1) {
                    neworbcount++;
                }
            }
            List<Pair<MapleBuffStat, Integer>> stat = Collections.singletonList(new Pair<MapleBuffStat, Integer>(MapleBuffStat.COMBO, neworbcount));
            setBuffedValue(MapleBuffStat.COMBO, neworbcount);
            int duration = ceffect.getDuration();
            duration += (int) ((getBuffedStarttime(MapleBuffStat.COMBO) - System.currentTimeMillis()));
            getClient().getSession().write(MaplePacketCreator.giveBuff(1111002, duration, stat));
            getMap().broadcastMessage(this, MaplePacketCreator.giveForeignBuff(getId(), stat, ceffect), false);
        }
    }

    public void handleOrbconsume() {
        ISkill combo = SkillFactory.getSkill(1111002);
        MapleStatEffect ceffect = combo.getEffect(getSkillLevel(combo));
        List<Pair<MapleBuffStat, Integer>> stat = Collections.singletonList(new Pair<MapleBuffStat, Integer>(MapleBuffStat.COMBO, 1));
        setBuffedValue(MapleBuffStat.COMBO, 1);
        int duration = ceffect.getDuration();
        duration += (int) ((getBuffedStarttime(MapleBuffStat.COMBO) - System.currentTimeMillis()));
        getClient().getSession().write(MaplePacketCreator.giveBuff(1111002, duration, stat));
        getMap().broadcastMessage(this, MaplePacketCreator.giveForeignBuff(getId(), stat, ceffect), false);
    }

    private void silentEnforceMaxHpMp() {
        setMp(getMp());
        setHp(getHp(), true);
    }

    private void enforceMaxHpMp() {
        List<Pair<MapleStat, Integer>> stats = new ArrayList<Pair<MapleStat, Integer>>(2);
        if (getMp() > getCurrentMaxMp()) {
            setMp(getMp());
            stats.add(new Pair<MapleStat, Integer>(MapleStat.MP, Integer.valueOf(getMp())));
        }
        if (getHp() > getCurrentMaxHp()) {
            setHp(getHp());
            stats.add(new Pair<MapleStat, Integer>(MapleStat.HP, Integer.valueOf(getHp())));
        }
        if (stats.size() > 0) {
            getClient().getSession().write(MaplePacketCreator.updatePlayerStats(stats));
        }
    }

    public MapleMap getMap() {
        return map;
    }

    public void setMap(MapleMap newmap) {
        this.map = newmap;
    }

    public boolean inHideout() {
        return (getMapId() == getGuild().getHideout());
    }

    public int getMapId() {
        if (map != null) {
            return map.getId();
        }
        return mapid;
    }

    public int getInitialSpawnpoint() {
        return initialSpawnPoint;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public int getRank() {
        return rank;
    }

    public int getRankMove() {
        return rankMove;
    }

    public int getJobRank() {
        return jobRank;
    }

    public int getJobRankMove() {
        return jobRankMove;
    }

    public int getAPQScore() {
        return APQScore;
    }

    public int getFame() {
        return fame;
    }

    public int getCP() {
        return this.CP;
    }

    public int getTeam() {
        return this.team;
    }

    public int getTotalCP() {
        return this.totalCP;
    }

    public void setCP(int cp) {
        this.CP = cp;
    }

    public void setTeam(int team) {
        this.team = team;
    }

    public void setTotalCP(int totalcp) {
        this.totalCP = totalcp;
    }

    public void gainCP(int gain) {
        this.setCP(this.getCP() + gain);
        if (this.getCP() > this.getTotalCP()) {
            this.setTotalCP(this.getCP());
        }
        this.getClient().getSession().write(MaplePacketCreator.CPUpdate(false, this.getCP(), this.getTotalCP(), this.getTeam()));
        if (this.getParty() != null && this.getParty().getTeam() != -1) {
            this.getMap().broadcastMessage(MaplePacketCreator.CPUpdate(true, this.getParty().getCP(), this.getParty().getTotalCP(), this.getParty().getTeam()));
        }
    }

    public int getOriginalSkin() {
        return originalSkin;
    }

    public void setOriginalSkin(int skin) {
        originalSkin = skin;
    }

    public int getChaseyCount() {
        return chaseyCount;
    }

    public void addChaseyCount() {
        chaseyCount++;
    }

    public void clearChaseyCount() {
        chaseyCount = 0;
    }

    public int getFalseItemCount() {
        return falseItemCount;
    }

    public void addFalseItemCount() {
        falseItemCount++;
    }

    public void clearFalseItemCount() {
        falseItemCount = 0;
    }

    public int getStr() {
        return str;
    }

    public int getDex() {
        return dex;
    }

    public int getLuk() {
        return luk;
    }

    public int getInt() {
        return int_;
    }

    public MapleClient getClient() {
        return client;
    }

    public int getExp() {
        return exp.get();
    }

    public int getHp() {
        return hp;
    }

    public int getMaxHp() {
        return maxhp;
    }

    public int getMp() {
        return mp;
    }

    public int getMaxMp() {
        return maxmp;
    }

    public int getRemainingAp() {
        return remainingAp;
    }

    public int getRemainingSp() {
        return remainingSp;
    }

    public int getMpApUsed() {
        return mpApUsed;
    }

    public List<String> getLatestPlayers() {
        List<String> players = new LinkedList<String>();
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT name FROM characters ORDER BY createdate desc LIMIT 10");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                players.add(rs.getString("name"));
            }
            rs.close();
            ps.close();
        } catch (SQLException se) {
            se.printStackTrace();
            return null;
        }
        return players;
    }
// begin question stuff

    public String listQuestions() {
        String end = "#b";
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT id,question FROM questions WHERE answered = 0 ORDER BY id desc");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                end += "\r\n#L" + rs.getInt("id") + "#" + rs.getString("question") + "#l";
            }
            rs.close();
            ps.close();
        } catch (SQLException se) {
            se.printStackTrace();
            return "#k#eNo questions to display#n";
        }
        if (end.length() > 2) {
            return end;
        } else {
            return "#k#eNo questions to display#n";
        }
    }

    public String getQuestion(int id) {
        String result = "#e";
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT name,question,prize,quantity FROM questions WHERE id = ? ORDER BY id desc");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result += "#rInformation:#k\r\n\r\nQuestion: #b" + rs.getString("question") + "#k\r\nAsker: #b" + rs.getString("name") + "#k\r\nPrize: #b#i" + rs.getInt("prize") + "##k\r\nQuantity: #b" + rs.getInt("quantity") + "#k";
            }
            rs.close();
            ps.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return result;
    }

    private boolean isAnswered(int id) {
        boolean a = true;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT answered FROM questions WHERE id = ? ORDER BY id desc");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                a = rs.getInt("answered") == 1;
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            return true;
        }
        return a;
    }

    public boolean checkAnswer(int id, String answer) {
        if (answer == null) {
            return false;
        }
        String aanswer = "";
        String nname = "";
        int prize = 0;
        int quantity = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT name,question,answer,prize,quantity FROM questions WHERE id = ? ORDER BY id desc");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                aanswer = rs.getString("answer");
                nname = rs.getString("name");
                prize = rs.getInt("prize");
                quantity = rs.getInt("quantity");
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            return false;
        }
        if (!aanswer.equalsIgnoreCase(answer) || nname.equalsIgnoreCase(getName()) || isAnswered(id)) {
            getClient().getSession().write(MaplePacketCreator.showEffect("quest/party/wrong_kor"));
            getClient().getSession().write(MaplePacketCreator.playSound("Party1/Failed"));
            return false;
        } else {
            MapleInventoryManipulator.addById(getClient(), prize, (short) quantity);
            setAnswered(id);
            MaplePacket packet = MaplePacketCreator.serverNotice(5, "[Q&A] Congratulations to " + getName() + " for answering " + nname + "'s question.");
            c.getChannelServer().broadcastPacket(packet);
            getMap().broadcastMessage(MaplePacketCreator.showEffect("quest/party/clear"));
            getMap().broadcastMessage(MaplePacketCreator.playSound("Party1/Clear"));
            return true;
        }
    }

    public void addQuestion(String name, String question, String answer, int prize, int quantity) {
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("INSERT INTO questions (name, question, answer, prize, quantity) VALUES (?, ?, ?, ?, ?)");
            ps.setString(1, name);
            ps.setString(2, question);
            ps.setString(3, answer);
            ps.setInt(4, prize);
            ps.setInt(5, quantity);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
        }
        MaplePacket packet = MaplePacketCreator.serverNotice(5, "[Q&A] " + getName() + " has just registered a new question! Go check it out in henesys");
        ChannelServer.getInstance(c.getChannel()).broadcastPacket(packet);
    }

    public void setAnswered(int id) {
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("UPDATE questions SET answered = 1 WHERE id = ?");
            ps.setInt(1, id);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
        }
    }

    public int getEtcQuantity(byte slot) {
        return (int) getInventory(MapleInventoryType.ETC).getItem(slot).getQuantity();
    }

    public int getUseQuantity(byte slot) {
        return (int) getInventory(MapleInventoryType.USE).getItem(slot).getQuantity();
    }

    public int getCashQuantity(byte slot) {
        return (int) getInventory(MapleInventoryType.CASH).getItem(slot).getQuantity();
    }

    public int getSetupQuantity(byte slot) {
        return (int) getInventory(MapleInventoryType.SETUP).getItem(slot).getQuantity();
    }

    public void deleteEquipItem(byte slot) {
        MapleInventoryManipulator.removeAllById(getClient(), getInventory(MapleInventoryType.EQUIP).getItem(slot).getItemId(), true);
    }

    public void deleteEtcItem(byte slot) {
        MapleInventoryManipulator.removeAllById(getClient(), getInventory(MapleInventoryType.ETC).getItem(slot).getItemId(), true);
    }

    public void deleteUseItem(byte slot) {
        MapleInventoryManipulator.removeAllById(getClient(), getInventory(MapleInventoryType.USE).getItem(slot).getItemId(), true);
    }

    public void deleteSetupItem(byte slot) {
        MapleInventoryManipulator.removeAllById(getClient(), getInventory(MapleInventoryType.SETUP).getItem(slot).getItemId(), true);
    }

    public void deleteCashItem(byte slot) {
        MapleInventoryManipulator.removeAllById(getClient(), getInventory(MapleInventoryType.CASH).getItem(slot).getItemId(), true);
    }

    public void setMpApUsed(int mpApUsed) {
        this.mpApUsed = mpApUsed;
    }

    public int getHpApUsed() {
        return hpApUsed;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHpApUsed(int hpApUsed) {
        this.hpApUsed = hpApUsed;
    }

    public MapleSkinColor getSkinColor() {
        return skinColor;
    }

    public MapleJob getJob() {
        return job;
    }

    public int getGender() {
        return gender;
    }

    public int getHair() {
        return hair;
    }

    public int getFace() {
        return face;
    }

    public void setName(String name, boolean changeName) {
        if (!changeName) {
            this.name = name;
        } else {
            Connection con = DatabaseConnection.getConnection();
            try {
                con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
                con.setAutoCommit(false);
                PreparedStatement sn = con.prepareStatement("UPDATE characters SET name = ? WHERE id = ?");
                sn.setString(1, name);
                sn.setInt(2, id);
                sn.execute();
                con.commit();
                sn.close();
                this.name = name;
            } catch (SQLException e) {
                sqlException(e);
            }
        }
    }

    public void setStr(int str) {
        this.str = str;
        recalcLocalStats();
    }

    public void setDex(int dex) {
        this.dex = dex;
        recalcLocalStats();
    }

    public void setLuk(int luk) {
        this.luk = luk;
        recalcLocalStats();
    }

    public void setInt(int int_) {
        this.int_ = int_;
        recalcLocalStats();
    }

    public void setMaxHp(int hp) {
        this.maxhp = hp;
        recalcLocalStats();
    }

    public void setMaxMp(int mp) {
        this.maxmp = mp;
        recalcLocalStats();
    }

    public void setHair(int hair) {
        this.hair = hair;
    }

    public void setFace(int face) {
        this.face = face;
    }

    public void setFame(int fame) {
        this.fame = fame;
    }

    public void setAPQScore(int score) {
        this.APQScore = score;
    }

    public void setRemainingAp(int remainingAp) {
        this.remainingAp = remainingAp;
    }

    public void setRemainingSp(int remainingSp) {
        this.remainingSp = remainingSp;
    }

    public void setSkinColor(MapleSkinColor skinColor) {
        this.skinColor = skinColor;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public void setGM(int gmlevel) {
        this.gmLevel = gmlevel;
    }

    public CheatTracker getCheatTracker() {
        return anticheat;
    }

    public BuddyList getBuddylist() {
        return buddylist;
    }

    public void addFame(int famechange) {
        this.fame += famechange;
    }

    public void changeMap(int map) {
        changeMap(map, 0);
    }

    public void changeMapSpecial(int map) {
        MapleMap warpMap = client.getChannelServer().getMapFactory().getMap(map);
        changeMapSpecial(warpMap, warpMap.getPortal(0));
    }

    public void changeMapSpecial(final MapleMap to, final MaplePortal pto) {
        MaplePacket warpPacket = MaplePacketCreator.getWarpToMap(to, pto.getId(), this);
        changeMapInternal(to, pto.getPosition(), warpPacket, false);
    }

    public void changeMap(int map, int portal) {
        MapleMap warpMap = client.getChannelServer().getMapFactory().getMap(map);
        changeMap(warpMap, warpMap.getPortal(portal));
    }

    public void changeMap(int map, String portal) {
        MapleMap warpMap = client.getChannelServer().getMapFactory().getMap(map);
        changeMap(warpMap, warpMap.getPortal(portal));
    }

    public void changeMap(int map, MaplePortal portal) {
        MapleMap warpMap = client.getChannelServer().getMapFactory().getMap(map);
        changeMap(warpMap, portal);
    }

    public void changeMap(final MapleMap to, final Point pos) {
        MaplePacket warpPacket = MaplePacketCreator.getWarpToMap(to, 0x80, this);
        changeMapInternal(to, pos, warpPacket, true);
    }

    public void changeMapSpecial(final MapleMap to, final Point pos) {
        MaplePacket warpPacket = MaplePacketCreator.getWarpToMap(to, 0x80, this);
        changeMapInternal(to, pos, warpPacket, false);
    }

    public void changeMap(final MapleMap to, final MaplePortal pto) {
        MaplePacket warpPacket = MaplePacketCreator.getWarpToMap(to, pto.getId(), this);
        changeMapInternal(to, pto.getPosition(), warpPacket, true);
    }

    private void changeMapInternal(final MapleMap to, final Point pos, MaplePacket warpPacket, boolean restrict) {
        if ((to.getId() == 922010800 || to.getId() == 109020001 || to.getId() == 109070000) && restrict) {
            if (!wserv.canEventWarp() && getGMLevel() < 1) {
                return;
            }
        }
        if (getCheatTracker().Spam(2000, 5)) {
            client.getSession().write(MaplePacketCreator.enableActions());
        } else {
            warpPacket.setOnSend(new Runnable() {

                @Override
                public void run() {
                    IPlayerInteractionManager interaction = MapleCharacter.this.getInteraction();
                    if (interaction != null) {
                        if (interaction.isOwner(MapleCharacter.this)) {
                            if (interaction.getShopType() == 2) {
                                interaction.removeAllVisitors(3, 1);
                                interaction.closeShop(((MaplePlayerShop) interaction).returnItems(getClient()));
                            } else if (interaction.getShopType() == 1) {
                                getClient().getSession().write(MaplePacketCreator.shopVisitorLeave(0));
                                if (interaction.getItems().size() == 0) {
                                    interaction.removeAllVisitors(3, 1);
                                    interaction.closeShop(((HiredMerchant) interaction).returnItems(getClient()));
                                }
                            } else if (interaction.getShopType() == 3 || interaction.getShopType() == 4) {
                                interaction.removeAllVisitors(3, 1);
                            }
                        } else {
                            interaction.removeVisitor(MapleCharacter.this);
                        }
                    }
//                    if (follower > -1) {
//                        MapleCharacter follower = c.getChannelServer().getPlayerStorage().getCharacterById(c.getPlayer().getFollower());
//                        if (follower != null) {
//                            follower.changeMap(to, pos);
//                            follower.dropMessage(getName() + " has changed to map '" + to.getMapName() + "'");
//                        }
//                    }
                    MapleCharacter.this.setInteraction(null);
                    map.removePlayer(MapleCharacter.this);
                    if (getClient().getChannelServer().getPlayerStorage().getCharacterById(getId()) != null) {
                        map = to;
                        setPosition(pos);
                        to.addPlayer(MapleCharacter.this);
                        if (party != null) {
                            silentPartyUpdate();
                            getClient().getSession().write(MaplePacketCreator.updateParty(getClient().getChannel(), party, PartyOperation.SILENT_UPDATE, null));
                            updatePartyMemberHP();
                        }
                        if (getMap().getHPDec() > 0 && !inCS() && isAlive()) {
                            hpDecreaseTask = TimerManager.getInstance().schedule(new Runnable() {

                                @Override
                                public void run() {
                                    doHurtHp();
                                }
                            }, 10000);
                        }
                        if (to.getId() == 980000301) {
                            setTeam(MapleCharacter.rand(0, 1));
                            getClient().getSession().write(MaplePacketCreator.startMonsterCarnival(getTeam()));
                        }
                    }
                }
            });
            if (hasFakeChar()) {
                for (FakeCharacter ch : getFakeChars()) {
                    if (ch.follow()) {
                        ch.getFakeChar().getMap().removePlayer(ch.getFakeChar());
                    }
                }
            }
            getClient().getSession().write(warpPacket);
        }
    }

    public void leaveMap() {
        controlled.clear();
        visibleMapObjects.clear();
        if (chair != 0) {
            chair = 0;
        }
        if (hpDecreaseTask != null) {
            hpDecreaseTask.cancel(false);
        }
    }

    public void doHurtHp() {
        if (this.getInventory(MapleInventoryType.EQUIPPED).findById(getMap().getHPDecProtect()) != null) {
            return;
        }
        addHP(-getMap().getHPDec());
        hpDecreaseTask = TimerManager.getInstance().schedule(new Runnable() {

            @Override
            public void run() {
                doHurtHp();
            }
        }, 10000);
    }

    public void changeJob(MapleJob newJob) {
        if (hp < 1) {
            return;
        }

        this.job = newJob;
        this.remainingSp++;
        if (newJob.getId() % 10 == 2) {
            this.remainingSp += 2;
        }
        updateSingleStat(MapleStat.AVAILABLESP, this.remainingSp);
        updateSingleStat(MapleStat.JOB, newJob.getId());
        if (job.getId() == 100) {
            maxhp += rand(200, 250);
        } else if (job.getId() == 200) {
            maxmp += rand(100, 150);
        } else if (job.getId() % 100 == 0) {
            maxhp += rand(100, 150);
            maxhp += rand(25, 50);
        } else if (job.getId() > 0 && job.getId() < 200) {
            maxhp += rand(300, 350);
        } else if (job.getId() < 300) {
            maxmp += rand(450, 500);
        } else if (job.getId() > 0) {
            maxhp += rand(300, 350);
            maxmp += rand(150, 200);
        }
        if (maxhp >= 30000) {
            maxhp = 30000;
        }
        if (maxmp >= 30000) {
            maxmp = 30000;
        }
        setHp(maxhp);
        setMp(maxmp);
        List<Pair<MapleStat, Integer>> statup = new ArrayList<Pair<MapleStat, Integer>>(2);
        statup.add(new Pair<MapleStat, Integer>(MapleStat.MAXHP, Integer.valueOf(maxhp)));
        statup.add(new Pair<MapleStat, Integer>(MapleStat.MAXMP, Integer.valueOf(maxmp)));
        recalcLocalStats();
        getClient().getSession().write(MaplePacketCreator.updatePlayerStats(statup));
        silentPartyUpdate();
        guildUpdate();
        getMap().broadcastMessage(this, MaplePacketCreator.showJobChange(getId()), false);
    }

    public void gainAp(int ap) {
        this.remainingAp += ap;
        updateSingleStat(MapleStat.AVAILABLEAP, this.remainingAp);
    }

    public void changeSkillLevel(ISkill skill, int newLevel, int newMasterlevel) {
        skills.put(skill, new SkillEntry(newLevel, newMasterlevel));
        this.getClient().getSession().write(MaplePacketCreator.updateSkill(skill.getId(), newLevel, newMasterlevel));
    }

    public void setHp(int newhp) {
        setHp(newhp, false);
    }

    public void setHp(int newhp, boolean silent) {
        int oldHp = hp;
        if (newhp < 0) {
            newhp = 0;
        } else if (newhp > localmaxhp) {
            newhp = localmaxhp;
        }
        this.hp = newhp;

        if (!silent) {
            updatePartyMemberHP();
        }
        if (oldHp > hp && !isAlive()) {
            playerDead();
        }
        this.checkBerserk();
    }

    public void addAP(MapleClient c, int stat, int amount) {
        MapleCharacter player = c.getPlayer();
        switch (stat) {
            case 1: // STR
                player.setStr(player.getStr() + amount);
                player.updateSingleStat(MapleStat.STR, player.getStr());
                break;
            case 2: // DEX
                player.setDex(player.getDex() + amount);
                player.updateSingleStat(MapleStat.DEX, player.getDex());
                break;
            case 3: // INT
                player.setInt(player.getInt() + amount);
                player.updateSingleStat(MapleStat.INT, player.getInt());
                break;
            case 4: // LUK
                player.setLuk(player.getLuk() + amount);
                player.updateSingleStat(MapleStat.LUK, player.getLuk());
                break;
            case 5: // HP
                player.setMaxHp(amount);
                player.updateSingleStat(MapleStat.MAXHP, player.getMaxHp());
                break;
            case 6: // MP
                player.setMaxMp(amount);
                player.updateSingleStat(MapleStat.MAXMP, player.getMaxMp());
                break;
        }

        player.setRemainingAp(player.getRemainingAp() - amount);
        player.updateSingleStat(MapleStat.AVAILABLEAP, player.getRemainingAp());
    }

    private void playerDead() {
        if (getEventInstance() != null) {
            getEventInstance().playerKilled(this);
        }
        cancelAllBuffs();
        cancelAllDebuffs();
        MapleCharacter player = getClient().getPlayer();
        player.getClient().getSession().write(MaplePacketCreator.cancelChair(-1));
        player.getMap().broadcastMessage(player, MaplePacketCreator.showChair(player.getId(), 0), false);
        int[] charmID = {5130000, 4031283, 4140903};
        int possesed = 0;
        int i;
        for (i = 0; i < charmID.length; i++) {
            int quantity = getItemQuantity(charmID[i], false);
            if (possesed == 0 && quantity > 0) {
                possesed = quantity;
                break;
            }
        }
        if (possesed > 0) {
            possesed -= 1;
            getClient().getSession().write(MaplePacketCreator.serverNotice(5, "You have used the safety charm once, so your EXP points have not been decreased. (" + possesed + "time(s) left)"));
            MapleInventoryManipulator.removeById(getClient(), MapleItemInformationProvider.getInstance().getInventoryType(charmID[i]), charmID[i], 1, true, false);
        } else {
            if (player.getJob() != MapleJob.BEGINNER) {
                int XPdummy = ExpTable.getExpNeededForLevel(player.getLevel() + 1);
                if (player.getMap().isTown()) {
                    XPdummy *= 0.01;
                }
                if (XPdummy == ExpTable.getExpNeededForLevel(player.getLevel() + 1)) {
                    if (player.getLuk() <= 100 && player.getLuk() > 8) {
                        XPdummy *= 0.10 - (player.getLuk() * 0.0005);
                    } else if (player.getLuk() < 8) {
                        XPdummy *= 0.10;
                    } else {
                        XPdummy *= 0.10 - (100 * 0.0005);
                    }
                }
                if ((player.getExp() - XPdummy) > 0) {
                    player.gainExp(-XPdummy, false, false);
                } else {
                    player.gainExp(-player.getExp(), false, false);
                }
            }
        }
        getClient().getSession().write(MaplePacketCreator.enableActions());
    }

    public void updatePartyMemberHP() {
        if (party != null) {
            int channel = client.getChannel();
            for (MaplePartyCharacter partychar : party.getMembers()) {
                if (partychar.getMapid() == getMapId() && partychar.getChannel() == channel) {
                    MapleCharacter other = ChannelServer.getInstance(channel).getPlayerStorage().getCharacterByName(partychar.getName());
                    if (other != null) {
                        other.getClient().getSession().write(MaplePacketCreator.updatePartyMemberHP(getId(), this.hp, localmaxhp));
                    }
                }
            }
        }
    }

    public void receivePartyMemberHP() {
        if (party != null) {
            int channel = client.getChannel();
            for (MaplePartyCharacter partychar : party.getMembers()) {
                if (partychar.getMapid() == getMapId() && partychar.getChannel() == channel) {
                    MapleCharacter other = ChannelServer.getInstance(channel).getPlayerStorage().getCharacterByName(partychar.getName());
                    if (other != null) {
                        getClient().getSession().write(
                                MaplePacketCreator.updatePartyMemberHP(other.getId(), other.getHp(), other.getCurrentMaxHp()));
                    }
                }
            }
        }
    }

    public void setMp(int newmp) {
        if (newmp < 0) {
            newmp = 0;
        } else if (newmp > localmaxmp) {
            newmp = localmaxmp;
        }
        this.mp = newmp;
    }

    public void addHP(int delta) {
        setHp(hp + delta);
        updateSingleStat(MapleStat.HP, hp);
    }

    public void addMP(int delta) {
        setMp(mp + delta);
        updateSingleStat(MapleStat.MP, mp);
    }

    public void addMPHP(int hpDiff, int mpDiff) {
        setHp(hp + hpDiff);
        setMp(mp + mpDiff);
        List<Pair<MapleStat, Integer>> stats = new ArrayList<Pair<MapleStat, Integer>>();
        stats.add(new Pair<MapleStat, Integer>(MapleStat.HP, Integer.valueOf(hp)));
        stats.add(new Pair<MapleStat, Integer>(MapleStat.MP, Integer.valueOf(mp)));
        MaplePacket updatePacket = MaplePacketCreator.updatePlayerStats(stats);
        client.getSession().write(updatePacket);
    }

    public void updateSingleStat(MapleStat stat, int newval, boolean itemReaction) {
        Pair<MapleStat, Integer> statpair = new Pair<MapleStat, Integer>(stat, Integer.valueOf(newval));
        MaplePacket updatePacket = MaplePacketCreator.updatePlayerStats(Collections.singletonList(statpair), itemReaction);
        client.getSession().write(updatePacket);
    }

    public void updateSingleStat(MapleStat stat, int newval) {
        updateSingleStat(stat, newval, false);
    }

    public void gainExp(int gain, boolean show, boolean inChat) {
        gainExp(gain, show, inChat, true, true);
    }

    public void gainExp(int gain, boolean show, boolean inChat, boolean white) {
        gainExp(gain, show, inChat, white, true);
    }

    public void gainExp(int gain, boolean show, boolean inChat, boolean white, boolean etcLose) {
        int levelCap = getClient().getChannelServer().getLevelCap();
        if (!etcLose && gain < 0) {
            gain += Integer.MAX_VALUE;
            if (getLevel() < levelCap) {
                levelUp();
            }
            while (gain > 0) {
                gain -= (ExpTable.getExpNeededForLevel(level) - this.exp.get());
                if (getLevel() < levelCap) {
                    levelUp();
                }
            }
            setExp(0);
            updateSingleStat(MapleStat.EXP, exp.get());
            client.getSession().write(MaplePacketCreator.getShowExpGain(Integer.MAX_VALUE, inChat, white));
            return;
        }
        if (getLevel() < levelCap) {
            if ((long) this.exp.get() + (long) gain > (long) Integer.MAX_VALUE) {
                int gainFirst = ExpTable.getExpNeededForLevel(level) - this.exp.get();
                gain -= gainFirst + 1;
                this.gainExp(gainFirst + 1, false, inChat, white);
            }
            updateSingleStat(MapleStat.EXP, this.exp.addAndGet(gain));
        } else {
            return;
        }
        if (show && gain != 0) {
            client.getSession().write(MaplePacketCreator.getShowExpGain(gain, inChat, white));
        }
        if (exp.get() >= ExpTable.getExpNeededForLevel(level) && level < levelCap) {
            if (getClient().getChannelServer().getMultiLevel()) {
                while (level < levelCap && exp.get() >= ExpTable.getExpNeededForLevel(level)) {
                    levelUp();
                }
            } else {
                levelUp();
                int need = ExpTable.getExpNeededForLevel(level);
                if (exp.get() >= need) {
                    setExp(need - 1);
                    updateSingleStat(MapleStat.EXP, exp.get());
                }
            }
        }
    }

    public void silentPartyUpdate() {
        if (party != null) {
            try {
                getClient().getChannelServer().getWorldInterface().updateParty(party.getId(), PartyOperation.SILENT_UPDATE, new MaplePartyCharacter(MapleCharacter.this));
            } catch (RemoteException e) {
                sqlException(e);
                getClient().getChannelServer().reconnectWorld();
            }
        }
    }

    public boolean isTagged() {
        return tagged == true;
    }

    public void setTagged(boolean tag) {
        tagged = tag;
    }

    public void setPvpPlayerSettingOn() {
        pvp = true;
        TimerManager.getInstance().schedule(new Runnable() {

            public void run() {
                setPvpSetting(false);
                dropMessage("Your time has run out, PVP Setting is set to off.");
            }
        }, 600000);
    }

    public boolean getPvpSetting() {
        return pvp;
    }

    public void setPvpSetting(boolean lol) {
        pvp = lol;
    }

    public boolean isGM() {
        return gmLevel >= 3;
    }

    public int getGMLevel() {
        return gmLevel;
    }

    public boolean hasGmLevel(int level) {
        return gmLevel >= level;
    }

    public MapleInventory getInventory(MapleInventoryType type) {
        return inventory[type.ordinal()];
    }

    public MapleShop getShop() {
        return shop;
    }

    public void setShop(MapleShop shop) {
        this.shop = shop;
    }

    public int getMeso() {
        return meso.get();
    }

    public int getSavedLocation(SavedLocationType type) {
        return savedLocations[type.ordinal()];
    }

    public void saveLocation(SavedLocationType type) {
        savedLocations[type.ordinal()] = getMapId();
    }

    public void clearSavedLocation(SavedLocationType type) {
        savedLocations[type.ordinal()] = -1;
    }

    public void setMeso(int set) {
        meso.set(set);
        updateSingleStat(MapleStat.MESO, set, false);
    }

    public void gainMeso(int gain) {
        gainMeso(gain, true, false, false);
    }

    public void gainMeso(int gain, boolean show) {
        gainMeso(gain, show, false, false);
    }

    public void gainMeso(int gain, boolean show, boolean enableActions) {
        gainMeso(gain, show, enableActions, false);
    }

    public void gainMeso(int gain, boolean show, boolean enableActions, boolean inChat) {
        int newVal;
        long total = ((long) meso.get() + (long) gain);
        if (total >= Integer.MAX_VALUE) {
            meso.set(Integer.MAX_VALUE);
            newVal = Integer.MAX_VALUE;
        } else if (total < 0) {
            meso.set(0);
            newVal = 0;
        } else {
            newVal = meso.addAndGet(gain);
        }
        updateSingleStat(MapleStat.MESO, newVal, enableActions);
        if (show) {
            client.getSession().write(MaplePacketCreator.getShowMesoGain(gain, inChat));
        }
    }

    public void controlMonster(MapleMonster monster, boolean aggro) {
        monster.setController(this);
        controlled.add(monster);
        client.getSession().write(MaplePacketCreator.controlMonster(monster, false, aggro));
    }

    public void stopControllingMonster(MapleMonster monster) {
        controlled.remove(monster);
    }

    public void checkMonsterAggro(MapleMonster monster) {
        if (!monster.isControllerHasAggro()) {
            if (monster.getController() == this) {
                monster.setControllerHasAggro(true);
            } else {
                monster.switchController(this, true);
            }
        }
    }

    public Collection<MapleMonster> getControlledMonsters() {
        return Collections.unmodifiableCollection(controlled);
    }

    public int getNumControlledMonsters() {
        return controlled.size();
    }

    @Override
    public String toString() {
        return "Character: " + this.name;
    }

    public int getAccountID() {
        return accountid;
    }

    public void mobKilled(int id) {
        for (MapleQuestStatus q : quests.values()) {
            if (q.getStatus() == MapleQuestStatus.Status.COMPLETED || q.getQuest().canComplete(this, null)) {
                continue;
            }
            if (q.mobKilled(id) && !(q.getQuest() instanceof MapleCustomQuest)) {
                client.getSession().write(MaplePacketCreator.updateQuestMobKills(q));
                if (q.getQuest().canComplete(this, null)) {
                    client.getSession().write(MaplePacketCreator.getShowQuestCompletion(q.getQuest().getId()));
                }
            }
        }
    }

    public final List<MapleQuestStatus> getStartedQuests() {
        List<MapleQuestStatus> ret = new LinkedList<MapleQuestStatus>();
        for (MapleQuestStatus q : quests.values()) {
            if (q.getStatus().equals(MapleQuestStatus.Status.STARTED) && !(q.getQuest() instanceof MapleCustomQuest)) {
                ret.add(q);
            }
        }
        return Collections.unmodifiableList(ret);
    }

    public final List<MapleQuestStatus> getCompletedQuests() {
        List<MapleQuestStatus> ret = new LinkedList<MapleQuestStatus>();
        for (MapleQuestStatus q : quests.values()) {
            if (q.getStatus().equals(MapleQuestStatus.Status.COMPLETED) && !(q.getQuest() instanceof MapleCustomQuest)) {
                ret.add(q);
            }
        }
        return Collections.unmodifiableList(ret);
    }

    public IPlayerInteractionManager getInteraction() {
        return interaction;
    }

    public void setInteraction(IPlayerInteractionManager box) {
        interaction = box;
    }

    public Map<ISkill, SkillEntry> getSkills() {
        return Collections.unmodifiableMap(skills);
    }

    public void setChaser(MapleCharacter victim) {
        wserv.getChasey().setChaser(this, victim);
    }

    public void dispelSkill(int skillid) {
        LinkedList<MapleBuffStatValueHolder> allBuffs = new LinkedList<MapleBuffStatValueHolder>(effects.values());
        for (MapleBuffStatValueHolder mbsvh : allBuffs) {
            if (skillid == 0) {
                if (mbsvh.effect.isSkill()) {
                    switch (mbsvh.effect.getSourceId()) {
                        case 1004:
                        case 1321007:
                        case 2121005:
                        case 2221005:
                        case 2311006:
                        case 2321003:
                        case 3111002:
                        case 3111005:
                        case 3211002:
                        case 3211005:
                        case 4111002:
                            cancelEffect(mbsvh.effect, false, mbsvh.startTime);
                    }
                }
            } else {
                if (mbsvh.effect.isSkill() && mbsvh.effect.getSourceId() == skillid) {
                    cancelEffect(mbsvh.effect, false, mbsvh.startTime);
                }
            }
        }
    }

    public boolean isActiveBuffedValue(int skillid) {
        LinkedList<MapleBuffStatValueHolder> allBuffs = new LinkedList<MapleBuffStatValueHolder>(effects.values());
        for (MapleBuffStatValueHolder mbsvh : allBuffs) {
            if (mbsvh.effect.isSkill() && mbsvh.effect.getSourceId() == skillid) {
                return true;
            }
        }
        return false;
    }

    public int getSkillLevel(ISkill skill) {
        SkillEntry ret = skills.get(skill);
        if (ret == null) {
            return 0;
        }
        return ret.skillevel;
    }

    public int getMasterLevel(ISkill skill) {
        SkillEntry ret = skills.get(skill);
        if (ret == null) {
            return 0;
        }
        return ret.masterlevel;
    }

    public int getTotalDex() {
        return localdex;
    }

    public int getTotalInt() {
        return localint;
    }

    public int getTotalStr() {
        return localstr;
    }

    public int getTotalLuk() {
        return localluk;
    }

    public int getTotalMagic() {
        return magic;
    }

    public double getSpeedMod() {
        return speedMod;
    }

    public double getJumpMod() {
        return jumpMod;
    }

    public int getTotalWatk() {
        return watk;
    }

    public static int rand(int lbound, int ubound) {
        return (int) ((Math.random() * (ubound - lbound + 1)) + lbound);
    }

    public int getMaxDis(MapleCharacter player) {
        IItem weapon_item = player.getInventory(MapleInventoryType.EQUIPPED).getItem((byte) -11);
        if (weapon_item != null) {
            MapleWeaponType weapon = MapleItemInformationProvider.getInstance().getWeaponType(weapon_item.getItemId());
            if (weapon == MapleWeaponType.SPEAR || weapon == MapleWeaponType.POLE_ARM) {
                maxDis = 106;
            }
            if (weapon == MapleWeaponType.DAGGER || weapon == MapleWeaponType.SWORD1H || weapon == MapleWeaponType.AXE1H || weapon == MapleWeaponType.BLUNT1H) {
                maxDis = 63;
            }
            if (weapon == MapleWeaponType.SWORD2H || weapon == MapleWeaponType.AXE1H || weapon == MapleWeaponType.BLUNT1H) {
                maxDis = 73;
            }
            if (weapon == MapleWeaponType.STAFF || weapon == MapleWeaponType.WAND) {
                maxDis = 51;
            }
            if (weapon == MapleWeaponType.CLAW) {
                skil = SkillFactory.getSkill(4000001);
                skill = player.getSkillLevel(skil);
                if (skill > 0) {
                    maxDis = (skil.getEffect(player.getSkillLevel(skil)).getRange()) + 205;
                } else {
                    maxDis = 205;
                }
            }
            if (weapon == MapleWeaponType.BOW || weapon == MapleWeaponType.CROSSBOW) {
                skil = SkillFactory.getSkill(3000002);
                skill = player.getSkillLevel(skil);
                if (skill > 0) {
                    maxDis = (skil.getEffect(player.getSkillLevel(skil)).getRange()) + 270;
                } else {
                    maxDis = 270;
                }
            }
        }
        return maxDis;
    }

    public int calculateMaxBaseDamage(int watk) {
        int maxbasedamage;
        if (watk == 0) {
            maxbasedamage = 1;
        } else {
            IItem weapon_item = getInventory(MapleInventoryType.EQUIPPED).getItem((byte) -11);
            if (weapon_item != null) {
                MapleWeaponType weapon = MapleItemInformationProvider.getInstance().getWeaponType(weapon_item.getItemId());
                int mainstat;
                int secondarystat;
                if (weapon == MapleWeaponType.BOW || weapon == MapleWeaponType.CROSSBOW) {
                    mainstat = localdex;
                    secondarystat = localstr;
                } else if (getJob().isA(MapleJob.THIEF) && (weapon == MapleWeaponType.CLAW || weapon == MapleWeaponType.DAGGER)) {
                    mainstat = localluk;
                    secondarystat = localdex + localstr;
                } else {
                    mainstat = localstr;
                    secondarystat = localdex;
                }
                maxbasedamage = (int) (((weapon.getMaxDamageMultiplier() * mainstat + secondarystat) / 100.0) * watk);
                maxbasedamage += 10;
            } else {
                maxbasedamage = 0;
            }
        }
        return maxbasedamage;
    }

    public int calculateMinBaseDamage(MapleCharacter player) {
        int minbasedamage = 0;
        int atk = player.getTotalWatk();
        if (atk == 0) {
            minbasedamage = 1;
        } else {
            IItem weapon_item = getInventory(MapleInventoryType.EQUIPPED).getItem((byte) - 11);
            if (weapon_item != null) {
                MapleWeaponType weapon = MapleItemInformationProvider.getInstance().getWeaponType(weapon_item.getItemId());
                if (player.getJob().isA(MapleJob.FIGHTER)) {
                    skil = SkillFactory.getSkill(1100000);
                    skill = player.getSkillLevel(skil);
                    if (skill > 0) {
                        sword = ((skil.getEffect(player.getSkillLevel(skil)).getMastery() * 5 + 10) / 100);
                    } else {
                        sword = 0.1;
                    }
                } else {
                    skil = SkillFactory.getSkill(1200000);
                    skill = player.getSkillLevel(skil);
                    if (skill > 0) {
                        sword = ((skil.getEffect(player.getSkillLevel(skil)).getMastery() * 5 + 10) / 100);
                    } else {
                        sword = 0.1;
                    }
                }
                skil = SkillFactory.getSkill(1100001);
                skill = player.getSkillLevel(skil);
                if (skill > 0) {
                    axe = ((skil.getEffect(player.getSkillLevel(skil)).getMastery() * 5 + 10) / 100);
                } else {
                    axe = 0.1;
                }
                skil = SkillFactory.getSkill(1200001);
                skill = player.getSkillLevel(skil);
                if (skill > 0) {
                    blunt = ((skil.getEffect(player.getSkillLevel(skil)).getMastery() * 5 + 10) / 100);
                } else {
                    blunt = 0.1;
                }
                skil = SkillFactory.getSkill(1300000);
                skill = player.getSkillLevel(skil);
                if (skill > 0) {
                    spear = ((skil.getEffect(player.getSkillLevel(skil)).getMastery() * 5 + 10) / 100);
                } else {
                    spear = 0.1;
                }
                skil = SkillFactory.getSkill(1300001);
                skill = player.getSkillLevel(skil);
                if (skill > 0) {
                    polearm = ((skil.getEffect(player.getSkillLevel(skil)).getMastery() * 5 + 10) / 100);
                } else {
                    polearm = 0.1;
                }
                skil = SkillFactory.getSkill(3200000);
                skill = player.getSkillLevel(skil);
                if (skill > 0) {
                    crossbow = ((skil.getEffect(player.getSkillLevel(skil)).getMastery() * 5 + 10) / 100);
                } else {
                    crossbow = 0.1;
                }
                skil = SkillFactory.getSkill(3100000);
                skill = player.getSkillLevel(skil);
                if (skill > 0) {
                    bow = ((skil.getEffect(player.getSkillLevel(skil)).getMastery() * 5 + 10) / 100);
                } else {
                    bow = 0.1;
                }
                if (weapon == MapleWeaponType.CROSSBOW) {
                    minbasedamage = (int) (localdex * 0.9 * 3.6 * crossbow + localstr) / 100 * (atk + 15);
                }
                if (weapon == MapleWeaponType.BOW) {
                    minbasedamage = (int) (localdex * 0.9 * 3.4 * bow + localstr) / 100 * (atk + 15);
                }
                if (getJob().isA(MapleJob.THIEF) && (weapon == MapleWeaponType.DAGGER)) {
                    minbasedamage = (int) (localluk * 0.9 * 3.6 * dagger + localstr + localdex) / 100 * atk;
                }
                if (!getJob().isA(MapleJob.THIEF) && (weapon == MapleWeaponType.DAGGER)) {
                    minbasedamage = (int) (localstr * 0.9 * 4.0 * dagger + localdex) / 100 * atk;
                }
                if (getJob().isA(MapleJob.THIEF) && (weapon == MapleWeaponType.CLAW)) {
                    minbasedamage = (int) (localluk * 0.9 * 3.6 * claw + localstr + localdex) / 100 * (atk + 15);
                }
                if (weapon == MapleWeaponType.SPEAR) {
                    minbasedamage = (int) (localstr * 0.9 * 3.0 * spear + localdex) / 100 * atk;
                }
                if (weapon == MapleWeaponType.POLE_ARM) {
                    minbasedamage = (int) (localstr * 0.9 * 3.0 * polearm + localdex) / 100 * atk;
                }
                if (weapon == MapleWeaponType.SWORD1H) {
                    minbasedamage = (int) (localstr * 0.9 * 4.0 * sword + localdex) / 100 * atk;
                }
                if (weapon == MapleWeaponType.SWORD2H) {
                    minbasedamage = (int) (localstr * 0.9 * 4.6 * sword + localdex) / 100 * atk;
                }
                if (weapon == MapleWeaponType.AXE1H) {
                    minbasedamage = (int) (localstr * 0.9 * 3.2 * axe + localdex) / 100 * atk;
                }
                if (weapon == MapleWeaponType.BLUNT1H) {
                    minbasedamage = (int) (localstr * 0.9 * 3.2 * blunt + localdex) / 100 * atk;
                }
                if (weapon == MapleWeaponType.AXE2H) {
                    minbasedamage = (int) (localstr * 0.9 * 3.4 * axe + localdex) / 100 * atk;
                }
                if (weapon == MapleWeaponType.BLUNT2H) {
                    minbasedamage = (int) (localstr * 0.9 * 3.4 * blunt + localdex) / 100 * atk;
                }
                if (weapon == MapleWeaponType.STAFF || weapon == MapleWeaponType.WAND) {
                    minbasedamage = (int) (localstr * 0.9 * 3.0 * staffwand + localdex) / 100 * atk;
                }
            }
        }
        return minbasedamage;
    }

    public int getRandomage(MapleCharacter player) {
        int maxdamage = player.getCurrentMaxBaseDamage();
        int mindamage = player.calculateMinBaseDamage(player);
        return MapleCharacter.rand(mindamage, maxdamage);
    }

    public void levelUp() {
        ISkill improvingMaxHP = null;
        int improvingMaxHPLevel = 0;
        ISkill improvingMaxMP = SkillFactory.getSkill(2000001);
        int improvingMaxMPLevel = getSkillLevel(improvingMaxMP);
        remainingAp += 5;
        if (job == MapleJob.BEGINNER) {
            maxhp += rand(12, 16);
            maxmp += rand(10, 12);
        } else if (job.isA(MapleJob.WARRIOR)) {
            improvingMaxHP = SkillFactory.getSkill(1000001);
            improvingMaxHPLevel = getSkillLevel(improvingMaxHP);
            maxhp += rand(24, 28);
            maxmp += rand(4, 6);
        } else if (job.isA(MapleJob.MAGICIAN)) {
            maxhp += rand(10, 14);
            maxmp += rand(22, 24);
        } else if (job.isA(MapleJob.BOWMAN) || job.isA(MapleJob.THIEF) || job.isA(MapleJob.GM)) {
            maxhp += rand(20, 24);
            maxmp += rand(14, 16);
        } else if (job.isA(MapleJob.PIRATE)) {
            improvingMaxHP = SkillFactory.getSkill(5100000);
            improvingMaxHPLevel = getSkillLevel(improvingMaxHP);
            maxhp += rand(22, 28);
            maxmp += rand(18, 23);
        }
        if (improvingMaxHPLevel > 0) {
            maxhp += improvingMaxHP.getEffect(improvingMaxHPLevel).getX();
        }
        if (improvingMaxMPLevel > 0) {
            maxmp += improvingMaxMP.getEffect(improvingMaxMPLevel).getX();
        }
        maxmp += getTotalInt() / 10;
        exp.addAndGet(-ExpTable.getExpNeededForLevel(level));
        level += 1;
        maxhp = Math.min(30000, maxhp);
        maxmp = Math.min(30000, maxmp);
        setHp(maxhp);
        setMp(maxmp);
        List<Pair<MapleStat, Integer>> statup = new ArrayList<Pair<MapleStat, Integer>>(8);
        statup.add(new Pair<MapleStat, Integer>(MapleStat.AVAILABLEAP, Integer.valueOf(remainingAp)));
        statup.add(new Pair<MapleStat, Integer>(MapleStat.MAXHP, Integer.valueOf(maxhp)));
        statup.add(new Pair<MapleStat, Integer>(MapleStat.MAXMP, Integer.valueOf(maxmp)));
        statup.add(new Pair<MapleStat, Integer>(MapleStat.HP, getHp()));
        statup.add(new Pair<MapleStat, Integer>(MapleStat.MP, getMp()));
        statup.add(new Pair<MapleStat, Integer>(MapleStat.EXP, Integer.valueOf(exp.get())));
        statup.add(new Pair<MapleStat, Integer>(MapleStat.LEVEL, Integer.valueOf(level)));
        if (job != MapleJob.BEGINNER) {
            remainingSp += 3;
            statup.add(new Pair<MapleStat, Integer>(MapleStat.AVAILABLESP, Integer.valueOf(remainingSp)));
        }
        getClient().getSession().write(MaplePacketCreator.updatePlayerStats(statup));
        if (getReborns() > 20 && getLevel() > 130) {
            getMap().broadcastMessage(this, MaplePacketCreator.showLevelup(getId()), false);
        } else if (getReborns() <= 20) {
            getMap().broadcastMessage(this, MaplePacketCreator.showLevelup(getId()), false);
        }
        recalcLocalStats();
        silentPartyUpdate();
        guildUpdate();
        if (getReborns() == 0) {
            switch (level) {
                case 15:
                    sendHint("You could do #ea lot more damage#n if you equip yourself with #e@shop", 450, 0);
                    break;
                case 30:
                    sendHint("Congratulations on level 30! You can now #eadvance your job#n by using #e@job", 450, 0);
                    break;
                case 50:
                    sendHint("Are you #econstantly disconnecting?#n Maybe you haven't added in the #ewz edits.\r\n#rYou need to click yes to overwrite when you're putting in the client files", 500, 0);
                    break;
                case 60:
                    sendHint("Did you know you can #eadd stats easily#n with #e@<stat>#n?\r\nIf you want to add 20 AP to str you could do #e@str 20#n.\r\nYou can also add #eall available AP#n to a stat using #e@<stat> all#n", 400, 0);
                    break;
                case 70:
                    sendHint("You should #eboost up your damage#n with some cool buffs.\r\nYou can #epersonalize your buffs#n using #e@buff set#n and use them with #e@buff", 425, 0);
                    break;
                case 90:
                    sendHint("Feeling lucky? Got some spare #yellow wish tickets#? Why not try our #ecasino?#n Just use #e@casino", 350, 0);
                    break;
                case 120:
                    sendHint("Congratulations on level 120!\r\nIf you're having a hard time training, why not try our #espawner?#n\r\nJust use #e@spawner", 350, 0);
                    break;
                case 150:
                    sendHint("Do you want some #eeasy, fast and rare new items#n?\r\nJust check out #eHelena at Henesys#n and exchange your #evotepoints#n.\r\nYou can acquire votepoints by #evoting on the website#n (#evoidms.com#n)", 400, 0);
                    break;
                case 200:
                    sendHint("You can now #erebirth!#n\r\nUse #e@rebirth#n to go back to level 2 but keep your stats", 350, 0);
                    break;
            }
        }
        if (getPartyId() != -1) {
            MapleCharacter leader = getClient().getChannelServer().getPlayerStorage().getCharacterById(getParty().getLeader().getId());
            if (leader != null && (leader.haveItem(4002002, 1, false, true) || leader.haveItem(4031560, 1, false, true)) && leader.stampOn() && leader.getMapId() == getMapId() && level > 199 && leader.getId() != getId()) {
                doReborn();
            }
        }
        if (level > 199) {
            if ((haveItem(4002002, 1, false, true) || haveItem(4031560, 1, false, true)) && stampOn()) {
                doReborn();
                changeJob(MapleJob.getById(getStampJob()));
            } else {
                setExp(0);
                updateSingleStat(MapleStat.EXP, 0);
            }
        }
    }

    public void changeKeybinding(int key, MapleKeyBinding keybinding) {
        if (keybinding.getType() != 0) {
            keymap.put(Integer.valueOf(key), keybinding);
        } else {
            keymap.remove(Integer.valueOf(key));
        }
    }

    public void sendKeymap() {
        getClient().getSession().write(MaplePacketCreator.getKeymap(keymap));
    }

    public void sendMacros() {
        boolean macros = false;
        for (int i = 0; i < 5; i++) {
            if (skillMacros[i] != null) {
                macros = true;
            }
        }
        if (macros) {
            getClient().getSession().write(MaplePacketCreator.getMacros(skillMacros));
        }
    }

    public void updateMacros(int position, SkillMacro updateMacro) {
        skillMacros[position] = updateMacro;
    }

    public void tempban(String reason, Calendar duration, int greason) {
        tempban(reason, duration, greason, client.getAccID());
        client.getSession().write(MaplePacketCreator.sendGMPolice(greason, reason, (int) (duration.getTimeInMillis() / 1000))); //put duration as seconds
        TimerManager.getInstance().schedule(new Runnable() {

            public void run() {
                client.getSession().close();
            }
        }, 10000);
    }

    public static boolean tempban(String reason, Calendar duration, int greason, int accountid) {
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("UPDATE accounts SET tempban = ?, banreason = ?, greason = ? WHERE id = ?");
            Timestamp TS = new Timestamp(duration.getTimeInMillis());
            ps.setTimestamp(1, TS);
            ps.setString(2, reason);
            ps.setInt(3, greason);
            ps.setInt(4, accountid);
            ps.executeUpdate();
            ps.close();
            return true;
        } catch (SQLException e) {
            sqlException(e);
        }
        return false;
    }

    public void ban(String reason, boolean permBan) {
        if (!client.isGuest()) {
            try {
                Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps;
                if (permBan) {
                    getClient().banMacs();
                    ps = con.prepareStatement("INSERT INTO ipbans VALUES (DEFAULT, ?)");
                    String[] ipSplit = client.getSession().getRemoteAddress().toString().split(":");
                    ps.setString(1, ipSplit[0]);
                    ps.executeUpdate();
                    ps.close();
                }
                ps = con.prepareStatement("UPDATE accounts SET banned = ?, banreason = ?, greason = ? WHERE id = ?");
                ps.setInt(1, 1);
                ps.setString(2, reason);
                ps.setInt(3, 12);
                ps.setInt(4, accountid);
                ps.executeUpdate();
                ps.close();
                setBanned();
            } catch (SQLException e) {
                sqlException(e);
            }
        }
        client.getSession().write(MaplePacketCreator.getNPCTalk(2100, (byte) 0, "You have been banned. Here is your reason:\r\n\r\n#r" + reason + "#k\r\n\r\nIf you think you have been unfairly banned, feel free to post an unban request at #bhttp://forum.voidms.com#k.", "00 00"));
        TimerManager.getInstance().schedule(new Runnable() {

            public void run() {
                client.getSession().close();
            }
        }, 10 * 1000);

    }

    public static boolean ban(String id, String reason, boolean accountId) {
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps;
            if (id.matches("/[0-9]{1,3}\\..*")) {
                ps = con.prepareStatement("INSERT INTO ipbans VALUES (DEFAULT, ?)");
                ps.setString(1, id);
                ps.executeUpdate();
                ps.close();
            }
            if (accountId) {
                ps = con.prepareStatement("SELECT id FROM accounts WHERE name = ?");
            } else {
                ps = con.prepareStatement("SELECT accountid FROM characters WHERE name = ?");
            }
            boolean ret = false;
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ps = con.prepareStatement("UPDATE accounts SET banned = 1, banreason = ? WHERE id = ?");
                ps.setString(1, reason);
                ps.setInt(2, rs.getInt(1));
                ps.executeUpdate();
                ret = true;
            }
            rs.close();
            ps.close();
            return ret;
        } catch (SQLException e) {
            sqlException(e);
        }
        return false;
    }

    public static int getAccIdFromCNAME(String name) {
        Connection con = DatabaseConnection.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT accountid FROM characters WHERE name = ?");
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                rs.close();
                ps.close();
                return -1;
            }
            int id_ = rs.getInt("accountid");
            rs.close();
            ps.close();
            return id_;
        } catch (SQLException e) {
            sqlException(e);
        }
        return -1;
    }

    @Override
    public int getObjectId() {
        return getId();
    }

    @Override
    public void setObjectId(int id) {
        throw new UnsupportedOperationException();
    }

    public MapleStorage getStorage() {
        return storage;
    }

    public int getCurrentMaxHp() {
        return localmaxhp;
    }

    public int getCurrentMaxMp() {
        return localmaxmp;
    }

    public int getCurrentMaxBaseDamage() {
        return localmaxbasedamage;
    }

    public int getTotalMdef() {
        return mdef;
    }

    public int getTotalWdef() {
        return wdef;
    }

    public void addVisibleMapObject(MapleMapObject mo) {
        visibleMapObjects.add(mo);
    }

    public void removeVisibleMapObject(MapleMapObject mo) {
        visibleMapObjects.remove(mo);
    }

    public boolean isMapObjectVisible(MapleMapObject mo) {
        return visibleMapObjects.contains(mo);
    }

    public Collection<MapleMapObject> getVisibleMapObjects() {
        return Collections.unmodifiableCollection(visibleMapObjects);
    }

    public boolean isAlive() {
        return this.hp > 0;
    }

    public boolean isDead() {
        return this.hp <= 0;
    }

    @Override
    public void sendDestroyData(MapleClient client) {
        client.getSession().write(MaplePacketCreator.removePlayerFromMap(this.getObjectId()));
    }

    @Override
    public void sendSpawnData(MapleClient client) {
        if ((this.isHidden() && client.getPlayer().isGM()) || !this.isHidden()) {
            client.getSession().write(MaplePacketCreator.spawnPlayerMapobject(this));
            for (int i = 0; i < 3; i++) {
                if (pets[i] != null) {
                    client.getSession().write(MaplePacketCreator.showPet(this, pets[i], false, false));
                } else {
                    break;
                }
            }
        }
    }

    private void recalcLocalStats() {
        int oldmaxhp = localmaxhp;
        localmaxhp = getMaxHp();
        localmaxmp = getMaxMp();
        localdex = getDex();
        localint = getInt();
        localstr = getStr();
        localluk = getLuk();
        int speed = 100;
        int jump = 100;
        magic = localint;
        watk = 0;
        wdef = 0;
        mdef = 0;
        for (IItem item : getInventory(MapleInventoryType.EQUIPPED)) {
            IEquip equip = (IEquip) item;
            localmaxhp += equip.getHp();
            localmaxmp += equip.getMp();
            localdex += equip.getDex();
            localint += equip.getInt();
            localstr += equip.getStr();
            localluk += equip.getLuk();
            magic += equip.getMatk() + equip.getInt();
            watk += equip.getWatk();
            speed += equip.getSpeed();
            jump += equip.getJump();
            wdef += equip.getWdef();
            mdef += equip.getMdef();
        }
        magic = Math.min(magic, 2000);
        Integer hbhp = getBuffedValue(MapleBuffStat.HYPERBODYHP);
        if (hbhp != null) {
            localmaxhp += (hbhp.doubleValue() / 100) * localmaxhp;
        }
        Integer hbmp = getBuffedValue(MapleBuffStat.HYPERBODYMP);
        if (hbmp != null) {
            localmaxmp += (hbmp.doubleValue() / 100) * localmaxmp;
        }
        localmaxhp = Math.min(30000, localmaxhp);
        localmaxmp = Math.min(30000, localmaxmp);
        Integer watkbuff = getBuffedValue(MapleBuffStat.WATK);
        if (watkbuff != null) {
            watk += watkbuff.intValue();
        }
        if (job.isA(MapleJob.BOWMAN)) {
            ISkill expert = null;
            if (job.isA(MapleJob.CROSSBOWMASTER)) {
                expert = SkillFactory.getSkill(3220004);
            } else if (job.isA(MapleJob.BOWMASTER)) {
                expert = SkillFactory.getSkill(3120005);
            }
            if (expert != null) {
                int boostLevel = getSkillLevel(expert);
                if (boostLevel > 0) {
                    watk += expert.getEffect(boostLevel).getX();
                }
            }
        }
        Integer matkbuff = getBuffedValue(MapleBuffStat.MATK);
        if (matkbuff != null) {
            magic += matkbuff.intValue();
        }
        Integer speedbuff = getBuffedValue(MapleBuffStat.SPEED);
        if (speedbuff != null) {
            speed += speedbuff.intValue();
        }
        Integer jumpbuff = getBuffedValue(MapleBuffStat.JUMP);
        if (jumpbuff != null) {
            jump += jumpbuff.intValue();
        }
        if (speed > 140) {
            speed = 140;
        }
        if (jump > 123) {
            jump = 123;
        }
        speedMod = speed / 100.0;
        jumpMod = jump / 100.0;
        Integer mount = getBuffedValue(MapleBuffStat.MONSTER_RIDING);
        if (mount != null) {
            jumpMod = 1.23;
            switch (mount.intValue()) {
                case 1:
                    speedMod = 1.5;
                    break;
                case 2:
                    speedMod = 1.7;
                    break;
                case 3:
                    speedMod = 1.8;
                    break;
                case 5:
                    speedMod = 1.0;
                    jumpMod = 1.0;
                    break;
                default:
                    speedMod = 2.0;
            }
        }
        localmaxbasedamage = calculateMaxBaseDamage(watk);
        if (oldmaxhp != 0 && oldmaxhp != localmaxhp) {
            updatePartyMemberHP();
        }
    }

    public void Mount(int id, int skillid) {
        maplemount = new MapleMount(this, id, skillid);
    }

    public MapleMount getMount() {
        return maplemount;
    }

    public void equipChanged() {
        getMap().broadcastMessage(this, MaplePacketCreator.updateCharLook(this), false);
        recalcLocalStats();
        enforceMaxHpMp();
        if (getClient().getPlayer().getMessenger() != null) {
            WorldChannelInterface wci = ChannelServer.getInstance(getClient().getChannel()).getWorldInterface();
            try {
                wci.updateMessenger(getClient().getPlayer().getMessenger().getId(), getClient().getPlayer().getName(), getClient().getChannel());
            } catch (RemoteException e) {
                getClient().getChannelServer().reconnectWorld();
            }
        }
    }

    public MaplePet getPet(int index) {
        return pets[index];
    }

    public void addPet(MaplePet pet) {
        for (int i = 0; i < 3; i++) {
            if (pets[i] == null) {
                pets[i] = pet;
                return;
            }
        }
    }

    public void removePet(MaplePet pet, boolean shift_left) {
        int slot = -1;
        for (int i = 0; i < 3; i++) {
            if (pets[i] != null) {
                if (pets[i].getUniqueId() == pet.getUniqueId()) {
                    pets[i] = null;
                    slot = i;
                    break;
                }
            }
        }
        if (shift_left) {
            if (slot > -1) {
                for (int i = slot; i < 3; i++) {
                    if (i != 2) {
                        pets[i] = pets[i + 1];
                    } else {
                        pets[i] = null;
                    }
                }
            }
        }
    }

    public int getNoPets() {
        int ret = 0;
        for (int i = 0; i < 3; i++) {
            if (pets[i] != null) {
                ret++;
            } else {
                break;
            }
        }
        return ret;
    }

    public int getPetIndex(MaplePet pet) {
        for (int i = 0; i < 3; i++) {
            if (pets[i] != null) {
                if (pets[i].getUniqueId() == pet.getUniqueId()) {
                    return i;
                }
            } else {
                break;
            }
        }
        return -1;
    }

    public int getPetIndex(int petId) {
        for (int i = 0; i < 3; i++) {
            if (pets[i] != null) {
                if (pets[i].getUniqueId() == petId) {
                    return i;
                }
            } else {
                break;
            }
        }
        return -1;
    }

    public int getNextEmptyPetIndex() {
        for (int i = 0; i < 3; i++) {
            if (pets[i] == null) {
                return i;
            }
        }
        return 3;
    }

    public MaplePet[] getPets() {
        return pets;
    }

    public void unequipAllPets() {
        for (int i = 0; i < 3; i++) {
            if (pets[i] != null) {
                unequipPet(pets[i], true);
                cancelFullnessSchedule(i);
            } else {
                break;
            }
        }
    }

    public void unequipPet(MaplePet pet, boolean shift_left) {
        unequipPet(pet, shift_left, false);
    }

    public void unequipPet(MaplePet pet, boolean shift_left, boolean hunger) {
        cancelFullnessSchedule(getPetIndex(pet));
        for (int i = 0; i < 3; i++) {
            if (pets[i] != null) {
                pets[i].saveToDb();
            }
        }
        getMap().broadcastMessage(this, MaplePacketCreator.showPet(this, pet, true, hunger), true);
        List<Pair<MapleStat, Integer>> stats = new ArrayList<Pair<MapleStat, Integer>>();
        stats.add(new Pair<MapleStat, Integer>(MapleStat.PET, Integer.valueOf(0)));
        getClient().getSession().write(MaplePacketCreator.petStatUpdate(this));
        getClient().getSession().write(MaplePacketCreator.enableActions());
        removePet(pet, shift_left);
    }

    public void shiftPetsRight() {
        if (pets[2] == null) {
            pets[2] = pets[1];
            pets[1] = pets[0];
            pets[0] = null;
        }
    }

    public FameStatus canGiveFame(MapleCharacter from) {
        if (lastfametime >= System.currentTimeMillis() - 60 * 60 * 24 * 1000) {
            return FameStatus.NOT_TODAY;
        } else if (lastmonthfameids.contains(Integer.valueOf(from.getId()))) {
            return FameStatus.NOT_THIS_MONTH;
        } else {
            return FameStatus.OK;
        }
    }

    public void hasGivenFame(MapleCharacter to) {
        lastfametime = System.currentTimeMillis();
        lastmonthfameids.add(Integer.valueOf(to.getId()));
        Connection con = DatabaseConnection.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("INSERT INTO famelog (characterid, characterid_to) VALUES (?, ?)");
            ps.setInt(1, getId());
            ps.setInt(2, to.getId());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            sqlException(e);
        }
    }

    public MapleParty getParty() {
        return party;
    }

    public int getPartyId() {
        return (party != null ? party.getId() : -1);
    }

    public int getWorld() {
        return world;
    }

    public void setWorld(int world) {
        this.world = world;
    }

    public void setParty(MapleParty party) {
        this.party = party;
    }

    public MapleTrade getTrade() {
        return trade;
    }

    public void setTrade(MapleTrade trade) {
        this.trade = trade;
    }

    public EventInstanceManager getEventInstance() {
        return eventInstance;
    }

    public void setEventInstance(EventInstanceManager eventInstance) {
        this.eventInstance = eventInstance;
    }

    public void addDoor(MapleDoor door) {
        doors.add(door);
    }

    public void clearDoors() {
        doors.clear();
    }

    public List<MapleDoor> getDoors() {
        return new ArrayList<MapleDoor>(doors);
    }

    public boolean canDoor() {
        return canDoor;
    }

    public void disableDoor() {
        canDoor = false;
        TimerManager tMan = TimerManager.getInstance();
        tMan.schedule(new Runnable() {

            @Override
            public void run() {
                canDoor = true;
            }
        }, 5000);
    }

    public Map<Integer, MapleSummon> getSummons() {
        return summons;
    }

    public int getChair() {
        return chair;
    }

    public int getItemEffect() {
        return itemEffect;
    }

    public void setChair(int chair) {
        this.chair = chair;
    }

    public void setItemEffect(int itemEffect) {
        this.itemEffect = itemEffect;
    }

    @Override
    public Collection<MapleInventory> allInventories() {
        return Arrays.asList(inventory);
    }

    @Override
    public MapleMapObjectType getType() {
        return MapleMapObjectType.PLAYER;
    }

    public int getGuildId() {
        return guildid;
    }

    public int getGuildRank() {
        return guildrank;
    }

    public void setGuildId(int _id) {
        guildid = _id;
        if (guildid > 0) {
            if (mgc == null) {
                mgc = new MapleGuildCharacter(this);
            } else {
                mgc.setGuildId(guildid);
            }
        } else {
            mgc = null;
        }
    }

    public void setGuildRank(int _rank) {
        guildrank = _rank;
        if (mgc != null) {
            mgc.setGuildRank(_rank);
        }
    }

    public void setAllianceRank(int rank) {
        allianceRank = rank;
        if (mgc != null) {
            mgc.setAllianceRank(rank);
        }
    }

    public int getAllianceRank() {
        return this.allianceRank;
    }

    public MapleGuildCharacter getMGC() {
        return mgc;
    }

    public void guildUpdate() {
        if (this.guildid <= 0) {
            return;
        }
        mgc.setLevel(this.level);
        mgc.setJobId(this.job.getId());
        try {
            this.client.getChannelServer().getWorldInterface().memberLevelJobUpdate(this.mgc);
            int allianceId = getGuild().getAllianceId();
            if (allianceId > 0) {
                client.getChannelServer().getWorldInterface().allianceMessage(allianceId, MaplePacketCreator.updateAllianceJobLevel(this), getId(), -1);
            }
        } catch (RemoteException e) {
            sqlException(e);
        }
    }
    private NumberFormat nf = new DecimalFormat("#,###,###,###");

    public String guildCost() {
        return nf.format(MapleGuild.CREATE_GUILD_COST);
    }

    public String emblemCost() {
        return nf.format(MapleGuild.CHANGE_EMBLEM_COST);
    }

    public String capacityCost() {
        return nf.format(MapleGuild.INCREASE_CAPACITY_COST);
    }

    public void genericGuildMessage(int code) {
        this.client.getSession().write(MaplePacketCreator.genericGuildMessage((byte) code));
    }

    public void disbandGuild() {
        if (guildid <= 0 || guildrank != 1) {
            return;
        }
        try {
            client.getChannelServer().getWorldInterface().disbandGuild(this.guildid);
        } catch (RemoteException e) {
            client.getChannelServer().reconnectWorld();
            sqlException(e);
        }
    }

    public void increaseGuildCapacity() {
        if (this.guildid <= 0) {
            return;
        }
        if (this.getMeso() < MapleGuild.INCREASE_CAPACITY_COST) {
            client.getSession().write(MaplePacketCreator.serverNotice(1, "You do not have enough mesos."));
            return;
        }
        try {
            client.getChannelServer().getWorldInterface().increaseGuildCapacity(this.guildid);
        } catch (RemoteException e) {
            client.getChannelServer().reconnectWorld();
            sqlException(e);
            return;
        }
        this.gainMeso(-MapleGuild.INCREASE_CAPACITY_COST, true, false, true);
    }

    public void saveGuildStatus() {
        Connection con = DatabaseConnection.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("UPDATE characters SET guildid = ?, guildrank = ?, allianceRank = ? WHERE id = ?");
            ps.setInt(1, this.guildid);
            ps.setInt(2, this.guildrank);
            ps.setInt(3, this.allianceRank);
            ps.setInt(4, this.id);
            ps.execute();
            ps.close();
        } catch (SQLException e) {
            sqlException(e);
        }
    }

    public int getCSPoints(int type) {
//        switch (type) {
//            case 1:
//                return this.paypalnx;
//            case 2:
//                return this.maplepoints;
//            case 4:
//                return this.cardnx;
//            default:
//                return 0;
//        }
        return 80085;
    }

    public void modifyCSPoints(int type, int quantity) {
//        switch (type) {
//            case 1:
//                this.paypalnx += quantity;
//                break;
//            case 2:
//                this.maplepoints += quantity;
//                break;
//            case 4:
//                this.cardnx += quantity;
//                break;
//        }
    }

    public void setNpc(boolean foo) {
        this.cannpc = foo;
    }

    public boolean canNpc() {
        return cannpc == true;
    }

    public boolean gainShopItem(int ID, int quantity) {
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        return MapleInventoryManipulator.addFromDrop(c, ii.randomizeStats(c, (Equip) ii.getEquipById(ID)), true, true);
    }

    public boolean haveItem(int itemid, int quantity, boolean checkEquipped, boolean greaterOrEquals) {
        int possesed = getItemQuantity(itemid, checkEquipped);
        if (greaterOrEquals) {
            return possesed >= quantity;
        } else {
            return possesed == quantity;
        }
    }

    public boolean haveItemEquipped(int itemid) {
        if (getInventory(MapleInventoryType.EQUIPPED).findById(itemid) != null) {
            return true;
        }
        return false;
    }

    private static class MapleBuffStatValueHolder {

        public MapleStatEffect effect;
        public long startTime;
        public int value;
        public ScheduledFuture<?> schedule;

        public MapleBuffStatValueHolder(MapleStatEffect effect, long startTime, ScheduledFuture<?> schedule, int value) {
            super();
            this.effect = effect;
            this.startTime = startTime;
            this.schedule = schedule;
            this.value = value;
        }
    }

    public static class MapleCoolDownValueHolder {

        public int skillId;
        public long startTime;
        public long length;
        public ScheduledFuture<?> timer;

        public MapleCoolDownValueHolder(int skillId, long startTime, long length, ScheduledFuture<?> timer) {
            super();
            this.skillId = skillId;
            this.startTime = startTime;
            this.length = length;
            this.timer = timer;
        }
    }

    public static class SkillEntry {

        public int skillevel;
        public int masterlevel;

        public SkillEntry(int skillevel, int masterlevel) {
            this.skillevel = skillevel;
            this.masterlevel = masterlevel;
        }

        @Override
        public String toString() {
            return skillevel + ":" + masterlevel;
        }
    }

    public enum FameStatus {

        OK, NOT_TODAY, NOT_THIS_MONTH
    }

    public int getBuddyCapacity() {
        return buddylist.getCapacity();
    }

    public void setBuddyCapacity(int capacity) {
        buddylist.setCapacity(capacity);
        client.getSession().write(MaplePacketCreator.updateBuddyCapacity(capacity));
    }

    public MapleMessenger getMessenger() {
        return messenger;
    }

    public void setMessenger(MapleMessenger messenger) {
        this.messenger = messenger;
    }

    public void checkMessenger() {
        if (messenger != null && messengerposition < 4 && messengerposition > -1) {
            try {
                WorldChannelInterface wci = ChannelServer.getInstance(client.getChannel()).getWorldInterface();
                MapleMessengerCharacter messengerplayer = new MapleMessengerCharacter(client.getPlayer(), messengerposition);
                wci.silentJoinMessenger(messenger.getId(), messengerplayer, messengerposition);
                wci.updateMessenger(getClient().getPlayer().getMessenger().getId(), getClient().getPlayer().getName(), getClient().getChannel());
            } catch (RemoteException e) {
                client.getChannelServer().reconnectWorld();
            }
        }
    }

    public int getMessengerPosition() {
        return messengerposition;
    }

    public void setMessengerPosition(int position) {
        this.messengerposition = position;
    }

    public int hasEXPCard() {
        return 1;
    }

    public void setInCS(boolean yesno) {
        this.incs = yesno;
    }

    public boolean inCS() {
        return this.incs;
    }

    public void setInMTS(boolean yesno) {
        this.inmts = yesno;
    }

    public boolean inMTS() {
        return this.inmts;
    }

    public void addCooldown(int skillId, long startTime, long length, ScheduledFuture<?> timer) {
        if (this.coolDowns.containsKey(Integer.valueOf(skillId))) {
            this.coolDowns.remove(skillId);
        }
        this.coolDowns.put(Integer.valueOf(skillId), new MapleCoolDownValueHolder(skillId, startTime, length, timer));
    }

    public void removeCooldown(int skillId) {
        if (this.coolDowns.containsKey(Integer.valueOf(skillId))) {
            this.coolDowns.remove(Integer.valueOf(skillId));
            client.getSession().write(MaplePacketCreator.skillCooldown(skillId, 0));
        }
    }

    public boolean skillisCooling(int skillId) {
        return this.coolDowns.containsKey(Integer.valueOf(skillId));
    }

    public void giveCoolDowns(final List<PlayerCoolDownValueHolder> cooldowns) {
        for (PlayerCoolDownValueHolder cooldown : cooldowns) {
            int time = (int) ((cooldown.length + cooldown.startTime) - System.currentTimeMillis());
            ScheduledFuture<?> timer = TimerManager.getInstance().schedule(new CancelCooldownAction(this, cooldown.skillId), time);
            addCooldown(cooldown.skillId, System.currentTimeMillis(), time, timer);
        }
    }

    public void giveCoolDowns(final int skillid, long starttime, long length) {
        int time = (int) ((length + starttime) - System.currentTimeMillis());
        ScheduledFuture<?> timer = TimerManager.getInstance().schedule(new CancelCooldownAction(this, skillid), time);
        addCooldown(skillid, System.currentTimeMillis(), time, timer);
    }

    public List<PlayerCoolDownValueHolder> getAllCooldowns() {
        List<PlayerCoolDownValueHolder> ret = new ArrayList<PlayerCoolDownValueHolder>();
        for (MapleCoolDownValueHolder mcdvh : coolDowns.values()) {
            ret.add(new PlayerCoolDownValueHolder(mcdvh.skillId, mcdvh.startTime, mcdvh.length));
        }
        return ret;
    }

    public static class CancelCooldownAction implements Runnable {

        private int skillId;
        private WeakReference<MapleCharacter> target;

        public CancelCooldownAction(MapleCharacter target, int skillId) {
            this.target = new WeakReference<MapleCharacter>(target);
            this.skillId = skillId;
        }

        @Override
        public void run() {
            MapleCharacter realTarget = target.get();
            if (realTarget != null) {
                realTarget.removeCooldown(skillId);
            }
        }
    }

    public void giveDebuff(MapleDisease disease, MobSkill skill) {
        synchronized (diseases) {
            if (isAlive() && !isActiveBuffedValue(2321005) && !diseases.contains(disease) && diseases.size() < 2) {
                diseases.add(disease);
                List<Pair<MapleDisease, Integer>> debuff = Collections.singletonList(new Pair<MapleDisease, Integer>(disease, Integer.valueOf(skill.getX())));
                long mask = 0;
                for (Pair<MapleDisease, Integer> statup : debuff) {
                    mask |= statup.getLeft().getValue();
                }
                getClient().getSession().write(MaplePacketCreator.giveDebuff(mask, debuff, skill));
                getMap().broadcastMessage(this, MaplePacketCreator.giveForeignDebuff(id, mask, skill), false);
                if (isAlive()) {
                    final MapleCharacter character = this;
                    final MapleDisease disease_ = disease;
                    TimerManager.getInstance().schedule(new Runnable() {

                        @Override
                        public void run() {
                            if (character.diseases.contains(disease_)) {
                                dispelDebuff(disease_);
                            }
                        }
                    }, skill.getDuration());
                }
            }
        }
    }

    public void dispelDebuff(MapleDisease debuff) {
        if (diseases.contains(debuff)) {
            diseases.remove(debuff);
            long mask = debuff.getValue();
            getClient().getSession().write(MaplePacketCreator.cancelDebuff(mask));
            getMap().broadcastMessage(this, MaplePacketCreator.cancelForeignDebuff(id, mask), false);
        }
    }

    public MapleCharacter getPartner() {
        return client.getChannelServer().getPlayerStorage().getCharacterById(partnerid);
    }

    public void dispelDebuffs() {
        MapleDisease[] disease = {MapleDisease.POISON, MapleDisease.SLOW, MapleDisease.SEAL, MapleDisease.DARKNESS, MapleDisease.WEAKEN, MapleDisease.CURSE};
        for (int i = 0; i < diseases.size(); i++) {
            if (diseases.contains(disease[i])) {
                diseases.remove(disease);
                long mask = 0;
                for (MapleDisease statup : diseases) {
                    mask |= statup.getValue();
                }
                getClient().getSession().write(MaplePacketCreator.cancelDebuff(mask));
                getMap().broadcastMessage(this, MaplePacketCreator.cancelForeignDebuff(id, mask), false);
            }
        }
    }

    public static boolean isRare(int id) {
        return MapleItemInformationProvider.isRare(id);
    }

    public void setLevel(int level) {
        if (level <= 0) {
            level = 1;
        }
        this.level = level;
    }

    public void setMap(int PmapId) {
        this.mapid = PmapId;
    }

    public List<Integer> getQuestItemsToShow() {
        Set<Integer> delta = new HashSet<Integer>();
        for (Map.Entry<MapleQuest, MapleQuestStatus> questEntry : this.quests.entrySet()) {
            if (questEntry.getValue().getStatus() != MapleQuestStatus.Status.STARTED) {
                delta.addAll(questEntry.getKey().getQuestItemsToShowOnlyIfQuestIsActivated());
            }
        }
        List<Integer> returnThis = new ArrayList<Integer>();
        returnThis.addAll(delta);
        return Collections.unmodifiableList(returnThis);
    }

    public void sendNote(int to, String msg) throws SQLException {
        Connection con = DatabaseConnection.getConnection();
        PreparedStatement ps = con.prepareStatement("INSERT INTO notes (`to`, `from`, `message`, `timestamp`) VALUES (?, ?, ?, ?)");
        ps.setInt(1, to);
        ps.setString(2, getName());
        ps.setString(3, msg);
        ps.setLong(4, System.currentTimeMillis());
        ps.executeUpdate();
        ps.close();
    }

    public void showNote() throws SQLException {
        Connection con = DatabaseConnection.getConnection();
        PreparedStatement ps = con.prepareStatement("SELECT * FROM notes WHERE `to`=?", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ps.setInt(1, getId());
        ResultSet rs = ps.executeQuery();
        rs.last();
        int count = rs.getRow();
        rs.first();
        client.getSession().write(MaplePacketCreator.showNotes(rs, count));
        ps.close();
    }

    public boolean isMarried() {
        return married;
    }

    public void setMarried(boolean status) {
        this.married = status;
    }

    public int getMarriageQuestLevel() {
        return marriageQuestLevel;
    }

    public void setMarriageQuestLevel(int nf) {
        marriageQuestLevel = nf;
    }

    public void addMarriageQuestLevel() {
        marriageQuestLevel++;
    }

    public void subtractMarriageQuestLevel() {
        marriageQuestLevel -= 1;
    }

    public void setZakumLevel(int level) {
        this.zakumLvl = level;
    }

    public int getZakumLevel() {
        return this.zakumLvl;
    }

    public void addZakumLevel() {
        this.zakumLvl += 1;
    }

    public void subtractZakumLevel() {
        this.zakumLvl -= 1;
    }

    public void setPartnerId(int pem) {
        this.partnerid = pem;
    }

    public int getPartnerId() {
        return partnerid;
    }

    public void checkBerserk() {
        if (BerserkSchedule != null) {
            BerserkSchedule.cancel(false);
        }
        final MapleCharacter chr = this;
        ISkill BerserkX = SkillFactory.getSkill(1320006);
        final int skilllevel = getSkillLevel(BerserkX);
        if (!chr.getJob().equals(MapleJob.DARKKNIGHT)) {
            Berserk = false;
        }
        if (chr.getJob().equals(MapleJob.DARKKNIGHT) && skilllevel >= 1) {
            MapleStatEffect ampStat = BerserkX.getEffect(skilllevel);
            int x = ampStat.getX();
            int HP = chr.getHp();
            int MHP = chr.getMaxHp();
            int ratio = HP * 100 / MHP;
            if (ratio > x) {
                Berserk = false;
            } else {
                Berserk = true;
            }

            BerserkSchedule = TimerManager.getInstance().register(new Runnable() {

                @Override
                public void run() {

                    getClient().getSession().write(MaplePacketCreator.showOwnBerserk(skilllevel, Berserk));
                    getMap().broadcastMessage(MapleCharacter.this, MaplePacketCreator.showBerserk(getId(), skilllevel, Berserk), false);
                }
            }, 5000, 3000);
        }
    }

    private void prepareBeholderEffect() {
        if (beholderHealingSchedule != null) {
            beholderHealingSchedule.cancel(false);
        }
        if (beholderBuffSchedule != null) {
            beholderBuffSchedule.cancel(false);
        }

        ISkill bHealing = SkillFactory.getSkill(1320008);
        if (getSkillLevel(bHealing) > 0) {
            final MapleStatEffect healEffect = bHealing.getEffect(getSkillLevel(bHealing));
            beholderHealingSchedule = TimerManager.getInstance().register(new Runnable() {

                @Override
                public void run() {
                    addHP(healEffect.getHp());
                    getClient().getSession().write(MaplePacketCreator.showOwnBuffEffect(1321007, 2));
                    getMap().broadcastMessage(MapleCharacter.this, MaplePacketCreator.summonSkill(getId(), 1321007, 5), true);
                    getMap().broadcastMessage(MapleCharacter.this, MaplePacketCreator.showBuffeffect(getId(), 1321007, 2, (byte) 3), false);
                }
            }, healEffect.getX() * 1000, healEffect.getX() * 1000);
        }
        ISkill bBuffing = SkillFactory.getSkill(1320009);
        if (getSkillLevel(bBuffing) > 0) {
            final MapleStatEffect buffEffect = bBuffing.getEffect(getSkillLevel(bBuffing));
            beholderBuffSchedule = TimerManager.getInstance().register(new Runnable() {

                @Override
                public void run() {
                    buffEffect.applyTo(MapleCharacter.this);
                    getClient().getSession().write(MaplePacketCreator.beholderAnimation(getId(), 1320009));
                    getMap().broadcastMessage(MapleCharacter.this, MaplePacketCreator.summonSkill(getId(), 1321007, (int) (Math.random() * 3) + 6), true);
                    getMap().broadcastMessage(MapleCharacter.this, MaplePacketCreator.showBuffeffect(getId(), 1321007, 2, (byte) 3), false);
                }
            }, buffEffect.getX() * 1000, buffEffect.getX() * 1000);
        }
    }

    public void setChalkboard(String text) {
        if (interaction != null) {
            return;
        }
        if (map.isEvent() && !isGM() && text != null) {
            dropMessage("You cannot open a chalkboard here");
            return;
        }
        this.chalktext = text;
        for (FakeCharacter ch : fakes) {
            ch.getFakeChar().setChalkboard(text);
        }
        if (chalktext == null) {
            getMap().broadcastMessage(MaplePacketCreator.useChalkboard(this, true));
        } else {
            getMap().broadcastMessage(MaplePacketCreator.useChalkboard(this, false));
        }
    }

    public String getChalkboard() {
        return this.chalktext;
    }

    public void setDefaultKeyMap() {
        keymap.clear();
        int[] num1 = {2, 3, 4, 5, 6, 7, 16, 17, 18, 19, 23, 25, 26, 27, 29, 31, 34, 35, 37, 38, 40, 41, 43, 44, 45, 46, 48, 50, 56, 57, 59, 60, 61, 62, 63, 64, 65};
        int[] num2 = {4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 5, 4, 4, 4, 4, 4, 4, 4, 4, 5, 5, 4, 4, 4, 5, 5, 6, 6, 6, 6, 6, 6, 6};
        int[] num3 = {10, 12, 13, 18, 24, 21, 8, 5, 0, 4, 1, 19, 14, 15, 52, 2, 17, 11, 3, 20, 16, 23, 9, 50, 51, 6, 22, 7, 53, 54, 100, 101, 102, 103, 104, 105, 106};
        for (int i = 0; i < num1.length; i++) {
            keymap.put(Integer.valueOf(num1[i]), new MapleKeyBinding(num2[i], num3[i]));
        }
        sendKeymap();
    }

    public void setReborns(int amt) {
        reborns = amt;
    }

    public int getReborns() {
        return reborns;
    }

    public void sendHint(String msg, int width, int height) {
        getClient().getSession().write(MaplePacketCreator.sendHint(msg, width, height));
    }

    public void doReborn() {
        int negexp;
        String message;
        setLevel(2);
        achieve(Achievement.FREBORN);
        setReborns(getReborns() + 1);
        negexp = getExp();
        gainExp(-negexp, false, false);
        updateSingleStat(MapleStat.EXP, getExp());
        dropMessage("You have just rebirthed! You now have " + getReborns() + " rebirths.");
        if (getReborns() == 21) {
            sendHint("You can now #eSuper Rebirth!#n use #e@srb#n for more information", 0, 0);
        }
        if (getReborns() > 9) {
            achieve(Achievement.TREBORN);
        }
        if (getReborns() > 99) {
            achieve(Achievement.HREBORN);
        }
        if (getReborns() > 999) {
            achieve(Achievement.KREBORN);
        }
        if (getReborns() == 1) {
            String hisher = (getGender() == 1 ? "her" : "his");
            message = "[Congrats] " + getName() + " has reached " + hisher + " first rebirth! Congratulations on your achievement, " + getName() + "!";
            MaplePacket packet = MaplePacketCreator.serverNotice(6, message);
            try {
                getClient().getChannelServer().getWorldInterface().broadcastMessage(getName(), packet.getBytes());
            } catch (RemoteException e) {
                getClient().getChannelServer().reconnectWorld();
            }
        } else if (getReborns() % 100 == 0) {
            message = "[Congrats] WOW! " + getName() + " has just reached " + getReborns() + " reborns! Quite an achievement!";
            MaplePacket packet = MaplePacketCreator.serverNotice(6, message);
            try {
                getClient().getChannelServer().getWorldInterface().broadcastMessage(getName(), packet.getBytes());
            } catch (RemoteException e) {
                getClient().getChannelServer().reconnectWorld();
            }
        } else if (getReborns() % 1000 == 0) {
            message = "[Congrats] CRAZY! " + getName() + " has just reached " + getReborns() + " reborns! What a beast!";
            MaplePacket packet = MaplePacketCreator.serverNotice(6, message);
            try {
                getClient().getChannelServer().getWorldInterface().broadcastMessage(getName(), packet.getBytes());
            } catch (RemoteException e) {
                getClient().getChannelServer().reconnectWorld();
            }
        } else if (getReborns() % 10000 == 0) {
            message = "[Congrats] THIS IS MADNESS! " + getName() + " has just reached " + getReborns() + " reborns. How crazy!";
            MaplePacket packet = MaplePacketCreator.serverNotice(6, message);
            try {
                getClient().getChannelServer().getWorldInterface().broadcastMessage(getName(), packet.getBytes());
            } catch (RemoteException e) {
                getClient().getChannelServer().reconnectWorld();
            }
        }
        checkBerserk();
    }

    public int getReminds() {
        return reminds;
    }

    public void addRemind() {
        reminds++;
    }

    public void clearReminds() {
        reminds = 0;
    }

    public void setPvpDeaths(int amount) {
        pvpdeaths = amount;
    }

    public void setPvpKills(int amount) {
        pvpkills = amount;
    }

    public void gainPvpDeath() {
        pvpdeaths += 1;
    }

    public void gainPvpKill() {
        pvpkills += 1;
    }

    public boolean getCanSmega() {
        return canSmega;
    }

    public void setCanSmega(boolean yn) {
        canSmega = yn;
    }

    public boolean getSmegaEnabled() {
        return smegaEnabled;
    }

    public void setSmegaEnabled(boolean yn) {
        smegaEnabled = yn;
    }

    public boolean getCanTalk() {
        return canTalk;
    }

    public boolean canTalk(boolean yn) {
        return canTalk = yn;
    }

    public int getPvpKills() {
        return pvpkills;
    }

    public int getPvpDeaths() {
        return pvpdeaths;
    }

    public MapleGuild getGuild() {
        try {
            return getClient().getChannelServer().getWorldInterface().getGuild(getGuildId(), null);
        } catch (RemoteException e) {
            client.getChannelServer().reconnectWorld();
        }
        return null;
    }

    public void gainGP(int amount) {
        getGuild().gainGP(amount);
    }

    public void addBuddyCapacity(int capacity) {
        buddylist.addCapacity(capacity);
        client.getSession().write(MaplePacketCreator.updateBuddyCapacity(getBuddyCapacity()));
    }

    public int getPetMaxLevel() {
        if (pets.length > 0) {
            int petlevel = 0;
            for (MaplePet pet : getPets()) {
                if (pet.getLevel() > petlevel) {
                    petlevel = pet.getLevel();
                }
            }
            return petlevel;
        }
        return -1;
    }

    public void maxSkillLevel(int skillid) {
        int maxlevel = SkillFactory.getSkill(skillid).getMaxLevel();
        changeSkillLevel(SkillFactory.getSkill(skillid), maxlevel, maxlevel);
    }

    public void maxAllSkills() {
        for (int s : SkillFactory.getSkillIds()) {
            maxSkillLevel(s);
        }
        if (isGM()) {
            for (int s : SkillFactory.getGMSkillIds()) {
                maxSkillLevel(s);
            }
        }
    }

    public void unequipEverything() {
        MapleInventory equipped = this.getInventory(MapleInventoryType.EQUIPPED);
        List<Byte> position = new ArrayList<Byte>();
        for (IItem item : equipped.list()) {
            position.add(item.getPosition());
        }
        for (byte pos : position) {
            if (!getInventory(MapleInventoryType.EQUIP).isFull()) {
                MapleInventoryManipulator.unequip(client, pos, getInventory(MapleInventoryType.EQUIP).getNextFreeSlot());
            }
        }
    }

    public void setOffOnline(boolean online) {
        try {
            WorldChannelInterface wci = client.getChannelServer().getWorldInterface();
            if (online) {
                wci.loggedOn(getName(), getId(), client.getChannel(), getBuddylist().getBuddyIds());
            } else {
                wci.loggedOff(getName(), getId(), client.getChannel(), getBuddylist().getBuddyIds());
            }
        } catch (RemoteException e) {
            client.getChannelServer().reconnectWorld();
        }
    }

    public static boolean unban(String name) {
        try {
            int accountid = -1;
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT accountid FROM characters WHERE name = ?");
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                accountid = rs.getInt("accountid");
            }
            ps.close();
            rs.close();
            if (accountid == -1) {
                return false;
            }
            ps = con.prepareStatement("UPDATE accounts SET banned = -1 WHERE id = ?");
            ps.setInt(1, accountid);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            sqlException(e);
            return false;
        }
        return true;
    }

    public void addWatcher(MapleCharacter spy) {
        if ((getGMLevel() < spy.getGMLevel()) && !watcher.contains(spy)) {
            watcher.add(spy);
        }
    }

    public void clearWatcher() {
        watcher.clear();
    }

    public void removeWatcher(MapleCharacter spy) {
        watcher.remove(spy);
    }

    public boolean hasWatcher(MapleCharacter spy) {
        return watcher.contains(spy);
    }

    public boolean hasWatchers() {
        return !watcher.isEmpty();
    }

    public void sendWatcherMessage(String text) {
        for (MapleCharacter troll : watcher) {
            if (troll != null) {
                troll.dropMessage(text);
            }
        }
    }

    public int getEquipId(byte slot) {
        MapleInventory equip = getInventory(MapleInventoryType.EQUIP);
        Equip eu = (Equip) equip.getItem(slot);
        return equip.getItem(slot).getItemId();
    }

    public int getEquippedId(byte slot) {
        MapleInventory equip = getInventory(MapleInventoryType.EQUIPPED);
        Equip eu = (Equip) equip.getItem(slot);
        return equip.getItem(slot).getItemId();
    }

    public int getUseId(byte slot) {
        MapleInventory use = getInventory(MapleInventoryType.USE);
        return use.getItem(slot).getItemId();
    }

    public int getSetupId(byte slot) {
        MapleInventory setup = getInventory(MapleInventoryType.SETUP);
        return setup.getItem(slot).getItemId();
    }

    public int getCashId(byte slot) {
        MapleInventory cash = getInventory(MapleInventoryType.CASH);
        return cash.getItem(slot).getItemId();
    }

    public int getETCId(byte slot) {
        MapleInventory etc = getInventory(MapleInventoryType.ETC);
        return etc.getItem(slot).getItemId();
    }

    public void deleteAll(int id) {
        MapleInventoryManipulator.removeAllById(getClient(), id, true);
    }

    public void dropMessage(String message) {
        dropMessage(6, message);
    }

    public void dropMessage(int type, String message) {
        if (type == 7) {
            getClient().getSession().write(MaplePacketCreator.sendYellowTip(message));
        } else {
            client.getSession().write(MaplePacketCreator.serverNotice(type, message));
        }
    }

    public void setClan(int num) {
        clan = num;
    }

    public int getClan() {
        return clan;
    }

    public void setBombPoints(int bombpoints) {
        this.bombpoints = bombpoints;
    }

    public void setJob(int job) {
        if (isfake) {
            this.job = MapleJob.getById(job);
        } else {
            this.changeJob(MapleJob.getById(job));
        }
    }

    public void setFake() {
        isfake = true;
    }

    public void setJob(MapleJob job) {
        this.changeJob(job);
    }

    public int getBombPoints() {
        return bombpoints;
    }

    public void setID(int id) {
        this.id = id;
    }

    public void setInventory(MapleInventoryType type, MapleInventory inv) {
        inventory[type.ordinal()] = inv;
    }

    public boolean isIgnored(String victim) {
        return ignore.contains(victim);
    }

    public void ignore(String victim) {
        ignore.add(victim);
    }

    public void unignore(String victim) {
        ignore.remove(victim);
    }

    public boolean hasFakeChar() {
        if (fakes.size() > 0) {
            return true;
        }
        return false;
    }

    public List<FakeCharacter> getFakeChars() {
        return fakes;
    }

    public void setGMText(int text) {
        gmtext = text;
    }

    public int getGMText() {
        return gmtext;
    }

    public void setExp(int set) {
        exp.set(set);
        if (exp.get() < 0) {
            exp.set(0);
        }
    }

    public boolean gainItem(int id) {
        return gainItem(id, 1);
    }

    public boolean gainItem(int id, int quantity) {
        return MapleInventoryManipulator.addById(getClient(), id, (short) quantity);
    }

    public void giveItemBuff(int itemID) {
        MapleItemInformationProvider mii = MapleItemInformationProvider.getInstance();
        MapleStatEffect statEffect = mii.getItemEffect(itemID);
        statEffect.applyTo(this);
    }

    public void cancelAllDebuffs() {
        for (int i = 0; i < diseases.size(); i++) {
            diseases.remove(i);
            long mask = 0;
            for (MapleDisease statup : diseases) {
                mask |= statup.getValue();
            }
            getClient().getSession().write(MaplePacketCreator.cancelDebuff(mask));
            getMap().broadcastMessage(this, MaplePacketCreator.cancelForeignDebuff(id, mask), false);
        }
    }

    public List<MapleDisease> getDiseases() {
        return diseases;
    }

    @SuppressWarnings("unchecked")
    public void removeJobSkills() {
        HashMap<Integer, MapleKeyBinding> keymapCloned = (HashMap<Integer, MapleKeyBinding>) keymap.clone();
        for (Integer keys : keymapCloned.keySet()) {
            if (SkillFactory.getSkillName(keys) != null) {
                if (keymapCloned.get(keys).getAction() >= 1000000) {
                    keymap.remove(keys);
                }
            }
        }
        sendKeymap();
    }

    public void changePage(int page) {
        this.currentPage = page;
    }

    public void changeTab(int tab) {
        this.currentTab = tab;
    }

    public void changeType(int type) {
        this.currentType = type;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getCurrentTab() {
        return currentTab;
    }

    public int getCurrentType() {
        return currentType;
    }

    public void handleEnergyChargeGain() {
        ISkill energycharge = SkillFactory.getSkill(5110001);
        int energyChargeSkillLevel = getSkillLevel(energycharge);
        MapleStatEffect ceffect = energycharge.getEffect(energyChargeSkillLevel);
        int gain = rand((int) ceffect.getProp(), (int) ceffect.getProp() * 2);
        if (energybar < 10000) {
            energybar += gain;
            if (energybar > 10000) {
                energybar = 10000;
            }
            getClient().getSession().write(MaplePacketCreator.giveEnergyCharge(energybar));
        } else {
            TimerManager.getInstance().schedule(new Runnable() {

                @Override
                public void run() {
                    getClient().getSession().write(MaplePacketCreator.giveEnergyCharge(0));
                    energybar = 0;
                }
            }, ceffect.getDuration());
        }
    }

    public int getEnergyBar() {
        return this.energybar;
    }

    public void setEnergyBar(int set) {
        energybar = set;
    }

    public long getAfkTime() {
        return afkTime;
    }

    public void resetAfkTime() {
        if (this.chalktext != null && this.chalktext.equals("I'm afk ! Drop me a message <3")) {
            setChalkboard(null);
        }
        afkTime = System.currentTimeMillis();
    }

    public void setClient(MapleClient c) {
        client = c;
    }

    public boolean isFake() {
        return this.isfake;
    }

    public long getLastLogin() {
        return lastLogin;
    }

    public int getRingRequested() {
        return this.ringRequest;
    }

    public void setRingRequested(int set) {
        ringRequest = set;
    }

    public boolean hasMerchant() {
        return hasMerchant;
    }

    public boolean tempHasItems() {
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT ownerid FROM hiredmerchanttemp WHERE ownerid = ?");
            ps.setInt(1, getId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                rs.close();
                ps.close();
                return true;
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            sqlException(e);
        }
        return false;
    }

    public void setHasMerchant(boolean set) {
        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("UPDATE characters SET HasMerchant = ? WHERE id = ?");
            ps.setInt(1, set ? 1 : 0);
            ps.setInt(2, getId());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            sqlException(e);
        }
        hasMerchant = set;
    }

    public List<Integer> getVIPRockMaps(int type) {
        List<Integer> rockmaps = new LinkedList<Integer>();
        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT mapid FROM VIPRockMaps WHERE cid = ? AND type = ?");
            ps.setInt(1, id);
            ps.setInt(2, type);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                rockmaps.add(rs.getInt("mapid"));
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            return null;
        }
        return rockmaps;
    }

    public void leaveParty() {
        WorldChannelInterface wci = ChannelServer.getInstance(getClient().getChannel()).getWorldInterface();
        MaplePartyCharacter partyplayer = new MaplePartyCharacter(this);
        if (party != null) {
            try {
                if (partyplayer.equals(party.getLeader())) {
                    wci.updateParty(party.getId(), PartyOperation.DISBAND, partyplayer);
                    if (getEventInstance() != null) {
                        getEventInstance().disbandParty();
                    }
                } else {
                    wci.updateParty(party.getId(), PartyOperation.LEAVE, partyplayer);
                    if (getEventInstance() != null) {
                        getEventInstance().leftParty(this);
                    }
                }
            } catch (RemoteException e) {
                getClient().getChannelServer().reconnectWorld();
            }
            setParty(null);
        }
    }

    /**
     * SirPali's Functions *
     */
    /**
     * Creates a countdown timer, after timer runs out, player gets teleported
     * to Henesys
     *
     * @int time - Set the amount of time the player can train in seconds
     * Credits to Ace from RaGEZONE
     *
     */
    public void startTraining(int time) {
        getClient().getSession().write(MaplePacketCreator.getClock(time));
        int min = time / 60; // Devides time by 60 to calculate minutes
        int seconds = time % 60; // Divides time by 60 untill sum is smaller then 60, saves what is left over. This provides the seconds.
        dropMessage("You will be teleported back in " + min + " minutes and " + seconds + " seconds.");
        TimerManager.getInstance().schedule(new Runnable() {

            public void run() {
                changeMap(100000000); // Teleports to Henesys, can be changed if wanted.
                dropMessage("Your time is up! You are being teleported back to Henesys.");
            }
        }, time * 1000); // Time * 1000 to get miliseconds.
    }

    public int votePoints() {
        this.updateVotePoints();
        return votepoints;
    }

    public void setVotePoints(int votepoints) {
        this.votepoints = votepoints;
        Connection con = DatabaseConnection.getConnection();
        try {
            con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
            con.setAutoCommit(false);
            PreparedStatement ps = con.prepareStatement("UPDATE accounts SET votepoints = ? WHERE id = ?");
            ps.setInt(1, votepoints);
            ps.setInt(2, accountid);
            ps.executeUpdate();
            con.commit();
            ps.close();
        } catch (SQLException e) {
            sqlException(e);
        }
    }

    public void updateVotePoints() {
        ResultSet rs = null;
        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("select votepoints from accounts where id = (select accountid from characters where name = \"" + this.getName() + "\" )");
            rs = ps.executeQuery();
            rs.first();
            this.votepoints = rs.getInt("votepoints");
            rs.close();
            ps.close();
        } catch (SQLException se) {
        }
    }

    public void changeVotePoints(int amount) {
        this.updateVotePoints();
        this.votepoints = votepoints + amount;
        Connection con = DatabaseConnection.getConnection();
        try {
            con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
            con.setAutoCommit(false);
            PreparedStatement ps = con.prepareStatement("UPDATE accounts SET votepoints = ? WHERE id = ?");
            ps.setInt(1, votepoints);
            ps.setInt(2, accountid);
            ps.executeUpdate();
            con.commit();
            ps.close();
        } catch (SQLException e) {
            sqlException(e);
        }
    }

    public int getDonorLevel() {
        return donatorLevel;
    }

    public void setDonorLevel(int level) {
        donatorLevel = level;
    }

    public int getDP() {
        return this.donatePoints;
    }

    public void giveDP(int set) {
        donatePoints = donatePoints + set;
    }

    public void setHoster(String ballsack) {
        this.hoster = ballsack;
    }

    public String getHoster() {
        return this.hoster;
    }

    public boolean isInJail() {
        return getMap().getId() == 980000404 || getMap().getId() == 222020211 || getMap().getId() == 222020111 || getMap().getId() == 980000304;
    }

    public boolean inEvent() {
        return getMap().isEvent();
    }

    public void gainKarma(int gain) {
        this.karma += gain;
    }

    public int getKarma() {
        return karma;
    }

    public void setKarma(int x) {
        this.karma = x;
    }

    public void upKarma() {
        this.karma += 1;
    }

    public void downKarma() {
        this.karma -= 1;
    }

    public void setMP(int MP) {
        this.mp = mp - 1;
    }

    public void sethp(int hp) {
        this.hp = hp - 1;
    }

    public void setFollower(int id) {
        this.follower = id;
    }

    public int getFollower() {
        return follower;
    }

    public void removeItem(int id, int amount) {
        MapleInventoryManipulator.removeById(getClient(), MapleItemInformationProvider.getInstance().getInventoryType(id), id, amount, true, false);
    }

    public int getHiredMerchantMesos() {
        Connection con = DatabaseConnection.getConnection();
        int mesos;
        try {
            PreparedStatement ps = con.prepareStatement("SELECT MerchantMesos FROM characters WHERE id = ?");
            ps.setInt(1, id);
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
            ps.setInt(1, id);
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
            ps.setInt(2, id);
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
            ps.setInt(2, id);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException se) {
        }
    }

    public List<MapleRing> getCrushRings() {
        Collections.sort(crushRings);
        return crushRings;
    }

    public List<MapleRing> getFriendshipRings() {
        Collections.sort(friendshipRings);
        return friendshipRings;
    }

    public MapleRing getMarriageRing() {
        return marriageRing;
    }

    public void addCrushRing(MapleRing r) {
        crushRings.add(r);
    }

    public void addFriendshipRing(MapleRing r) {
        friendshipRings.add(r);
    }

    public MapleRing getRingById(int id) {
        for (MapleRing ring : getCrushRings()) {
            if (ring.getRingId() == id) {
                return ring;
            }
        }
        for (MapleRing ring : getFriendshipRings()) {
            if (ring.getRingId() == id) {
                return ring;
            }
        }
        if (getMarriageRing().getRingId() == id) {
            return getMarriageRing();
        }

        return null;
    }

    public int getHideoutNpc() {
        return hideoutNpc;
    }

    public void setHideoutNpc(int id) {
        hideoutNpc = id;
    }

    public void spawnHideoutNpc() {
        if (hideoutNpc > 0) {
            MapleNPC npc = MapleLifeFactory.getNPC(hideoutNpc);
            int mid = getMapId();
            Point pos = getMap().getGroundBelow(getPosition());
            if (npc != null && !npc.getName().equalsIgnoreCase("MISSINGNO")) {
                npc.setPosition(pos);
                npc.setCy(pos.y);
                npc.setRx0(pos.x + 50);
                npc.setRx1(pos.x - 50);
                npc.setFh(getMap().getFootholds().findBelow(pos).getId());
                try {
                    PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("INSERT INTO `hideout_npcs` (`npcid`, `guildid`, `x`, `y`, `fh`, `mapid`) VALUES (?, ?, ?, ?, ?, ?)");
                    ps.setInt(1, hideoutNpc);
                    ps.setInt(2, getGuildId());
                    ps.setInt(3, pos.x);
                    ps.setInt(4, pos.y);
                    ps.setInt(5, getMap().getFootholds().findBelow(pos).getId());
                    ps.setInt(6, getMapId());
                    ps.executeUpdate();
                    ps.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
                for (ChannelServer cservs : ChannelServer.getAllInstances()) {
                    cservs.getMapFactory().getMap(mid).addMapObject(npc);
                    cservs.getMapFactory().getMap(mid).broadcastMessage(MaplePacketCreator.spawnNPC(npc));
                }
            }
            hideoutNpc = -1;
        } else {
            dropMessage("[GuildHideout] You either logged off or changed channels before dropping the Blue Snail Stamp");
        }
    }
}
