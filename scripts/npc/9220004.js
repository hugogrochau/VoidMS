var status = 0;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (mode == 0 && status == 0) {
            cm.dispose();
            return;
        }
        if (mode == 1)
            status++;
        else
            status--;
        if (status == 0) {
            if (cm.getChar().getMapId() == 209000000) {
                cm.sendSimple("Hello, I'm the Elf of Voidmas. I guess you've heard the stories about the #d#eSnowman#k#n living near #r#eSanta's#k#n house. Well, you came to the right guy. What is it that you need?\r\n\r\n#b#L0#What is the snowman and how do I kill him?#l\r\n#L1#I would like to fight the snowman.#l\r\n#L2#I need some snowballs.#l\r\n#L3#Nothing, I'll see you around.#l");
            } else {
                cm.sendSimple("What do you want?\r\n\r\n#b#L2#Buy some Snowballs#l\r\n#L20#Go back to Happyville!#l\r\n#L21#Nothing.#l");
            }
        } else if (status == 1) {
            if (selection == 20) {
                cm.warp(209000000);
                cm.dispose();
            } else if (selection == 21) {
                cm.sendOk("Okay, continue what you were doing.");
                cm.dispose();
            } else if (selection == 0) {
                cm.sendOk("The snowman near santas house can only be illed with snowballs. You can only attack by using a magic mitten bought in the Thief claw section of @shop. Good luck. I sell snowballs aswell!");
                cm.dispose();
            } else if (selection == 1) {
                cm.warp(209080000);
                cm.dispose();
            } else if (selection == 2) {
                cm.sendSimple("Would you like to buy some snowballs? How many?\r\n\r\n#b#L10#100 for 100 mil#l\r\n#L11#1000 for 1 bil#l");
            } else if (selection == 3) {
                cm.sendOk("I'll see you later then.");
                cm.dispose();
            }
        } else if (status == 2) {
            if (selection == 10) {
                if (cm.getMeso() >= 100000000) {
                    cm.gainMeso(-100000000);
                    cm.gainItem(2060006, 100);
                    cm.sendOk("Here's your snowballs.");
                    cm.dispose();
                } else { 
                    cm.sendOk("You don't have that many mesos.");
                    cm.dispose();
                }
            } else if (selection == 11) {
                if (cm.getMeso() >= 1000000000) {
                    cm.gainMeso(-1000000000);
                    cm.gainItem(2060006, 1000);
                    cm.sendOk("Here's your snowballs.");
                    cm.dispose();
                } else { 
                    cm.sendOk("You don't have that many mesos.");
                    cm.dispose();
                }
            }
        }
    }
}