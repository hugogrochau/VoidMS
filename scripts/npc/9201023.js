/**
  Nana(K) - 9201023.js
-- Original Author --------------------------------------------------------------------------------
	Jvlaple
-- Modified by -----------------------------------------------------------------------------------
	XoticMS.
---------------------------------------------------------------------------------------------------
**/

var status;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else {
        cm.dispose();
        return;
    }
	
    var mqlvl = cm.getPlayer().getMarriageQuestLevel();
    if (mqlvl == 1 || mqlvl == 52) {
        if (!cm.haveItem(4000015, 40)) {
            if (status == 0) {
                cm.sendNext("Hey, you look like you need proofs of love? I can get them for you.");
            } else if (status == 1) {
                cm.sendNext("All you have to do is bring me 40 #bHorned Mushroom Caps#k.");
                cm.dispose();
            }
        } else {
            if (status == 0) {
                cm.sendNext("Wow, you were quick! Heres the proof of love...");
                cm.gainItem(4000015, -40);
                cm.gainItem(4031367, 1);
                cm.dispose();
            }
        }
    } else {
        cm.sendOk("Hi, I'm Nana the love fairy... Hows it going?");
        cm.dispose();
    }
}
					