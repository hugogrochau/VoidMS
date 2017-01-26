/*
 Made by XiuzSu
 Credits to watzmename
 Edited by SirPali/Hugo
*/
importPackage(Packages.server);

var status;
var choice;
var mvs = 0;

function start() {
    status = -1;
    action(1, 0, 0);
} 

function action(mode, type, selection) {
    if (mode == 1)
        status++;
    else {
        cm.dispose();
        return;
    }
    if (status == 0)
        cm.sendNext("Hello there #b#h ##k, I'm Frederick the store banker!\r\n\r\nWelcome to the Free Market, the place where money is made. In this room you can set up your shop to sell your items.\r\n\r\nWe're currently using this system: \r\n#e#r- Under 1000 mesos, 1 meso is worth 1 #bSnail Stamp#k#r.\r\n- At or above 1000 mesos, 1 meso is just 1 meso!#k#n \r\n\r\nIf you sold some items but you didn't get your #bSnail Stamps#k,\r\ndon't worry! Talk to me and click on #rnext#k to withdraw your item and/or money.\r\n\r\nHappy selling or shopping!");
    else if (status == 1) {
        if (cm.hasTemp()) {
            if (cm.getHiredMerchantItems(true)) {
                cm.sendOk("These items where saved from the last server shutdown!");
            } else {
                cm.sendOk("Please make some space to receive all your items.");
            }
            cm.dispose();
        } else {
            cm.sendSimple("Would you like to withdraw anything?\r\n\r\n#b#L0#Mesos or Stamps\r\n#L1#Items");
        }
    } else if (status == 2) {
        if (selection == 0) {
            var meso = cm.getHiredMerchantMesos();
            var stamp = cm.getHiredMerchantStamps();
            if (meso > 0 && stamp < 1) {
                cm.sendYesNo("You have made " + meso + " mesos in your store so far. You have not made any Stamps. \r\nWould you like to withdraw them?");
                mvs = 1;
            } else if (meso < 1 && stamp < 1) {
                cm.sendOk("You have not made any mesos nor stamps.");
                cm.dispose();
            } else if (meso < 1 && stamp > 0) {
                cm.sendYesNo("You have not made any mesos but you did make " + stamp + " Snail Stamps! \r\n Would you like to withdraw them?");
                mvs = 2;
            } else if(meso > 0 && stamp > 0){
                cm.sendYesNo("You have made " + meso + " mesos and " + stamp + " Snail Stamps so far! Would you like to withdraw them?");
                mvs = 3;
            } else {
                cm.sendOk("Something went horribly wrong.");
            }
        } else {
            if (cm.getHiredMerchantItems(false)) {
                cm.sendOk("Thank you for using my services, your item has been received.");
            } else {
                cm.sendOk("You either have no items to receive or not enough space in your inventory.");
            }
            cm.dispose();
        }
    } else if (status == 3) {
        if (mvs == 1) {
            cm.gainMeso(cm.getHiredMerchantMesos());
            cm.setHiredMerchantMesos(0);
            cm.sendOk("Thank you for using my services, your mesos have been given.");
        } else if (mvs == 2) {
            cm.gainItem(4002000,cm.getHiredMerchantStamps());
            cm.setHiredMerchantStamps(0);
            cm.sendOk("Thank you for using my services, your stamps have been given.");
        } else if (mvs == 3){
            cm.gainItem(4002000,cm.getHiredMerchantStamps());
            cm.setHiredMerchantStamps(0);
            cm.gainMeso(cm.getHiredMerchantMesos());
            cm.setHiredMerchantMesos(0);
            cm.sendOk("Thank you for using my services, your stamps and mesos have been given.");
        }
        cm.dispose();
    }
}
