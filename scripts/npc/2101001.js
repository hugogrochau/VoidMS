/*
	By Mac
	Max Stat Item NPC
        AIM:darkriuxd MSN:darkriuxd@hotmail.com
*/
importPackage(Packages.client);

var status = 0;
var selected = 1;
var wui = 0;

function start() {
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, selection) {
    selected = selection;
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
 			cm.sendAcceptDecline("Hey, Welcome to #rVoidMS#k Max Stat Item NPC!#k\r\n#rPlease Meet these Requirements: \r\n\r\n#b30,000 Stats in all#k\r\n#b250 Tetris Pieces, these ones: #v4030002#\n\r\nAnd 120 rebirths");
		} else if (status == 1) {
				if (cm.getPlayer().getStr() > 29999 && cm.getPlayer().getDex() > 29999 && cm.getPlayer().getInt() > 29999 && cm.getPlayer().getLuk() > 29999  && cm.haveItem(4030002, 250) && cm.getPlayer().getReborns() == 120){
				            var String = "Please Choose your desire item or nx you want as your new MSI. Please check your Inventory to make sure u have enough room because, we don't give back refunds.Enjoy!\r\n\r\n";
                            cm.sendSimple(""+cm.EquipList(cm.getC()));
				} else  {
					cm.sendOk ("Sorry but you don't meet the requirements to do this procedure");
					cm.dispose();
				}
		} else if (status == 2) {
		     cm.MakeGMItem(selected, cm.getP());
			  cm.getPlayer().setStr(4); cm.getPlayer().setDex(4); cm.getPlayer().setLuk(4); cm.getPlayer().setInt(4);
              cm.loseItem(4030002, 250);
              cm.getplayer().setReborns(cm.getPlayer().getReborns-120);
                cm.reloadChar();
                cm.dispose();
         }
        if (selection == 1) {
				cm.sendOk("Alright see you next time.");
				cm.dispose();
			}
		}
	}
