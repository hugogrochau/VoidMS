var status = -1;
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
/*var items = [
[1302146, 100], [1302147, 100], [1312061, 100], [1312062, 100], [1322089, 100],
[1322090, 100], [1332119, 100], [1332120, 100], [1332124, 100], [1332125, 100],
[1372077, 100], [1372078, 100], [1382098, 100], [1382099, 100], [1402089, 100],
[1402090, 100], [1412061, 100], [1412062, 100], [1422062, 100], [1422063, 100],
[1432080, 100], [1432081, 100], [1442110, 100], [1442111, 100], [1452105, 100],
[1452106, 100], [1462090, 100], [1462091, 100], [1472116, 100], [1472117, 100],
[1482078, 100], [1482079, 100], [1492078, 100], [1492079, 100]];*/



function start() {
    text = "#eHello #h #! I am the JQ points exchanger.\r\n\r\nWhat would you like to spend your #r" + cm.getKarma() + "#k JQ point" + (cm.getKarma() > 1 ? "s" : "") + " on today?#n#b\r\n";
    for (i=0;i<sections.length;i++)
        text += "\r\n#L" + i + "#" + sections[i];
    cm.sendSimple(text);
}

function action(mode, type, selection) {
    if (mode == 1) {
        status++;
        if (status == 0) {
            sel = selection;
            text = "#eChoose a weapon to spend your #r" + cm.getKarma() + "#k JQ point" + (cm.getKarma() > 1 ? "s" : "") + " on:\r\n#b";
            for (i=0;i<items[sel].length;i++)
                text += "\r\n#L" + i + "##i" + items[sel][i][0] + "# for #r" + items[sel][i][1] + "#k#b JQ points";
            cm.sendSimple(text);
        } else if (status == 1) {
            choice = selection;
            jqp = items[sel][choice][1];
            item = items[sel][choice][0];
            cm.sendYesNo("#eAre you sure you want to buy #i" + item + "# for #r" + jqp + "#k JQ point" + (cm.getKarma() > 1 ? "s" : "") + "?");
        } else if (status == 2) {
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
    } else {
        cm.dispose();
    }
}