var status = 0;
var shops = Array(1337,1338,1339,1340,1341);

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (mode == 1)
            status++;
        else
            status--;
        if (status == 0) {
            if (cm.getP().getGMLevel() > 2) {
                cm.sendSimple("Choose your shop. \n\r\n#L0#GM Shop\n\r\n#L1#Glimmer man Shop\n\r\n#L2#Misc. Items\n\r\n#L3#GM Scroll Shop\n\r\n#L4#Summoning Bags");
            } else {
                cm.sendOk("GTFO Non-GM");
                cm.dispose()
            }
        } else if (status == 1) {
            cm.openShop(shops[selection]);
        }
    }
}