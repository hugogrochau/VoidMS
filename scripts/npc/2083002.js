var status = 0;

function start() {
		if (cm.getChar().getMapId() == 240050500) {
    cm.sendSimple ("Are you buffed up and ready to go back into battle?\r\nOr are you going to leave?\r\n\r\n#b#e#L0#Continue Battling!#l\r\n#L1#Leave...#l");
} else {
	cm.dispose();
}
}

function action(mode, type, selection) {
    if (mode == 1)
        status++;
    else {
        cm.dispose();
        return;
    }
    if (status == 1) {
        if (selection == 0) {
		cm.warp(240050310);
		cm.dispose();
	} else if (selection == 1) {
            cm.warp(100000000);
        cm.sendOk("Hope you Enjoyed PVP, Have fun!");
        cm.dispose();
	}
}
}