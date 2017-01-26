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

var status;
var minLevel = 0;
var maxLevel = 256;
var minPlayers = 1;
var maxPlayers = 8;

function start() {
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, selection) {
	if (mode == 1)
		status++;
	else {
		cm.dispose();
		return;
	}
	
	if (status == 0) {
			if (cm.getPlayer().getParty() == null) {//NO PARTY
				cm.sendOk("From here on above, this place is full of dangerous objects and monsters, so i can't let you make your way up anymore. If you're intrested in saving us and bring peace back into Ludibrium, however, that's a different story. If you want to defeat a powerful creature residing at the very top, then please gather up your party members. It won't be easy, but ... I think you can do it.");
			} else if (!cm.isLeader()) {//NOT LEADER
				cm.sendOk("If you want to try the quest, please tell the #bleader of your party#k to talk to me.");
			} else {
				var party = cm.getParty().getMembers();
				var inMap = cm.partyMembersInMap();
				var levelValid = 0;
				for (var i = 0; i < party.size(); i++) {
					if (party.get(i).getLevel() >= minLevel && party.get(i).getLevel() <= maxLevel)
						levelValid++;
				}
				if (inMap < minPlayers || inMap > maxPlayers || levelValid != inMap) {
					cm.sendOk("Your party does not consist of "+minPlayers+" members, therefore making you ineligible this party quest. Please adjust your party members to make it 6.");
				} else {
					var em = cm.getEventManager("LudiPQ");
					if (em == null) {
						cm.sendOk("This PQ is not currently available.");
					} else if (em.getProperty("LudiPQOpen").equals("true")) {
						// Begin the PQ.
						em.startInstance(cm.getParty(), cm.getPlayer().getMap());
						// Remove Passes and Coupons
						party = cm.getChar().getEventInstance().getPlayers();
						cm.removeFromParty(4001022, party);
						em.setProperty("LudiPQOpen" , "false");
					} else {
						cm.sendNext("Another party is inside taking on the party quest. Please try again after that group vacates the room.");
					}
				}
			}
		cm.dispose();
	}
}