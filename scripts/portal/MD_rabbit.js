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

/*
MiniDungeon - Rabbit
*/ 

function enter(pi) {
	var dungeonid = 221023401;
	if (pi.getMapId() == 221023400) {
	    for(var i = 0; i < 30; i++) { // counts dungeons
		if (pi.getPlayerCount(dungeonid + i) == 0) {
		    pi.warp(dungeonid + i, 0);
		    return true;
		}
	    }
	    pi.playerMessage("All of the Mini-Dungeons are in use right now, please try again later.");
	    return false;
	} else {
	pi.warp(221023400, "MD00");
        return true;
	}
}
