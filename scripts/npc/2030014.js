/*importPackage(Packages.client);

var status = 0;
var selected = 1;
var wui = 0;


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
            cm.sendSimple("Hello #d#h ##k, Im the MSI Rock. What would you like to do?\r\n\r\n#b#L0#What does this NPC do?#l\r\n#L1#I want my MSI now#l\r\n#L2#Not Now, Sorry#l");
        } else if (status == 1) {    
            if (selection == 0) {
                cm.sendOk("#bThis NPC takes an equip from your inventory of your choice, and erases all the stats, then gives it 30000 Str, Luk, Int and Dex with 0 slots. If you want to do this, you need the following Items.\r\n\r\n#r#e#i1112116##i1112120##i1112226##i1112230#\r\n1 of #i4031545# \r\n30,000 of each stat(str, luk, int, dex)\r\n5 Karma");
                cm.dispose();
            } else if (selection == 2) {
                cm.sendOk("Alright see you next time.");	    
                cm.dispose(); 
            } else if (selection == 1) {
		if (cm.isFull()) {
			cm.sendOk("Your inventory is #r#eFull#k#n, Make some space.");
			cm.dispose();
               } else if (cm.getPlayer().getStr() > 29999 && cm.getPlayer().getDex() > 29999 && cm.getPlayer().getInt() > 29999 && 
                    cm.getPlayer().getLuk() > 29999 && cm.haveItem (4031545, 1) && cm.haveItem (1112116, 1) && cm.haveItem (1112120, 1) && 
                    cm.haveItem (1112226, 1) && cm.haveItem (1112230, 1) && cm.getKarma() > 4){
                    var String = "Please Choose your desire item or nx you want as your new MSI. Please check your Inventory to make sure you have enough room because, we don't give back refunds.Enjoy!\r\n";
                    cm.sendSimple(String+cm.EquipList(cm.getC()));
                } else {
                    cm.sendOk("#e#rSorry, you do not have the requirements.\r\n\r\n#b30000 of Each Stat\r\n1 of Every SRB ring (4 in all)\r\n1 Blue Wish Ticket\r\n5 Karma");
                    cm.dispose();
                } 
            }
        } else if (status == 2) {
            cm.MakeGMItemPlayer(selection, cm.getP());
            cm.getPlayer().setStr(4);
            cm.getPlayer().setDex(4);
            cm.getPlayer().setLuk(4);
            cm.getPlayer().setInt(4);
            cm.gainItem(1112116, -1);
            cm.gainItem(1112120, -1);
            cm.gainItem(1112226, -1);
            cm.gainItem(1112230, -1);
            cm.gainItem(4031545, -1);
            cm.gainKarma(-5);
            cm.reloadChar();
            cm.dispose();
        }
    }
}*/


function start() {
cm.sendOk("#e#rMSI's are discontinued. Please check out the new srb system by using the command '@srb'.");
cm.dispose();
}