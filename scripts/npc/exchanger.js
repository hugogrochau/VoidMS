//4002000 sns
//4002003 sls

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    pick = selection;
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
            cm.sendSimple("#eHello! I am the #dVoidMS#k currency NPC.\r\n\r\n#b#L0#I would like to trade 1 #i4002000# for 1bil mesos#l\r\n#L1#I would like to trade 1bil mesos for 1 #i4002000##l\r\n#L2#I would like to trade 10 #i4002000# for 1 #i4002003##l\r\n#L3#I would like to trade 1 #i4002003# for 10 #i4002000##l\r\n#L4#I would like to trade all my #i4002000# for #i4002003##l\r\n#L5#I would like to trade all my #i4002003# for #i4002000##l");
        } else if (pick == 0) {
            if (cm.itemQuantity(4002000)) {
                if (cm.getPlayer().getMeso() >= 1000000000) {
                    cm.gainItem(4002000, -1);
                    cm.gainMeso(1000000000);
                    cm.dispose();
                } else {
                    cm.sendOk("#eYou will have over the max meso limit! Please get under #b1.1bil#k so you don't waste your #i4002000#");
                }
            } else {
                cm.sendOk("#eYou do not have 1 #i4002000#");
                cm.dispose();
            }
        } else if (pick == 1) {
            if (cm.getPlayer().getMeso() >= 1000000000) {
                cm.gainItem(4002000);
                cm.gainMeso(-1000000000);
            } else {
                cm.sendOk("#eYou do not have 1bil mesos.")
            }
        } else if (pick == 2) {
            if (cm.itemQuantity(4002000) >= 10) {
                cm.gainItem(4002000, -10);
                cm.gainItem(4002003);
                cm.dispose();
            } else {
                cm.sendOk("#eYou do not have 10 #i4002000#");
            }
        } else if (pick == 3) {
            if (cm.itemQuantity(4002003)) {
                cm.gainItem(4002003, -1);
                cm.gainItem(4002000, 10);
            } else {
                cm.sendOk("#eYou do not have 1 #i4002003#");
            }
        } else if (pick == 4) {
            var sls = Math.floor(cm.itemQuantity(4002000)/10);
            if (sls > 0) {
                cm.gainItem(sls, 4002003);
                cm.gainItem(-(sls * 10), 4002000);
            } else {
                cm.sendOk("#eYou do not have at least 10 #i4002000#")
            }
        } else if (pick == 5) {
            var sns = cm.itemQuantity(4002003) * 10;
            if (sns > 0) {
                cm.gainItem(sns, 4002000);
                cm.gaineItem(-(sns/10), 4002003);
            } else {
                cm.sendOk("#eYou do not have any #i4002003#");
            }
        }
    }
}