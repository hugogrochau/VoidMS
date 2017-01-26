importPackage(Packages.client);

var status = 0;
var selected = 1;
var wui = 0;
var stat = 30000; 

function start() {
    donaPoints = cm.getPlayer().getDP();
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
            cm.sendSimple ("#eHey #h #, do you want a #rSuper Rebirth Ring#k?\r\n\r\nYou will need #r40 Donation Points#k. I will give you a ring with " + stat + " of the selected stat.\r\n\r\nChoose a stat:#n#b\r\n#L0#STR\r\n#L1#DEX\r\n#L2#LUK\r\n#L3#INT#l\r\n\r\n#rYou must have a slot in your inventory!#k");
        } else if (status == 1) {
            if (donaPoints >= 40) {
                if (cm.isFull()) {
                    cm.sendOk("#rYou do not have enough space in your inventory");
                    cm.dispose();
                    return;
                }
                switch (selection) {
                    case 0 :
                        cm.makeItem(1112230, cm.getP(),30000,0,0,0,0);
                        break; //STR = Coke(White) Quote Ring
                    case 1 :
                        cm.makeItem(1112211, cm.getP(),0,30000,0,0,0);
                        break; //DEX = Pink Lady Quote Ring
                    case 2 :
                        cm.makeItem(1112106, cm.getP(),0,0,0,30000,0);
                        break; //LUK = Blue-Ribboned Label Ring
                    case 3 :
                        cm.makeItem(1112122, cm.getP(),0,0,30000,0,0);
                        break; //INT = Rainbow Label Ring
                }
                cm.getPlayer().giveDP(-40);
                cm.sendOk ("#eYou have Super rebirthed. Nice job!\r\n Here is your #n#r" + stat + " stat SRB Ring!");
                cm.dispose();
            } else  {
                cm.sendOk ("#eYou do not have at least #r40#k Donation Points.");
                cm.dispose();
            }
        }
    }
}

