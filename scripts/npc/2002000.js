var status = 0;

function start() {
		cm.sendOk("Hi.");
		cm.dispose();
} /*else {

    cm.sendSimple ("You want to enter PVP? It's dark and rough!\r\n\r\n#b#e#L0#Yes#l                         #L1#No#l");
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
		if (cm.getChar().getMapId() == 240050310) {
		cm.sendOk("You are already in PVP! Don't get killed! Go back to fighting!");
		cm.dispose();
	} else {
            cm.warp(240050310);
	    cm.sendOk("#fMob/9300093.img/stand/0##r#eFIGHT!!!!!#fMob/9001003.img/attack2/0#");
	    cm.dispose();
        }
	} else if (selection == 1) {
        cm.sendOk("Alright, Have a nice day.");
        cm.dispose();
	}
        else if (selection == 10) {
            cm.warp(100000000);
        cm.sendOk("Hope you Enjoyed PVP, Have fun!");
        cm.dispose();
	} else if (selection == 11) {
		cm.sendOk("Don't jsut stand there then, Go fight!");
		cm.dispose();
	}
	}
}
}*/