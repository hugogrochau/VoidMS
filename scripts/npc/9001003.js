var status = 0;
function start () {
	status = -1
	action(1, 0, 0);
}	

function action(mode, type, selection) {
	selected = selection
	if (mode == -1) {
		cm.dispose();
	} else {
		if (status >= 0 && mode == 0) {
			cm.sendOk("Alright, see ya later!");
			cm.dispose();
			return;
		}
		if (mode == 1)
			status ++;
		else
			status --;
		if (status == 0) {
			if (cm.getP().getLevel() >= 120 && cm.getP().getLevel() <= 159) {
			cm.sendNext("#eHello, #h #! I am your fourth Instructional NPC! I love talking about Spawners! Here at Void, we have #r3 #kdifferent Boss Spawners! There are 3 difficulties of the Spawner. #rNoob, Medium, and Hard.\r\n\r\nThe #dNoob Spawner #khas lower leveled monsters. Not only is their HP lower, but their EXP aswell.\r\n The #dMedium Spawner #khas medium leveled monsters. Their HP and EXP are higher than that of the Noob Spawner though.\r\n And finally, the #dPro Spawner #khas monsters with the most EXP, and the most HP of the 3 spawners.\r\n We did this to try and accomodate to the fact that not everyone can kill high HP'ed Monsters. You can get to these Spawners from Shane in Henesys, or through the command @spawner1/2/3/4/5/6. There are 2 Maps for each Spawner, so it does not get too crowded.\r\n\r\nThat is all there is to know on the Boss Spawners of VoidMS! Who knows, maybe one day you'll be able to train at the Pro Spawner!");
		} else {
			cm.sendOk("#e#rUh-Oh! #kIt seems you are not the required Level for this NPC.\r\nPlease talk to the NPCs in order.");
			cm.dispose();
			}
		} else if (status == 1) {
			cm.sendYesNo("#eNow that you have information on VoidMS' Boss Spawners, are you ready for your quiz?");
		} else if (status == 2) {
			cm.sendSimple("#eQuestion 1:\r\nThere are 2 Maps for each Spawner, to avoid congestion of players wanting to train, and not having available maps.\r\n\r\n#L1##bTrue#l\r\n#L0#False#l");
		} else if (selection == 0) {
			cm.sendOk("#e#rSorry, but that answer is incorrect. Try again.");
			cm.dispose();
		} else if (selection == 1) {
			cm.sendSimple("#e#gThat is correct!\r\n\r\n#L2#Click here to go to your next question!#l");
		} else if (status == 3) {
		} else if (selection == 2) {
			cm.sendSimple("#eQuestion 2: In VoidMS, we have 2 different Boss Spawners, a Noob Spawner, and a Pro Spawner.\r\n\r\n#L3##bTrue#l\r\n#L4#False#l");
		} else if (selection == 3) {
			cm.sendOk("#e#rSorry, but that answer is incorrect. Try again.");
			cm.dispose();
		} else if (selection == 4) {
			cm.sendOk("#e#gThat answer is correct!\r\nCongratulations, now talk to your next NPC to continue on in your leveling!");
            for (i = 0; i < 40; i++)
            cm.getP().levelUp();
            cm.fixExp();
            cm.dispose();  
			}
		}
	}	
	