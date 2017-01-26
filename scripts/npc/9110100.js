//Author: Moogra
var status = 0;
var map = Array(240010501);

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1)
        cm.dispose();
    else {
        if (mode == 0 && status == 0)
            cm.dispose();
        if (mode == 1)
            status++;
        else
            status--;
        if (status == 0) {
            cm.sendSimple("Hello, I can exchange your wish tickets to your desire. What would you like?\r\n#b#L1# I would like to exchange 10 #v4031543# for a #v4031544# #l\r\n#L3# I would like to exchange a #v4031544# for 10 #v4031543# #l\r\n#L2# I would like to exchange 10 #v4031544# for a #v4031545# #l\r\n#L4# I would like to exchange a #v4031545# for 10 #v4031544# #l\r\n#L5#I would like to exchange all my #v4031543# into #v4031544# #l\r\n#L6#I would like to exchange all my #v4031544# into #v4031543# #l\r\n#L7#I would like to exchange all my #v4031544# into #v4031545# #l\r\n#L8#I would like to exchange all my #v4031545# into #v4031544# #l");
        } else if (status == 1) {
            if (selection == 1) {
                if(cm.haveItem(4031543,10)) {
                    cm.gainItem(4031543,-10);
                    cm.gainItem(4031544, 1);
                    cm.sendOk("Enjoy Your tickets!");
                } else
                    cm.sendOk("Sorry, you don't have 10 #b#v4031543##k");
                cm.dispose();
            } else if (selection == 2) {
                if(cm.haveItem(4031544,10)) {
                    cm.gainItem(4031544,-10);
                    cm.gainItem(4031545,1);
                    cm.sendOk("Enjoy Your tickets!");
                } else
                    cm.sendOk("Sorry, you don't have 10 #b#v4031544##k");
                cm.dispose();
            } else if (selection == 3) {
                if(cm.haveItem(4031544,1)) {
                    cm.gainItem(4031544, -1);
                    cm.gainItem(4031543,10);
                    cm.sendOk("Enjoy Your tickets!");
                } else
                    cm.sendOk("Sorry, you don't have a #b#v4031544##k");
                cm.dispose();
            } else if (selection == 4) {
                if(cm.haveItem(4031545,1)) {
                    cm.gainItem(4031544,10);
                    cm.gainItem(4031545,-1);
                    cm.sendOk("Enjoy Your tickets!");
                } else
                    cm.sendOk("Sorry, you don't have a #b#v4031545##k");
                cm.dispose();
            } else if (selection == 5) { //All YWT - GWT
				var ywt = Math.floor(cm.itemQuantity(4031543)/10)
				if (ywt > 0) {
					cm.gainItem(4031544, ywt);
					cm.loseItem(4031543, (ywt*10));
					cm.sendOk("Enjoy your tickets!");
					cm.dispose();
				} else 
					cm.sendOk("You do not have any #v4031543#");
					cm.dispose();						
			} else if (selection == 6) { //ALL GWT - YWT
				var gwt = cm.itemQuantity(4031544) * 10
				if (gwt > 0) {
					cm.gainItem(4031543, gwt);
					cm.loseItem(4031544, (gwt/10));
					cm.sendOk("Enjoy your tickets!");
					cm.dispose();
				} else
					cm.sendOk("You do not have any #v4031544#");
					cm.dispose();
			} else if (selection == 7) { //ALL GWT-BWT
				var gwt = Math.floor(cm.itemQuantity(4031544)/10)
				if (gwt > 0) {
					cm.gainItem(4031545, gwt);
					cm.loseItem(4031544, (gwt*10));
					cm.sendOk("Enjoy your tickets!");
					cm.dispose();
				} else
					cm.sendOk("You do not have any #v4031544#");
					cm.dispose();
				} else if (selection == 8) { //ALL BWT-GWT
				var bwt = cm.itemQuantity(4031545) * 10
				if (bwt > 0) {
					cm.gainItem(4031544, bwt);
					cm.loseItem(4031545, (bwt/10));
					cm.sendOk("Enjoy your tickets!");
					cm.dispose();
				} else
					cm.sendOk("You do not have any #v4031545#");
					cm.dispose();
				}
            else
                cm.sendOk("All right. Come back later");
			
        }
    }
}


