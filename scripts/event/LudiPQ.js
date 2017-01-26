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
 * @Author Raz
 * 
 * Ludi Maze PQ
 */
 
importPackage(Packages.world);

var exitMap;

function init() {
	exitMap = em.getChannelServer().getMapFactory().getMap(922010000);
	em.setProperty("LudiPQOpen", "true");
}

function monsterValue(eim, mobId) {
	return 1;
}

function setup() {
	var eim = em.newInstance("LudiPQ");
	var pqTime = 60 * 60000; //60 Minutes
	eim.startEventTimer(pqTime);
	var firstPortal = eim.getMapInstance(922010100).getPortal("next00");
    firstPortal.setScriptName("lpq1");
    em.schedule("timeOut", eim, pqTime);

	return eim;
}

function playerEntry(eim, player) {
	var map = eim.getMapInstance(922010100);
	player.changeMap(map, map.getPortal(0));
}

function playerDead(eim, player) {

}

function playerDisconnected(eim, player) {
    var party = eim.getPlayers();
    if (eim.isLeader(player)) {
        var party = eim.getPlayers();
        for (var i = 0; i < party.size(); i++) {
            if (party.get(i).equals(player)) {
                removePlayer(eim, player);
            } else {
                playerExit(eim, party.get(i));
            }
        }
        eim.dispose();
    } else { // non leader.
        removePlayer(eim, player);
    }
}

function leftParty(eim, player) {
	playerExit(eim, player);
}

function disbandParty(eim) {
	//boot whole party and end
	var party = eim.getPlayers();
	for (var i = 0; i < party.size(); i++) {
		playerExit(eim, party.get(i));
	}
	eim.dispose();
}

function playerExit(eim, player) {
	eim.unregisterPlayer(player);
	player.changeMap(exitMap, exitMap.getPortal(0));
}


function playerFinish(eim, player) {
	var map = eim.getMapInstance(922011100);
	player.changeMap(map, map.getPortal(0));
}

//for offline players
function removePlayer(eim, player) {
	eim.unregisterPlayer(player);
	player.getMap().removePlayer(player);
	player.setMap(exitMap);
}

function clearPQ(eim) {
	var party = eim.getPlayers();
	for (var i = 0; i < party.size(); i++) {
		   playerFinish(eim, party.get(i));
	}
	eim.dispose();
}

function allMonstersDead(eim) {
        //do nothing; LPQ has nothing to do with monster killing
}

function cancelSchedule() {
}

function timeOut(eim) {
	if (eim != null) {
		if (eim.getPlayerCount() > 0) {
			var pIter = eim.getPlayers().iterator();
			while (pIter.hasNext()) {
				playerExit(eim, pIter.next());
			}
		}
		eim.dispose();
	}
}

 function playerRevive(eim, player) {
     if (eim.isLeader(player)) { // Check for party leader
        var party = eim.getPlayers();
        for (var i = 0; i < party.size(); i++) {
            playerExit(eim, party.get(i));
        }
        eim.dispose();
    } else
        playerExit(eim, player);
}

function startBonus(eim) {
	if (eim != null) {
		if (eim.getPlayerCount() > 0) {
			var pIter = eim.getPlayers().iterator();
			while (pIter.hasNext()) {
				if (pIter.next().getMap().getId() == 922011000) {
					playerFinish(eim, pIter.next());
				}
			}
		}
	}
}

function dispose() {
	em.schedule("LudiPQOpen1", 10000); // 10 seconds. gMS is most likely 10 seconds.
}

function LudiPQOpen1() {
	em.setProperty("LudiPQOpen", "true");
}