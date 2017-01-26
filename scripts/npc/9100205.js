importPackage(Packages.client);

var status = 0;
var fee;
var chance = Math.floor(Math.random()*6+1);

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (mode == 0) {
            cm.sendOk("Later suckka...");
            cm.dispose();
            return;
        }
        if (mode == 1)
            status++;
        else
            status--;
        if (status == 0) {
            cm.sendNext("Welcome to VoidMS's Russian Roulette Machine. Want to take a chance getting shot in the head?");
        } else if (status == 1) {
            cm.sendGetText("How many Yellow Wish Tickets do you want to wager?");
        } else if (status == 2) {
            fee = cm.getText();
            cm.sendYesNo("Are you sure you want put in #r" + fee + "#k Tickets?... ");
        } else if (status == 3) {
            if (!cm.haveItem(4031543,[fee]) ) {
                cm.sendOk("You don't have that many tickets... You scammer. Go to hell! ");
                cm.dispose();
            } else {
                if (chance <= 1) {
                    cm.gainItem(4031543,[-fee]);
                    cm.setHP(0);
                    cm.setMP(0);
                    cm.sendNext("Jesus just doesn't Like you!");
                    cm.dispose();
                }
                else if (chance == 2) {
                    cm.gainItem(4031543,[-fee]);
                    cm.setHP(0);
                    cm.setMP(0);
                    cm.sendNext("Raped in your eyes by the VoidMS Community!");
                    cm.dispose();
                }
                else if (chance == 3) {
                    cm.gainItem(4031543,[-fee]);
                    cm.setHP(0);
                    cm.setMP(0);
                    cm.sendNext("Kicked In The Face By Chatez!");
                    cm.dispose();
                }
		else if (chance == 4) {
                    cm.gainItem(4031543,[-fee]);
                    cm.setHP(0);
                    cm.setMP(0);
                    cm.sendNext("Teabagged By Hugo!");
                    cm.dispose();
                }
		else if (chance == 5) {
                    cm.gainItem(4031543,[-fee]);
                    cm.setHP(0);
                    cm.setMP(0);
                    cm.sendNext("Boom! HEADSHOT!");
                    cm.dispose();
                }
                else if (chance >= 6) {
                    cm.gainItem(4031543,[fee*3]);
                    cm.sendNext("You got lucky boy! Wait till next time!");
                    cm.dispose();
                }
            }
        }
    }
}