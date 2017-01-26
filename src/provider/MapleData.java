package provider;

import java.util.List;
import provider.wz.MapleDataType;

public interface MapleData extends MapleDataEntity, Iterable<MapleData> {
    public String getName();
    public MapleDataType getType();
    public List<MapleData> getChildren();
    public MapleData getChildByPath(String path);
    public Object getData();
}