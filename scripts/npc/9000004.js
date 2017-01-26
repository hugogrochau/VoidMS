function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1 || status > 1)  {
        cm.dispose();
        return;
    }
    if (mode == 1)
        status++;
    else
        status--;
    if (status == 0) {
        cm.sendSimple("#eHello and welcome to the #rzombie refugee center#k! The zombies have taken over VoidMS!\r\n\r\n#n#b#L0#How do I play?\r\n#L1#How do I win?\r\n#L2#How do I leave?");
    } else if (status == 1) {
        if (selection == 0) {
            cm.sendPrev("#eOnce the one minute timer runs out, someone at random will be chosen. He or she will become a zombie! Being a zombie means you get blue skin and a dark flaming skull above your head. Once a zombie, you must walk over different people to contaminate them. They will then become a zombie as well!");
        } else if (selection == 1) {
            cm.sendPrev("#eYou can either win by being the last one to survive, surviving til the end of the 10 minute clock, or by being the zombie who contaminates the most people.\r\n\r\nGood luck!");
        } else if (selection == 2) {
            cm.sendPrev("#eYou can leave by using the command @leave");
        }
    }
}
