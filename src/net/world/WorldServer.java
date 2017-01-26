package net.world;

import java.io.FileReader;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Properties;
import javax.rmi.ssl.SslRMIClientSocketFactory;
import javax.rmi.ssl.SslRMIServerSocketFactory;
import client.IItem;
import database.DatabaseConnection;
import tools.ChaseyEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorldServer {

    private static WorldServer instance = null;
    private static Logger log = LoggerFactory.getLogger(WorldServer.class);
    private int worldId;
    private Properties dbProp = new Properties();
    private Properties worldProp = new Properties();
    private String eventHost;
    private String eventName;
    private int eventChannel;
    private int eventMap;
    private int eventOwner;
    private boolean eventWarp;
    private boolean dropEvent = false;
    private IItem dropEventItem = null;
    private String dropEventHost = "";
    private int dropEventMap = 0;
    private int hints = 4;
    private ChaseyEvent chasey;

    private WorldServer() {
        try {
            InputStreamReader is = new FileReader("db.properties");
            dbProp.load(is);
            is.close();
            DatabaseConnection.setProps(dbProp);
            DatabaseConnection.getConnection();
            is = new FileReader("world.properties");
            worldProp.load(is);
            is.close();
            // new ChaseyEvent();
        } catch (Exception e) {
            log.error("Could not configuration", e);
        }
    }

    public synchronized static WorldServer getInstance() {
        if (instance == null) {
            instance = new WorldServer();
        }
        return instance;
    }

    public int getWorldId() {
        return worldId;
    }

    public Properties getDbProp() {
        return dbProp;
    }

    public Properties getWorldProp() {
        return worldProp;
    }
// start of drop event functions

    public boolean hasDropEvent() {
        return dropEvent;
    }

    public void setHasDropEvent(boolean foo) {
        dropEvent = foo;
    }

    public void endDropEvent() {
        dropEventItem = null;
        dropEventHost = "";
        dropEventMap = 0;
        hints = 4;
        setHasDropEvent(false);
    }

    public int getHints() {
        return hints;
    }

    public void useHint() {
        hints--;
    }

    public boolean startChasey() {
        if (chasey == null) {
            chasey = new ChaseyEvent();
            return true;
        } else if (!chasey.isOn()) {
            chasey.open();
            return true;
        }
        return false;
    }

    public ChaseyEvent getChasey() {
        return chasey;
    }

    public void setDropEvent(IItem item, int map, String player) {
        dropEventItem = item;
        dropEventHost = player;
        dropEventMap = map;
        setHasDropEvent(true);
    }

    public IItem getDropEventItem() {
        return dropEventItem;
    }

    public int getDropEventMap() {
        return dropEventMap;
    }

    public String getDropEventHost() {
        return dropEventHost;
    }

    public boolean hasEvent() {
        return eventMap != 0;
    }

    public int getEventChannel() {
        return eventChannel;
    }

    public int getEventOwner() {
        return eventOwner;
    }

    public void startEvent(int mapId, String host, int owner, String name, int channel) {
        if (eventMap == 0) {
            eventOwner = owner;
            eventMap = mapId;
            eventHost = host;
            eventName = name;
            eventWarp = true;
            eventChannel = channel;
        }
    }

    public boolean canEventWarp() {
        return eventWarp;
    }

    public void setEventWarp(boolean allow) {
        eventWarp = allow;
    }

    public int getEventMap() {
        return eventMap;
    }

    public void setEventMap(int mapId) {
        if (eventMap != 0) {
            eventMap = mapId;
        }
    }

    public String getEventHost() {
        return eventHost;
    }

    public void setEventHost(String host) {
        if (eventMap != 0) {
            eventHost = host;
        }
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String name) {
        eventName = name;
    }

    public void endEvent() {
        if (eventMap != 0) {
            eventMap = 0;
            eventHost = "";
            eventName = "";
            eventWarp = false;
            eventChannel = 0;
        }
    }

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.createRegistry(Registry.REGISTRY_PORT, new SslRMIClientSocketFactory(), new SslRMIServerSocketFactory());
            registry.rebind("WorldRegistry", WorldRegistryImpl.getInstance());
        } catch (RemoteException ex) {
            log.error("Could not initialize RMI system", ex);
        }
    }
}
