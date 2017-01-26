/**
  Moony - 9201000.js
-- Original Author --------------------------------------------------------------------------------
	Jvlaple
-- Modified by -----------------------------------------------------------------------------------
	XoticMS.
---------------------------------------------------------------------------------------------------
**/

var status;
var ring = new Array(2240000, 2240001, 2240002, 2240002);
 
function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 1)
        status++;
    else if (type == 0 && mode == 0)
        status--;
    else {
	if (type == 1 && mode == 0)
		cm.sendOk("Maybe another time huh ?");
        cm.dispose();
        return;
    }
	
    var mqLvl = cm.getPlayer().getMarriageQuestLevel();
    if (status == 0) {
        if(cm.getLevel() >= 10) {
            if (mqLvl == 0)
                cm.sendNext("Hey, I'm Moony, and I make engagement rings for marriage.");
            else if (mqLvl == 1) {
                var numberOfLoves = 0;
                for (var i = 4031367; i <= 4031372; i++)
                    numberOfLoves += cm.itemQuantity(i);
                if (numberOfLoves >= 4) {
                    cm.sendNext("Wow, you're back pretty early. Got the #bProof of Loves#k? Lets see...");
                } else {
                    cm.sendOk("Please come back when you got 4 different #bProof of Loves#k.");
                    cm.dispose();
                }
            } else if (mqLvl == 2) {
                var text = "Hey, you're back! Ready to choose your ring ?#b";
                for (var i = 0; i < ring.length; i++)
                    text += "\r\n#L"+i+"##t"+ring[i]+"##l";
                cm.sendSimple(text);
            } else { // already has ring ?
                cm.sendOk("I hate making rings...");
                cm.dispose();
            }
        } else {
            cm.sendOk("Please get to level 10 before talking to me");
            cm.dispose();
        }
    } else if (status == 1) {
        if (mqLvl == 0)
            cm.sendYesNo("It looks like you want to get married! Want to make an engagement ring?");
        else if (mqLvl == 1)
            cm.sendNext("Great work getting the #bProof of Loves#k! Now we can make the #bEngagement Ring#k.");
        else if (mqLvl == 2) {
            var reqItem = new Array(4011007, 4021009, 4011006, 4011004);
            var price = new Array(3000000, 2000000, 1000000, 500000);
            if (cm.haveItem(reqItem[selection]) && cm.haveItem(4021007) && cm.getMeso() >= price[selection]) {
                cm.gainItem(reqItem[selection], -1);
                cm.gainItem(4021007, -1);
                cm.gainMeso(-price[selection]);
                cm.gainItem(ring[selection], 1);
                cm.sendOk("Here's the ring as promised! Have fun!");
                cm.getPlayer().addMarriageQuestLevel();
            } else {
                cm.sendNext("You did not get all the right materials. To make an engagement ring, I need one of the following:\r\n\r\n#e#dMoonstone Ring:#k\r\n#v4011007#Moon Rock 1,#v4021007#Diamond 1, 3,000,000 Meso\r\n#dStar Gem Ring:#k\r\n#v4021009#Star Rock 1,#v4021007#Diamond 1, 2,000,000 Meso\r\n#dGolden Heart Ring:#k\r\n#v4011006#Gold Plate 1,#v4021007#Diamond 1, 1,000,000 Meso\r\n#dSilver Swan Ring:#k\r\n#v4011004#Silver Plate 1,#v4021007#Diamond 1, 500,000 Meso\r\n");
            }
            cm.dispose();
        }
    } else if (status == 2) {
        if (mqLvl == 0) {
            cm.getPlayer().addMarriageQuestLevel();
            cm.sendOk("Okay, first bring me back any four colored #bProof of Loves#k. You can get them from talking to #bNana the Love Fairy#k in any town. Also, only one of you, either the Groom or Bride will do this quest.");
            cm.dispose();
        }else if (mqLvl == 1) {
            cm.removeAll(4031367);
            cm.removeAll(4031368);
            cm.removeAll(4031369);
            cm.removeAll(4031370);
            cm.removeAll(4031371);
            cm.removeAll(4031372);
            cm.getPlayer().addMarriageQuestLevel();
            cm.sendNextPrev("You need the following raw materials to make an\r\n#bEngagement Ring#k.\r\n\r\n#e#dMoonstone Ring:#k\r\n#v4011007#Moon Rock 1,#v4021007#Diamond 1, 3,000,000 Meso\r\n#dStar Gem Ring:#k\r\n#v4021009#Star Rock 1,#v4021007#Diamond 1, 2,000,000 Meso\r\n#dGolden Heart Ring:#k\r\n#v4011006#Gold Plate 1,#v4021007#Diamond 1, 1,000,000 Meso\r\n#dSilver Swan Ring:#k\r\n#v4011004#Silver Plate 1,#v4021007#Diamond 1, 500,000 Meso\r\n");
            cm.dispose();
        }
    }
}