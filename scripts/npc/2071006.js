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
                        cm.sendOk("Hope to see you later for more training!");
                        cm.dispose();
                        return;
                }
                if (mode == 1) 
                        status ++;
                else
                        status --;
                if (status == 0) {
                if (cm.getP().getLevel <= 40) && if (cm.getP().getLevel() >= 79) {
                        cm.sendSimple("Hey, #h #! I am the Second Instructional NPC for your fifth and last rebirth. My main concern is the #bSRB System #k. Here at Void, we have a very unique #bSRB System. #kWe have #r4 #kdifferent SRB Stages. There are 4 different Stats per #bSRB Stage. #rSTR, DEX, INT and Luk. #kThe #rSRB 1 #kwill take 20k off of the selected stat, and give you, in return, #r1 ring with 10k of that same stat. #kIt also takes #b3 Vote Points #kand #b1 Snail Stamp. #rSRB 2 #kwill take #r4 #bSRB1 Rings, #kand will give you #b1 #kring with #r20k of the selected stat. #kIt also takes #b7 Vote Points #kand #b3 Snail Stamps. #rSRB 3 #kwill take #r4 #bSRB2 Rings, #kand will give you #b1 #kring with #r30k of the selected stat. #kIt also takes #b13 Vote Points #kand #b6 Snail Stamps. #rSRB 4 #kwill take #r4 #bSRB3 Rings, #kand will give you #b1 #kring with #r30k of the selected stat, and 100 Weapon Attack. #kIt also takes #b20 Vote Points #kand #b10 Snail Stamps. \r\n\r\n#k#eThat is all you need to know on our SRB System here at Void, see you later!\r\n\r\n#L0#I am ready to take my quiz!#l\r\n#L1#I'm going to wait a bit#l");
                } else {
                        cm.sendOk("Uh-Oh! It seems you are not the required Level for this NPC. Please talk to the NPCs in order.");
                } else if (selection == 0) {
						cm.sendSimple("#e#bQuestion 1:\r\nWhen doing your second SRB, you need #r4 #bSRB1 Rings, 7 Vote Points, and 5 Snail Stamps.\r\n\r\n#k#L2#True#l\r\n#L3#False#l");
				} else if (selection == 1) {
						cm.sendOk("Ok, come back soon.");
				} else if (selection == 2) {
						cm.sendOk("Sorry, but that answer is #rincorrect.");
						cm.dispose();
				} else if (selection == 3) {		
						cm.setLevel(59);
						cm.levelUp();
						cm.gainAp(50);
						cm.sendSimple("That is #rcorrect! #L4#I want the next question!");
						cm.dispose();
				} else if (selection == 4) {
						cm.sendSimple("#e#bQuestion 2:\r\nThere are 3 SRB Stages. 1 Ring for 10k Stat, 1 Ring for 20k Stat, and then 1 Ring for 30k Stat.\r\n\r\n#k#L5#True#l\r\n#L6#False#l");
				} else if (selection == 5) {
						cm.sendOk("Sorry, but that answer is #rincorrect.");
						cm.dispose();
				} else if (selection == 6) {
						cm.setLevel(79);
						cm.levelUp();
						cm.gainAp(50);
						cm.sendOk("That is #rcorrect! Please talk to the next NPC to continue on!");
						cm.dispose();
						}
					}
				}
			}