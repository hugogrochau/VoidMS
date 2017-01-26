/* Sera
* By Moogra
*/


function start() {
	if (cm.getP().getLevel() < 2 && cm.getP().getReborns() < 1) {
    cm.sendSimple("#v3991048##v3991030##v3991037##v3991028##v3991040##v3991038##v3991030#  #v3991045##v3991040# \r\n #v3991021##v3991014##v3991008##v3991003##v3991012##v3991018#\r\n \r\n#rYou are about to begin your new story in VoidMS! Great adventures await you. I will be giving you a small package so you can have a better chance at being a great player one day. Ready to begin? Pick a job : \r\n#L0##bBeginner#k#l \r\n\#L1##bWarrior#k#l \r\n\#L2##bMagician#k#l \r\n\#L3##bBowman#k#l \r\n\#L4##bThief#k#l \r\n\#L5##bPirate#k#l");
} else {
	cm.sendOk("Cheater! Go Away!");
	cm.dispose();
}
}

function action(mode, type, selection) {
    if (mode < 1) {
        cm.dispose();
    } else {	
        cm.gainItem(2000005, 100);
        cm.gainItem(2022282, 10);
        cm.gainItem(1002232, 1);
        cm.gainItem(1052047, 1);	
        cm.gainItem(1002140, 1);
        cm.gainItem(1902040, 1);
        cm.gainItem(1912033, 1);
        switch (selection) {
            case 0: // beginner
                cm.gainItem(1302000);
                break;
            case 1: // warrior
                cm.gainItem(1302027);
		cm.gainItem(1302006);
		cm.gainItem(1302071);
                break;
            case 2: // mage
                cm.gainItem(1372005);
		cm.gainItem(1382004);
		cm.gainItem(1382042);
                break;
            case 3: // bowman
                cm.gainItem(1452002);
		cm.gainItem(1452027);
		cm.gainItem(1452048);
		cm.gainItem(2060000, 1000);
                break;
            case 4: // thief
                cm.gainItem(1472000);
		cm.gainItem(1472006);
		cm.gainItem(1472058);
		cm.gainItem(1332007);
		cm.gainItem(1332008);
		cm.gainItem(1332059);
		cm.gainItem(2070005, 1000);
                break;
            case 5: // pirate
                cm.gainItem(1492000);
		cm.gainItem(1492002);
		cm.gainItem(1492004);
		cm.gainItem(1482000);
		cm.gainItem(1482002);
		cm.gainItem(1482004);
		cm.gainItem(2330000, 1000);
                break;
        }
        cm.gainAp(250);
        cm.gainMeso(25000000);
        cm.warp(100000000,0);
        cm.changeJobById([selection] * 100);
        cm.getP().levelUp();
        cm.getP().levelUp();
        cm.getP().levelUp();
        cm.getP().levelUp();
        cm.getP().levelUp();
        cm.getP().levelUp();
        cm.getP().levelUp();
        cm.getP().levelUp();
        cm.getP().levelUp();
        cm.fixExp();
        cm.openNpc(9201097);
    }
}