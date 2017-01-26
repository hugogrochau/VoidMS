/* Sera
* Made by imPro of VoidMS
*/


var status = 0;
function start() {
	status = -1
	action(1, 0, 0);
}
	
function action(mode, type, selection) {
	selected = selection
	if (mode == -1) {
	    cm.dispose();
	} else {
		if (status >= 0 && mode == 0) {
				cm.sendOk("Ok! Good luck on your journey!");
				cm.gainItem(2000005, 100);
				cm.gainItem(2022282, 10);
				cm.gainItem(1002232, 1);
				cm.gainItem(1052047, 1);       
				cm.gainItem(1002140, 1);
				cm.gainItem(1902040, 1);
				cm.gainItem(1912033, 1);
				cm.gainAp(250);
				cm.gainMeso(25000000);
				cm.maxSkills();
				cm.warp(910000000,0);
				for (i = 0; i < 10; i++)
				cm.getP().levelUp();
				cm.fixExp();
				cm.dispose();
            }
		
		if (mode == 1)
			status ++;
		else
			status --;
		if (status == 0) {
			cm.sendYesNo("#eWelcome to VoidMS!! We are all glad to see you here! Here at VoidMS, we have a Starter Pack for all our new Players! It includes Naricain Elixirs, Power Elixirs, an NX look, and a Custom Mount!\r\n\r\nNow, we know how hard those first Rebirths can get, so here at Void, we offer an exclusive training package to all of our new players! We will help you through your first 5 Rebirths of your MapleStory Life, through easy methods! However, we aren't going to force you to do so.\r\n#rWould you like to take part in our exclusive training package?");
		}
		else if (status == 1) {
			cm.sendOk("Great! I hope this will make your first Rebirths a lot easier, and in case I don't see you again, have fun on your journey!");
			cm.gainItem(2000005, 100);
			cm.gainItem(2022282, 10);
			cm.gainItem(1002232, 1);
			cm.gainItem(1052047, 1);       
			cm.gainItem(1002140, 1);
			cm.gainItem(1902040, 1);
			cm.gainItem(1912033, 1);
			cm.maxSkills();
			cm.gainMeso(25000000);
			cm.warp(925100100, 0);
			cm.dispose();
			}
		}
	}