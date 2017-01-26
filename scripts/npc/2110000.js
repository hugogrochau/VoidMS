var hideouts = [600010005, 220000305, 108010100, 923000100, 260000204, 260010201, 110030001, 240050200, 921100301, 923010100, 924000002, 120000201, 120000202, 105040402, 200090010, 924000100, 251010404, 600010003]; //222020000
var npcs = [9201097,1032003,2132001,2111004,2042002,1040001,9900000,9900001,2040030,9010002,1061008,2131000,9201073,9300006,9300010,9000017,9120020,1052017,9110100,9300005,9300013,9300014];//0022000

var status = -1;
var d = 1; // daily price
var s = (d * 3) + 2; // initial price
var o = -1;
var map = -1;
var text;

function start() {
    if (cm.getGuild() == null)
        cm.sendSimple("#e#rYou need to have a guild to start a guild hideout.\r\n\r\n#n#b#L4#Tell me more about guild hideouts#l");
    else if (!cm.getP().getMap().isHideout()) {
        if (!cm.hideoutExpired()) {
            cm.sendSimple("#eYou have #r" + Math.floor(cm.getHideoutTimeLeft() / 24) + " days#k and #r" + (cm.getHideoutTimeLeft() % 24) + " hours#k left until your time expires. What would you like to do?#n#b\r\n\r\n#L0#Go to the hideout\r\n#L1#Remove the hideout\r\n#L2#Read more information about guild hideouts");
        } else {
            cm.sendSimple("#eHello #h #, I am the #rguild hideout real estate agent#k. What would you like to do?#k#b\r\n\r\n#L3#Rent a guild hideout\r\n#L2#Read more information about guild hideouts");
        }
    } else if (!cm.hideoutExpired()) {
        cm.sendSimple("#eI hope you are enjoying your hideout #h #. You have #r" + Math.floor(cm.getHideoutTimeLeft() / 24) + " days#k and #r" + (cm.getHideoutTimeLeft() % 24) + " hours#k left until your time expires. What would you like to do?#n#b\r\n\r\n#L2#Find out what I can do with my hideout\r\n#L4#Pay my rent in advance\r\n#L5#Invite a NPC to my hideout\r\n#L6#Kick an NPC out of my hideout");
    } else {
        cm.getP().changeMap(100000000);
    }

}
function action(mode,type,selection) {
    if (mode > 0) {
        status++;
        if (status == 0) {
            o = selection;
            if (selection == 0) {
                cm.warp(cm.getHideout(),0);
                cm.dispose();
            } else if (selection == 1) {
                cm.sendYesNo("#eAre you sure you want to remove your hideout? #rThis can't be undone");
            } else if (selection == 2) {
                cm.sendOk("#e#rGuild Hideout Information:#k\r\n\r\n\r\n-A guild hideout may only be chosen by the Guild Leader.\r\n\r\n-It costs 5 #i4002000# to rent a hideout for 3 days\r\n\r\n-It costs one #i4002000# to extend the rent time by a day\r\n\r\n-Both the Guild Master and the JR Masters may extend the rent time\r\n\r\n-Once your guild has rented a map, no other guild may take it\r\n\r\n-Only members of your guild may enter your hideout\r\n\r\n-Once your rent time runs out, the hideout map is free for another guild to take\r\n\r\n-You can invite NPCs to stay at your hideout for one #i4002000#\r\n\r\n-If your time runs out, the NPCs will leave your hideout");
                cm.dispose();
            } else  if (selection == 3) {
                if (cm.getP().getGuildRank() == 1) {
                    text = "#ePlease choose a map to rent:#k#b\r\n";
                    for(var i=0;i<hideouts.length;i++) {
                        if (cm.getP().getGuild().hideoutMapAvailable(hideouts[i])) {
                            text += "\r\n#L" + i + "##m" + hideouts[i] + "#";
                        }
                    }
                    cm.sendSimple(text);
                } else {
                    cm.sendOk("#e#rOnly the Guild Master can rent hideouts");
                    cm.dispose();
                }
            } else if (selection == 4) {
                cm.sendYesNo("#eAre you sure you want to extend your rent period? #rThis will cost one #i4002000#");
            } else if (selection == 5) {
                text = "#eWhich NPC would you like to invite for one #i4002000#?#n#b";
                for (i = 0; i < npcs.length; i++) {
                    text += "\r\n\r\n#L" + npcs[i] + "##fNpc/" + npcs[i] + "/stand/0#" + "\r\n#p" + npcs[i] + "#";
                }
                cm.sendSimple(text);
            } else if (selection == 6) {
                cm.sendSimple("#eWhat NPC would you like to kick out of your hideout?#n#b" + cm.listHideoutNpcs());
            }
        } else if (status == 1) {
            if (o == 1) {
                if (cm.getPlayer().getGuildRank() == 1) {
                    cm.removeHideout();
                    cm.sendOk("#e#gHideout removed!");
                } else {
                    cm.sendOk("#e#rOnly the Guild Master can remove the hideout");
                }
                cm.dispose();
            } else if (o == 3) {
                map = hideouts[selection];
                cm.sendYesNo("#eAre you sure you want to rent a hideout for your guild?\r\n #rThis will cost five #i4002000# for three days");
            } else if (o == 4) {
                if (cm.getP().getGuildRank() < 3) {
                    if (cm.itemQuantity(4002000) >= d) {         
                        if (cm.extendHideoutTime()) {
                            cm.sendOk("#e#gTime extended!");
                            cm.gainItem(4002000, -d);
                        } else {
                            cm.sendOk("#e#rYou can only rent seven days in advance!");
                        }
                    } else {
                        cm.sendOk("#e#rYou do not have enough #i4002000#");
                    }
                } else {
                    cm.sendOk("#e#rOnly Jr. Masters or the Guild Master can extend the guild hideout time");
                }
                cm.dispose();
            } else if (o == 5) {
                if (cm.getPlayer().getGuildRank() < 3) {
                    if (cm.itemQuantity(4002000) > 0) {
                        if(cm.giveHideoutNpc(selection)) {
                            cm.gainItem(4002000, -1);
                            cm.sendOk("#fNpc/" + selection + "/stand/0#\r\n#p" + selection + "#\r\n\r\nWill appear wherever you drop the #i4002001#\r\n\r\n#rDo NOT drop the #i4002001# anywhere other than in your hideout and do not change channels before you drop it");
                        } else {
                            cm.sendOk("#e#rYou already have this NPC in your hideout");
                        }
                    } else {
                        cm.sendOk("#e#rYou do not have a #i4002000#");
                    }
                } else {
                    cm.sendOk("#e#rOnly Jr. Masters or the Guild Master can invite NPCs");
                }
                cm.dispose();
            } else if (o == 6) {
                if (cm.getPlayer().getGuildRank() < 3) {
                    cm.removeHideoutNpc(selection);
                    cm.sendOk("#fNpc/" + selection + "/stand/0#\r\n#p" + selection + "#\r\n\r\nwas removed");
                    cm.dispose();
                } else {
                    cm.sendOk("#e#rOnly Jr. Masters or the Guild Master can kick NPCs out");
                }
            }
        } else if (status == 2) {
            if (cm.itemQuantity(4002000) >= s) {
                if (!cm.createHideout(map)) {
                    cm.sendOk("#e#rMap is being used");
                } else {
                    cm.gainItem(4002000, -s);
                    cm.sendOk("#e#gHideout created!");
                }
            } else {
                cm.sendOk("#e#rYou don't have enough #i4002000#");
            }
            cm.dispose();
        }
    } else {
        cm.dispose();
    }
}