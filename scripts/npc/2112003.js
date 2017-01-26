function start () {
cm.sendOk("Event over, come back next year");
cm.dispose();
}
//Valentines Day NPC
//Made by imPro of VoidMS



/*var status = 0;
function start() {
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, selection) {
	selected = selection
	if (mode == -1) {
		cm.dispose();
	} else {
		if (status >= 0 && mode == 0) {
			cm.dispose();
		}
		if (mode == 1)
			status ++;
		else
			status --;
		if (status == 0) {
			cm.sendNext("I'm so bored!");
		} else if (status == 1) {
			cm.sendSimple("What brings you here today, #h #?\r\n\r\n#L0#Oh, nevermind#l\r\n#L1#I have a present for you!#l");
		} else if (selection == 0) {	
			cm.sendOk("Oh, um... Ok then.");
			cm.dispose();
		} else if (selection == 1) {
			if (cm.itemQuantity(4140100) >= 1 && cm.itemQuantity(4031252) >= 1) {
			cm.gainItem(4140100, -1);
			cm.gainItem(4031252, -1);
			cm.sendOk("Oh, a present? For me? I wonder who its from.\r\n\r\n#d-Reading the Note- 'Dear Brittany,\r\nI love you so much, and would like for you to be my girlfriend.\r\nLove, \r\n\r\nOh no! The note is ripped! Now I will never know who this was from!");
			cm.gainItem(4000038, 3);
                        cm.dispose();
		} else {
			cm.sendOk("I don't think you have the items for my present! Please go back to Alex and find out what you need");
			cm.dispose();
		}
	}
}
}*/