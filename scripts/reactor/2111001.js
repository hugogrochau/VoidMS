/*
	This file is part of the OdinMS Maple Story Server
    Copyright (C) 2008 Patrick Huy <patrick.huy@frz.cc> 
					   Matthias Butz <matze@odinms.de>
					   Jan Christian Meyer <vimes@odinms.de>

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation version 3 as published by
    the Free Software Foundation. You may not use, modify or distribute
    this program under any other version of the GNU Affero General Public
    License.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

/* @Author Lerk
 * 
 * 2111001.js: Zakum Altar - Summons Zakum.
 * 
 * Note that Zakum is currently spawned body + 8 arms at once, with no special handling for the body
 * before the arms are defeated. Use caution.
*/

function act() {
	if (rm.getReactor().getMap().getId() == 910000000) {
		if (rm.getPlayer().getClan != -1) {
			
		}
	} else {
		rm.closeDoor(211042300);
		rm.closePortal(211042300, "sp");
		rm.changeMusic("Bgm06/FinalFight");
		rm.spawnFakeMonster(8800000);
		for (var i = 8800003; i <= 8800010; i++) {
			rm.spawnMonster(i);
		}
		rm.createMapMonitor(211042300, "sp");
		rm.mapMessage("Zakum is summoned by the force of eye of fire.");
	}
}
