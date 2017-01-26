/*
    This file is part of the OdinMS Maple Story Server
    Copyright (C) 2008 Patrick Huy <patrick.huy@frz.cc> 
                       Matthias Butz <matze@odinms.de>
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
	NLC ticket gate - NLC Subway Station(600010001)
-- By ---------------------------------------------------------------------------------------------
	Information
-- Version Info -----------------------------------------------------------------------------------
	1.0 - First Version by Information
	    - Thanks for Davidkun and Shogi for the information
---------------------------------------------------------------------------------------------------
**/

importPackage(Packages.client);
var sw;

function start() {
	status = -1;
	sw = cm.getEventManager("Subway");
	action(1, 0, 0);
}

function action(mode, type, selection) {
	if(mode == -1) {
		cm.dispose();
		return;
	} else {
		status++;
		if(mode == 0) {
			cm.sendNext("You must have some business to take care of here, right?");
			cm.dispose();
			return;
		}
		if (status == 0) {
			if(sw == null) {
				cm.sendNext("Event error, please restart your server for solution");
				cm.dispose();
			} else if(sw.getProperty("entry").equals("true")) {
				cm.sendYesNo("It looks like there's plenty of room for this ride. Please have your ticket ready so I can let you in, The ride will be long, but you'll get to your destination just fine. What do you think? Do you want to get on this ride?");
			} else if(sw.getProperty("entry").equals("false") && sw.getProperty("docked").equals("true")) {
				cm.sendNext("The subway is getting ready for takeoff. I'm sorry, but you'll have to get on the next ride. The ride schedule is available through the usher at the ticketing booth.");
				cm.dispose();
			} else {
				cm.sendNext("We will begin boarding 1 minutes before the takeoff. Please be patient and wait for a few minutes. Be aware that the subway will take off on time, and we stop receiving tickets 1 minute before that, so please make sure to be here on time.");
				cm.dispose();
			}
		} else if(status == 1) {
			if(!cm.haveItem(4031713)) {
				cm.sendNext("Oh no ... I don't think you have the ticket with you. I can't let you in without it. Please buy the ticket at the ticketing booth.");
			} else {
				cm.gainItem(4031713,-1);
				cm.warp(600010002);
			}
			cm.dispose();
		}
	}
}
