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

/* 
 * @Author Lerk
 * 9040011
 * Bulletin Board, Victoria Road: Excavation Site<Camp> (101030104) AND Sharenian: Excavation Site (990000000)
 * 
 * Start of Guild Quest
 */
var name;
var update1;
var update2;
var update3;
var update4;
var update5;
var update6;

update1 = "Full Item Wipe."
update2 = "New Vote Exchanger Prizes; check out Helena in Henesys"
update3 = "Check out our new Forums; www.voiddev.com/forum"
update4 = "ALL Cash Shop Items are now in @shop for 1 meso each!"
update5 = "Also be sure to check out our new website; www.voiddev.com"
update6 = "Constant removal of bugs and glitches."

function start() {
	cm.sendOk("#e#bVOIDMS UPDATES \r\n---------------------------- #k\r\n\r\n- "+update1+"\r\n- "+update2+"  \r\n- "+update3+"  \r\n- "+update4+" \r\n- "+update5+" \r\n- "+update6+" \r\n\r\nMore coming soon!#n");
        cm.dispose();
}

function action(mode, type, selection) {
        
}