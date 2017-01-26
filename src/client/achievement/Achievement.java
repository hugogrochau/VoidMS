package client.achievement;

public enum Achievement {

    LOGIN(100),
    SPAWNER1(101),
    SPAWNER2(102),
    SPAWNER3(103),
    FREBORN(104),
    TREBORN(105),
    FSEX(106),
    FPVP(107),
    SGUILD(108),
    ZOMBIE(109),
    FZK(201),
    FBOSS(202),
    FJQ(203),
    FBUDDY(204),
    FGUILD(205),
    FFAME(206),
    FPARTY(207),
    FPVPK(208),
    DMG10K(209),
    ZCATCH(210),
    PVP100(300),
    HREBORN(301),
    DMG100K(302),
    DMG1M(303),
    FHT(304),
    ACH10(305),
    DMG10M(400),
    ACH20(401),
    ACH30(402),
    ZSURVIVE(403),
    ZMOST(404),
    KREBORN(500),
    PVP1000(501),
    DMG100M(502),
    DMG1B(503);
    final int id;

    private Achievement(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static Achievement getById(int id) {
        for (Achievement a : Achievement.values()) {
            if (a.getId() == id) {
                return a;
            }
        }
        return null;
    }

    public int getDifficulty() {
        return (int) Math.floor((double) (id / 100));
    }

    public static int getDifficulty(int id) {
        return (int) Math.floor((double) (id / 100));
    }

    public String getName() {
        switch (id) {
            case 100:
                return "Newbie";
            case 101:
                return "Noob Spawner";
            case 102:
                return "Medium Spawner";
            case 103:
                return "Pro Spawner";
            case 104:
                return "Reborn";
            case 105:
                return "Starting Pro";
            case 106:
                return "Becoming a (Wo)man";
            case 107:
                return "PvP Noob";
            case 108:
                return "Guild Starter";
            case 109:
                return "Zombie";
            case 201:
                return "Zakum Destroyer";
            case 202:
                return "Boss Vanquisher";
            case 203:
                return "Baby Bunny";
            case 204:
                return "Getting a Life";
            case 205:
                return "Guild Noob";
            case 206:
                return "Child Star";
            case 207:
                return "Team Work";
            case 208:
                return "Killer";
            case 209:
                return "Powerful";
            case 210:
                return "Contaminate";
            case 300:
                return "Serial Killer";
            case 301:
                return "On the Way to the Top";
            case 302:
                return "Vigorous";
            case 303:
                return "Colossal";
            case 304:
                return "Horntail Slayer";
            case 305:
                return "Achiever";
            case 400:
                return "Godly";
            case 401:
                return "Overachiever";
            case 402:
                return "Professional Achiever";
            case 403:
                return "Survivor";
            case 404:
                return "Menace";
            case 500:
                return "Rebirth Whore";
            case 501:
                return "Maniac";
            case 502:
                return "Titan";
            case 503:
                return "Winning";
            default:
                return "";
        }
    }

    public String getDesc() {
        switch (id) {
            case 100:
                return "your first login";
            case 101:
                return "using the noob spawner";
            case 102:
                return "using the medium spawner";
            case 103:
                return "using the pro spawner";
            case 104:
                return "your first reborn";
            case 105:
                return "10 reborns";
            case 106:
                return "losing your virginity";
            case 107:
                return "joining pvp for the first time";
            case 108:
                return "starting your first guild";
            case 109:
                return "becoming a Zombie";
            case 201:
                return "killing your first Zakum";
            case 202:
                return "killing your first boss";
            case 203:
                return "finishing your first Jump Quest";
            case 204:
                return "your first buddy";
            case 205:
                return "joining your first guild";
            case 206:
                return "being famed for the first time";
            case 207:
                return "joining your first party";
            case 208:
                return "your first pvp kill";
            case 209:
                return "hitting with 10,000 damage";
            case 210:
                return "contaminating someone";
            case 300:
                return "killing 100 people in pvp";
            case 301:
                return "100 reborns";
            case 302:
                return "hitting with 100,000 damage";
            case 303:
                return "hitting with 1,000,000 damage";
            case 304:
                return "killing your first Horntail";
            case 305:
                return "gaining 10 achievements";
            case 400:
                return "hitting with 10,000,000 damage";
            case 401:
                return "gaining 20 achievements";
            case 402:
                return "gaining 30 achievements";
            case 403:
                return "surviving through a zombie apocalypse";
            case 404:
                return "contaminating the most people";
            case 500:
                return "1000 reborns";
            case 501:
                return "killing 1000 people in pvp";
            case 502:
                return "hitting with 100,000,000 damage";
            case 503:
                return "hitting with 1,000,000,000 damage";
            default:
                return "";
        }
    }
}
