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
            if (cm.getPlayer().getGMLevel() >= 1) {
                cm.sendSimple("#e#rGM PANEL#k#n \r\n\r\n#L0#Start a game#l \r\n#L1#Nothing yet#l");
            } else {
                cm.sendOk("You are not a GM.");
                cm.dispose();
            }
        } else if (selection == 0) {
            cm.sendOk("#ePlace the reactor (9202001) using !sreactor. \r\n\r\nUsing !rreactor will refresh it when it's  after a round. \r\n\r\nDo !clock 30 for them to prepare, then !clock 300. \r\n\r\nUse !killnear in a skill Macro, but use a slow skill, otherwise it's overpowered. \r\n\r\nYour also free to use !tag if that's easier (USE TAG).#n");
            //Map timer 5 minutes start
            //Reactor respawn (like !rreactor)
            //Map Message: The game has started!
            cm.dispose();
        } else if (selection == 1) {
            cm.dispose();          
        }
    }
}