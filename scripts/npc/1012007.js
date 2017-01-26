/* Trainer Frod
	Pet Trainer
	located in Pet-Walking Road (100000202)


importPackage(Packages.server.maps);

var status = 0;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) 
        cm.dispose();
    else {
        if (status >= 2 && mode == 0) {
            cm.sendOk("Alright, see you next time.");
            cm.dispose();
            return;
        }
        if (mode == 1)
            status++;
        else
            status--;
        if (status == 0)
            cm.sendNext("Great job! You made it! As a reward, I shall make you and your pet closer! Come back soon, pets need exercise daily.");
        else if (status == 1) {
            cm.gainCloseness(3);
            cm.warp(100000202, 0);
            cm.dispose();
        }
    }
}



var status = 0;

function start() {
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, selection) {
	if (mode == -1) {
		cm.warp(100000202, 0);
		cm.dispose();
	} else {
	if (status >= 0 && mode == 0) {
		cm.warp(100000202, 0);
		cm.dispose();
		return;
	}
	if (mode == 1)
		status++;
	else
		status--;
	if (status == 0) {
		if (cm.haveItem(4031922, 3)) {
			cm.warp(100000202, 0);
			cm.sendOk("Congratulations! Gathering all that poop like a madman, you truly deserve some Karma levels, two in fact!");		
			cm.gainKarma(2);
			cm.gainItem(4031922, -3);
			cm.dispose();
			} else {
			cm.warp(100000202, 0);
			cm.sendOk("Wow you made it dude! great work, here is some poop!");
			cm.gainItem(4031922);
			cm.dispose();
}					
		}

	}
}*/

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode,type,selection)
{
	cm.finishJQ();
	cm.dispose();
}