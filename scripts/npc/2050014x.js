var status = 0;
function start () {
    status = -1;
    action(1, 0, 0);
}
 
function action(mode, type, selection) {
    selected = selection
    if (mode == -1) {
        cm.dispose();
    } else {
        if (status >= 0 && mode == 0) {
            cm.sendOk("#eBye! Hope you come back later for more training!");
            cm.dispose();
            return;
        }
        if (mode == 1)
            status ++;
        else
            status --;
        if (status == 0) {
            cm.sendNext("#eHello, #h #. I see you're interested in our training? Your first task is going to be to kill certain monsters. You have checkpoints at Levels #r10, 30, 50, 70, 100, 120 and 200. #k When you reach these levels, please talk to me. Click next to get started!");
        } else if (status == 1) {
            cm.sendSimple("#eI am ready for my training! I am...\r\n\r\n#r#L0#Level 1#l\r\n#L1#Level 10#l\r\n#L2#Level 30#l\r\n#L3#Level 50#l\r\n#L4#Level 70#l\r\n#L5#Level 100#l\r\n#L6#Level 120#l\r\n#L7#Level 200#l");
        } else if (selection == 0) {
            if (cm.getP().getLevel() <= 9) {
                cm.summonMob(100100, 5, 1, 10);
                cm.sendOk("#ePlease kill these monsters, and then talk to me again.");
                cm.dispose();
            } else {
                cm.sendOk("#eUh-Oh! It seems you are higher than Level 10. Please check your level, and pick accordingly.");
                cm.dispose();
            }
        } else if (selection == 1) {
            if (cm.getP().getLevel() <= 29 && cm.getP().getLevel() >= 10) {
                cm.summonMob(9500104, 100, 64, 20);
                cm.sendOk("#ePlease kill these monsters, and then talk to me again.");
                cm.dispose();
            } else {
                cm.sendOk("#eUh-Oh! It seems you are either higher than Level 30 or lower than Level 10. Please check your level, and pick accordingly.");
                cm.dispose();
            }
        } else if (selection == 2) {
            if (cm.getP().getLevel() <= 49 && cm.getP().getLevel() >= 30) {
                cm.summonMob(9420510, 5000, 277, 25);
                cm.summonMob(9420511, 5000, 277, 25);
                cm.sendOk("#ePlease kill these monsters, and then talk to me again.");
                cm.dispose();
            } else {
                cm.sendOk("#eUh-Oh! It seems you are either higher than Level 50 or lower than Level 30. Please check your level, and pick accordingly.");
                cm.dispose();
            }
        } else if (selection == 3) {
            if (cm.getP().getLevel() <= 69 && cm.getP().getLevel() >= 50) {
                cm.summonMob(9400545, 50000, 1042, 25);
                cm.summonMob(9500111, 50000, 1042, 25);
                cm.sendOk("#ePlease kill these monsters, and then talk to me again.");
                cm.dispose();
            } else {
                cm.sendOk("#eUh-Oh! It seems you are either higher than Level 70 or lower than Level 50. Please check your level, and pick accordingly.");
                cm.dispose();
            }
        } else if (selection == 4) {
            if (cm.getP().getLevel() <= 99 && cm.getP().getLevel() >= 70) {
                cm.summonMob(9400576, 100000, 2618, 30);
                cm.summonMob(9400581, 100000, 2618, 30);
                cm.summonMob(9400579, 100000, 2618, 30);
                cm.summonMob(9400582, 100000, 2618, 30);
                cm.sendOk("#ePlease kill these monsters, and then talk to me again.");
                cm.dispose();
            } else {
                cm.sendOk("#eUh-Oh! It seems you are either higher than Level 100 or lower than Level 70.. Please check your level, and pick accordingly.");
                cm.dispose();
            }
        } else if (selection == 5) {
            if (cm.getP().getLevel() <= 119 && cm.getP().getLevel() >= 100) {
                cm.summonMob(8190003, 250000, 4744, 50);
                cm.summonMob(8190004, 250000, 4744, 50);
                cm.summonMob(4230125, 250000, 4744, 50);
                cm.sendOk("#ePlease kill these monsters, and then talk to me again.");
                cm.dispose();
            } else {
                cm.sendOk("#eUh-Oh! It seems you are either higher than Level 120 or lower than Level 100. Please check your level, and pick accordingly.");
                cm.dispose();
            }
        } else if (selection == 6) {
            if (cm.getP().getLevel() <= 199 && cm.getP().getLevel() >= 120) {
                cm.summonMob(8150200, 400000, 1000000000, 18);
                cm.summonMob(8150201, 400000, 1000000000, 18);
                cm.sendOk("#ePlease kill these monsters, and then talk to me again.");
                cm.dispose();
            } else {
                cm.sendOk("#eUh-Oh! It seems you are Level 200 or lower than Level 120. Please check your level, and pick accordingly.");
                cm.dispose();
            }
        } else if (selection == 7) {
            if (cm.getP().getLevel() == 200) {
                if (cm.getP().getLevel() == 200) {
                    cm.getP().doReborn();
                    cm.warp(925100200, 0);
                    cm.dispose();
                }
            } else {
                cm.sendOk("#eIt seems you are not Level 200. Please continue training, and talk to me when you are done.");
                cm.dispose();
            }
        }
    }
}