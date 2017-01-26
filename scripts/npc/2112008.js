function start() {
    cm.sendSimple("Hello, #h #! Welcome to the #bJQ Championship Registry#k. What would you like to do today?#b\r\n\r\n#L0#I want to join the JQ Championship for one #i4002000#\r\n#L1#I want to leave the JQ Championship\r\n#L2#I want to see a list of participants\r\n#L3#I want to see what prizes I can get for winning");
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