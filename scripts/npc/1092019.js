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
-- JavaScript -----------------
Lord Jonathan - Nautilus' Port
-- Failed By --
Cody/Cyndicate
-- Created By --
Moogra
-- Function --
No specific function, useless text.
-- GMS LIKE --
*/
/*
var status;

function start() {
    status = -1;
    action(1,0,0);
}

function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else {
        status--;
    }

    if (status == 0) {
        cm.sendOk("What are you doing here, if you aren't going to do anything usefull, then #bleave#n! \r\n#L0#Your a fag!#l \r\n#L1#Maybe I could help you with something?#l");
        cm.dispose();
        }
     else if (status == 1) {
            if (selection == 0) {
                cm.sendOk("Step back now because you don't know what your getting yourself into my friend!");
                cm.dispose();

            } else if (selection == 1) {
                cm.sendNext("Do you know who I am young friend? \r\n\r\nI am #rLord Jonathan#k the first pirate ever! I was the one who taught Kyrin everything, and I was the one that build #bThe Nautilus#k. \r\nDon't be fooled my young friend, I am not the old weak bird you think I am..");
                cm.dispose();
            }
    } else if (status == 2) {
                cm.sendAcceptDecline("But you are also right.. I am not that fit anymore. \r\nAnd I'm looking for a young and fierce pirate who can complete the quest, that I once started. \r\n\r\nThe old pirate books tell a tale of a big treasure who was gathered by #bBlackbeard#n, the famous pirate! When I was young, I started searching far and wide for this treasure, I searched many years and one day, I found a #rmap#k! \r\n \r\nThe map was very old but still readable, so I set sail for this glorious treasure! But alas.. The seas didn't want to to find this treasure and a storm made my ship sink. Now the map is lost forever! The only thing I have left is one #i[4001118]#. \r\n\r\nCould you gather the other pieces and complete this map for me?");
                cm.dispose();
    }
}*/

function start() {
	cm.sendOk("#r#h ##k, I'm 80 years old and ready to mingle.");
	cm.dispose();
}