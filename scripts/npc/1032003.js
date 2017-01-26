/*****************************\
*   * Coded by:LilSmilee  *   *
*  * Use: Warp to Spawners *  * 
*/////////////////////////////*

var status = 0;


function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (mode == 0 && status == 1) {
            cm.dispose();
            return;
        }
        if (mode == 1)
            status++;
        else
            status--;
            if (status == 0) {
                cm.sendNext("Hello #e#b#h ##n#k!\r\nI am the #e#rVoidMS #k#nAll in one #g#eBoss Spawner Warper#k#n. \r\nI can warp you to different types of boss spawners of your choice. We have all types of spawners, therefore, no one will be missing out!");	    
    } else if (status == 1) { 
        cm.sendSimple("Here we go, there are three different type of spawners. The spawners have different monsters, HP, and EXP. Which type of spawner would you like to go? \r\n\r\n#L1##eNoob Spawner\r\n#L2#Medium Spawner\r\n#L3#Pro Spawner");	   	  
    } else if (status >= 2) {
	    if (selection == 1) {
		cm.sendSimple("#e#bNoob Spawner \r\n\#k#nSelect the map you want to go to. \r\n\r\n#L4#Noob Spawner 1\r\n#L5#Noob Spawner 2")	    	    
    } else if (selection == 2) {
		cm.sendSimple("#e#bMedium Spawner \r\n#k#nSelect the map you want to go to. \r\n\r\n#L6#Medium Spawner 1\r\n#L7#Medium Spawner 2");	    	   
	} else if (selection == 3) {
		cm.sendSimple("#e#bPro Spawner \r\n#k#nSelect the map you want to go to. \r\n\r\n#L8#Pro Spawner 1\r\n#L9#Pro Spawner 2")
	} else if (selection == 4) {
		cm.warp(912010000);
		cm.dispose();
	} else if (selection == 5) {
		cm.warp(912010200);
		cm.dispose();
	} else if (selection == 6) {
		cm.warp(980010100);
		cm.dispose();
	} else if (selection == 7) {
		cm.warp(980010200);
		cm.dispose();
	} else if (selection == 8) {
		cm.warp(980000504);
		cm.dispose();
	} else if (selection == 9) {
		cm.warp(980000604);
		cm.dispose();
            }
        }
    } 
}