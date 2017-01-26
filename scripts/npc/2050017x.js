var status = 0;

function start() {
    status = -1;
    action(1, 0, 0);
}
 
function action(mode, type, selection) {
    selected = selection
    if (mode == -1) {
        cm.dispose();
    } else {
        if (status >= 0 && mode == 0) {
            cm.sendOk("See you later!");
            cm.dispose();
            return;
        }
        if (mode == 1)
            status ++;
        else
            status --;
        if (status == 0) {
            cm.sendNext("#eHello, #h #! I am your fourth instructor to help your gain your 5 rebirths! If you want to rebirth with me, you'll have to get me some items! To do this, you will choose Monsters to spawn at random. 10 of them will be Roosters, and the other 5 will get you my items!\r\n\r\nClick next to continue!");
        } else if (status == 1) {
            cm.sendSimple("#eChoose an option...\r\n\r\n#L0#I want to see what items I need!#l\r\n#L1#I want to begin my spawning!#l\r\n#L2#I have your items!#l\r\n#L3#I am Level 200, and want to continue!#l");
        } else if (status >= 2) {
            if (selection == 0) {
                cm.sendOk("#eYou need...#b\r\n\r\n#i4000049# - #z4000049#\r\n#i4000039# - #z4000039#\r\n#i4000059# - #z4000059#\r\n#i4000110# - #z4000110#\r\n#i4000331# - #z4000331#");
            } else if (selection == 1) {
                cm.sendSimple("#eOk, choose your monsters to spawn, don't forget, 10 of them are useless!\r\n\r\n#L4#Monster 1#l\r\n#L5#Monster 2#l\r\n#L6#Monster 3#l\r\n#L7#Monster 4#l\r\n#L8#Monster 5#l\r\n#L9#Monster 6#l\r\n#L10#Monster 7#l\r\n#L11#Monster 8#l\r\n#L12#Monster 9#l\r\n#L13#Monster 10#l");
            } else if (selection == 2) {
                if ((cm.itemQuantity(4000049) >= 5) && (cm.itemQuantity(4000059) >= 5) && (cm.itemQuantity(4000039) >= 5) && (cm.itemQuantity(4000110) >= 5) && (cm.itemQuantity(4000331) >= 5)) {
                    cm.loseItem(4000049, 5);
                    cm.loseItem(4000059, 5);
                    cm.loseItem(4000039, 5);
                    cm.loseItem(4000110, 5);
                    cm.loseItem(4000331, 5);
                    cm.sendOk("#eCongrats! You did it!");
                    cm.getP().setLevel(199);
		    cm.getP().levelUp();
                    cm.fixExp();
                    cm.dispose();
                } else {
                    cm.sendOk("#e#rUh-Oh! It seems you do not have all my items!");
                    cm.dispose();
                }
            } else if (selection == 3) {
                if (cm.getP().getLevel() >= 200) {
                    cm.getP().doReborn();
                    cm.warp(925100500, 0);
                    cm.dispose();
 
                } else {
                    cm.sendOk("#eIt seems you are not Level 200. Please continue training, and talk to me when you are done.");
                    cm.dispose();
                }
            }
 
            if (selection == 4) {
                cm.summonMob(9600001, 1, 1, 5);
		cm.sendOk("#e#rWrong one!");
                cm.dispose();
            } else if (selection == 5) {
                cm.sendOk("#e#gCorrect!");
                cm.summonMob(4230103, 1, 1, 10);
                cm.dispose();
            } else if (selection == 6) {
                cm.sendOk("#e#gCorrect!");
                cm.summonMob(2100104, 1, 1, 10);
                cm.dispose();
            } else if (selection == 7) {
                cm.sendOk("#e#rWrong one!");
                cm.summonMob(9600001, 1, 1, 1);
                cm.dispose();
            } else if (selection == 8) {
                cm.sendOk("#e#gCorrect!");
                cm.summonMob(3230200, 1, 1, 10);
                cm.dispose();
            } else if (selection == 9) {
                cm.sendOk("#e#rWrong one!");
                cm.summonMob(9600001, 1, 1, 1);
                cm.dispose();
            } else if (selection == 10) {
                cm.sendOk("#e#gCorrect!");
                cm.summonMob(6300000, 1, 1, 10);
                cm.dispose();
            } else if (selection == 11) {
                cm.sendOk("#e#rWrong one!");
                cm.summonMob(9600001, 1, 1, 1);
                cm.dispose();
            } else if (selection == 12) {
                cm.sendOk("#e#rWrong one!");
                cm.summonMob(9600001, 1, 1, 1);
                cm.dispose();
            } else if (selection == 13) {
                cm.sendOk("#e#gCorrect!");
                cm.summonMob(3230305, 1, 1, 10);
                cm.dispose();
            }
        }
    }
}
