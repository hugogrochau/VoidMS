var question1 = ["#e#b#L2#A. 19#l\r\n#L3#B. 23#l\r\n#L9#C. 18#l\r\n#L10#D. 15#l", "#e#b#L3#A. 15#l\r\n#L2#B. 19#l\r\n#L9#C. 23#l\r\n#L10#D. 18#l", "#e#b#L9#A. 18#l\r\n#L10#B. 15#l\r\n#L2#C. 19#l\r\n#L3#D. 23#l", "#e#b#L9#A. 23#l\r\n#L10#B. 18#l\r\n#L3#C. 15#l\r\n#L2#D. 19#l"];
var question1rand = question1[Math.floor(Math.random()*question1.length)];
var question2 = ["#e#b#L4#A. 12#l\r\n#L3#B. 10#l\r\n#L39#C. 13#l\r\n#L10#D. 7#l", "#e#b#L9#A. 7#l\r\n#L4#B. 12#l\r\n#L10#C. 10#l\r\n#L3#D. 13#l", "#e#b#L9#A. 13#l\r\n#L10#B. 7#l\r\n#L4#C. 12#l\r\n#L3#D. 10#l", "#e#b#L9#A. 10#l\r\n#L10#B. 13#l\r\n#L3#C. 7#l\r\n#L4#D. 12#l"];
var question2rand = question2[Math.floor(Math.random()*question2.length)];
var question3 = ["#e#b#L5#A. 5#l\r\n#L3#B. 3#l\r\n#L9#C. 4#l\r\n#L10#D. 6#l", "#e#b#L3#A. 6#l\r\n#L5#B. 5#l\r\n#L9#C. 3#l\r\n#L10#D. 4#l", "#e#b#L9#A. 4#l\r\n#L10#B. 6#l\r\n#L5#C. 5#l\r\n#L3#D. 3#l", "#e#b#L9#A. 3#l\r\n#L10#B. 4#l\r\n#L3#C. 6#l\r\n#L5#D. 5#l"];
var question3rand = question3[Math.floor(Math.random()*question3.length)];
var question4 = ["#e#b#L6#A. 4#l\r\n#L3#B. 5#l\r\n#L9#C. 1#l\r\n#L10#D. 2#l", "#e#b#L9#A. 2#l\r\n#L6#B. 4#l\r\n#L10#C. 5#l\r\n#L3#D. 1#l", "#e#b#L9#A. 1#l\r\n#L10#B. 2#l\r\n#L6#C. 4#l\r\n#L3#D. 5#l", "#e#b#L9#A. 5#l\r\n#L10#B. 1#l\r\n#L3#C. 2#l\r\n#L6#D. 4#l"];
var question4rand = question4[Math.floor(Math.random()*question4.length)];
var question5 = ["#e#b#L7#A. 3#l\r\n#L3#B. 6#l\r\n#L9#C. 1#l\r\n#L10#D. 5#l", "#e#b#L9#A. 5#l\r\n#L7#B. 3#l\r\n#L10#C. 6#l\r\n#L3#D. 1#l", "#e#b#L9#A. 1#l\r\n#L10#B. 5#l\r\n#L7#C. 3#l\r\n#L3#D. 6#l", "#e#b#L9#A. 6#l\r\n#L10#B. 1#l\r\n#L3#C. 5#l\r\n#L7#D. 3#l"];
var question5rand = question5[Math.floor(Math.random()*question5.length)];
 
function start() {
    status = -1;
    action(1, 0, 0);
}
 
function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (status >= 0 && mode == 0) {
            cm.dispose();
            return;
        }
        if (mode == 1)
            status++;
        else
            status--;
        if (status == 0) {
            cm.sendNext("#eHello! I am the third NPC in your series of miniquests to gain 5 rebirths!");
        }
        else if (status == 1) {
            cm.sendSimple("#eMy job is to quiz you about this map. I will be asking you a series of 5 questions regarding this map.\r\n\r\n#b #L0#I am ready to take the quiz!#l\r\n #L1#I need to look around the map some more.#l\r\n #L8#I am level 200 and ready to continue to the next map!#l");
        } else {
            if (selection == 0) {
                cm.sendSimple("#e#dQuestion 1#k\r\nHow many boxes/crates are in this map?\r\n\r\n\r\n" + question1rand + "");
            } else if (selection == 2) {
                cm.sendSimple("#e#dQuestion 2#k\r\nHow many steps/stairs are in this map?\r\n\r\n\r\n" + question2rand + "");
            } else if (selection == 3) {
                cm.sendOk("#eSorry, that answer is incorrect. Please try again!");
                cm.dispose();
            } else if (selection == 4) {
                cm.sendSimple("#e#dQuestion 3#k\r\nHow many doors are in this map?\r\n\r\n\r\n" + question3rand + "");
            } else if (selection == 5) {
                cm.sendSimple("#e#dQuestion 4#k\r\nHow many life rafts are in this map?\r\n\r\n\r\n" + question4rand + "");
            } else if (selection == 6) {
                cm.sendSimple("#e#dQuestion 5#k\r\nHow many skulls are in this map?\r\n\r\n\r\n" + question5rand + "");
            } else if (selection == 7) {
                cm.sendOk("Congratulations! You have finished my quest. Here is some exp to help you in your adventure!");
                for (var i=0; i<20; i++){
                    cm.gainExp(2100000000);
                }
                cm.dispose();
            } else if (selection == 8) {
                if (cm.getP().getLevel() == 200) {
                    cm.getP().doReborn();
                    cm.warp(925100400, 0);
                    cm.dispose();
                } else {
                    cm.sendOk("#eIt seems you are not Level 200. Please continue the quiz and talk to me when you are done.");
                    cm.dispose();
                }
            } else if (selection == 9) {
                cm.sendOk("#eSorry, that answer is incorrect. Please try again!");
                cm.dispose();
            } else if (selection == 10) {
                cm.sendOk("#eSorry, that answer is incorrect. Please try again!");
                cm.dispose();
            }
        }
    }
}