importPackage(Packages.client); 

var status = 0; 
var fee; 
var winner; 
var card1 = Math.floor(Math.random()*5);  Array(4000001, 4000001, 4000002, 4000003, 4000004)
var card2 = Math.floor(Math.random()*5);  Array(4000001, 4000001, 4000002, 4000003, 4000004)
var card3 = Math.floor(Math.random()*5);  Array(4000001, 4000001, 4000002, 4000003, 4000004)
var card4 = Math.floor(Math.random()*5);  Array(4000001, 4000001, 4000002, 4000003, 4000004)
var card5 = Math.floor(Math.random()*5);  Array(4000001, 4000001, 4000002, 4000003, 4000004)
var card6 = Math.floor(Math.random()*5);  Array(4000001, 4000001, 4000002, 4000003, 4000004)
var card7 = Math.floor(Math.random()*5);  Array(4000001, 4000001, 4000002, 4000003, 4000004)
var card8 = Math.floor(Math.random()*5);  Array(4000001, 4000001, 4000002, 4000003, 4000004)
var card9 = Math.floor(Math.random()*5);  Array(4000001, 4000001, 4000002, 4000003, 4000004)

function start() { 
    status = -1; 
    action(1, 0, 0); 
} 

function action(mode, type, selection) { 
    if (mode == -1) { 
        cm.dispose(); 
    } else { 
        if (mode == 0) { 
            cm.sendOk("Your loss. You could have won big money."); 
        cm.dispose(); 
        return; 
        } 
        if (mode == 1) 
            status++; 
        else 
            status--; 
        if (status == 0) { 
            cm.sendNext("Would you like to play #bSlots?#k If you win, you can double your mesos!"); 
        } else if (status == 1) { 
		if (cm.getMeso() < 1000000000) {
            cm.sendGetText("How many mesos would you like to bet?"); 
		} else {
			cm.sendOk("You have more than 1 Bil, Otherwise you'd lose your money");
			cm.dispose();
	}
        } else if (status == 2) { 
            fee = cm.getText(); 
            cm.sendYesNo("Are you sure you want to bet #r" + fee + "#k Mesos?... "); 
        } else if (status == 3) { 
            if (cm.getMeso() < fee) { 
                cm.sendOk("You don't have that much money..."); 
                cm.dispose(); 
            } else if (cm.getText() < 1) { 
                cm.sendOk("You have to bet at least 1 meso."); 
                cm.dispose(); 
            } else if (cm.getMeso() + fee * 2 > 2000000000) { 
                cm.sendOk("You can't bet that much money."); 
                cm.dispose(); 
            } else {
                if (card1 == card2 && card2 == card3) { 
                    winner = 1;
                } else if (card1 == card5 && card5 == card9) { 
                    winner = 1; 
                } else if (card4 == card5 && card5 == card6) { 
                    winner = 1; 
                } else if (card7 == card8 && card8 == card9) { 
                    winner = 1; 
                } else if (card7 == card5 && card5 == card3) { 
                    winner = 1; 
                } else { 
                    winner = 2; 
                } 
                if (winner == 1) { 
                    cm.sendOk("Slots: \r\n\r\n#r#e" + 
                    card1 + " " + card2 + " " + card3 + "\r\n" + 
                    card4 + " " + card5 + " " + card6 + "\r\n" +
                    card7 + " " + card8 + " " + card9 +  
                    "\r\n\r\n#k#n You won! Congratulations!");
                    cm.gainMeso(fee); 
                    cm.dispose(); 

                } else if (winner == 2) {
                    cm.sendOk("Slots: \r\n\r\n#r#e" + 
                    card1 + " " + card2 + " " + card3 + "\r\n" + 
                    card4 + " " + card5 + " " + card6 + "\r\n" +
                    card7 + " " + card8 + " " + card9 +  
                    "\r\n\r\n#k#n You lost, come again!"); 
                    cm.gainMeso(-fee); 
                    cm.dispose(); 
        } else {
            cm.dispose();

                } 
            } 
        } 
    } 
}