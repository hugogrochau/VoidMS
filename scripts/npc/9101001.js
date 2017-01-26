/* Author: Xterminator
	NPC Name: 		Peter
	Map(s): 		Maple Road: Entrance - Mushroom Town Training Camp (3)
	Description: 	Takes you out of Entrace of Mushroom Town Training Camp
*/
var status = 0;

function start() {
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, selection) {
	if (mode == -1) {
		cm.dispose();
	} else {
		if (mode == 1)
			status++;
		else
			status--;
		if (status == 0) {
			cm.sendYesNo("You finished your training! Want to head off to maple Island?");
		} else if (status == 1) {
			cm.warp(100000000);
			cm.setHP(0);
			cm.sendOk("That's what you get for trying to cheat! No items for you either!");
			cm.dispose();
		} 
	}
}