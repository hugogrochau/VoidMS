var dif = ["Simple","Easy","Medium","Hard","Extreme"];
var status = 0;

function start() {
    status = -1
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
        return;
    } else {
        if (mode == 1)
            status++;
        else if (mode == 0 && status == 1)
            status--;
        else{
            cm.dispose();
            return;
        }
        if (status == 0) {
            cm.sendSimple("#eHello #h #, I can show you the achievements you already have and the ones you're missing. Please choose a difficulty:\r\n\r\n#n#b#L0#Simple\r\n#L1#Easy\r\n#L2#Medium\r\n#L3#Hard\r\n#L4#Extreme");
        } else if (status == 1) {
            cm.sendPrev("#e" + dif[selection] + " Achievements:\r\n" + cm.showAchievements([selection + 1]));
        }
    }
}