function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (status >= 0 && mode == 0) {
            cm.dispose();
            return;
        }
        if (mode == 1)
            status++;
        else
            status--;
        if (status == 0) {
	if (cm.isEtcFull()) {
		cm.sendOk("#ePlease make some room in your inventory.");
		cm.dispose();
	} else {
            cm.sendSimple("#eHello! I am the #bVoidMS#k currency NPC.\r\nWhat would you like to do today?\r\n\r\n#n#b#L0#I would like to trade a #i4002000# for 1,000,000,000 mesos\r\n#L1#I would like to trade 1,000,000,000 mesos for a #i4002000#\r\n#L2#I would like to trade 10 #i4002000# for a #i4002003#\r\n#L3#I would like to trade a #i4002003# for 10 #i4002000#\r\n#L4#I would like to trade all my #i4002000# for #i4002003#\r\n#L5#I would like to trade all my #i4002003# for #i4002000#");
	}
        } else if (status == 1) {
            if (cm.isEtcFull()) {
                cm.sendOk("#rYou do not have enough space in your inventory");
                cm.dispose();
                return;
            }
            if (selection == 0) { // sns -> meso
                if (cm.itemQuantity(4002000) > 0) {
                    if (cm.getPlayer().getMeso() <= 1147483647) { // if player has enough space for mesos
                        cm.gainItem(4002000, -1);
                        cm.gainMeso(1000000000);
                    } else {
                        cm.sendOk("#eDoing this will make you go over the max meso limit. Please get under #r1147483647#k mesos!");
                    }
                } else {
                    cm.sendOk("#eYou do not have a #i4002000#");
                }
            } else if (selection == 1) { // meso -> sns
                if (cm.getPlayer().getMeso() >= 1000000000) {
                    cm.gainItem(4002000);
                    cm.gainMeso(-1000000000);
                    cm.dispose();
                } else {
                    cm.sendOk("#eYou do not have 1,000,000,000 mesos.")
                }
            } else if (selection == 2) { // sns -> sls
                if (cm.itemQuantity(4002000) >= 10) {
                    cm.gainItem(4002000, -10);
                    cm.gainItem(4002003);
                } else {
                    cm.sendOk("#eYou do not have 10 #i4002000#");
                }
            } else if (selection == 3) { // sls -> sns
                if (cm.itemQuantity(4002003) > 0) {
                    cm.gainItem(4002003, -1);
                    cm.gainItem(4002000, 10);
                } else {
                    cm.sendOk("#eYou do not have a #i4002003#");
                }
            } else if (selection == 4) {
                var sls = Math.floor(cm.itemQuantity(4002000)/10); // number of sns divided by 10 and rounded down = number of sls to give.
                if (sls > 0) {
                    cm.gainItem(4002000, -(sls * 10)); // number of sls to give * 10 to = number of sns to take away
                    cm.gainItem(4002003, sls);
                } else {
                    cm.sendOk("#eYou do not have at least 10 #i4002000#")
                }
            } else if (selection == 5) {
                var sns = cm.itemQuantity(4002003) * 10; // number of sls * 10 = number of sns to give
                if (sns > 0) {
                    cm.gainItem(4002000, sns);
                    cm.gainItem(4002003, -(sns/10)); // number of sns to give / 10 = number of sls to take away
                } else {
                    cm.sendOk("#eYou do not have any #i4002003#");
                }
            }
            cm.dispose();
        }
    }
}