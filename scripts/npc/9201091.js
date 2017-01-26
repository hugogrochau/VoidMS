/* * * * * * * * * * * * * * * \
 *    Personalized Buffs NPC    *
 *  By Hugo of MadStory/VoidMS  *
 *      gugubro1@gmail.com      *
 *         madstory.org         *
 *          voidms.com          *
\ * * * * * * * * * * * * * * */

//importPackage(client);
var status = 0;
var stuff = 0;
var skills = new Array(1001003, 2001003, 2001002, 3001003, 4001003, 1101007, 1101006, 1101005, 1201005, 1201004, 1301007, 1301006, 1301005, 1301004, 2101001, 2301003, 3101002, 3101004, 3201002, 4101003, 4201002, 1111002, 1211006, 1211004, 1211003, 1211005, 1211008, 1211007, 1311008, 2111005, 2311003, 4111001, 4211005, 4211003, 5221000, 1221004, 1221003, 2121004, 2121002, 3121002, 4121006, 1321002, 9001000, 2301004, 3121007, 3221006, 1005);
var buffs = 0;
var toShow;
var skills2;
var error = 0;
var toDispose = 0;
var toAdd;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (type == 12) {
        status = -1;
        if (mode == 1) {
            cm.sendNext("#eYou are now fixed. Enjoy!");
            cm.deleteAllBuffs();
        } else {
            action(1, 0, 0);
        }
    } else {
        if (mode == 1)
            status++;
        else if (mode == 0 && type == 0)
            status = 0;
        else {
            cm.dispose();
            return;
        }
        if (status > 2) {
            cm.dispose();
            return;
        }
        var allBuffs = cm.getBuff();
        var seleskill = "#eChoose a buff to add in:#k\r\n#b";
        var viewbuffs = "#eHere are all your current buffs:#k\r\n";
        var deletebuffs = "#eChoose a buff to delete:#k\r\n#b"
        if (status == 0) {
            stuff = 0;
            buffs = 0;
            toShow = "";
            skills2 = 0;
            error = 0;
            toAdd = 0;
            cm.sendSimple("#eHello #h #! I am the buff customizer npc. I can add or delete particular buffs to your @buffme command. What do you want to do?#k\r\n\r\n#b#L0#I want to add a new buff to @buffme#l\r\n#L1#I want to see my current buffs in @buffme\r\n#L2#I want to delete a buff\r\n#L3#My buffs are glitched. Fix me please!");
        }
        if (status == 1) {
            if (selection == 0) {
                if (buffs != 0) {
                    seleskill += "#k\r\n#rPicked Items:\r\n\r\n";
                    if (buffs.length > 9) {
                        toShow = buffs.split(",");
                        for (i = 0; i < toShow.length; i++) {
                            seleskill += "#s " + toShow[i] + "#  -  #q" + toShow[i] + "#\r\n";
                        }
                    } else {
                        seleskill += "#s " + buffs + "#  -  #q" + buffs + "#\r\n";
                    }
                    seleskill += "#k\r\n#L6969##bI'm done choosing.#l\r\n\r\n---------------------------------------- \r\n\r\n"
                }
                for (i = 0; i < skills.length; i++) {
                    if (allBuffs.search(skills[i]) == -1) {
                        if (buffs != 0) {
                            if (buffs.length > 9) {
                                toShow = buffs.split(",");
                                if (!contains(toShow, skills[i])) {
                                    seleskill += "\r\n#L" + skills[i] + "##s" + skills[i] + "# - #q" + skills[i] + "##l";
                                }
                            } else {
                                if (buffs != skills[i]) {
                                    seleskill += "\r\n#L" + skills[i] + "##s" + skills[i] + "# - #q" + skills[i] + "##l";
                                }
                            }
                        } else {
                            seleskill += "\r\n#L" + skills[i] + "##s" + skills[i] + "# - #q" + skills[i] + "##l";
                        }
                    }
                }
                cm.sendSimple(seleskill);
            } else if (selection == 1) {
                if (cm.getBuff().equals("none")) {
                    cm.sendPrev("#eYou have no buffs to display! Add some.");
                    toDispose = 1;
                } else {
                    skills2 = cm.getBuff().split(",");
                    for (i = 0; i < skills2.length; i++) {
                        viewbuffs += "\r\n#s" + skills2[i] + "# - #q" + skills2[i] + "#";
                    }
                    viewbuffs += "#k\r\n\r\n#r(Total " + skills2.length + ")#k";
                    cm.sendPrev(viewbuffs);
                    toDispose = 1;
                }
            } else if (selection == 2) {
                if (allBuffs.equals('none')) {
                    cm.sendPrev("#eYou have no buffs to delete! Add some.");
                    toDispose = 1;
                } else {
                    skills2 = cm.getBuff().split(",");
                    for (i = 0; i < skills2.length; i++) {
                        deletebuffs += "\r\n#L" + i + "##s" + skills2[i] + "# - #q" + skills2[i] + "##l";
                    }
                    deletebuffs += "#k\r\n\r\n#L1337##rDelete all buffs.#k#l";
                    cm.sendSimple(deletebuffs);
                    stuff = 1;
                }
            } else if (selection == 3) {
                cm.sendAcceptDecline("#eAre you sure you want to do this? This will delete all your buffs.");
            } else {
                cm.dispose();
            }
        } else if (status == 2) {
            if (toDispose == 1) {
                cm.dispose();
                return;
            }
            skills2 = allBuffs.split(",");
            if (stuff == 1) {
                if (selection == 1337) {
                    cm.deleteAllBuffs();
                    cm.sendPrev("#eAll your buffs were deleted successfully!");
                } else {
                    cm.deleteBuff(skills2[selection]);
                    cm.sendPrev("#eYou successfully deleted the following buff:\r\n\r\n#s" + skills2[selection] + "# - #q" +skills2[selection] + "#");
                }
            } else {
                if (selection == 6969) {
                    if (buffs.length > 9) {
                        toAdd =  buffs.split(",");
                        for (i = 0; i < toAdd.length; i++) {
                            if (cm.getBuff().split(",").length < 15) {
                                cm.addBuff(toAdd[i]);
                            } else {
                                cm.sendPrev("#e#rYou can't add any more buffs.");
                                return;
                                cm.dispose();
                            }
                        }
                    } else {
                        if (cm.getBuff().split(",").length < 15) {
                            cm.addBuff(buffs);
                        } else {
                            cm.sendPrev("#e#rYou can't add any more buffs.");
                        }
                    }
                    var goodBye = "#eThe following buffs were added:#g\r\n\r\n";
                    if (buffs.length > 9) {
                        toAdd = buffs.split(",");
                        for (i = 0; i < toAdd.length; i++) {
                            goodBye += "#s " + toAdd[i] + "#  -  #q" + toAdd[i] + "#\r\n";
                        }
                    } else {
                        goodBye = "#e The following buff was added:#g\r\n\r\n #s" + buffs + "#  -  #q" + buffs + "#\r\n"
                    }
                    cm.sendPrev(goodBye);
                 
                } else {
                    if (buffs == 0) {
                        buffs = selection;
                    } else {
                        buffs += "," + selection;
                    }
                    status = 0;
                    action(1, 0, 0);
                }
            }
        }
    }
}



function contains(a, obj) {
    var i = a.length;
    while (i--) {
        if (a[i] == obj) {
            return true;
        }
    }
    return false;
}
