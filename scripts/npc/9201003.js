/**
  Mom and Dad - 9201003.js
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
    if (mode == 1)
        status++;
    else if (type == 1 && mode == 0) {
        cm.sendOk("Not ready to be married yet ? Thats understandable");
        cm.dispose();
    } else {
        cm.dispose();
        return;
    }
	
    var p = cm.getPlayer();
    if (p.getMarriageQuestLevel() == 51) {
        if (status == 0) {
            var sex
            if (p.getGender() == 1)
                sex = "man";
            else
                sex = "girl";
            cm.sendYesNo("Hello my child. Are you sure that you want to get married to this "+sex+"? I believe in love at first sight, but this is rather sudden... I don't think we are ready for this. Lets think about it. Do you really love this "+sex+"");
        } else if (status == 1) {
            p.addMarriageQuestLevel();
            cm.sendNext("Okay then. Go back to town and collect two more #bProof of Loves#k to prove it.");
            cm.dispose();
        }
    } else if (p.getMarriageQuestLevel() == 52) {
        var numberOfLoves = 0;
        if (status == 0) {
            for (var i = 4031367; i <= 4031372; i++)
                numberOfLoves += cm.itemQuantity(i);
            if (numberOfLoves >= 2) {
                cm.sendNext("Wow, you really are serious! Okay then, here is our blessing.");
            } else {
                cm.sendNext("Come back when you get two #bProof of Loves#k.");
                cm.dispose();
            }
        } else if (status == 1) {
            p.addMarriageQuestLevel();
            cm.removeAll(4031367);
            cm.removeAll(4031368);
            cm.removeAll(4031369);
            cm.removeAll(4031370);
            cm.removeAll(4031371);
            cm.removeAll(4031372);
            cm.gainItem(4031373, 1);
            cm.dispose();
        }
    } else {
        cm.sendOk("Hello we're Mom and Dad... (Daddy sucks cocks).");
        cm.dispose();
    }
}