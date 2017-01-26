/**
  Nana(P) - 9201027.js
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
    if (mode != 1) {
        cm.dispose();
        return;
    }
    var mqlvl = cm.getPlayer().getMarriageQuestLevel();
    if (mqlvl == 1 || mqlvl == 52) {
        if (!cm.haveItem(4000018, 40)) {
            if (status == 0) {
                cm.sendNext("Hey, you look like you need some proof of love. I can get them for you.");
            } else if (status == 1) {
                cm.sendNext("Please bring back 40 #bFirewood#k and I'll get started.");
                cm.dispose();
            }
        } else {
            if (status == 0) {
                cm.sendNext("Wow, you were quick! Heres the proof of love...");
                cm.gainItem(4000018, -40);
                cm.gainItem(4031371, 1);
                cm.dispose();
            }
        }
    } else {
        cm.sendOk("Hi, I'm Nana the love fairy... Hows it going?");
        cm.dispose();
    }
}