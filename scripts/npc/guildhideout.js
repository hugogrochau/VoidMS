var status = -1;

function start() {
    if (cm.getP().getGuildRank() == 5) {
        if (cm.getGuild().getHideout() == null || cm.getGuild().getHideout().getTimeLeft() < 0) {    
            cm.sendYesNo("#eHello #h #, so do you want to get a hideout for your guild?");
        } else {
            cm.sendOk("You already have an active hideout");
            cm.dispose();
        }
    } else {
        cm.sendOk("#eYou are not the leader of your guild, please tell your leader to talk to me.");
        cm.dispose();    
    }
}

function action(mode, type, selection) {
    if (mode == 1) {
        status++
        if (status == 0) {
          
    }
    } else {
        cm.dispose();
    }
}
