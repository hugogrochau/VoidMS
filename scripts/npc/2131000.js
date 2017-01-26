var status = -1;
var choice = -1;
var choice2 = -1;
var items = [
[[1003164, 10], [1052310, 5], [1102268, 7], [1072479, 3], [1902049, 10], [1912042, 5], [1003001, 10], [1052213, 10], [1072406,5], [1002999, 10], [1052211, 10], [1002998, 10],[1702235,10],[1052210,7],[1072404,3]], // rares
[[4031544, 5], [4031545, 45], [4002000, 1], [4002003, 9],], // currencies
[[2340000, 1], [2049100, 2]]// [4031560, 30], [4031561, 20]] // others
];
var section = ["a rare","a currency", "something"];


function start() {
    if (cm.isFull() || cm.isEtcFull() || cm.isUseFull()) {
        cm.sendOk("#rSorry, you do not have enough of room in your inventory.\r\n\r\nPlease make some room and try again");
        cm.dispose();
    } else {
        cm.sendSimple("#eHello #h #! I am Helena, the vote point exchanger.\r\nYou have #r" + cm.getP().votePoints() + "#k Vote Points.\r\n\r\nWhat would you like today?#n#b\r\n\r\n#L0#Item Sets\r\n#L1#Currencies\r\n#L2#Others");
    }
}

function action(mode, type, selection) {
    if (mode == 1) {
        status++;
        if (status == 0) {
            choice = selection;
            var text = "#ePick " + section[selection] + " to exchange for your vote points#n#b\r\n";
            if (selection == 0) {
                text += "\r\n\r\n#L1001##i1002140# with 999 on all stats and 10 weapon attack for #r25#b vote points\r\n\r\n";
                text += "#L1002##i1082223# 15 slots for #r25#b vote points\r\n\r\n";
                text += "#L1005#";
                for (i=0; i < 6; i++) {
                    text+= "#i" + items[selection][i][0] + "#";
                }
                text += "for #r30#b vote points (hat is alien only)\r\n\r\n";
                text += "#L1006#";
                for (i=6; i < 9; i++) {
                    text+= "#i" + items[selection][i][0] + "#";
                }
                text += "for #r20#b vote points (helmet is alien only)\r\n\r\n";
                text += "#L1007#";
                for (i=9; i < 11; i++) {
                    text+= "#i" + items[selection][i][0] + "#";
                }
                text += "for #r10#b vote points\r\n\r\n";
                text += "#L1008#";
                for (i=11; i < 15; i++) {
                    text+= "#i" + items[selection][i][0] + "#";
                }
                text += "for #r25#b vote points";
            } else if (selection == 2) {
                text += "\r\n\r\n#L1003#Become the Alien Gender which allows you to wear any female or male item at the same time for #r5#b vote points";
                text += "\r\n\r\n#L1004#Gain 5 additional buddy slots for #r3#b vote points";
                text += "\r\n\r\n#L1009#Gain one #i4031560#, which will automatically rebirth you for #r30#b vote points";
                text += "\r\n\r\n#L1010#Gain one #i4031561#, which will automatically exchange mesos for snail stamps for #r20#b vote points";
            }
            for (i=0;i < items[selection].length; i++) {
                text += "\r\n\r\n#L" + i + "##i" + items[selection][i][0] + "# for #r" + items[selection][i][1] + "#b vote points";
            }
            cm.sendSimple(text);
        } else if (status == 1) {
            choice2 = selection;
            var vp1 = "";
            var item1 = "";
            switch (choice2) {
                case 1001:
                    vp1 = 25;
                    item1 = "#i1002140#";
                    break;
                case 1002:
                    vp1 = 25;
                    item1 = "#i1082223#";
                    break;
                case 1003:
                    vp1 = 5;
                    item1 = "alien gender";
                    break;
                case 1004:
                    vp1 = 3;
                    item1 = "5 buddy slots";
                    break;
                case 1005:
                    vp1 = 30;
                    for (i = 0; i < 6; i++)
                        item1 += "#i" + items[0][i][0] + "#";
                    break;
                case 1006:
                    vp1 = 20;
                    for (i = 6; i < 9; i++)
                        item1 += "#i" + items[0][i][0] + "#";
                    break;
                case 1007:
                    vp1 = 10;
                    for (i = 9; i < 11; i++)
                        item1 += "#i" + items[0][i][0] + "#";
                    break;
                case 1008:
                    vp1 = 25;
                    for (i = 11; i < 15; i++)
                        item1 += "#i" + items[0][i][0] + "#";
                    break;
                case 1009:
                    vp1 = 30;
                    item1 = "#i4031560#";
                    break;
                case 1010:
                    vp1 = 20;
                    item1 = "#i4031561#";
                    break;
                default:
                    vp1 = items[choice][choice2][1];
                    item1 = "#i" + items[choice][choice2][0] + "#";
                    break;
            }
            cm.sendYesNo("#eAre you sure you want to exchange #r" + vp1 + "#k vote points for #b" + item1 + "#k?");
        } else if (status == 2) {
            switch (choice2) {
                case 1001:
                    if (cm.getP().votePoints() >= 25) {
                        cm.makeCustomItem(1002140, cm.getP(), 999, 999, 999, 999, 50, 30, 200, 200, 200, 200, 0, 0, 10, 0, 7);
                        cm.reloadChar();
                        cm.getP().changeVotePoints(-25);
                        cm.sendOk("#eEnjoy your special wizet hat!");
                    } else {
                        error(25);
                    }
                    break;
                case 1002:
                    if (cm.getP().votePoints() >= 25) {
                        cm.makeSItem(1082223, cm.getP(), 0, 0, 0, 0, 0, 15);
                        cm.getP().changeVotePoints(-25);
                        cm.reloadChar();
                        cm.sendOk("#eEnjoy your awesome new glove!");
                    } else {
                        error(25);
                    }
                    break;
                case 1003:
                    if (cm.getP().votePoints() >= 5) {
                        cm.getP().setGender(2);
                        cm.reloadChar();
                        cm.getP().changeVotePoints(-5);
                        cm.sendOk("#eYou are now an alien, make sure you dont use @changegender");
                    } else {
                        error(5);
                    }
                    break;
                case 1004:
                    if (cm.getP().votePoints() >= 3) {
                        var cap = cm.getBuddyCapacity();
                        if (cap < 251) {
                            cm.getP().changeVotePoints(-3);
                            cm.updateBuddyCapacity(cap + 5);
                            cm.sendOk("#eYour buddy capacity is now #r" + (cap + 5));
                        } else {
                            cm.sendOk("#eYou already have the limit of 250 buddies");
                        }
                    } else {
                        error(3);
                    }
                    break;
                case 1005:
                    if (cm.getP().votePoints() >= 30) {
                        cm.getP().changeVotePoints(-30);
                        for (i = 0; i < 6; i++)
                            cm.gainItem(items[0][i][0], 1);
                        cm.sendOk("#eEnjoy your set!");
                    } else {
                        error(30);
                    }
                    break;
                case 1006:
                    if (cm.getP().votePoints() >= 20) {
                        cm.getP().changeVotePoints(-20);
                        for (i = 6; i < 9; i++)
                            cm.gainItem(items[0][i][0], 1);
                        cm.sendOk("#eEnjoy your set!");
                    } else {
                        error(20);
                    }
                    break;
                case 1007:
                    if (cm.getP().votePoints() >= 10) {
                        cm.getP().changeVotePoints(-10);
                        for (i = 9; i < 11; i++)
                            cm.gainItem(items[0][i][0], 1);
                        cm.sendOk("#eEnjoy your set!");
                    } else {
                        error(10);
                    }
                    break;
                case 1008:
                    if (cm.getP().votePoints() >= 25) {
                        cm.getP().changeVotePoints(-25);
                        for (i = 11; i < 15; i++)
                            cm.gainItem(items[0][i][0], 1);
                        cm.sendOk("#eEnjoy your set!");
                    } else {
                        error(25);
                    }
                    break;
                case 1009:
                    if (cm.getP().votePoints() >= 30) {
                        cm.getP().changeVotePoints(-30);
                        cm.gainItem(4031560, 1);
                        cm.sendOk("#eEnjoy your #i4031560#!\r\n\r\nUse '@stamp' to toggle it on and off and '@stamp <job initial>' to change the job that it rebirths you in");
                    } else {
                        error(30);
                    }
                    break;
                case 1010:
                    if (cm.getP().votePoints() >= 20) {
                        cm.getP().changeVotePoints(-20);
                        cm.gainItem(4031561, 1);
                        cm.sendOk("#eEnjoy your #i4031561#!\r\n\r\nUse '@stamp' to toggle it on and off");
                    } else {
                        error(30);
                    }
                    break;
                default:
                    var vp = items[choice][choice2][1];
                    var item = items[choice][choice2][0];
                    if (cm.getP().votePoints() >= vp) {
                        cm.gainItem(item, 1);
                        cm.getP().changeVotePoints(-vp)
                        cm.sendOk("#eEnjoy your #i" + item + "#");
                    } else {
                        error(vp);
                    }
                    break;
            }
            cm.dispose();
        }
    } else {
        cm.dispose();
    }
}

function error(vp) {
    cm.sendOk("#e#rYou do not have enough vote points#k\r\n\r\nYou have #r" + cm.getP().votePoints() + "#k vote points out of the #g" + vp + "#k needed");
}