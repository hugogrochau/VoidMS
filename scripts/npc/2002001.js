var status = 0;
function start() {
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, selection) {
	selected = selection
	if (mode == -1) {
		cm.dispose();
	} else {
		if (status >= 0 && mode == 0) {
			cm.dispose();
		}
		if (mode == 1)
			status ++;
		else
			status --;
    	if (status == 0) {
			cm.sendSimple("#eWelcome to the Secret Santa Gift Shop! Santa is out right now, but luckily Rudi is here to run the shop! Here you can browse through all the available items that you can get for your Secret Santa! Which category would you like to see?\r\n\r\n#b#L0#Hats#l\r\n#L1#Tops#l\r\n#L2#Bottoms#l\r\n#L3#Overalls#l\r\n#L4#Shoes#l\r\n#L5#Weapons#l");
	} else if (selection == 0) {
			cm.sendOk("#eThe available hats are:\r\n\r\n#i1002996# - Mini Cake\r\n#i1003039# - #z1003039#\r\n#i1003086# - Baby Turkey Hat\r\n#i1003134# - #z1003134#\r\n#i1002838# - White Bunny Ears\r\n#i1003165# - Antoinette Wig\r\n#i1003184# - Christmas Holly Reindeer Hat\r\n#i1003207# - #z1003207#\r\n#i1002881# - #z1002881#\r\n#i1002880# - #z1002880#\r\n#i1003048# - Christmas Wreath\r\n#i1003058# - #z1003058#");
			cm.dispose();
	} else if (selection == 1) {
			cm.sendOk("#eThe available tops are:\r\n\r\n#i1042154# - #z1042154#\r\n#i1042188# - #z1042188#\r\n#i1042193# - Blue Vest Ensemble\r\n#i1042201# - Black Referee Top\r\n#i1041109# - #z1041109#\r\n#i1041138# - #z1041138#");
			cm.dispose();
	} else if (selection == 2) {
			cm.sendOk("#eThe available bottoms are:\r\n\r\n#i1062126# - #z1062126#\r\n#i1062123# - #z1062123#\r\n#i1060132# - #z1060132#");
			cm.dispose();
	} else if (selection == 3) {
			cm.sendOk("#eThe available overalls are:\r\n\r\n#i1052325# - Christmas Overall\r\n#i1052331# - #z1052331#\r\n#i1052284# - #z1052284#\r\n#i1052228# - #z1052228#\r\n#i1052190# - Reindeer Tights\r\n#i1052145# - #z1052145#");
			cm.dispose();
	} else if (selection == 4) {
			cm.sendOk("#eThe available shoes are:\r\n\r\n#i1072437# - #z1072437#\r\n#i1072439# - #z1072439#\r\n#i1072278# - #z1072278#");
			cm.dispose();
	} else if (selection == 5) {
			cm.sendOk("#eThe available weapons are:\r\n\r\n#i1702287# - #z1702287#\r\n#i1702285# - #z1702285#\r\n#i1702138# - #z1702138#\r\n#i1702100# - #z1702100#");
			cm.dispose();
	}
}
}