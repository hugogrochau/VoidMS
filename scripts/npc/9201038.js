function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    selected = selection;
    if (mode == -1) {
        cm.dispose();
    } else {
        if (status >= 0 && mode == 0) {
			cm.sendOk("Ahh! Maybe next time!");
            cm.dispose();
            return;
        }
        if (mode == 1)
            status++;
        else
            status--;
        if (status == 0) {
		cm.sendNext("Ahoooy!")
		}
		else if (status == 1) {
		cm.sendNext("Mate, my wife, I miss her so much! I have been out at this sea damn near 274 days! My wife said on the 150th day she would send me 150 hearts!");
		}
		else if (status == 2) {
		cm.sendSimple("Well mate, the thing is I still haven't gotten those hearts and I've been waiting. I know she sent them. Please mate, go find them for me. I believe the #bBubble Fish#k have stolen them. Please go kill them mate. Get my 150 #i4000164# and come back. \r\n\r\n  #L0#Okay, I will get them for you!#l \r\n  #L1#I have 150 #i4000164#!#l");
		}
		else if (selection == 0) {
		cm.sendOk("Thank you mate!");
		cm.dispose();
		}
		else if (selection == 1) {
		if (cm.haveMeso() < 1800000000){
		cm.gainItem(4000164, -150);
		cm.gainMeso(200000000);
		cm.dispose();
		} else {
		cm.sendOk("You do not have them all yet mate!");
		cm.dispose();
			}
		}
	}
}