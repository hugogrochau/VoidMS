var status = 0;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (mode == 0 && status == 0) {
            cm.dispose();
            return;
        }
        if (mode == 1)
            status++;
        else
            status--;
        if (status == 0) {
            cm.sendSimple("#eWelcome to the All In One Shop!\r\nPlease choose a category:#n#b" +
                "\r\n#L80#Magician Equips" +
                "\r\n#L81#Thief Equips" +
                "\r\n#L82#Warrior Equips" +
                "\r\n#L83#Archer Equips" +
                "\r\n#L84#Pirate Equips" +
                "\r\n#L85#Common Equips" +
                "\r\n#L86#Scrolls, Pots, ETC" +
                "\r\n#L87#Cash Items");
        } else if (selection == 80) {
            cm.sendSimple("#ePick a Category:#n#b" +
                "\r\n#L0#Magican Shoes" +
                "\r\n#L1#Magican Overalls" +
                "\r\n#L2#Magican Gloves" +
                "\r\n#L3#Magican Hats" +
                "\r\n#L4#Magican Shields" +
                "\r\n#L5#Magican and Common Wands" +
                "\r\n#L6#Magican and Common Staffs");
        } else if (selection == 81) {
            cm.sendSimple ("#nPick a Category:#n#b" +
                "\r\n#L7#Thief Shoes" +
                "\r\n#L8#Thief Bottoms" +
                "\r\n#L9#Thief Tops" +
                "\r\n#L10#Thief Overalls" +
                "\r\n#L11#Thief Gloves" +
                "\r\n#L12#Thief Hats" +
                "\r\n#L13#Thief Shields" +
                "\r\n#L14#Thief and Common Daggers" +
                "\r\n#L15#Thief and Common Claws" +
                "\r\n#L16#Thief Throwing Stars");
        } else if (selection == 82) {
            cm.sendSimple ("#nPick a Category:#n#b" +
                "\r\n#L17#Warrior Shoes" +
                "\r\n#L18#Warrior Bottom" +
                "\r\n#L19#Warrior Top" +
                "\r\n#L20#Warrior Overalls" +
                "\r\n#L21#Warrior Gloves" +
                "\r\n#L22#Warrior Hats" +
                "\r\n#L23#Warrior Shields" +
                "\r\n#L24#Warrior and Common One-Handed Axes" +
                "\r\n#L25#Warrior and Common Two-Handed Axes" +
                "\r\n#L26#Warrior and Common One-Handed BWs" +
                "\r\n#L27#Warrior and Common Two-Handed BWs" +
                "\r\n#L28#Warrior and Common One-Handed Swords" +
                "\r\n#L29#Warrior and Common Two-Handed Swords" +
                "\r\n#L30#Warrior and Common Spears" +
                "\r\n#L31#Warrior and Common Pole Arms");
        } else if (selection == 83) {
            cm.sendSimple ("#nPick a Category:#e#b" +
                "\r\n#L32#Archer Shoes" +
                "\r\n#L33#Archer Overalls" +
                "\r\n#L34#Archer Gloves" +
                "\r\n#L35#Archer Hats" +
                "\r\n#L36#Archer and Common Bows" +
                "\r\n#L37#Archer and Common Crossbows" +
                "\r\n#L38#Archer Arrows");
        } else if (selection == 84) {
            cm.sendSimple ("#ePick a Category:#n#b" +
                "\r\n#L76#Pirate Hats" +
                "\r\n#L71#Pirate Weapons" +
                "\r\n#L72#Pirate Bullets and Capsules" +
                "\r\n#L73#Pirate Overalls" +
                "\r\n#L74#Pirate Gloves" +
                "\r\n#L75#Pirate Shoes");
        } else if (selection == 85) {
            cm.sendSimple ("#ePick a Category:#n#b" +
                "\r\n#L39#Maple Weapons" +
                "\r\n#L40#Earrings" +
                "\r\n#L41#Necklaces and Face Accessories" +
                "\r\n#L42#Capes" +
                "\r\n#L43#Common Shoes" +
                "\r\n#L44#Common Hats" +
                "\r\n#L46#Common Overalls" +
                "\r\n#L47#Common Shields" +
                "\r\n#L48#Level 0 Weapons");
        } else if (selection == 86) {
            cm.sendSimple ("#ePick a Category:#n#b" +
                "\r\n#L51#Buffs and Potions" +
                "\r\n#L52#Boss Pieces and Rocks" +
                "\r\n#L57#Chairs" +
                "\r\n#L53#Mounts" +
                "\r\n#L54#All scrolls!");
        } else if (selection == 87) {
            cm.sendSimple ("#ePick a Category:#n#b" +
                "\r\n#L67#All Accessories" +
                "\r\n#L94#Bottoms" +
                "\r\n#L59#Capes" +
                "\r\n#L65#Cash Earrings" +
                "\r\n#L68#Effects" +
                "\r\n#L64#Emotion" +
                "\r\n#L66#Eye Accessories" +
                "\r\n#L60#Gloves" +
                "\r\n#L98#Hats" +
                "\r\n#L49#Megaphones" +
                "\r\n#L70#Messengers" +
                "\r\n#L55#Pets" +
                "\r\n#L56#Pet Equips, Pet Food, Scrolls, Books" +
                "\r\n#L62#Rings" +
                "\r\n#L61#Shields" +
                "\r\n#L97#Shoes" +
                "\r\n#L69#Throwing Stars" +
                "\r\n#L95#Tops" +
                "\r\n#L96#Overalls" +
                "\r\n#L99#Weapons");
        } else if (selection == 0) {
            cm.openShop(10000);
            cm.dispose();
        } else if (selection == 1) {
            cm.openShop(10001);
            cm.dispose();
        } else if (selection == 2) {
            cm.openShop(10002);
            cm.dispose();
        } else if (selection == 3) {
            cm.openShop(10003);
            cm.dispose();
        } else if (selection == 4) {
            cm.openShop(10004);
            cm.dispose();
        } else if (selection == 5) {
            cm.openShop(10005);
            cm.dispose();
        } else if (selection == 6) {
            cm.openShop(10006);
            cm.dispose();
        } else if (selection == 7) {
            cm.openShop(10007);
            cm.dispose();
        } else if (selection == 8) {
            cm.openShop(10008);
            cm.dispose();
        } else if (selection == 9) {
            cm.openShop(10009);
            cm.dispose();
        } else if (selection == 10) {
            cm.openShop(10010);
            cm.dispose();
        } else if (selection == 11) {
            cm.openShop(10011);
            cm.dispose();
        } else if (selection == 12) {
            cm.openShop(10012);
            cm.dispose();
        } else if (selection == 13) {
            cm.openShop(10013);
            cm.dispose();
        } else if (selection == 14) {
            cm.openShop(10014);
            cm.dispose();
        } else if (selection == 15) {
            cm.openShop(10015);
            cm.dispose();
        } else if (selection == 16) {
            cm.openShop(10038);
            cm.dispose();
        } else if (selection == 17) {
            cm.openShop(10016);
            cm.dispose();
        } else if (selection == 18) {
            cm.openShop(10017);
            cm.dispose();
        } else if (selection == 19) {
            cm.openShop(10018);
            cm.dispose();
        } else if (selection == 20) {
            cm.openShop(10019);
            cm.dispose();
        } else if (selection == 21) {
            cm.openShop(10020);
            cm.dispose();
        } else if (selection == 22) {
            cm.openShop(10021);
            cm.dispose();
        } else if (selection == 23) {
            cm.openShop(10022);
            cm.dispose();
        } else if (selection == 24) {
            cm.openShop(10023);
            cm.dispose();
        } else if (selection == 25) {
            cm.openShop(10024);
            cm.dispose();
        } else if (selection == 26) {
            cm.openShop(10025);
            cm.dispose();
        } else if (selection == 27) {
            cm.openShop(10026);
            cm.dispose();
        } else if (selection == 28) {
            cm.openShop(10027);
            cm.dispose();
        } else if (selection == 29) {
            cm.openShop(10028);
            cm.dispose();
        } else if (selection == 30) {
            cm.openShop(10029);
            cm.dispose();
        } else if (selection == 31) {
            cm.openShop(10030);
            cm.dispose();
        } else if (selection == 32) {
            cm.openShop(10031);
            cm.dispose();
        } else if (selection == 33) {
            cm.openShop(10032);
            cm.dispose();
        } else if (selection == 34) {
            cm.openShop(10033);
            cm.dispose();
        } else if (selection == 35) {
            cm.openShop(10034);
            cm.dispose();
        } else if (selection == 36) {
            cm.openShop(10035);
            cm.dispose();
        } else if (selection == 37) {
            cm.openShop(100320);
            cm.dispose();
        } else if (selection == 38) {
            cm.openShop(10037);
            cm.dispose();
        } else if (selection == 39) {
            cm.openShop(10051);
            cm.dispose();
        } else if (selection == 40) {
            cm.openShop(10039);
            cm.dispose();
        } else if (selection == 41) {
            cm.openShop(10040);
            cm.dispose();
        } else if (selection == 42) {
            cm.openShop(10041);
            cm.dispose();
        } else if (selection == 43) {
            cm.openShop(10042);
            cm.dispose();
        } else if (selection == 44) {
            cm.openShop(10043);
            cm.dispose();
        } else if (selection == 45) {
            cm.openShop(10044);
            cm.dispose();
        } else if (selection == 46) {
            cm.openShop(10045);
            cm.dispose();
        } else if (selection == 47) {
            cm.openShop(10046);
            cm.dispose();
        } else if (selection == 48) {
            cm.openShop(10047);
            cm.dispose();
        } else if (selection == 49) {
            cm.openShop(10048);
            cm.dispose();
        } else if (selection == 50) {
            cm.openShop(10048);
            cm.dispose();
        } else if (selection == 51) {
            cm.openShop(10049);
            cm.dispose();
        } else if (selection == 52) {
            cm.openShop(10050);
            cm.dispose();
        } else if (selection == 53) {
            cm.openShop(10052);
            cm.dispose();
        } else if (selection == 54) {
            cm.openShop(10053);
            cm.dispose();
        } else if (selection == 55) {
            cm.openShop(10054);
            cm.dispose();
        } else if (selection == 56) {
            cm.openShop(10055);
            cm.dispose();
        } else if (selection == 57) {
            cm.openShop(10056);
            cm.dispose();
        } else if (selection == 58) {
            cm.openShop(10057);
            cm.dispose();
        } else if (selection == 59) {
            cm.openShop(10058);
            cm.dispose();
        } else if (selection == 60) {
            cm.openShop(10059);
            cm.dispose();
        } else if (selection == 61) {
            cm.openShop(10060);
            cm.dispose();
        } else if (selection == 62) {
            cm.openShop(10061);
            cm.dispose();
        } else if (selection == 63) {
            cm.openShop(10048);
            cm.dispose();
        } else if (selection == 64) {
            cm.openShop(10063);
            cm.dispose();
        } else if (selection == 65) {
            cm.openShop(10064);
            cm.dispose();
        } else if (selection == 66) {
            cm.openShop(10065);
            cm.dispose();
        } else if (selection == 67) {
            cm.openShop(10066);
            cm.dispose();
        } else if (selection == 68) {
            cm.openShop(10067);
            cm.dispose();
        } else if (selection == 69) {
            cm.openShop(10068);
            cm.dispose();
        } else if (selection == 70) {
            cm.openShop(10069);
            cm.dispose();
        } else if (selection == 71) {
            cm.openShop(13035);
            cm.dispose();
        } else if (selection == 72) {
            cm.openShop(13001);
            cm.dispose();
        } else if (selection == 73) {
            cm.openShop(13002);
            cm.dispose();
        } else if (selection == 74) {
            cm.openShop(13003);
            cm.dispose();
        } else if (selection == 75) {
            cm.openShop(13004);
            cm.dispose();
        } else if (selection == 76) {
            cm.openShop(13005);
            cm.dispose();
        } else if (selection == 94) {
            cm.openShop(13036);
            cm.dispose();
        } else if (selection == 95) {
            cm.openShop(13037);
            cm.dispose();
        } else if (selection == 96) {
            cm.openShop(13038);
            cm.dispose();
        } else if (selection == 97) {
            cm.openShop(13039);
            cm.dispose();
        } else if (selection == 98) {
            cm.openShop(13040);
            cm.dispose();
        } else if (selection == 99) {
            cm.openShop(13041);
            cm.dispose();
        }
    }
}