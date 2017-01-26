var status = 0;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
	if (mode == -1) {
		cm.dispose();
	} else {
		if (mode == 0 && status == 0) {
			cm.dispose();
			return;
		}
		if (mode == 1)
			status++;
		else
			status--;
		if (status == 0) {
		if (cm.getChar().getMapId() == 240050310) {
    			cm.sendSimple ("You want to Leave?\r\n\r\n#b#e#L10#Yes#l\r\n#L11#No#l");
		} else {
			cm.achieve(107);
			cm.sendSimple ("You want to enter PVP? Its dark and rough!\r\n\r\n#b#e#L0#Yes#l\r\n#L1#No#l");
	}
		} else if (status == 1) {
		if (selection == 0) {
		if (cm.getChar().getMapId() == 240050310) {
		cm.sendOk("You are already in PVP! Dont get killed! Go back to fighting!");
		cm.dispose();
	} else {
            cm.warp(240050310);
	    cm.sendOk("#fMob/9300093.img/stand/0##r#eFIGHT!#fMob/9001003.img/attack2/0#");
	    cm.dispose();
        }
	} else if (selection == 1) {
        cm.sendOk("Alright, Have a nice day.");
        cm.dispose();
	}
        else if (selection == 10) {
            cm.warp(100000000);
        cm.dispose();
	} else if (selection == 11) {
		cm.sendOk("Dont just stand there then, Go fight!");
		cm.dispose();
	}
}
}
}