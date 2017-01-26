package client;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import provider.MapleData;
import provider.MapleDataProvider;
import provider.MapleDataProviderFactory;
import provider.MapleDataTool;
import tools.StringUtil;

public class SkillFactory {

    private static Map<Integer, ISkill> skills = new HashMap<Integer, ISkill>();
    private static MapleDataProvider datasource = MapleDataProviderFactory.getDataProvider(new File(System.getProperty("wzpath") + "/Skill.wz"));
    private static MapleData stringData = MapleDataProviderFactory.getDataProvider(new File(System.getProperty("wzpath") + "/String.wz")).getData("Skill.img");
    private static final int[] skillIds = {8, 1000, 1001, 1002, 1003, 1004, 1000000, 1000001, 1000002, 1001003, 1001004, 1001005, 2000000, 2000001,
        2001002, 2001003, 2001004, 2001005, 3000000, 3000001, 3000002, 3001003, 3001004, 3001005, 4000000, 4000001, 4001002, 4001003,
        4001334, 4001344, 1100000, 1100001, 1100002, 1100003, 1101004, 1101005, 1101006, 1101007, 1200000, 1200001, 1200002, 1200003,
        1201004, 1201005, 1201006, 1201007, 1300000, 1300001, 1300002, 1300003, 1301004, 1301005, 1301006, 1301007, 2100000, 2101001,
        2101002, 2101003, 2101004, 2101005, 2200000, 2201001, 2201002, 2201003, 2201004, 2201005, 2300000, 2301001, 2301002, 2301003,
        2301004, 2301005, 3100000, 3100001, 3101002, 3101003, 3101004, 3101005, 3200000, 3200001, 3201002, 3201003, 3201004, 3201005,
        4100000, 4100001, 4100002, 4101003, 4101004, 4101005, 4200000, 4200001, 4201002, 4201003, 4201004, 4201005, 1110000, 1110001,
        1111002, 1111003, 1111004, 1111005, 1111006, 1111007, 1111008, 1210000, 1210001, 1211002, 1211003, 1211004, 1211005, 1211006,
        1211007, 1211008, 1211009, 1310000, 1311001, 1311002, 1311003, 1311004, 1311005, 1311006, 1311007, 1311008, 2110000, 2110001,
        2111002, 2111003, 2111004, 2111005, 2111006, 2210000, 2210001, 2211002, 2211003, 2211004, 2211005, 2211006, 2310000, 2311001,
        2311002, 2311003, 2311004, 2311005, 2311006, 3110000, 3110001, 3111002, 3111003, 3111004, 3111005, 3111006, 3210000, 3210001,
        3211002, 3211003, 3211004, 3211005, 3211006, 4110000, 4111001, 4111002, 4111003, 4111004, 4111005, 4111006, 4210000, 4211001,
        4211002, 4211003, 4211004, 4211005, 4211006, 1120003, 1120004, 1120005, 1121000, 1121001, 1121002, 1121006, 1121008, 1121010,
        1121011, 1220005, 1220006, 1220010, 1221000, 1221001, 1221002, 1221003, 1221004, 1221007, 1221009, 1221011, 1221012, 1320005,
        1320008, 1320009, 1321000, 1321001, 1321002, 1321003, 1321007, 1321010, 2121000, 2121001, 2121002, 2121003, 2121004,
        2121005, 2121006, 2121007, 2121008, 2221000, 2221001, 2221002, 2221003, 2221004, 2221005, 2221006, 2221007, 2221008, 2321000,
        2321001, 2321002, 2321003, 2321004, 2321005, 2321006, 2321007, 2321008, 2321009, 3120005, 3121000, 3121002, 3121003, 3121004,
        3121006, 3121007, 3121008, 3121009, 3220004, 3221000, 3221001, 3221002, 3221003, 3221005, 3221006, 3221008, 4120002,
        4120005, 4121000, 4121003, 4121004, 4121006, 4121007, 4121008, 4121009, 4220002, 4220005, 4221000, 4221001, 4221003, 4221004,
        4221006, 4221007, 4221008, 5000000, 5001001, 5001002, 5001003, 5100000, 5100001, 5101002, 5101003, 5101004, 5101005,
        5101006, 5200000, 5201001, 5201002, 5201003, 5201004, 5201005, 5201006, 5110000, 5110001, 5111002, 5111004, 5111005,
        5111006, 5220011, 5221010, 5221009, 5221008, 5221007, 5221004, 5221003, 5220002, 5220001, 5221000, 5121010, 5121009,
        5121008, 5121007, 5121005, 5121004, 5121003, 5121002, 5121001, 5121000, 5211006, 5211005, 5211004, 5211002, 5211001, 5210000,
        9001001, 9001002, 9001000, 1320006};
    private static final int[] gmSkillIds = {9101000, 9101001, 9101002, 9101003, 9101004, 9101005, 9101006, 9101007, 9101008};

    public static ISkill getSkill(int id) {
        ISkill ret = skills.get(Integer.valueOf(id));
        if (ret != null) {
            return ret;
        }
        synchronized (skills) {
            // Check if someone else that's also synchronized has loaded the skill by now.
            ret = skills.get(Integer.valueOf(id));
            if (ret == null) {
                int job = id / 10000;
                MapleData skillroot = datasource.getData(StringUtil.getLeftPaddedStr(String.valueOf(job), '0', 3) + ".img");
                MapleData skillData = skillroot.getChildByPath("skill/" + StringUtil.getLeftPaddedStr(String.valueOf(id), '0', 7));
                if (skillData != null) {
                    ret = Skill.loadFromData(id, skillData);
                }
                skills.put(Integer.valueOf(id), ret);
            }
            return ret;
        }
    }

    public static int[] getSkillIds() {
        return skillIds;
    }

    public static int[] getGMSkillIds() {
        return gmSkillIds;
    }

    public static String getSkillName(int id) {
        String strId = Integer.toString(id);
        strId = StringUtil.getLeftPaddedStr(strId, '0', 7);
        MapleData skillroot = stringData.getChildByPath(strId);
        if (skillroot != null) {
            return MapleDataTool.getString(skillroot.getChildByPath("name"), "");
        }
        return null;
    }
}
