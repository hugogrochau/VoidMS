/* * * * * * * * * * * * * * * \
*        Corine                *
*       By PeterPan            *
*        9201094               *
\ * * * * * * * * * * * * * * */

var status;
var weapons = [1332067, 1332068, 1332069, 1332070, 1332071, 1332072, 1382054, 1382055, 1382056, 1462047, 1462048, 1462049, 1472065, 1472066, 1472067, 1452054, 1452055, 1452056];
var take;

function start() {
    status = -1;
    action(1, 0, 0);

}

function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else {
        status--;
    }
    if (status == 0) {
        cm.sendSimple("Hello there friend! I'm Corine, the #rTaru Spirit#k. \r\nHow may I help you?\r\n\r\n#L0#I want one of those cool weapons!#l \r\n#L1#I want to upgrade my weapon#l");
    } else if (status == 1) {
        if (selection == 0) {
            var b = false;
            for (i = 0; i < weapons.length; i++) {
                if (cm.itemQuantity(weapons[i]) > 0) {
                    b = true;
                }
            }
            if (b) {
                cm.sendOk("You already have a weapon my friend!");
                cm.dispose();
            } else {
                cm.sendNext("Mmm, you look pretty strong, but you must still grow alot! I'll give you the basic weapon!");
            }
        } else if (selection == 1) {        
            if ((cm.itemQuantity(4031936)) && (cm.getPetMaxlevel() > 19)) {
                cm.loseItem(weapons);
                string = "#eWhich item should I upgrade?:\r\n#n";
                thing = 0;
                cm.sendSimple(string+cm.EquipList(cm.getC()));

            } else {
                cm.sendAcceptDecline("To upgrade you items, you need to prove you are a worthy Taru Warrior! All Taru warriors have a loyal #rPet#k, wich stays by their side as they hunt. I will give you your new weapon if you bring me a #i4031936#.\r\nAlso, your pet should be level 20+ \r\n\r\nI suggest that you go talk with Muhamad, the master craftsman, to craft the #rTaru Totem#k. The last time I checked he lived in #bKerning#k.");
                cm.dispose();
            }
        }
    } else if (status == 2) {
        for (i = 0; i < weapons.length; i++) {
            if (cm.itemQuantity(weapons[i]) > 0) {
                b = true;
            }
        }
        if (b) {
            take = cm.getPlayer().getEquipId(selection);
            if (take == 1472065) {
                cm.loseItem(1472065);
                cm.gainItem(1472066);
                cm.dispose();
            } else if (take == 1332067) {
                cm.loseItem(1332067);
                cm.gainItem(1332068);
                cm.dispose();
            } else if (take == 1332070) {
                cm.loseItem(1332070);
                cm.gainItem(1332071);
                cm.dispose();
            } else if (take == 1452054) {
                cm.loseItem(1452054);
                cm.gainItem(1452055);
                cm.dispose();
            } else if (take == 1462047) {
                cm.loseItem(1462047);
                cm.gainItem(1462048);
                cm.dispose();
            } else if (take == 1382054) {
                cm.loseItem(1382054);
                cm.gainItem(1382055);
                cm.dispose();

            } else {
                cm.sendSimple("Which weapon do you want? \r\n#L8#Claw #i1472065##l \r\n\r\n#L9#Dagger #i1332067##l \r\n\r\n#L10#Sword #i1332070##l \r\n\r\n#L11#Bow #i1452054##l \r\n\r\n#L12#Crossbow #i1462047##l \r\n\r\n#L13#Staff #i1382054##l");

            }
        }
    } else if (status == 3) {
        var item = 0;
        switch (selection) {
            case 8:
                item = 1472065;
                break;
            case 9:
                item = 1332067;
                break;
            case 10:
                item = 1332070;
                break;
            case 11:
                item = 1452054;
                break;
            case 12:
                item = 1462047;
                break;
            case 13:
                item = 1382054;
                break;
        }
        cm.sendOk("Here you go!");
        cm.gainItem(item);
        cm.dispose();
    }
}