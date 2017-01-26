function start () {
cm.sendOk("Event over, come back next year");
cm.dispose();
}
//Valentines Day NPC
//Made by imPro of VoidMS



/*var status = 0;
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
			cm.dispose();
		}
		if (mode == 1)
			status ++;
		else
			status --;
		if (status == 0) {
			cm.sendNext("Hi #r#h ##k! As you most likely know, it is Valentines Day soon! There is this girl in my class, Brittany, who I have a huge crush on! I want to try and impress her on Valentines Day.");
		} else if (status == 1) {
			cm.sendSimple("Do you think you could help me? If so, please get me 30 of these Chocolates #i4031938#, and 1 Heart-Shaped Box #i4031111#. They drop from monsters, so you should be able to get them! Please return to me when you have the items.\r\n\r\n#L0#Alright, I'm going to get your Items now!#l\r\n#L1#I have your items right here!#l");
		} else if (selection == 0) {
			cm.sendOk("Thank you so much!");
			cm.dispose();
		}
		else if (selection == 1) {
			if (cm.itemQuantity(4031938) >= 30 && cm.itemQuantity(4031111) >= 1) {
			cm.gainItem(4031938, -30);
			cm.gainItem(4031111, -1);
			cm.sendOk("Thank you so much for retrieving my items, #r#h #. I made this box of chocolates with the items you gave me. I also wrote her a poem. I'm really shy around Brittany, so could you go give it to her? She hangs out in Kerning");
			cm.gainItem(4140100, 1);
			cm.gainItem(4031252, 1);
			cm.dispose();
		} else {
			cm.sendOk("Are you sure you have my items? Please go get them and come back.");
			cm.dispose();
		}
	}
}
}*/