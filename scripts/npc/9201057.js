/*
    This file is part of the OdinMS Maple Story Server
    Copyright (C) 2008 Patrick Huy <patrick.huy@frz.cc> 
                       Matthias Butz <matze@odinms.de>
                       Jan Christian Meyer <vimes@odinms.de>

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License version 3
    as published by the Free Software Foundation. You may not use, modify
    or distribute this program under any other version of the
    GNU Affero General Public License.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

/**
-- Odin JavaScript --------------------------------------------------------------------------------
	Bell - KC/NLC Subway Station(103000100/600010001), Waiting Room(600010002/600010004)
-- By ---------------------------------------------------------------------------------------------
	Information
-- Version Info -----------------------------------------------------------------------------------
	1.0 - First Version by Information
	    - Thanks for Davidkun and Shogi for the information
---------------------------------------------------------------------------------------------------
**/

var section;
var msg = new Array("New Leaf City of Masteria","Kerning City of Victoria Island","Kerning City","New Leaf City");
var ticket = new Array(4031711,4031713);
var cost = 5000;
var returnMap = new Array(103000100,600010001);

function start() {
	status = -1;
	sw = cm.getEventManager("Subway");
    action(1, 0, 0);
}

function action(mode, type, selection) {
	if(mode == -1 || (mode == 0 && status ==0)) {
		cm.dispose();
	} else {
		status++;
		if(mode == 0) {
			if(section == 2) {
				cm.sendNext("Okay, Please wait~!");
			} else {
				cm.sendOk("You must have some business to take care of here, right?");
			}
			cm.dispose();
			return;
		}
	    if (status == 0) {
			switch(cm.getChar().getMapId()) {
				case 103000100:
					section = 0;
					break;
				case 600010001:
					section = 1;
					break;
				case 600010004:
					section = 2;
					break;
				case 600010002:
					section = 3;
					break;
				default:
					cm.sendNext("Error~");
					cm.dispose();
					break;
			}
			if(section < 2) {
				cm.sendSimple("Hello. Would you like to buy a ticket for the subway?\r\n#L0##b"+msg[section]+"#l");
			} else {
				cm.sendYesNo("Do you want to go back to "+msg[section]+" subway station now?");
			}
		}
		else if(status == 1) {
			if(section < 2) {
				cm.sendYesNo("The ride to "+msg[section]+" takes off every 10 minutes, beginning on the hour, and it'll cost you #b"+cost+" mesos#k. Are you sure you want to purchase #b#t"+ticket[section]+"##k?");
			} else {
				section -= 2;
				cm.warp(returnMap[section]);
				cm.dispose();
			}
		}
		else if(status == 2) {
			if(cm.getMeso() < cost || !cm.canHold(ticket[section])) {
				cm.sendNext("Are you sure you have #b"+cost+" mesos#k? If so, then I urge you to check your etc. inventory, and see if it's full or not.");
			}
			else {
				cm.gainItem(ticket[section],1);
				cm.gainMeso(-cost);
			}
			cm.dispose();
		}
	}
}
