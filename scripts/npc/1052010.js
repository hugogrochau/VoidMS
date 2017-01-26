var maps = new Array(100000000, 103000900);

function start() {
    status = 1;
    cm.sendNext("Congratulations on finishing the B3 Jump Quest! Here, you deserve 4 #bKarma levels!");
}

function action(mode, type, selection) {
    if (status == 1) {
        status = 2;
        var warpText = "Do you to do the Jump Quest again or get our of here?";
        for (var i = 0; i < maps.length; i++)
            warpText += "\r\n#L" + i + "##m" + maps[i] + "##l";
        cm.sendSimple(warpText);
    } else if (status == 2) {
        cm.warp(maps[selection], 0);
	cm.gainKarma(4);
        cm.dispose();
    }
}	


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


 Shumi JQ Chest #1


importPackage(Packages.client);

var status = 0;

function start() {
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, selection) {
	var prizes = Array(1050013, 1050034, 1050040, 1050116, 1050119, 1051029, 1051035, 1051049, 1051073, 1051116, 1052007, 1052027, 1052032, 1052050, 1702021, 1702050);
	var chances = Array(10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 6, 6);
	var totalodds = 0;
	var choice = 0;
	for (var i = 0; i < chances.length; i++) {
		var itemGender = (Math.floor(prizes[i]/1000)%10);
			if ((cm.getChar().getGender() != itemGender) && (itemGender != 2)) {
			chances[i] = 0;
			}
	}
	for (var i = 0; i < chances.length; i++) {
	totalodds += chances[i];
	}
	var randomPick = Math.floor(Math.random()*totalodds)+1;
	for (var i = 0; i < chances.length; i++) {
	randomPick -= chances[i];
		if (randomPick <= 0) {
		choice = i;
		randomPick = totalodds + 100;
			}
		}

	if (cm.getQuestStatus(2057).equals(MapleQuestStatus.Status.STARTED)) {
			cm.gainItem(4031041,1);
	}
			cm.gainItem(prizes[choice],1);
			cm.warp(103000100, 0);
			cm.dispose();
}
*/