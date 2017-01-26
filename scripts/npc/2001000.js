/*var status = 0;

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
			cm.sendSimple("Hello #d#h ##k and Merry Christmas. Have you seen presents around lately? A gang of snowmen stole all my presents for christmas. You have any? I will reward you with one of  my #rVoidmas Socks#k.\r\n\r\n#b#L0#Where can I find presents?#l\r\n#L1#I have presents, I want a sock.#l\r\n#L2#I have all your socks!#l\r\n#L3#I don't need anything anymore.#l");
		} else if (selection == 0) {
			cm.sendOk("Well, the snowmen stole them so, I assume from them. Heard rumors around that they hang around in this Town. Something about a Magic mitten and special snowballs to take em down. Also the Snowman Leader keeps stalking my house. Happy knows the way there... hope someone can take him down. There are 6 kinds of presents, each of a different color. If you could just bring me 25 of a color, I could give you a sock. Willing to help me?");
			cm.dispose();
		} else if (selection == 1) {
			cm.sendSimple("Oh goodie, Which kind of presents have you brought me? \r\n\r\n#L10##i4031168##l#L11##i4031442##l#L12##i4000422##l#L13##i4000423##l#L14##i4000424##l#L15##i4000425##l");
		} else if (selection == 2) {
			cm.sendSimple("You already have most of my socks? Oh dear! Well, in that case, I have a couple special items if youd like to give the socks in for them. They just came in. Ill give you the #rHorns#k for #d4 Socks each#k.\r\n\r\n#L30##i1002850##l#L31##i1002867##l\r\n\r\nIf you collect all my socks, Ill be greatful and join you in your journeys. Would you like that?\r\n#e#b#L32#Yes, I'd love that.#l");
		} else if (selection == 3) {
			cm.sendOk("Alright, hope to see you soon!");
			cm.dispose();
		} else if (selection == 10) {
			if (cm.haveItem(4031168, 25)) {
				cm.gainItem(4031168, -25);
				cm.gainItem(1072431);
				cm.sendOk("Enjoy your #rYellow Christmas Sock#k.");
				cm.dispose();
			} else {
				cm.sendOk("You do not have #r25 Yellow Presents#k.");
				cm.dispose();
			}
		} else if (selection == 11) {
			if (cm.haveItem(4031442, 25)) {
				cm.gainItem(4031442, -25);
				cm.gainItem(1072428);
				cm.sendOk("Enjoy your #rGreen Christmas Sock#k.");
				cm.dispose();
			} else {
				cm.sendOk("You do not have #r25 Green Presents#k.");
				cm.dispose();
			}
		} else if (selection == 12) {
			if (cm.haveItem(4000422, 25)) {
				cm.gainItem(4000422, -25);
				cm.gainItem(1072430);
				cm.sendOk("Enjoy your #rWhite Christmas Sock#k.");
				cm.dispose();
			} else {
				cm.sendOk("You do not have #r25 White Presents#k.");
				cm.dispose();
			}
		} else if (selection == 13) {
			if (cm.haveItem(4000423, 25)) {
				cm.gainItem(4000423, -25);
				cm.gainItem(1072427);
				cm.sendOk("Enjoy your #rRed Christmas Sock#k.");
				cm.dispose();
			} else {
				cm.sendOk("You do not have #r25 Red Presents#k.");
				cm.dispose();
			}
		} else if (selection == 14) {
			if (cm.haveItem(4000424, 25)) {
				cm.gainItem(4000424, -25);
				cm.gainItem(1072429);
				cm.sendOk("Enjoy your #rBlue Christmas Sock#k.");
				cm.dispose();
			} else {
				cm.sendOk("You do not have #r25 Blue Presents#k.");
				cm.dispose();
			}
		} else if (selection == 15) {
			if (cm.haveItem(4000425, 25)) {
				cm.gainItem(4000425, -25);
				cm.gainItem(1072432);
				cm.sendOk("Enjoy your #rPurple Christmas Sock#k.");
				cm.dispose();
			} else {
				cm.sendOk("You do not have #r25 Purple Presents#k.");
				cm.dispose();
			}
		} else if (selection == 30) {
			if (cm.haveItem(1072427) && cm.haveItem(1072428) && cm.haveItem(1072429) && cm.haveItem(1072430)) {
				cm.gainItem(1072427, -1);
				cm.gainItem(1072428, -1);
				cm.gainItem(1072429, -1);
				cm.gainItem(1072430, -1);
				cm.gainItem(1002850);
				cm.sendOk("Enjoy your #rHorn with Nose.#k.");
				cm.dispose();
			} else if (cm.haveItem(1072427) && cm.haveItem(1072428) && cm.haveItem(1072429) && cm.haveItem(1072431)) {
				cm.gainItem(1072427, -1);
				cm.gainItem(1072428, -1);
				cm.gainItem(1072429, -1);
				cm.gainItem(1072431, -1);
				cm.gainItem(1002850);
				cm.sendOk("Enjoy your #rHorn with Nose.#k.");
				cm.dispose();
			} else if (cm.haveItem(1072427) && cm.haveItem(1072428) && cm.haveItem(1072429) && cm.haveItem(1072432)) {
				cm.gainItem(1072427, -1);
				cm.gainItem(1072428, -1);
				cm.gainItem(1072429, -1);
				cm.gainItem(1072432, -1);
				cm.gainItem(1002850);
				cm.sendOk("Enjoy your #rHorn with Nose.#k.");
				cm.dispose();
			} else if (cm.haveItem(1072427) && cm.haveItem(1072428) && cm.haveItem(1072431) && cm.haveItem(1072430)) {
				cm.gainItem(1072427, -1);
				cm.gainItem(1072428, -1);
				cm.gainItem(1072431, -1);
				cm.gainItem(1072430, -1);
				cm.gainItem(1002850);
				cm.sendOk("Enjoy your #rHorn with Nose.#k.");
				cm.dispose();
			} else if (cm.haveItem(1072427) && cm.haveItem(1072428) && cm.haveItem(1072432) && cm.haveItem(1072430)) {
				cm.gainItem(1072427, -1);
				cm.gainItem(1072428, -1);
				cm.gainItem(1072432, -1);
				cm.gainItem(1072430, -1);
				cm.gainItem(1002850);
				cm.sendOk("Enjoy your #rHorn with Nose.#k.");
				cm.dispose();
			} else if (cm.haveItem(1072427) && cm.haveItem(1072428) && cm.haveItem(1072432) && cm.haveItem(1072431)) {
				cm.gainItem(1072427, -1);
				cm.gainItem(1072428, -1);
				cm.gainItem(1072432, -1);
				cm.gainItem(1072431, -1);
				cm.gainItem(1002850);
				cm.sendOk("Enjoy your #rHorn with Nose.#k.");
				cm.dispose();
			} else if (cm.haveItem(1072427) && cm.haveItem(1072431) && cm.haveItem(1072429) && cm.haveItem(1072430)) {
				cm.gainItem(1072427, -1);
				cm.gainItem(1072431, -1);
				cm.gainItem(1072429, -1);
				cm.gainItem(1072430, -1);
				cm.gainItem(1002850);
				cm.sendOk("Enjoy your #rHorn with Nose.#k.");
				cm.dispose();
			} else if (cm.haveItem(1072427) && cm.haveItem(1072432) && cm.haveItem(1072429) && cm.haveItem(1072430)) {
				cm.gainItem(1072427, -1);
				cm.gainItem(1072432, -1);
				cm.gainItem(1072429, -1);
				cm.gainItem(1072430, -1);
				cm.gainItem(1002850);
				cm.sendOk("Enjoy your #rHorn with Nose.#k.");
				cm.dispose();
			} else if (cm.haveItem(1072427) && cm.haveItem(1072432) && cm.haveItem(1072429) && cm.haveItem(1072431)) {
				cm.gainItem(1072427, -1);
				cm.gainItem(1072432, -1);
				cm.gainItem(1072429, -1);
				cm.gainItem(1072431, -1);
				cm.gainItem(1002850);
				cm.sendOk("Enjoy your #rHorn with Nose.#k.");
				cm.dispose();
			} else if (cm.haveItem(1072427) && cm.haveItem(1072432) && cm.haveItem(1072431) && cm.haveItem(1072430)) {
				cm.gainItem(1072427, -1);
				cm.gainItem(1072432, -1);
				cm.gainItem(1072431, -1);
				cm.gainItem(1072430, -1);
				cm.gainItem(1002850);
				cm.sendOk("Enjoy your #rHorn with Nose.#k.");
				cm.dispose();
			} else if (cm.haveItem(1072431) && cm.haveItem(1072428) && cm.haveItem(1072429) && cm.haveItem(1072430)) {
				cm.gainItem(1072431, -1);
				cm.gainItem(1072428, -1);
				cm.gainItem(1072429, -1);
				cm.gainItem(1072430, -1);
				cm.gainItem(1002850);
				cm.sendOk("Enjoy your #rHorn with Nose.#k.");
				cm.dispose();
			} else if (cm.haveItem(1072432) && cm.haveItem(1072428) && cm.haveItem(1072429) && cm.haveItem(1072430)) {
				cm.gainItem(1072432, -1);
				cm.gainItem(1072428, -1);
				cm.gainItem(1072429, -1);
				cm.gainItem(1072430, -1);
				cm.gainItem(1002850);
				cm.sendOk("Enjoy your #rHorn with Nose.#k.");
				cm.dispose();
			} else if (cm.haveItem(1072432) && cm.haveItem(1072428) && cm.haveItem(1072429) && cm.haveItem(1072431)) {
				cm.gainItem(1072432, -1);
				cm.gainItem(1072428, -1);
				cm.gainItem(1072429, -1);
				cm.gainItem(1072431, -1);
				cm.gainItem(1002850);
				cm.sendOk("Enjoy your #rHorn with Nose.#k.");
				cm.dispose();
			} else if (cm.haveItem(1072432) && cm.haveItem(1072428) && cm.haveItem(1072431) && cm.haveItem(1072430)) {
				cm.gainItem(1072432, -1);
				cm.gainItem(1072428, -1);
				cm.gainItem(1072431, -1);
				cm.gainItem(1072430, -1);
				cm.gainItem(1002850);
				cm.sendOk("Enjoy your #rHorn with Nose.#k.");
				cm.dispose();
			} else if (cm.haveItem(1072432) && cm.haveItem(1072431) && cm.haveItem(1072429) && cm.haveItem(1072430)) {
				cm.gainItem(1072432, -1);
				cm.gainItem(1072431, -1);
				cm.gainItem(1072429, -1);
				cm.gainItem(1072430, -1);
				cm.gainItem(1002850);
				cm.sendOk("Enjoy your #rHorn with Nose.#k.");
				cm.dispose();
			} else {
				cm.sendOk("You do not have #r4 socks#k.");
				cm.dispose();
			}
		} else if (selection == 31) {
			if (cm.haveItem(1072427) && cm.haveItem(1072428) && cm.haveItem(1072429) && cm.haveItem(1072430)) {
				cm.gainItem(1072427, -1);
				cm.gainItem(1072428, -1);
				cm.gainItem(1072429, -1);
				cm.gainItem(1072430, -1);
				cm.gainItem(1002867);
				cm.sendOk("Enjoy your #rHorns.#k.");
				cm.dispose();
			} else if (cm.haveItem(1072427) && cm.haveItem(1072428) && cm.haveItem(1072429) && cm.haveItem(1072431)) {
				cm.gainItem(1072427, -1);
				cm.gainItem(1072428, -1);
				cm.gainItem(1072429, -1);
				cm.gainItem(1072431, -1);
				cm.gainItem(1002867);
				cm.sendOk("Enjoy your #rHorns.#k.");
				cm.dispose();
			} else if (cm.haveItem(1072427) && cm.haveItem(1072428) && cm.haveItem(1072429) && cm.haveItem(1072432)) {
				cm.gainItem(1072427, -1);
				cm.gainItem(1072428, -1);
				cm.gainItem(1072429, -1);
				cm.gainItem(1072432, -1);
				cm.gainItem(1002867);
				cm.sendOk("Enjoy your #rHorns.#k.");
				cm.dispose();
			} else if (cm.haveItem(1072427) && cm.haveItem(1072428) && cm.haveItem(1072431) && cm.haveItem(1072430)) {
				cm.gainItem(1072427, -1);
				cm.gainItem(1072428, -1);
				cm.gainItem(1072431, -1);
				cm.gainItem(1072430, -1);
				cm.gainItem(1002867);
				cm.sendOk("Enjoy your #rHorns.#k.");
				cm.dispose();
			} else if (cm.haveItem(1072427) && cm.haveItem(1072428) && cm.haveItem(1072432) && cm.haveItem(1072430)) {
				cm.gainItem(1072427, -1);
				cm.gainItem(1072428, -1);
				cm.gainItem(1072432, -1);
				cm.gainItem(1072430, -1);
				cm.gainItem(1002867);
				cm.sendOk("Enjoy your #rHorns.#k.");
				cm.dispose();
			} else if (cm.haveItem(1072427) && cm.haveItem(1072428) && cm.haveItem(1072432) && cm.haveItem(1072431)) {
				cm.gainItem(1072427, -1);
				cm.gainItem(1072428, -1);
				cm.gainItem(1072432, -1);
				cm.gainItem(1072431, -1);
				cm.gainItem(1002867);
				cm.sendOk("Enjoy your #rHorns.#k.");
				cm.dispose();
			} else if (cm.haveItem(1072427) && cm.haveItem(1072431) && cm.haveItem(1072429) && cm.haveItem(1072430)) {
				cm.gainItem(1072427, -1);
				cm.gainItem(1072431, -1);
				cm.gainItem(1072429, -1);
				cm.gainItem(1072430, -1);
				cm.gainItem(1002867);
				cm.sendOk("Enjoy your #rHorns.#k.");
				cm.dispose();
			} else if (cm.haveItem(1072427) && cm.haveItem(1072432) && cm.haveItem(1072429) && cm.haveItem(1072430)) {
				cm.gainItem(1072427, -1);
				cm.gainItem(1072432, -1);
				cm.gainItem(1072429, -1);
				cm.gainItem(1072430, -1);
				cm.gainItem(1002867);
				cm.sendOk("Enjoy your #rHorns.#k.");
				cm.dispose();
			} else if (cm.haveItem(1072427) && cm.haveItem(1072432) && cm.haveItem(1072429) && cm.haveItem(1072431)) {
				cm.gainItem(1072427, -1);
				cm.gainItem(1072432, -1);
				cm.gainItem(1072429, -1);
				cm.gainItem(1072431, -1);
				cm.gainItem(1002867);
				cm.sendOk("Enjoy your #rHorns.#k.");
				cm.dispose();
			} else if (cm.haveItem(1072427) && cm.haveItem(1072432) && cm.haveItem(1072431) && cm.haveItem(1072430)) {
				cm.gainItem(1072427, -1);
				cm.gainItem(1072432, -1);
				cm.gainItem(1072431, -1);
				cm.gainItem(1072430, -1);
				cm.gainItem(1002867);
				cm.sendOk("Enjoy your #rHorns.#k.");
				cm.dispose();
			} else if (cm.haveItem(1072431) && cm.haveItem(1072428) && cm.haveItem(1072429) && cm.haveItem(1072430)) {
				cm.gainItem(1072431, -1);
				cm.gainItem(1072428, -1);
				cm.gainItem(1072429, -1);
				cm.gainItem(1072430, -1);
				cm.gainItem(1002867);
				cm.sendOk("Enjoy your #rHorns.#k.");
				cm.dispose();
			} else if (cm.haveItem(1072432) && cm.haveItem(1072428) && cm.haveItem(1072429) && cm.haveItem(1072430)) {
				cm.gainItem(1072432, -1);
				cm.gainItem(1072428, -1);
				cm.gainItem(1072429, -1);
				cm.gainItem(1072430, -1);
				cm.gainItem(1002867);
				cm.sendOk("Enjoy your #rHorns.#k.");
				cm.dispose();
			} else if (cm.haveItem(1072432) && cm.haveItem(1072428) && cm.haveItem(1072429) && cm.haveItem(1072431)) {
				cm.gainItem(1072432, -1);
				cm.gainItem(1072428, -1);
				cm.gainItem(1072429, -1);
				cm.gainItem(1072431, -1);
				cm.gainItem(1002867);
				cm.sendOk("Enjoy your #rHorns.#k.");
				cm.dispose();
			} else if (cm.haveItem(1072432) && cm.haveItem(1072428) && cm.haveItem(1072431) && cm.haveItem(1072430)) {
				cm.gainItem(1072432, -1);
				cm.gainItem(1072428, -1);
				cm.gainItem(1072431, -1);
				cm.gainItem(1072430, -1);
				cm.gainItem(1002867);
				cm.sendOk("Enjoy your #rHorns.#k.");
				cm.dispose();
			} else if (cm.haveItem(1072432) && cm.haveItem(1072431) && cm.haveItem(1072429) && cm.haveItem(1072430)) {
				cm.gainItem(1072432, -1);
				cm.gainItem(1072431, -1);
				cm.gainItem(1072429, -1);
				cm.gainItem(1072430, -1);
				cm.gainItem(1002867);
				cm.sendOk("Enjoy your #rHorns.#k.");
				cm.dispose();
			} else {
				cm.sendOk("You do not have #r4 socks#k.");
				cm.dispose();
			}
		} else if (selection == 32) { 
			if (cm.haveItem(1072432) && cm.haveItem(1072431) && cm.haveItem(1072429) && cm.haveItem(1072430) && cm.haveItem(1072428) && cm.haveItem(1072427)) {
				cm.gainItem(1072432, -1);
				cm.gainItem(1072431, -1);
				cm.gainItem(1072429, -1);
				cm.gainItem(1072430, -1);
				cm.gainItem(1072428, -1);
				cm.gainItem(1072427, -1);
				cm.gainItem(1702210);
				cm.sendOk("I'll enjoy my journey along yourself. I hope I come of use!");
				cm.dispose();
			} else {
				cm.sendOk("You do not have #rAll my Socks#k.");
				cm.dispose();
			}
		}
	}
}*/

function start()
{
	action(1,0,0);
}

function action(m,t,s) {
	cm.sendOk("#rHave a Merry Christmas!");
	cm.dispose();
}