var status = 0;
function start() {
	status = -1
	action(1, 0, 0);
}

function action(mode, type, selection) {
	selected = selection;
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
			 if (cm.getP().getLevel() >= 1 && cm.getP().getLevel() <= 39) {
                                cm.sendNext("#eHey, #h #! In your final stage of Rebirthing with us, you will be given information on a topic, and then quizzed on it. My topic is Currencies .\r\n Here at VoidMS, we run two seperate Currencies. Stamps and Wish Tickets.\r\n There are three kinds of Stamps, Snail Stamps, Slime Stamps, and Stump Stamps.\r\n Snail Stamps are worth 1 billion Mesos each. You mainly get these as prizes from Events. You can also trade 1 Billion Mesos for 1 Snail Stamp or vice-versa from our Item Exchanger (@exchange). Another way of getting Snail Stamps is by Voting, and getting them from our Vote Exchanger, or even by Donating.\r\n Our other Stamp Currency is Slime Stamps. These are worth 10 Billion Mesos each, or 10 Snail Stamps. You can get these from our Item Exchanger.\r\n\ Our final Stamp Currency is Stump Stamps. You can get these from Donating 20$. What they do, is automatically rebirths you when you reach Level 200, and they also automatically trade 2 Billion Mesos for Snail Stamps.\r\n\r\nOur second currency is Wish Tickets. There are three different Wish Tickets we use. Yellow Wish Tickets, Green Wish Tickets, and Blue Wish Tickets. Yellow Wish Tickets are worth 100 Million Mesos each. You can get Yellow Wish Tickets from our Item Exchanger, for 100 Million Mesos, or even from Events.\r\nGreen Wish Tickets are worth 1 Billion Mesos each, or 10 Yellow Wish Tickets. Green Wish Tickets are also common Event Prizes, but you may get them from our Item Exchanger aswell. You can exchange your Green Wish Tickets for several things in VoidMS, like GM Job, Wizet Suit, and more!\r\n Blue Wish Tickets are the highest currency on Void. They are worth 10 Billion Mesos each, or 10 Green Wish Tickets. You use your Blue Wish Tickets for the Casino in Void (@casino) \r\n\r\nThis is all you need to know about the currencies here on VoidMS.");
                        } else {
                                cm.sendOk("#e#rUh-Oh!#k It seems you are not the required Level for this NPC. Please talk to the NPCs in order.");
								cm.dispose();
								}
		} else if (status == 1) {
			cm.sendYesNo("#eNow that you have information on our Currency, are you ready to take our quiz?");
		} else if (status == 2) {
			cm.sendSimple("#eQuestion 1:\r\nSlime Stamps are worth 10 Billion Mesos each.\r\n\r\n#L0##bTrue#l\r\n#L1#False#l");
		} else if (selection == 0) {
			cm.sendSimple("#e#gThat is correct!\r\n\r\n#L2#Click here to go to your next question!#l");
		} else if (selection == 1) {
			cm.sendOk("#e#rSorry, but that answer is incorrect. Try again.");
			cm.dispose();
		} else if (status == 3) {
		} else if (selection == 2) {	
			cm.sendSimple("There are 4 different currencies branching out from Stamps; Snail Stamp; Slime Stamp; Stump Stamp; Blue Snail Stamp\r\n\r\n#b#L3#True#l\r\n#L4#False#l");
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
	
                               
		
								