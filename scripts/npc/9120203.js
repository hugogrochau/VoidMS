//pro spawner

var status = -1;

function start() {
    if (cm.getPlayer().getMap().getMonsterCount() >= 15) {
        cm.sendSimple("#eThere are already enough monsters on the map, Kill those first\r\n\r\n#L9##rKill all mobs.");
        status = 0;
    } else {
        status = -1;
        action(1, 0, 0);
    }
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.sendOk("Are you sure? Don't  be scared");
        cm.dispose();
    } else {
        if (mode == 1) 
            status++;
        else 
            status--;        
        if (status == 0) {
            cm.achieve(103);
	    cm.sendSimple("Hello Donors! I am based off the pro spawner.\r\nWhat would you like me to spawn?\r\n\r\n#b#L1#Griffey (60,000,000 HP)#l\r\n#L2#Manon (75,000,000 HP)#l\r\n#L3#Leviathan (90,000,000 HP)#l\r\n#L4#Anego (100,000,000 HP)#l\r\n#L5#Papulatus (200,000,000 HP)#l\r\n#L6#Black Crow (300,000,000 HP)#l\r\n#L7#The Boss (450,000,000 HP)#l\r\n#L8#Bigfoot(800,000,000 HP)#l\r\n\r\n#r#L9#Kill all mobs");
        } else if (status == 1) {
            if (selection == 1) {
                cm.spawnMonster(9500173, 60000000, -1, -1, 1100000, 0, 0, 5, 44, -25);
            } else if (selection == 2) {
                cm.spawnMonster(9500174, 75000000, -1, -1, 1200000, 0, 0, 5, 44, -25);
            } else if (selection == 3) {
                cm.spawnMonster(9500333, 90000000, -1, -1, 1300000, 0, 0, 5, 44, -25);
            } else if (selection == 4) {
                cm.spawnMonster(9400121, 100000000, -1, -1, 1400000, 0, 0, 5, 44, -25);
            } else if (selection == 5) {
                cm.spawnMonster(9500180, 200000000, -1, -1, 1500000, 0, 0, 5, 44, -25);
            } else if (selection == 6) {
                cm.spawnMonster(9400014, 300000000, -1, -1, 1700000, 0, 0, 5, 44, -25);
            } else if (selection == 7) {
                cm.spawnMonster(9400300, 450000000, -1, -1, 1850000, 0, 0, 5, 44, -25);
	    } else if (selection == 8) {
		cm.spawnMonster(9400575, 800000000, -1, -1, 900000, 0 ,0, 5, 44, -25);
            } else if (selection == 9) {
                if (cm.killMonsters()) {
                    cm.sendOk("#gAll mobs killed");
                } else {
                    cm.sendOk("#rYou are not alone in the map, or not all map members are in your party.");
                }        
            }
            cm.dispose();
        }
    }
}