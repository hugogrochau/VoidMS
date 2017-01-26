/*function start() {
    cm.sendSimple("Hello, #h #! Welcome to the #bJQ Championship Registry \r\n#r(Registration closes Friday the 26th of August)#k\r\n What would you like to do today?#b\r\n\r\n#L0#I want to join the JQ Championship for one #i4002000#\r\n#L1#I want to leave the JQ Championship\r\n#L2#I want to see a list of participants\r\n#L3#I want to see what prizes I can get for winning");
}
 
function action(mode, type, selection) {
    if (mode == 1) {
        if (selection == 0) {
            if (cm.getP().inJQChampionship())
                cm.sendOk("#rYou are already in the JQ Championship!");
            else {
                if (cm.itemQuantity(4002000) > 0) {
                    cm.loseItem(4002000, 1);
                    cm.getP().joinJQChampionship();
                    cm.sendOk("#gYou have been entered into the JQ Championship. Good luck!");
                } else {
                    cm.sendOk("#rYou do not have a #i4002000#");
                }
            }
        } else if (selection == 1) {
            if (cm.getP().inJQChampionship()) {
                cm.getP().leaveJQChampionship();
                cm.sendOk("#bYou have left the JQ Championship.");
            } else
                cm.sendOk("#rYou are not in the JQ Championship.");
        } else if (selection == 2) {
            cm.sendOk("#eHere are all the current registered participants:#n#b\r\n" + cm.getP().getJQChampionshipParticipants());
        } else if (selection == 3) {
            cm.sendOk("#e1st Place:#n\r\n 2 rares of choice, 3 blue wish tickets, 200 jq points, 20 snail stamps and a forum medal\r\n\r\n#e2nd Place:#n\r\n 1 rare of choice, 2 blue wish tickets, 100 jq points, 10 snail stamps and a forum medal\r\n\r\n#e3rd Place:#n\r\n1 blue wish ticket, 50 jq points, 5 snail stamps and a forum medal");
        }
    }
cm.dispose();
}
function start() {
    if (cm.getP().inJQChampionship()) {
        cm.sendSimple("#eHello #h #, thank you for registering in the JQ Championship! Please choose your most favorable timezone for the sake of group-choosing:#n#b\r\n\r\n"
            + "#L0#GMT -12\r\n"
            + "#L1#GMT -11\r\n"
            + "#L2#GMT -10\r\n"
            + "#L3#GMT -9\r\n"
            + "#L4#GMT -8 - Los Angeles, \r\n"
            + "#L5#GMT -7\r\n"
            + "#L6#GMT -6 - Mexico City, Saskatchewan\r\n"
            + "#L7#GMT -5 - New York\r\n"
            + "#L8#GMT -4 - Caracas, Newfoundland\r\n"
            + "#L9#GMT -3 - Brasilia, Buenos Aires\r\n"
            + "#L10#GMT -2\r\n"
            + "#L11#GMT -1 - Azores\r\n"
            + "#L12#GMT +0 - London, Dublin, Edinburgh, Lisbon, Reykjavik, Casablanca\r\n"
            + "#L13#GMT +1 - Paris, Berlin, Amsterdam, Brussels, Vienna, Madrid, Rome, Bern, Stockholm, Oslo\r\n"
            + "#L14#GMT +2 - Athens, Helsinki, Istanbul, Jerusalem, Harare\r\n"
            + "#L15#GMT +3 - Moscow, Tehran\r\n"
            + "#L16#GMT +4 - Abu Dhabi\r\n"
            + "#L17#GMT +5 - New Delhi\r\n"
            + "#L18#GMT +6 - Cocos Islands\r\n"
            + "#L19#GMT +7 \r\n"
            + "#L20#GMT +8 - Perth, Singapore\r\n"
            + "#L21#GMT +9 - Darwin, Melbourne\r\n"
            + "#L22#GMT +10\r\n"
            + "#L23#GMT +11\r\n"
            + "#L24#GMT +12 - New Zealand\r\n");
    } else
        cm.sendOk("#rYou are not registered into the JQ championship");
}

function action(m,t,s) {
    if (m == 1) {
        cm.sendOk("#gSuccess! Your timezone is registered as: #bGMT" + (s - 12));
        cm.getP().setJQChampionshipTZ(s);
    }
    cm.dispose();
}*/
function start() {
  //  cm.sendSimple(cm.getP().getJQChampionshipParticipants());
    cm.dispose();
}