var choice = -1;
var choice1 = -1
var text = "";
var item = -1;
var items = [ 
[[3010036],[3010127],[3010126],[3010128],[3010096],[3010095],[3010110],[3010108],[3010097],[3010069],[3010118],[3010116]
,[3010092],[3010080],[3010111],[3010106],[3010073],[3010070],[3010099],[3010085],[3010071],[3010068]
,[3010064],[3010062],[3010060],[3010058],[3010057],[3010047],[3010046],[3010045],[3010055],[3010049],[3010021]
,[3012005],[3010028],[3010017],[3010016],[3010072],[3010086],[3010088],[3017950],[3010094],[3012006],[3017003],[3010091]
,[3013000],[3017002],[3017001],[3011518],[3010116],[3010078],[3010079],[3012008],[3010112],[3013006],[3013030],[3011006]
,[3011004],[3011003],[3011002],[3011001],[3010077],[3010093],[3010074],[3010075],[3012011],[3012010],[3010137],[3010152]
,[3010120],[3010117],[3010115],[3010109],[3010107],[3010101],[3010034],[3010035],[3010038],[3010037]], //Chairs
[[1902014,1912010],[1902011,1912007],[1902017,1912011],[1902015,1912011],[1902016,1912011],[1902019,1912012],
[1902020,1912013],[1902021,1912014],[1902022,1912015],[1902023,1912016],[1902024,1912017],[1902025,1912018],
[1902027,1912020],[1902028,1912021],[1902031,1912024],[1902032,1912025],[1902033,1912026],[1902034,1912027],
[1902035,1912028],[1902036,1912029],[1902037,1912030],[1902038,1912031],[1902039,1912032],[1902040,1912033],
[1902041,1912034],[1902043,1912036],[1902044,1912037],[1902045,1912038],[1902046,1912039],
[1902050,1912050],[1902056,1912049]],// Mount Saddle
[["600 stat points",5], ["GM Job",200], ["500 on all stats and 75 WAtt Earring",75], ["Wizet Suit",25], ["Wizet Pants",25]]]; // Others
var sections = ["Chairs","Mounts & Saddles", "Others"];
var status = -1;
 
function start() {
    var text = "What items would you like to buy today for Green Wish Tickets?#n#b\r\n";
    for (i=0;i<sections.length;i++)
        text+= "\r\n#L"+i+"#"+sections[i];
    cm.sendSimple(text);
}
 


function action(mode, type, selection) {
    if (mode == 1) {
        status++;
        if (status == 0) {
            choice = selection;
            text = "#eHere are all the " + sections[selection].toLowerCase() + ".\r\nThey are 5 #i4031544# #rGreen Wish Tickets#k each:#n\r\n";
            if (selection == 1) {
                for (i=0;i<items[selection].length;i++)
                    text += "\r\n\r\n#L" + i + "##i" + items[selection][i][0] + "##i" + items[selection][i][1];
            } else if (selection == 0) {
                text = "#eHere are all the " + sections[selection].toLowerCase() + ".\r\nThey are 2 #i4031544# #rGreen Wish Tickets#k each:#n\r\n";
                for (i=0;i<items[selection].length;i++)
                    text += "\r\n\r\n#L" + i + "##i" + items[selection][i][0];
            } else {
                text = "#eWhat would you like?#n#b\r\n";
                for (i=0;i<items[selection].length;i++)
                    text += "\r\n\r\n#L" + i + "#" + items[selection][i][0] + " for " + items[selection][i][1] + " #rGreen Wish Tickets#k#b";
            }
            cm.sendSimple(text);
        } else if (status == 1) {
            choice1 = selection;
            item = items[choice][selection][0];
            if (choice == 1 && cm.itemQuantity(4031544) >= 5) {
                cm.sendYesNo("#eAre you sure you want to buy #i" + item + "##i" + items[choice][selection][1] + "# for #r5 #i4031544#");
            } else if (choice == 0 && cm.itemQuantity(4031544) >= 2) {
                cm.sendYesNo("#eAre you sure you want to buy #i" + item + "# for #r2 #k#i4031544#?");
            } else if (choice == 2 && cm.itemQuantity(4031544) >= items[choice][selection][1]) {
                cm.sendYesNo("#eAre you sure you want to buy #r" + item + "#k for #r" + items[choice][selection][1] + " Green Wish Tickets#k?");
            } else {
                cm.sendOk("#rYou dont seem to have enough Green Wish Tickets");
                cm.dispose();
            }
        } else if (status == 2) {
            var item2 = -1;
            if (choice == 1) item2 = items[choice][choice1][1];
            if (!cm.isFull()) {
                if (choice < 2) {
                    cm.gainItem(item, 1);
                    if (item2 > 0) cm.gainItem(item2, 1);
                    if (choice == 1)
                        cm.loseItem(4031544, 5);
                    if (choice == 0)
                        cm.loseItem(4031544, 2);
                    text = "#eEnjoy your #i" + item + "#";
                    if (item2 > 0) text += "#i" + item2 +"#";
                } else {
                    text = "#eEnjoy your #r" + item;
                    switch (choice1) {
                        case 0:
                            cm.loseItem(4031544,5);
                            cm.gainAp(600);
                            break;
                        case 1:
                            cm.loseItem(4031544, 200);
                            cm.getPlayer().setJob(900);
                            break;
                        case 2:
                            cm.loseItem(4031544,75);
                            cm.makeItem(1032000, cm.getP(), 500,500,500,500,75);
                            //cm.reloadChar();
                            break;
                        case 3:
                            cm.loseItem(4031544, 25);
                            cm.gainItem(1042003, 1);
                            break;
                        case 4:
                            cm.loseItem(4031544, 25);
                            cm.gainItem(1062007, 1);
                            break;
                    }
                }
                cm.sendOk(text);
            } else {
                cm.sendOk("#r#eSorry, you do not have enough of room in your inventory.");
            }
            cm.dispose();
        }
    } else {
        cm.dispose();
    }
}