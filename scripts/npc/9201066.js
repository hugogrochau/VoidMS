/* Super Rebirth NPC 

If you don't have a good item to give, just search a nice item that you wish to edit.
As soon as you got this item go check the item up in your Wz folder/character and then your item choise.
Now you will get a list with stats to edit. Make it as you want and save it and replace the ItemId with your ID 

var status = 0;
var wui = 0;
var stat = 30000; /* Displays how much you need of every stat to make the reqstat complete */
var reqstat = 30000; /*The amount of stats you wish to have for a Super Rebirth */


function start() {
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
 			cm.sendSimple ("Hey, Do you want a #rsuper rebirth#k?\r\nYou will need #r120k ap#k in total\r\nIt will make all your stats 4 and give you a ring with " + stat + " #dStr#k .\r\nDo you wish to do this?\r\n#rYou must have a slot in your inventory!#k\r\n#L0#Yes!\r\n#L1#No thank you.");
		} else if (status == 1) {
			if (selection == 0) {
				var statup = new java.util.ArrayList();
				var p = cm.c.getPlayer();
				var totAp = p.getRemainingAp() + p.getStr() + p.getDex() + p.getInt() + p.getLuk();
				if (totAp > 119999) { /* Make this how much you want to, Make it 1 less than the reqstat */
				p.setStr(4);
				p.setDex(4);
				p.setInt(4);
				p.setLuk(4);
				p.setRemainingAp (0);
	                        statup.add (new tools.Pair(client.MapleStat.STR, java.lang.Integer.valueOf(4)));
	                        statup.add (new tools.Pair(client.MapleStat.LUK, java.lang.Integer.valueOf(4)));
	                        statup.add (new tools.Pair(client.MapleStat.DEX, java.lang.Integer.valueOf(4)));
	                        statup.add (new tools.Pair(client.MapleStat.INT, java.lang.Integer.valueOf(4)));
				statup.add (new tools.Pair(client.MapleStat.AVAILABLEAP, java.lang.Integer.valueOf(p.getRemainingAp())));
				p.getClient().getSession().write (tools.MaplePacketCreator.updatePlayerStats(statup));
				cm.gainItem(1112116,1); /* Make this the item you wish to give */
				cm.sendOk ("You have Super rebirthed!! Nice job!\r\n Here is your " + stat + " stats item!");
				cm.dispose();
				} else  {
					cm.sendOk ("You don't have enough stats.\r\nYou currently have #d" + totAp + "#k in total.\r\nYou need #d" + reqstat + "#k in total!");
					cm.dispose(); 
					}
				} else if (selection == 1) {
					cm.sendOk("Alright see you next time.");
					cm.dispose();
				}
			}
		}
	}
*/


function start() {
cm.sendOk("Shouldn't you be doing homework?");
cm.dispose();
}