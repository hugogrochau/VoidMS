/**  
  Amos the Strong - 9201043.js
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
    var mySelection = selection;
	
    if (mode == 1)
        status++;
    else if (type == 1 && mode == 0 && mySelection == 0) {
        cm.sendOk("Dont talk to me unless you would like to enter !");
        cm.dispose();
    } else if (type == 1 && mode == 0 && mySelection == 1) {
        cm.sendOk("I see, I'll give the ticket to someone worthy.");
        cm.dispose();
    } else {
        cm.dispose();
        return;
    }
	
    if (status == 0)
        cm.sendSimple("My name is Amos the Strong. What would you like to do?\r\n#b#L0#Enter the Amorian Challenge!#l\r\n#L1#Trade 10 Keys for a Ticket!#l\r\n");
    else if (status == 1) {
        mySelection = selection;
        if (selection == 0) {
            if (cm.haveItem(4031592) && cm.getPlayer().isMarried()) {
                cm.sendYesNo("So you would like to enter the #bEntrance#k?");
            } else {
                cm.sendOk("You must have an Entrance Ticket to enter, and you have to be married.");
                cm.dispose();
            }
        } else if (selection == 1) {
            if (cm.haveItem(4031593, 10)) {
                cm.sendYesNo("So you would like a Ticket?");
            } else {
                cm.sendOk("Please get me 10 Keys first!");
                cm.dispose();
            }
        }
    } else if (status == 2) {
        if (mySelection == 0) {
            cm.warp(670010100, 0);
            cm.gainItem(4031592, -1);
        } else if (mySelection == 1) {
            cm.gainItem(4031593, -10);
            cm.gainItem(4031592, 1);
        }
        cm.dispose();
    }
}