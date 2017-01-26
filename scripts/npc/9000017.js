

var status = 0;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (mode == 2 && status == 0) {
            cm.dispose();
            return;
        }
        if (mode == 1)
            status++;
        else
            status--;
        if (status == 0) {
            cm.sendSimple("#eHello #h #, I can sell you some counterfeit fame brother. How much do you need?#n#b\r\n#L1#1 fame for 1,000,000 mesos#l\r\n#L5#5 fame for 5,000,000 mesos#l\r\n#L15#15 fame for 15,000,000 mesos#l\r\n\r\n#L100#-1 fame for 1,000,000 mesos#l\r\n#L101#-5 fame for 5,000,000 mesos#l\r\n#L102#-15 fame for 15,000,000 mesos#l");
        } else if (status == 1) {
		if (selection == 100) {
			if (cm.getMeso() >= 1000000) {
				cm.gainMeso(-1000000);
				cm.gainFame(-1);
				cm.dispose();
			} else {
				cm.sendOk("You do not have enough Mesos.");
				cm.dispose();
		}
		} else if (selection == 101) {
			if (cm.getMeso() >= 5000000) {
				cm.gainMeso(-5000000);
				cm.gainFame(-5);
				cm.dispose();
			} else {
				cm.sendOk("You do not have enough Mesos.");
				cm.dispose();
		}
		} else if (selection == 102) {
			if (cm.getMeso() >= 15000000) {
				cm.gainMeso(-15000000);
				cm.gainFame(-15);
				cm.dispose();
			} else {
				cm.sendOk("You do not have enough Mesos.");
				cm.dispose();
		}
		} else if (selection == 1) {
			if (cm.getMeso() >= 1000000) {
				cm.gainMeso(-1000000);
				cm.gainFame(1);
				cm.dispose();
			} else {
				cm.sendOk("You do not have enough Mesos.");
				cm.dispose();
		}
		} else if (selection == 5) {
			if (cm.getMeso() >= 5000000) {
				cm.gainMeso(-5000000);
				cm.gainFame(5);
				cm.dispose();
			} else {
				cm.sendOk("You do not have enough Mesos.");
				cm.dispose();
		}
		} else if (selection == 15) {
			if (cm.getMeso() >= 15000000) {
				cm.gainMeso(-15000000);
				cm.gainFame(15);
				cm.dispose();
			} else {
				cm.sendOk("You do not have enough Mesos.");
				cm.dispose();
		}
		}
	}
    }
}