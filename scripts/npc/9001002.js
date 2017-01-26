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
			if (cm.getP().getLevel() >= 80 && cm.getP().getLevel() <= 119) {
			cm.sendNext("#eHello, #h #! Congrats on conquering the first and second Quiz! I am you're Third Instructional NPC, and I am all about VoidMS' Sites, Versions, and Miscellaneous! The website for VoidMS is #rwww.voidms.com. #kHere you can Vote for VoidMS, and see recent news on Void.\r\nVoidMS' Forums are #rwww.madstory.org. #kHere, you can see fun Events, in, and out, of game, see imporant server notices, or even just to have a chat with fellow Void Players.\r\nVoidMS has an official chatbox, and it is a #rChatango Chatbox.\r\n#kVoidMS is a v62 Private Server #k, and is #d500 EXP, 50 Drop, and 15 Mesos. #kDid you know that every #rMondays #k, VoidMS has a x2 EXP and Drop Event?\r\n\r\n#kThat is all you need to know on the Miscellaneous' of VoidMS. Good luck on your upcoming Quiz!");
		} else {
			cm.sendOk("#e#rUh-Oh! #kIt seems you are not the required Level for this NPC.\r\nPlease talk to the NPCs in order.");
			cm.dispose();
			}
		} else if (status == 1) {
			cm.sendYesNo("#eNow that you have information on VoidMS as a whole, are you ready for your quiz?");
		} else if (status == 2) {
			cm.sendSimple("#eQuestion 1:\r\nVoidMS has an official XAT ChatBox which you can see on our Forums and our Website.\r\n\r\n#L0##bTrue#l\r\n#L1#False#l");
		} else if (selection == 0) {
			cm.sendOk("#e#rSorry, but that answer is incorrect. Try again.");
			cm.dispose();
		} else if (selection == 1) {
			cm.sendSimple("#e#gThat is correct!\r\n\r\n#L2#Click here to go to your next question!#l");
		} else if (status == 3) {
		} else if (selection == 2) {
			cm.sendSimple("#eQuestion 2: VoidMS' website is www.voidms.com, and our forums are www.madstory.org.\r\n\r\n#L3##bTrue#l\r\n#L4#False#l");
		} else if (selection == 4) {
			cm.sendOk("#e#rSorry, but that answer is incorrect. Try again.");
			cm.dispose();
		} else if (selection == 3) {
			cm.sendOk("#e#gThat answer is correct!\r\nCongratulations, now talk to your next NPC to continue on in your leveling!");
            for (i = 0; i < 40; i++)
            cm.getP().levelUp();
            cm.fixExp();
            cm.dispose();  
			}
		}
	}	
	