//medium spawner

var status = -1;

function start() {
    cm.achieve(102);
    if (cm.getPlayer().getMap().getMonsterCount() >= 15) {
        cm.sendSimple("#eThere are already enough monsters on the map, Kill those first\r\n\r\n#L9##rKill all mobs.");
    } else {
        cm.sendSimple("Hello #h #! I am the Medium Boss Summoner NPC of VoidMS!\r\nWhat would you like me to spawn?#b\r\n#L1#Priest Cat (5,000,000 HP)#l\r\n#L2#Nona-Tailed Fox (7,000,000 HP)#l\r\n#L3#Kimera (8,000,000 HP)#l\r\n#L4#Timer (10,000,000 HP)#l\r\n#L5#Lord Pirate (15,000,000 HP)#l\r\n#L6#Headless Horseman (25,000,000 HP)#l\r\n#L7#Jr. Balrog (35,000,000 HP)#l\r\n#L8#Snack Bar (45,000,000 HP)#l\r\n\r\n#r#L9#Kill all mobs");
    }
}

function action(mode, type, selection) {
    if (mode == 1) {
        if (selection == 1) {
            cm.spawnMonster(7220002, 5000000, -1, -1, 600000, 0, 0, 5, 40, 20);
        } else if (selection == 2) {
            cm.spawnMonster(7220001, 7000000, -1, -1, 650000, 0, 0, 5, 40, 20);
        } else if (selection == 3) {
            cm.spawnMonster(8220002, 8000000, -1, -1, 700000, 0, 0, 5, 40, 20);
        } else if (selection == 4) {
            cm.spawnMonster(5220003, 10000000, -1, -1, 850000, 0, 0, 5, 40, 20);
        } else if (selection == 5) {
            cm.spawnMonster(9500175, 15000000, -1, -1, 900000, 0, 0, 5, 40, 20);
        } else if (selection == 6) {
            cm.spawnMonster(9400571, 25000000, -1, -1, 950000, 0, 0, 5, 40, 20);
        } else if (selection == 7) {
            cm.spawnMonster(8130100, 35000000, -1, -1, 1000000, 0, 0, 5, 40, 20);
        } else if (selection == 8) {
            cm.spawnMonster(9500179, 45000000, -1, -1, 1050000, 0, 0, 5, 40, 20);
        } else if (selection == 9) {
            if (cm.killMonsters()) {
                cm.sendOk("#e#gAll monsters killed");
            } else {
                cm.sendOk("#e#rYou are not alone in the map, or not all map members are in your party.");
            }
        }
    }
    cm.dispose();
}
