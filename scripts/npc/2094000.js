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
		cm.sendSimple("Hello, Im the Casino warper,\r\nWould you like to go to the casino?\r\nIt's Free.\r\n\r\n#b#e#L0#Warp Me#l\r\n#L1#Nevermind#l");
	
        } else if (selection == 0) {
			cm.warp(925100000);
			cm.sendOk("#bWelcome #d#e#h ##n#b to the VoidMS Casino.\r\n\r\nYou can play games here for Wish Tickets, Mesos or Karma Levels.\r\n\r\n Enjoy the Casino\r\n"); 
			cm.dispose();
	} else if (selection == 1) {
		cm.sendOk("Alright Then.");
		cm.dispose();
	}
    }
}
					
					
