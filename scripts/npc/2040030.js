var status = -1;
var choice = -1;
var choice1 = -1
var sns = -1;
var item = -1;
var items = [
[[1102228,45],[1102216,45],[1102215,45],[1102184,50],[1102223,45],[1102258,45],[1102186,45],[1102187,45]], // Capes
[[1042183,55],[1042194,55],[1042177,55],[1042151,55],[1042196,55],[1042168,55],[1042199,55],[1042169,55]], // Shirts
[[1062126,35],[1062107,35],[1062114,35],[1062116,35],[1062122,35],[1062113,35],[1062104,35],[1062118,35]], // Pants
[[1052173,45],[1052170,70],[1052231,45],[1052185,45],[1051180,45],[1052296,45],[1052169,45],[1052203,45]], // Overalls
[[1082292,25],[1082263,25],[1082272,25],[1082261,25]], // Gloves
[[1002995,65],[1002849,65],[1002735,65],[1003186,25],[1002978,65],[1002936,65],[1003013,65],[1002890,65],[1002842,65]], // Hats
[[1012191,35],[1022110,35],[1032078,35],[1012176,35],[1022075,35],[1032083,35],[1022083,35]], // Accessories
[[1902048,45,1912041],[1902300,45,1912300],[1902004,45,1912002],[1902013,45,1912009],[1902012,45,1912008],[1902018,45,1912011],[1902026,45,1912019],[1902042,45,1912035]], // Mounts
[[1072482,30],[1072395,30],[1072330,30],[1072448,30],[1072331,30],[1072469,30],[1072464,30]], // Shoes
[[1092051,20]], // Shields
[[1302142,45],[1312056,45],[1322084,45],[1332114,45],[1372071,45],[1382093,45],[1402085,45],[1412055,45],[1422057,45],[1432075,45],[1442104,45],[1452100,45],[1462085,45],[1472111,45],[1482073,45],[1492073,45]], // Pyrope Weapons
[[1702263, 70],[1702223, 70],[1702237, 70],[1702211, 70],[1702226, 70],[1702207, 70],[1702208, 70],[1702277,70],[1702233,70],[1702258,70],[1702264,70]], // Cash Weapons
];
var sections = ["Capes","Shirts","Pants","Overalls","Gloves","Hats","Accessories","Mounts","Shoes","Shields","Pyrope Weapons","Cash Weapons"];

function start() {
    var text = "#eHello #h #! I am the items vendor.\r\n\r\nWhat would you like to buy today?#n#b\r\n";
    for (i=0;i<sections.length;i++)
        text+= "\r\n#L"+i+"#"+sections[i];
    cm.sendSimple(text);
}


function action(mode, type, selection) {
    if (mode == 1) {
        status++;
        if (status == 0) {
            choice = selection;
            var text = "#eHere are all the " + sections[selection].toLowerCase() + " I have in stock:#n#b\r\n";
            if (selection == 7) {
                for (i=0;i<items[selection].length;i++)
                    text += "\r\n\r\n#L" + i + "##i" + items[selection][i][0] + "##i" + items[selection][i][2] +"# for #r" + items[selection][i][1] + "#b #i4002000#";
            } else {
                for (i=0;i<items[selection].length;i++)
                    text += "\r\n\r\n#L" + i + "##i" + items[selection][i][0] + "# for #r" + items[selection][i][1] + "#b #i4002000#";
            }
            cm.sendSimple(text);
        } else if (status == 1) {
            choice1 = selection;
            sns = items[choice][selection][1];
            item = items[choice][selection][0];
            if (choice == 7)
                cm.sendYesNo("#eAre you sure you want to buy #i" + item + "##i" + items[choice][selection][2] + "# for #r" + sns + "#k #i4002000#?");
            else
                cm.sendYesNo("#eAre you sure you want to buy #i" + item + "# for #r" + sns + "#k#i4002000#?");
        } else if (status == 2) {
            var item2 = -1;
            if (choice == 7) item2 = items[choice][choice1][2];
            if (cm.itemQuantity(4002000) >= sns) {
                if (!cm.isFull()) {
                    cm.gainItem(item, 1);
                    if (item2 > 0) cm.gainItem(item2, 1);
                    cm.loseItem(4002000, sns);
                    text = "#eEnjoy your #i" + item + "#";
                    if (item2 > 0) text += "#i" + item2 +"#";
                    cm.sendOk(text);
                } else {
                    cm.sendOk("#rSorry, you do not have enough of room in your inventory.");
                    cm.dispose();
                }
            } else {
                cm.sendOk("#r#eYou do not have enough #i4002000##k\r\n\r\nYou have #r" + cm.itemQuantity(4002000) + "#k #i4002000# out of the #g" + sns + "#k needed");
            }
            cm.dispose();
        }
    } else {
        cm.dispose();
    }
}