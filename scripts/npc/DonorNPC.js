var status = -1;
var scrolls = [2044503,2044703,2044603,2043303,2040807,2040806,2043103,2043203,2043003,2040506,
2044403,2040903,2040709,2040710,2040711,2044303,2043803,2040403,2044103,2044203,2044003,2043703];
var items = [[2340000,10,1],[4031544,1,1],[4002000,5,1],[4031544,7,5],[4002000,30,5],[2041200,25,15]]; // add normal item prizes here
var noDispose = 0;

function start() {
    var text = "#eHello #h #, you currently have #r" + cm.getPlayer().getDP() + "#k donation points. Which donation reward do you want?\r\n"; // header text
    for (i=0;i<items.length;i++)  // looping through normal items
        text += "\r\n#L" + i + "#" + (items[i][1] > 1 ? items[i][1] : "") + " #i" + items[i][0] + "# for #r" + items[i][2] + "#k donation points";
    // start of non item prizes
    text += "\r\n#L100#GM Job with Super Dragon Roar for #r10#k Donation Points";
    text += "\r\n#L101#r10 Donation Points #bfor an extra 100 slots in buddy list";
    text += "\r\n#L102#r15 Donation Points #bfor a gender change to alien where you can wear both gender clothes at the same time, and for future rares";
    text += "\r\n#L103#r25 Donation Points #bfor Max out Stat Points(120,000 Stat Points)";
    text += "\r\n#L104#r40 Donation Points #bfor a #i1112807# SRB 3 ring";
    text += "\r\n#L105#r50 Donation Points #bfor a #i1082223# Stormcaster Glove with 50 Slots Clean";
    text += "\r\n#L106#r5 Donation Points #bfor 1 #i2040807# GM Scroll";
    // end of non item prizes
    cm.sendSimple(text);
}

function action(mode, type, selection) {
    if (mode == 1) {
        // start of item rewards
        if (selection < 100) {  //white scrols
            var r = items[selection];
            if (cm.getPlayer.getDP() >= r[2]) {
                cm.giveDP(-r[2]);
                cm.gainItem(r[0], r[1]);
            } else {
                cm.sendOk("#rYou only have " + cm.getPlayer().getDP() + " donation points out of the " + r[2] + " needed for " + (r[1] > 1 ? r[1] : "") + " #i" + r[0] + "#");
            }
        } else if (selection < 200) {
            var dp = cm.getPlayer.getDP();
            // start of non item rewards
            if (selection == 100) {
                if (dp >= 10 && !cm.isEtcFull() && !cm.isUseFull()){//10 points
                    cm.getPlayer().giveDP(-10);
                    cm.getPlayer().setJob(900);
                    cm.sendSimple("Your job has changed into GM Job. Save the skill Super Dragon Roar onto your keyboard. When you change your job, you will not be able to change back\r\n\r\n#L0#Go Back#l\r\n#L2#Leave#l");
                } else {
                    cm.sendOk("You do not have enough points");
                }
            } else if (selection == 101) {
                if (dp >= 10 && !cm.isEtcFull()){//10 points
                    cm.getPlayer().giveDP(-10);
                    cm.getPlayer().setBuddyCapacity(100);
                    cm.sendSimple("Now you can have more friends, enjoy it!\r\n\r\n#L0#Go Back#l\r\n#L2#Leave#l");//maximum how many buddies?
                } else {
                    cm.sendOk("You do not have enough points");
                }
            } else if (selection == 102) {
                if (dp >= 15 && !cm.isEtcFull()){//15 points
                    cm.getPlayer().giveDP(-15);
                    cm.getP().setGender(2);
                    cm.reloadChar();
                } else {
                    cm.sendOk("You do not have enough donor Points.");
                }
            } else if (selection == 103) {
                if (dp >= 25 && !cm.isEtcFull()){//25 points
                    cm.getPlayer().giveDP(-25);
                    cm.gainAp(120000);
                    cm.sendSimple("You now have #r120,000#k Ability Points. Don't freak out when you see a negative ability point, you still have it.\r\n\r\n#L0#Go Back#l\r\n#L2#Leave#l");
                } else {
                    cm.sendOk("You do not have enough donor points");
                }
            } else if (selection == 104) {
                if (dp  >= 40 && !cm.isEtcFull()){//40 points
                    cm.openNpc(1061007);
                    noDispose = 1;
                } else {
                    cm.sendOk("You do not have enough donor points or your inventory is full");
                }
            } else if (selection == 105) {
                if (dp  >= 50 && !cm.isEtcFull()){//50 points
                    cm.getPlayer().giveDP(-50);
                    cm.makeSItem(1082223, cm.getP(), 0, 0, 0, 0, 0, 50);
                    cm.reloadChar();
                    cm.sendSimple("Enjoy your #i1082223# Stormcaster Gloves with 50 Slots\r\n\r\n#L0#Go Back#l\r\n#L2#Leave#l");
                } else {
                    cm.sendOk("You do not have enough donor points or your inventory is full");
                }
            } else if (selection == 106){
                var scrollText = "These scrolls give the same stats as a 10% scroll, but work 100% of the time.\r\nChoose a scroll:\r\n\r\n";
                for(p=0; p < scrolls.length; p++)
                    scrollText +="#L"+p+2000+"##t"+scrolls[p]+"#\r\n";
                cm.sendSimple(scrollText);
                noDispose = 1;
            }
        } else {
            var s = selection - 200;
            if (dp >= 5 && !cm.isUseFull()) {
                cm.getPlayer().giveDP(-5);
                cm.gainItem(scrolls[s], 1);
            } else {
                cm.sendOk("You do not have enough donor points or your inventory is full");
            }
        }
    // end of non item rewards
    }
    if (noDispose == 0)
        cm.dispose();
    else
        noDispose = 0;
}

