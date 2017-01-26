function start() {
    cm.sendNext("Are you here for the Coconut Harvest event?");
}

function action(mode,type,selection) {
    if (mode == -1)
        cm.dispose();
    else {
        if (cm.haveItem(4000136)) {
            cm.sendOk("You already have a #i4000136#, go deliver it!");
            cm.dispose();
        } else {
            cm.gainItem(4000136,1);
            cm.sendOk("Here's your #i4000136#, go deliver it ontop of the map!");
            cm.dispose();
        }
    }
}