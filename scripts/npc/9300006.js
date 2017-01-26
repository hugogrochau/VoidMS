var status = -1;
var section = -1;
var maps = [220000006, 100000202, 610020000, 610020001, 682000200, 280020000, 280020001, 109040001, 109040002, 109040003, 109040004, 101000100, 101000101, 101000102, 101000103, 101000104, 105040310, 105040311, 105040312, 105040313, 105040314, 105040315, 105040316, 103000900, 103000901, 103000903, 103000904, 103000906, 103000907, 103000908]
var choice = -1;
var sel = -1;
var text;
var jqp = -1;
var item = -1;
var items = [
[[1302147, 50], [1312062, 50], [1322090, 50], [1332120, 50], [1332125, 55], [1372078, 50], [1382099, 50], [1402090, 50], [1412062, 50], [1422063, 50], [1432081, 50], [1442111, 50], [1452106, 50], [1462091, 50], [1472117, 50], [1482079, 50], [1492079, 50]], // VIP Weapns
[[1302146, 35], [1312061, 35], [1322089, 35], [1332119, 35], [1332124, 40], [1372077, 35], [1382098, 35], [1402089, 35], [1412061, 35], [1422062, 35], [1432080, 35], [1442110, 35], [1452105, 35], [1462090, 35], [1472116, 35], [1482078, 35], [1492078, 35]], // Unwelcome Weapons
[[3010029, 40], [3010030, 40], [3010031, 45], [3010032, 40], [3010033, 40]]] // Other Weapons 33
var sections = ["VIP Weapons [Special Animation]","Unwelcome Weapons","Neon Chairs"];

function start() {
    cm.sendSimple("#eHello #h #! I am the #rJump Quest#k NPC.\r\nWhat would you like to do today?#n\r\n\r\n#b#L0#Finish JQs to win JQ points\r\n#L1#Spend JQ points on items");
}

function action(mode, type, selection) {
    if (mode == 1) {
        status++;
        if (status == 0) {
            section = selection;
            if (section == 0) {
                var selStr = "#eSelect a JQ map:#n\r\n\r\n";
                selStr += cm.isPractice() ? "#r#L1337#Turn practice mode off#k#l" : "#g#L1337#Turn practice mode on#k#l";
                for (var i = 0; i < maps.length; i++) {
                    selStr += "\r\n\r\n#b#L" + i + "##m" + maps[i] + "##k\r\n(JQ Points: #r" + cm.getJQPoints(maps[i]) + "#k Time Limit: #r" + cm.getTime(maps[i]) + "#k sec)";
                }
                cm.sendSimple(selStr);
            } else {
                text = "#eWhat would you like to spend your #r" + cm.getKarma() + "#k JQ point" + (cm.getKarma() > 1 ? "s" : "") + " on today?#n#b\r\n";
                for (i=0;i<sections.length;i++)
                    text += "\r\n#L" + i + "#" + sections[i];
                cm.sendSimple(text);
            }
        } else if (status == 1) {
            if (section == 0) {
                if (selection == 1337) {
                    cm.togglePractice();
                    status = -1;
                    action(1, 0, 0);
                } else {
                    cm.scheduleJQ(maps[selection]);
                    if (!cm.isPractice()) cm.sendOk("#eGood luck. Your time starts #rnow!#k\r\n\r\nUse #r@giveup#k if it's too hard.");
                    cm.dispose();
                }
            } else {
                sel = selection;
                text = "#eChoose a weapon to spend your #r" + cm.getKarma() + "#k JQ point" + (cm.getKarma() > 1 ? "s" : "") + " on:\r\n#b";
                for (i=0;i<items[sel].length;i++)
                    text += "\r\n#L" + i + "##i" + items[sel][i][0] + "# for #r" + items[sel][i][1] + "#k#b JQ points";
                cm.sendSimple(text);
            }
        } else if (status == 2) {
            choice = selection;
            jqp = items[sel][choice][1];
            item = items[sel][choice][0];
            cm.sendYesNo("#eAre you sure you want to buy #i" + item + "# for #r" + jqp + "#k JQ point" + (cm.getKarma() > 1 ? "s" : "") + "?");
        } else if (status == 3) {
            if (cm.getKarma() >= jqp) {
                if (!cm.isFull()) {
                    cm.gainItem(item, 1);
                    cm.gainKarma(-jqp);
                    cm.sendOk("#eEnjoy your #i" + item + "#");
                } else {
                    cm.sendOk("#rSorry, you do not have enough of room in your inventory.");
                }
            } else {
                cm.sendOk("#r#eYou do not have enough JQ point" + (cm.getKarma() > 1 ? "s" : "") + "#k\r\n\r\nYou have #r" + cm.getKarma() + "#k JQ point" + (cm.getKarma() > 1 ? "s" : "") + " out of the #g" + jqp + "#k needed");
            }
            cm.dispose();
        }
    } else
        cm.dispose();
}