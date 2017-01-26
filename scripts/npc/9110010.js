var status = 0;
//Chance of getting item type. Do not change if u do not noe what u are doing = ="

//Configurations From Here Onwards.

//Junk items id
//var junk = new Array();
//Junk item random. Change 2 to the size of the array - 1.
//var junkc = Math.floor(Math.random()*10);

//Scrolls items id
//var scrolls = new Array();
//Scroll item random
//var scrollsc = Math.floor(Math.random()*4);

//Eq items id
//var eq = new Array();
//Eq items random
//var eqc = Math.floor(Math.random()*4);

//Unique items id. Eg. BWG 
var items = new Array();
//Unique items random
var uniquec = Math.floor(Math.random()*294);

//itemamount. Amount of items u get for junks.
//var itemamount = Math.floor(Math.random()*50+1);

importPackage(Packages.net.sf.rise.client);

function start() {
    status = -1;
    action(1, 0, 0);
}	


function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (status >= 2 && mode == 0) {
            cm.sendOk("See you next time.");
            cm.dispose();
            return;
        }
        if (mode == 1)
            status++;
        else
            status--;
        if (status == 0) {
            if (cm.getP().getGMLevel() > 3) {
                cm.sendNext("Hello! I'm the Casino Gachapon, If you give me 1 #bBlue Wish Ticket#k you have a chance of winning rare items and other things! \r\n\r\n#rMake Sure you have space in your Inventory! We will not refund your loss. If you didn't read this, Don't complain to Staff.#k");
            } else {
                cm.sendOk("#eUnder Construction");
                cm.dispose();
            }
        } else if (status == 1) {
            if (cm.haveItem(4031545)) {
                cm.sendYesNo("I see you have a #bBlue Wish Ticket#k, Do you wish to use it?");
            }
            else if (!cm.haveItem(4031545)) {
                cm.sendOk("You dont have a #bBlue Wish Ticket#k.");
                cm.dispose();
            }
        }
        else if (status == 2) {
            cm.gainItem(4031545, -1);
            //Chance of getting item type. Do not change if u do not noe what u are doing = ="
            var type = Math.floor(Math.random());
            switch(type) {
                case 1:
                    cm.gainItem(items[uniquec], 1);
                    break;
                default:
                    cm.gainItem(items[uniquec], 1);
            }
            cm.dispose();
        }
    }
}