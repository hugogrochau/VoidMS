var status = 0;
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
                        cm.sendOk("Alright, see ya later!");
                        cm.dispose();
                        return;
                }
                if (mode == 1)
                        status ++;
                else
                        status --;
                if (status == 0) {
                if (cm.getP().getLevel() >= 40 && cm.getP().getLevel() <= 79) {
                        cm.sendNext("Hey, #h #! I am the Second Instructional NPC for your fifth and last rebirth. My main concern is the SRB System. Here at Void, we have a very unique SRB System. We have #r4 #kdifferent SRB Stages. There are 4 different Stats per SRB Stage. STR, DEX, INT and Luk.\r\n The SRB 1 will take 20k off of the selected stat, and give you, in return, #r1 ring with 10k of that same stat. #kIt also takes 1 Vote Point and 1 Snail Stamp.\r\nSRB 2 will take 4 SRB1 Rings, and will give you 1 ring with #r20k of the selected stat. #kIt also takes 2 Vote Points and 2 Snail Stamps.\r\nSRB 3 will take 4 SRB2 Rings, and will give you 1 ring with #r30k of the selected stat. #kIt also takes 3 Vote Points and 3 Snail Stamps.\r\n SRB 4 will take 4 SRB3 Rings, and will give you 1 ring with #r30k of the selected stat, and 100 Weapon Attack. #kIt also takes 4 Vote Points and 4 Snail Stamps. \r\n\r\n#k#eThat is all you need to know on our SRB System here at Void.");
                } else {
                        cm.sendOk("#e#rUh-Oh! #kIt seems you are not the required Level for this NPC. Please talk to the NPCs in order.");
                                                                cm.dispose();
                                                }
                } else if (status == 1) {
                                                cm.sendYesNo("#eNow that you have information on the SRB Stages, are you ready to take the quiz?");
                                } else if (status == 2) {
                                                cm.sendSimple("#eQuestion 1:\r\nWhen doing your second SRB, you need 4 SRB1 Rings, 2 Vote Points, and 3 Snail Stamps.\r\n\r\n#b#L1#True#l\r\n#L0#False#l");
                                } else if (selection == 0) {
                                                cm.sendSimple("#e#gThat is correct!\r\n\r\n#L2#Click here to go to your next question!#l");
                                } else if (selection == 1) {           
					cm.sendOk("#e#rSorry, but that answer is incorrect. Try again.");
					cm.dispose();
				} else if (status == 3) {
                                } else if (selection == 2) {
										  cm.sendSimple("Question 2:\r\nThere are 3 SRB Stages. 1 Ring for 10k Stat, 1 Ring for 20k Stat, and then 1 Ring for 30k Stat.\r\n\r\n#b#L3#True#l\r\n#L4#False#l");	
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
			