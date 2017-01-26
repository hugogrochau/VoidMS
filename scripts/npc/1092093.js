/*****************************\
*   * Coded by:LilSmilee  *   *
*  * Use:Noob Boss Spawner *  * 
*/////////////////////////////*

var status = -1;

function start() {

    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.sendOk("Are you sure? Don't  be scared");
        cm.dispose();
    } else {
        if (mode == 1) {
            status++;
        } else {
            status--;
        }
        if (status == 0) { 
        if (cm.getPlayer().getMap().getMonsterCount() >= 15){
		cm.sendOk("There are already enough monsters on the map, Kill those first.");
		cm.dispose();
	} else {
            cm.sendSimple("Hello #h #! I am the Noob Boss Summoner NPC of VoidMS! \r\n What would you like me to spawn? \r\n#e I'll spawn 5 bosses of your choice.#n \r\n Please choose #b\r\n#L1#Dyle (100,000 HP)#l\r\n#L2#Zeno (500,000 HP)#l\r\n#L3#Nona-Tailed Fox(1,000,000 HP)#l\r\n#L4#Tae Roon (5,000,000 HP)#l\r\n#L5#Frankenroid (10,000,000 HP)#l\r\n#L6#Priest Cat (25,000,000 HP)#l\r\n#L7#Leviathan(50,000,000 HP)#l#k");
        }
	} else {
            if (selection == 1) {
                cm.spawnMonster(6220000, 100000, -1, -1, 5000, 0, 0, 5, -98, 150);
                cm.dispose();
            } else if (selection == 2) {
                cm.spawnMonster(6220001, 500000, -1, -1, 10000, 0, 0, 5, -98, 150);
                cm.dispose();
            } else if (selection == 3) {
                cm.spawnMonster(7220001, 1000000, -1, -1, 20000, 0, 0, 5, -98, 150);
                cm.dispose();
            } else if (selection == 4) {
                cm.spawnMonster(7220000, 5000000, -1, -1, 50000, 0, 0, 5, -98, 150);
                cm.dispose();
            } else if (selection == 5) {
                cm.spawnMonster(9500335, 10000000, -1, -1, 100000, 0, 0, 5, -98, 150);
                cm.dispose();
            } else if (selection == 6) {
                cm.spawnMonster(7220002, 25000000, -1, -1, 150000, 0, 0, 5, -98, 150);
                cm.dispose();
            } else if (selection == 7) {
                cm.spawnMonster(9500333, 50000000, -1, -1, 250000, 0, 0, 5, -98, 150);
                cm.dispose();
            } else {
                cm.dispose();
            }
        }
    }
}