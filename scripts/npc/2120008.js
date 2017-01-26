var donaPoints;
var status;
var no = 0;
var rares = Array();
var text = "Choose a rare: \n\n\r\n";
var scrolls = Array(2044503,2044703,2044603,2043303,2040807,2040806,2043103,2043203,2043003,2040506,
    2044403,2040903,2040709,2040710,2040711,2044303,2043803,2040403,2044103,2044203,2044003,2043703); // 22
var scrolltext = "These scrolls give the same stats as a 10% scroll, but work 100% of the time.\r\nChoose a scroll: \n\n\r\n";

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    donaPoints = cm.getPlayer().getDP();
    if (mode == -1) {
        cm.dispose();
    } else {
        if (status == 1 && mode == 0) {
            cm.dispose();
            return;
        }
        if (mode == 1)
            status++;
        else
            status--;
	if(status == 3) { //this is part of the 10th selection
		cm.sendYesNo("Are you sure you want to tag #i" + cm.getEquipId(selection) + "#");
			 if (mode == 1){
			 	cm.getPlayer().giveDP(-5);
				var item = cm.tagItem(cm.getEquipId(selection));
				cm.loseItem(cm.getEquipId(selection));
				cm.gainItem(item, 1);
				cm.sendSimple("#i" + cm.getEquipId(selection) + "# has been tagged.\r\n\r\n#L0#Go Back#l\r\n#L2#Leave#l");
			}
						
	} else if (status == 0) {
            cm.sendSimple("You can exchange your Donation Points here.\r\nWhat would you like to do? \r\n\r\n#d#L0#Purchase with my Donation Points#l\r\n#L1#See the donation rewards#l\r\n#L2#Exit#l");
        } else if (selection == 0){
            if (!cm.isEtcFull()){
                cm.sendSimple("#e#bDonation Rewards: #kYou have #r"+donaPoints+" #kDonation Points#n\r\n\r\n\r\n#L4##r1 Donation Point #bfor 10 #i2340000# White Scrolls#l\r\n\r\n#L5##r1 Donation Point #bfor 1 #i4031544# Green Wish Ticket#l\r\n\r\n#L6##r1 Donation Point #bfor 5 #i4002000# Snail Stamps#l\r\n\r\n#L7##r5 Donation Points #bfor 7 #i4031544# Green Wish Tickets#l\r\n\r\n#L8##r5 Donation Points #bfor 30 #i4002000# Snail Stamps#l\r\n\r\n#L9##r5 Donation Points #bfor 1 #i2040807# GM Scroll#l\r\n\r\n#L10##r5 Donation Points #bfor your IGN tagged on an item in your inventory#l\r\n\r\n#L11##r10 Donation Points #bfor an extra 100 slots in buddy list#l\r\n\r\n#L12##r10 Donation Points #bfor GM Job with the skill Super Dragon Roar only#l\r\n\r\n#L13##r15 Donation Points #bfor 10 #i4002003# Slime Stamps#l\r\n\r\n#L14##r15 Donation Points #bfor a gender change to alien where you can wear both gender clothes at the same time, and for future rares#l\r\n\r\n#L15##r25 Donation Points #bfor Max out Stat Points(120,000 Stat Points)#l\r\n\r\n#L16##r40 Donation Points #bfor a #i1112807# SRB 3 ring#l\r\n\r\n#L17##r50 Donation Points #bfor a #i1082223# Stormcaster Glove with 50 Slots Clean#l\r\n\r\n#L18##r75 Donation Points #bfor 5 #i4002003# Slime Stamps, 10 #i4031544# Green Wish Tickets, #i1322013# Wizet Suitcase with 200 Weapon Attack#l\r\n\r\n#L19##r100 Donation Points #bfor 15 #i4002003# Slime Stamps, #i4031544# 30 Green Wish Tickets, #i1322013# #i1042003# #i1062007# Wizet Set.#l\r\n\r\n\r\n#L3##kGo Back\r\n#L2#Exit#l");
            } else {
                cm.sendOk("You must have donation points to access this.");
                cm.dispose();
            }
        } else if (selection == 1){
            cm.sendSimple("#e#bDonation Rewards#k#n\r\n\r\n\r\n#r1 Donation Point #bfor 10 #i2340000# White Scrolls\r\n\r\n#r1 Donation Point #bfor 1 #i4031544# Green Wish Ticket\r\n\r\n#r1 Donation Point #bfor 5 #i4002000# Snail Stamps\r\n\r\n#r5 Donation Points #bfor 7 #i4031544# Green Wish Tickets\r\n\r\n#r5 Donation Points #bfor 30 #i4002000# Snail Stamps\r\n\r\n#r5 Donation Points #bfor 1 #i2040807# GM Scroll\r\n\r\n#r5 Donation Points #bfor your IGN tagged on an item in your inventory\r\n\r\n#r10 Donation Points #bfor an extra 100 slots in buddy list\r\n\r\n#r10 Donation Points #bfor GM Job with the skill Super Dragon Roar only\r\n\r\n#r15 Donation Points #bfor 10 #i4002003# Slime Stamps\r\n\r\n#r15 Donation Points #bfor a gender change to alien where you can wear both gender clothes at the same time, and for future rares\r\n\r\n#r25 Donation Points #bfor Max out Stat Points(120,000 Stat Points)\r\n\r\n#r40 Donation Points #bfor a #i1112807# SRB 3 ring\r\n\r\n#r50 Donation Points #bfor a #i1082223# Stormcaster Glove with 50 Slots Clean\r\n\r\n#r75 Donation Points #bfor 5 #i4002003# Slime Stamps, 10 #i4031544# Green Wish Tickets, Wizet Suitcase with 200 Weapon Attack #i1322013#\r\n\r\n#r100 Donation Points #bfor 15 #i4002003# Slime Stamps, #i4031544# 30 Green Wish Tickets, #i1322013# #i1042003# #i1062007# Wizet Set.\r\n\r\n\r\n#L3##kGo Back\r\n#L2#Exit#l");
        } else if (selection == 2){
            cm.dispose();
        } else if (selection == 3){
            status = -1;
            action(1, 0, 0);
        } else if (selection == 4){
            if (donaPoints >= 1 && !cm.isEtcFull()){//1 point
                cm.getPlayer().giveDP(-1);
                cm.gainItem(2340000,10);
                cm.sendSimple("Enjoy your #r10#k #i2340000# White Scrolls\r\n\r\n#L0#Go Back#l\r\n#L2#Leave#l");
            } else {
                cm.sendOk("You do not have enough points or, your inventory is full");
                cm.dispose();
            }
        } else if (selection == 5){
            if (donaPoints >= 1 && !cm.isEtcFull()){//1 point
                cm.getPlayer().giveDP(-1);
                cm.gainItem(4031544,1);
                cm.sendSimple("Enjoy your #i4031544# Green Wish Tickets\r\n\r\n#L0#Go Back#l\r\n#L2#Leave#l");
            } else {
                cm.sendOk("You do not have enough points or, your inventory is full");
                cm.dispose();
            }
        } else if (selection == 6){
            if (donaPoints >= 1 && !cm.isEtcFull()){//1 point
                cm.getPlayer().giveDP(-1);
                cm.gainItem(4002000,5);
                cm.sendSimple("Enjoy your #r5#k #i4002000# Snail Stamps\r\n\r\n#L0#Go Back#l\r\n#L2#Leave#l");
            } else {
                cm.sendOk("You do not have enough points or, your inventory is full");
                cm.dispose();
            }
        } else if (selection == 7){
            if (donaPoints >= 5 && !cm.isEtcFull()){//5 points
                cm.getPlayer().giveDP(-5);
                cm.gainItem(4031544,7);
                cm.sendSimple("Enjoy your #r7#k #i4031544# Green Wish Ticket\r\n\r\n#L0#Go Back#l\r\n#L2#Leave#l");
            } else {
                cm.sendOk("You do not have enough points or, your inventory is full");
                cm.dispose();
            }
        } else if (selection == 8){
            if (donaPoints >= 5 && !cm.isEtcFull()){//5 points
                cm.getPlayer().giveDP(-5);
                cm.gainItem(4002000,30);
                cm.sendSimple("Enjoy your #r30#k #i4002000# Snail Stamps\r\n\r\n#L0#Go Back#l\r\n#L2#Leave#l");
            } else {
                cm.sendOk("You do not have enough points or, your inventory is full");
                cm.dispose();
            }
		} else if (selection == 10){
			if (donaPoints >= 5 && !cm.isEtcFull()){//5 points
				if (status == 2){
					cm.sendSimple("Please choose an item that you would like to tag:\r\n\r\n\r\n" + cm.EquipList(cm.getC()));
				}  
			/*} else {
				cm.sendOk("You do not have enough points or your inventory is full");
				cm.dispose();
			}*/
					
        } else if (selection == 11){
            if (donaPoints >= 10 && !cm.isEtcFull()){//10 points
                cm.getPlayer().giveDP(-10);
                cm.getPlayer().setBuddyCapacity(100);
                cm.sendSimple("Now you can have more friends, enjoy it!\r\n\r\n#L0#Go Back#l\r\n#L2#Leave#l");//maximum how many buddies?
            } else {
                cm.sendOk("You do not have enough points");
                cm.dispose();
            }
        } else if (selection == 12){
            if (donaPoints >= 10 && !cm.isEtcFull()){//10 points
                cm.getPlayer().giveDP(-10);
                cm.getPlayer().setJob(900);
                cm.sendSimple("Your job has changed into GM Job. Save the skill Super Dragon Roar onto your keyboard. When you change your job, you will not be able to change back\r\n\r\n#L0#Go Back#l\r\n#L2#Leave#l");
            } else {
                cm.sendOk("You do not have enough points");
                cm.dispose();
            }
        } else if (selection == 13){
            if (donaPoints >=15 && !cm.isEtcFull()){//15 points
			cm.getPlayer().giveDP(-15);
			cm.gainItem(4002003,10);
			cm.sendOk("Enjoy your #i4002003# Slime Stamps");
            cm.dispose();
			} else {
				cm.sendOk("You do not have enough points");
				cm.dispose();
			}
        } else if (selection == 14){
            if (donaPoints >= 15 && !cm.isEtcFull()){//15 points
                cm.getPlayer().giveDP(-15);
                cm.getP().setGender(2);
                cm.reloadChar();
            } else {
                cm.sendOk("You do not have enough Vote Points.");
                cm.dispose();
            }
        } else if (selection == 15){
            if (donaPoints >= 25 && !cm.isEtcFull()){//25 points
                cm.getPlayer().giveDP(-25);
                cm.gainAp(120000);
                cm.sendSimple("You now have #r120,000#k Ability Points. Don't freak out when you see a negative ability point, you still have it.\r\n\r\n#L0#Go Back#l\r\n#L2#Leave#l");
            } else {
                cm.sendOk("You do not have enough points");
                cm.dispose();
            }
        } else if (selection == 16){
            if (donaPoints >= 40 && !cm.isEtcFull()){//40 points
                cm.openNpc(1061007);
            } else {
                cm.sendOk("You do not have enough points or, your inventory is full");
                cm.dispose();
            }
        } else if (selection == 17){
            if (donaPoints >= 50 && !cm.isEtcFull()){//50 points
                cm.getPlayer().giveDP(-50);
                cm.makeSItem(1082223, cm.getP(), 0, 0, 0, 0, 0, 50);
                cm.reloadChar();
                cm.sendSimple("Enjoy your #i1082223# Stormcaster Gloves with 50 Slots\r\n\r\n#L0#Go Back#l\r\n#L2#Leave#l");
            } else {
                cm.sendOk("You do not have enough points or your inventory is full");
                cm.dispose();
            }
        } else if (selection == 18){
			if (donaPoints >= 75 && !cm.isEtcFull()){//75 points
				cm.getPlayer().giveDP(-75);
				cm.gainItem(4002003,5);
				cm.gainItem(4031544,10);
				cm.gainItem(1322013,1);
				cm.sendSimple("Enjoy your #i4002003# Slime Stamps, #i4031544# Green Wish Tickets, #i1322013# Wizet Suit Case\r\n\r\n#L0#Go Back#l\r\n#L2#Leave#l");
				cm.dispose();
			} else {
				cm.sendOk("You do not have enough points or your inventory is full");
				cm.dispose();
			}	
        } else if (selection == 19){
			if (donaPoints >= 100 && !cm.isEtcFull()){//100 points
				cm.getPlayer().giveDP(-100);
				cm.gainItem(4002003,15);
				cm.gainItem(4031544,30);
				cm.gainItem(1322013,1);
				cm.gainItem(1042003,1);
				cm.gainItem(1062007,1);
				cm.sendSimple("Enjoy your #i4002003# Slime Stamps, #i4031544# Green Wish Tickets, #i1322013# #i1042003# #i1062007# Wizet Set\r\n\r\n#L0#Go Back#l\r\n#L2#Leave#l");
				cm.dispose();
			} else {
				cm.sendOk("You do not have enough points or your inventory is full");
				cm.dispose();
			}
        } else if (selection == 9){
            if (donaPoints >= 5 && !cm.isUseFull()){
                var scrollc = 0;
                for(var p=0; p < scrolls.length; p++){
                    scrollc = p+22;
                    scrolltext +="#L"+scrollc+"##t"+scrolls[p]+"#\r\n";
                }
                cm.sendSimple(scrolltext);

            } else {
                cm.sendOk("You do not have enough points or, your inventory is full");
                cm.dispose();
            }
        } else if (selection >=22){//>=22
            if (donaPoints >= 5 && !cm.isFull()){
                var o = selection-22;//-22
                cm.sendOk("Enjoy your #t"+scrolls[o]+"#");
                cm.getPlayer().giveDP(-5);
                cm.gainItem(scrolls[o], 1);
            }else{
                cm.sendOk("You do not have enough points or, your inventory is full");
            }
        }
	}
	
	}
	}
   