package server;

import java.util.HashMap;
import java.util.Map;

public class MapleShopFactory {

    private Map<Integer, MapleShop> shops = new HashMap<Integer, MapleShop>();
    private Map<Integer, MapleShop> npcShops = new HashMap<Integer, MapleShop>();
    private static MapleShopFactory instance = new MapleShopFactory();

    public static MapleShopFactory getInstance() {
        return instance;
    }

    public void clear() {
        shops.clear();
        npcShops.clear();
    }

    public void reloadShop(int id) {
        MapleShop ret = MapleShop.createFromDB(id, true);
        if (ret != null && shops.containsKey(ret.getId())) {
            shops.remove(ret.getId());
            shops.put(ret.getId(), ret);
            npcShops.remove(ret.getNpcId());
            npcShops.put(ret.getNpcId(), ret);
        }
    }

    private MapleShop loadShop(int id, boolean isShopId) {
        MapleShop ret = MapleShop.createFromDB(id, isShopId);
        if (ret != null) {
            shops.put(ret.getId(), ret);
            npcShops.put(ret.getNpcId(), ret);
        } else if (isShopId) {
            shops.put(id, null);
        } else {
            npcShops.put(id, null);
        }
        return ret;
    }

    public boolean hasShop(int shopId) {
        return shops.containsKey(shopId);
    }

    public MapleShop getShop(int shopId) {
        if (shops.containsKey(shopId)) {
            return shops.get(shopId);
        }
        return loadShop(shopId, true);
    }

    public MapleShop getShopForNPC(int npcId) {
        if (npcShops.containsKey(npcId)) {
            npcShops.get(npcId);
        }
        return loadShop(npcId, false);
    }
}
