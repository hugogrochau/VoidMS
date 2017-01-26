//noob spawner

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
            cm.achieve(101);
            cm.sendSimple("Hello #h #! I am the Noob Boss Summoner NPC of VoidMS!\r\nWhat would you like me to spawn?#b\r\n\r\n#L1#Moss Snail (250,000 HP)#l\r\n#L2#Moss Mushroom (400,000 HP)#l\r\n#L3#Mushmom (600,000 HP)#l\r\n#L4#Blue Mushmom (800,000 HP)#l\r\n#L5#Zombie Mushmom (1,300,000 HP)#l\r\n#L6#King Slime (2,000,000 HP)#l\r\n#L7#Dyle (3,000,000 HP)#l\r\n#L8#Zeno (4,000,000 HP)#l\r\n\r\n#k#r#L9#Kill all mobs");
        } else if (status == 1) {
            if (selection == 1) {
                cm.spawnMonster(4250000, 250000, -1, -1, 150000, 0, 0, 5, 21, 20);
            } else if (selection == 2) {
                cm.spawnMonster(5250000, 400000, -1, -1, 160000, 0, 0, 5, 21, 20);
            } else if (selection == 3) {
                cm.spawnMonster(9500124, 600000, -1, -1, 170000, 0, 0, 5, 21, 20);
            } else if (selection == 4) {
                cm.spawnMonster(9400205, 800000, -1, -1, 180000, 0, 0, 5, 21, 20);
            } else if (selection == 5) {
                cm.spawnMonster(6300005, 1300000, -1, -1, 190000, 0, 0, 5, 21, 20);
            } else if (selection == 6) {
                cm.spawnMonster(9500168, 2000000, -1, -1, 300000, 0, 0, 5, 21, 20);
            } else if (selection == 7) {
                cm.spawnMonster(6220000, 3000000, -1, -1, 400000, 0, 0, 5, 21, 20);
            } else if (selection == 8) {
                cm.spawnMonster(6220001, 4000000, -1, -1, 500000, 0, 0, 5, 21, 20);
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