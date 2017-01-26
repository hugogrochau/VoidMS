var snailstamp = 4002000;
var slimestamp = 4002003;
var yellowwish = 4031543;
var greenwish = 4031544;
var bluewish = 4031545;
var finished = "#eHave a fun day playing VoidMS!";
var notenough = "#eYou do not have the required materials to make this item. If you believe this is incorrect, please contact a staff member with @gmmsg";
var status;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 1)
        status++;
    else {
        cm.dispose();
        return;
    }
    if (status == 0) {
        cm.sendYesNo("#eHello #h #. I am the #rItem Exchanger#k.\r\n\r\n#eI can exchange items such as:\r\n#v" + yellowwish + "# #v" + greenwish + "# #v" + bluewish + "# #v" + snailstamp + "# #v" + slimestamp + "#.\r\nWould you like to use my services?");
    } else if (status == 1) {
        cm.sendSimple("#eWhat do you want to exchange?\r\n\r\n#b#L0#10 #v" + yellowwish + "# for 1 #v" + greenwish + "#\r\n#L1#10 #v" + greenwish + "# for 1 #v" + bluewish + "#\r\n#L2#1 #v" + bluewish + "# for 10 #v" + greenwish + "#\r\n#L3#1 #v" + greenwish + "# for 10 #v" + yellowwish + "#\r\n#L4#1 #v" + slimestamp + "# for 10 #v" + snailstamp + "#\r\n#L5#1,000,000,000 mesos for 1 #v" + snailstamp + "#\r\n#L6#10 #v" + snailstamp + "# for 1 #v" + slimestamp + "#\r\n#L7#1 #v" + snailstamp + "# for 1,000,000,000 mesos#l");
    } else if (status == 2) {
        switch(selection) {
            case 0:
                if(cm.haveItem(yellowwish, 10)) {
                    cm.gainItem(yellowwish, -10);
                    cm.gainItem(greenwish, 1);
                    cm.sendOk(finished);
                } else {
                    cm.sendOk(notenough);
                }
                break;
            case 1:
                if(cm.haveItem(greenwish, 10)) {
                    cm.gainItem(greenwish, -10);
                    cm.gainItem(bluewish, 1);
                    cm.sendOk(finished);
                } else {
                    cm.sendOk(notenough);
                }
                break;
            case 2:
                if(cm.haveItem(bluewish, 1)) {
                    cm.gainItem(bluewish, -1);
                    cm.gainItem(greenwish, 10);
                    cm.sendOk(finished);
                } else {
                    cm.sendOk(notenough);
                }
                break;
            case 3:
                if(cm.haveItem(greenwish, 1)) {
                    cm.gainItem(greenwish, -1);
                    cm.gainItem(yellowwish, 10);
                    cm.sendOk(finished);
                } else {
                    cm.sendOk(notenough);
                }
                break;
            case 4:
                if(cm.haveItem(slimestamp, 1)) {
                    cm.gainItem(slimestamp, -1);
                    cm.gainItem(snailstamp, 10);
                    cm.sendOk(finished);
                } else {
                    cm.sendOk(notenough);
                }
                break;
            case 5:
                if(cm.getMeso() >= 1000000000) {
                    cm.gainMeso(-1000000000);
                    cm.gainItem(snailstamp, 1);
                    cm.sendOk(finished);
                } else {
                    cm.sendOk(notenough);
                }
                break;
            case 6:
                if(cm.haveItem(snailstamp, 10)) {
                    cm.gainItem(snailstamp, -10);
                    cm.gainItem(slimestamp, 1);
                    cm.sendOk(finished);
                } else {
                    cm.sendOk(notenough);
                }
                break;
            case 7:
                if(cm.haveItem(snailstamp, 1)) {
                    cm.gainItem(snailstamp, -1);
                    cm.gainMeso(1000000000);
                    cm.sendOk(finished);
                } else {
                    cm.sendOk(notenough);
                }
                break;
        }
    }
}