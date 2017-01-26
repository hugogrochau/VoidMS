/*
 boss summoner npc */

var status = -1;

function start() {

    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.sendOk("Are you sure? Scaredy cat.");
        cm.dispose();
    } else {
        if (mode == 1) {
            status++;
        } else {
            status--;
        }
        if (status == 0) { 
        if (cm.getPlayer().getMap().getMonsterCount() > 39){
		cm.sendOk("There are already enough monsters on the map, Kill those first");
	} else if (cm.getChar().getMapId() == 104000000) {
		cm.sendOk("I may be strong, but this octopus behind me is very deadly.\r\nI heard my brother Pason in FM knows how to #r#eSpawn Monsters#k#n.");
		cm.dispose();
	} else {
            cm.sendSimple("Hello #h #! I am the boss summoner NPC of VoidMS! \r\n What would you like me to spawn? \r\n#e I'll spawn 4 of boss of your choice.#n \r\n Please choose #b\r\n#L1#Papu clock (200,000,000 HP)#l\r\n#L2#Pianus (350,000,000 HP)#l\r\n#L3#BlackCrow(500,000,000 HP)#l\r\n#L4#Anego (750,000,000 HP)#l\r\n#L5#BodyGuard A (1,750,000,000 HP)#l\r\n#L6#Bodyguard B (1,900,000,000 HP)#l\r\n#L7#The Boss (2,100,000,000 HP)#l#k");
        }
	} else {
            if (selection == 1) {
                cm.summonMob(8500001, 200000000, 500000, 4);
                cm.dispose();
            } else if (selection == 2) {
                cm.summonMob(8510000, 350000000,600000, 4);
                cm.dispose();
            } else if (selection == 3) {
                cm.summonMob(9400014, 500000000,750000, 4);
                cm.dispose();
            } else if (selection == 4) {
                cm.summonMob(9400121, 750000000, 800000, 4);
                cm.dispose();
            } else if (selection == 5) {
                cm.summonMob(9400112, 1750000000, 850000, 4);
                cm.dispose();
            } else if (selection == 6) {
                cm.summonMob(9400113, 1900000000, 950000, 4);
                cm.dispose();
            } else if (selection == 7) {
                cm.summonMob(9400300, 2100000000, 1000000, 4);
                cm.dispose();
            } else {
                cm.dispose();
            }
        }
    }
}
