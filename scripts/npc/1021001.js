function start() {
    cm.sendNext("Are you here for the Coconut Rush event?");
}

function action(mode,type,selection) {
    if (mode == -1)
        cm.dispose();
    else {
        if (cm.haveItem(4000136)) {
            cm.sendOk("You already have a Coconut, go deliver it!");
            cm.dispose();
        } else {
            cm.gainItem(4000136,1);
            cm.sendOk("Here's your Coconut, go deliver it ontop of the map, but don't get tagged!");
            cm.dispose();
        }
    }
}