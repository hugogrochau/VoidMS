var status;
var number;

function start() {
    status = -1;
    action(1, 0, 0);
   
}
 
function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else {
        status--;
    }
   
    if (status == 0) {
	if (cm.isEtcFull()) {
		cm.sendOk("Please make some space in your inventory");
		cm.dispose();
	} else {
		cm.sendSimple("I have been working with lidium all my life. It is such a precious jewel. What would you need?\r\n\r\n#d#L0#I want to make some Lidium.#l\r\n#L1#Ill be on my way.#l");
	}
	} else if (status == 1) {
		if (selection == 0) {
		cm.sendGetText("How many do you want to make?");
	} else if (selection == 1) {
		cm.sendOk("Ill see you later then.");
		cm.dispose();
	}
	} else if (status == 2) {
			number = cm.getText();
		if (cm.itemQuantity(4010007) >= number*10) {
			cm.loseItem(4010007, number*10);
			cm.gainItem(4011008, number);
			cm.sendOk("Here ya go. Enjoy");
			cm.dispose();
	} else {
		cm.sendOk("Either you entered an invalid number or you dont have enough lidium ores.");
		cm.dispose();
	}
    }
}
