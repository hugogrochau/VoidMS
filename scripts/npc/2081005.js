var status;

function start() {
    status = -1;
    action(1, 0, 0);

}

function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else {
        status--;
    }

    if (status == 0) {
        cm.sendSimple("     Grrr, get lost intruder!\r\n\r\n#L0#Calm down dude!#l \r\n#L1#Have you ever seen sombody called Muhamad?#l");
    } else if (status == 1) {
        if (selection == 0) {
                cm.sendOk("* swings his spear forwards * \r\n\r\n#eI told you to leave!#n");
                cm.dispose();
        } else if (selection == 1) {
                cm.sendOk("That fool tried to enter the cave to look for some precious #rpurple jewel#k.. I stopped him and he left.");
                cm.dispose();
			}	
    }
}
