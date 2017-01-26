var status = 0;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    selected = selection;
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
            cm.sendSimple("#n#eHello #h #, I can check if an item is not normally available (rare) for you.\r\n\r\nPick an item:#k\r\n\r\n" + cm.EquipList(cm.getC()));
        } else if (status == 1) {
            if (cm.getP().isRare(cm.getEquipId(selection))) {
                cm.sendOk("#g#eYes #i" + cm.getEquipId(selection) + "# is not found in any shops.#k\r\n\r\n\r\n#b#eIf you think this is incorrect please notify a staff member.");
            } else {
                cm.sendOk("#r#eNo #i" + cm.getEquipId(selection) + "# can be found.#k\r\n\r\n\r\n#b#eIf you think this is incorrect please notify a staff member.");
            }
        }
    }
}




