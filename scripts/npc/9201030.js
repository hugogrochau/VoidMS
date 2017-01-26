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
			cm.sendSimple("Hey #d#h ##k. I heard you are looking into the voidmas spirit. Well do I got a surprise for you, I just happen to be making some #rVoidmas Snowman Surprise#k. Meow\r\nWant one?\r\n\r\n#b#L0#What is a Voidmas Snowman Surprise?#l\r\n#L1#Id like a Voidmas Snowman Surprise!#l\r\n#L2#Nevermind.#l");
		} else if (selection == 0) {
			cm.sendOk("All you need to do is give me #rFIVE#k of every present and you get a box. You may win a special item from the Snowman Surprise. Be careful, It drops #r3 random items#k in front of you so Make sure you arent in public.");
			cm.dispose();
		} else if (selection == 1) { 
			if (cm.haveItem(4031168, 5) && cm.haveItem(4031442, 5) && cm.haveItem(4000422, 5) && cm.haveItem(4000423, 5) && cm.haveItem(4000424, 5) && cm.haveItem(4000425, 5)) {
				cm.gainItem(4031168, -5);
				cm.gainItem(4031442, -5);
				cm.gainItem(4000422, -5);
				cm.gainItem(4000423, -5);
				cm.gainItem(4000424, -5);
				cm.gainItem(4000425, -5);
				cm.gainItem(2022280);
				cm.sendOk("Use this and Itll drop you a random item. Make sure you arent in Public.");
				cm.dispose();
			} else {
				cm.sendOk("You do not have #r5 Of each present#k.");
				cm.dispose();
			}
		} else if (selection == 2) {
			cm.sendOk("Alright. Meow");
			cm.dispose();
		}
	}	
}