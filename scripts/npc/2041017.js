

var status = 0;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (mode == 2 && status == 0) {
            cm.dispose();
            return;
        }
        if (mode == 1)
            status++;
        else
            status--;
        if (status == 0) {
            cm.sendSimple("#eHello #h #, I can sell you some counterfeit fame brother. How much do you need?#n#b\r\n#L1#1 fame for 1,000,000 mesos#l\r\n#L5#5 fame for 5,000,000 mesos#l\r\n#L15#15 fame for 15,000,000 mesos");
        } else if (status == 1) {
            var meso = selection * 1000000;
            var fame = selection;
            cm.sendOk("#eAlright dude, here's " + fame + " fame. Thanks for your mesos.");
            cm.gainMeso(-meso);
            cm.gainFame(selection);
            cm.dispose();
        }
    }
}