/* 
 * This file is part of the OdinMS Maple Story Server
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

function enter(pi) {
	if (pi.getPlayer().getClient().isGuest()) {
		pi.showInstruction("Welcome to the server,\r\nCurrently logged in as a guest ? create a new account if you like the server.", 350, 5);
	} else {
	var messages = new Array("Click on sera to get started!",
			 "You can use @help to check the available commands!", 
			 "Like the server ? Why not get your friends to play",
			 
			 "Have an idea or feature you'd like to see in the server ? Suggest it to a GM.");
	pi.showInstruction("NEVER GONNA GIVE YOU UP", 350, 5);
	}
	return true;
}