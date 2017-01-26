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
                        if (cm.getP().getLevel() >= 160 && cm.getP().getLevel() <= 199) {
                        cm.sendNext("#eHello, #h #! I am the last Instructional NPC! After this, you will be done your Introduction to VoidMS! I am going to tell you all about our #bVote Exchanger. #kHere at Void, we run a Vote Exchanger. When you vote for us on our website, you gain points. With these points, you may buy things. At our Vote Exchanger, you can buy several different prizes. You can buy...\r\n\ #n#d1 Snail Stamp for 3 Vote Points;\r\n3 Snail Stamps for 8 Vote Points;\r\n1 Slime Stamps for 25 Vote Points;\r\n20 Weapon Attack Pendant for 40 Vote Points;\r\n20 Weapon Attack Earring for 40 Vote Points;\r\n1 Chaos Scroll for 25 Vote Points;\r\n1 Event Trophy for 5 Vote Points;\r\n10 Slot Work Glove for 15 Vote Points;\r\nRandom Rare for 50 Vote Points;\r\nTimeless Weapon for 20 Vote Points.\r\nOur Vote Exchanger is located in Helena, in Henesys.\r\n\r\nThat is all you need to know on our Vote Exchanger! Good luck on your final quiz!");
                } else {
                        cm.sendOk("#e#rUh-Oh! #kIt seems you are not the required Level for this NPC.\r\nPlease talk to the NPCs in order.");
                        cm.dispose();
                        }
                } else if (status == 1) {
                        cm.sendYesNo("#eNow that you have information on VoidMS' Vote Exchanger, are you ready for your quiz?");
                } else if (status == 2) {
                        cm.sendSimple("#eQuestion 1:\r\nYou can buy Equips with 20 Weapon Attack on them, ie.\r\nPendants\r\nTops\r\nEarrings\r\nAnd more for 40 Vote Points\r\n\r\n#L0##bTrue#l\r\n#L1#False#l");
                } else if (selection == 0) {
                        cm.sendOk("#e#rSorry, but that answer is incorrect. Try again.");
                        cm.dispose();
                } else if (selection == 1) {
                        cm.sendSimple("#e#gThat is correct!\r\n\r\n#L2#Click here to go to your next question!#l");
                } else if (status == 3) {
                } else if (selection == 2) {
                        cm.sendSimple("#eQuestion 2: You can buy 15 Slot Work Gloves from our Vote Exchanger\r\n\r\n#L3##bTrue#l\r\n#L4#False#l");
                } else if (selection == 3) {
                        cm.sendOk("#e#rSorry, but that answer is incorrect. Try again.");
                        cm.dispose();
                } else if (selection == 4) {
                        cm.sendOk("#e#gThat answer is correct!");
            cm.getP().setLevel(199);
	    cm.getP().levelUp();
            cm.getP().levelUp();
            cm.fixExp();
			cm.getP().doReborn();
			cm.warp(925100600, 0);
            cm.dispose();  
                        }
                }
        }