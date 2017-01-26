var status;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    }else{
        status--;
    }
    
    if (status == 0) {
        cm.sendSimple("Hello #b#h ##k I can take you to the lobby of the Hit and Run game.\r\nSo, do you want to go? \r\n\r\n#L0#Yes, take me there!#l \r\n#L1#No thank you#l");
    } else if (status == 1) {
        if (selection == 0) {
	    cm.warp(910010100);
            cm.dispose();
        }
         else if (selection == 1) {
		if (cm.getP().getMap() == 910010100) {
            cm.sendOk("Okay, have a nice day #h #!");
            cm.dispose();
		} else {
	    cm.warp(100000200);
            cm.sendOk("Okay, have a nice day #h #!");
            cm.dispose();
            }
        }
    }
}
  