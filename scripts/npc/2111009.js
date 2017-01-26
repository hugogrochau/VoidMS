function start() {
    status = -1;
    action(1, 0, 0);
}
 
function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (status >= 0 && mode == 0) {
            cm.dispose();
            return;
        }
        if (mode == 1)
            status++;
        else
            status--;
        if (status == 0) {
                        cm.sendSimple("Hello! I can trade your #eBox of Presents#n for #eYellow Wish Tickets#n. Each Yellow Wish Ticket costs one Box of Presents.\r\nHow many Boxes of Presents would you like to trade?\r\n\r\n#b#L0#I want to trade 1 #i4031137##l\r\n#L1#I want to trade 10 #i4031137##l\r\n#L2#I want to trade all my #i4031137##l");
                } else if (selection == 0) {                   
                if (cm.itemQuantity(4031137) < 1) {
                        cm.sendOk("You do not have 1 #i4031137#!");
                        cm.dispose();
                } else {
                        cm.gainItem(4031137, -1);
                        cm.gainItem(4031543, 1);
                        cm.sendOk("Here is your #i4031543#!");
                        cm.dispose();
                }
                } else if (selection == 1) {
                if (cm.itemQuantity(4031137) < 10) {
                        cm.sendOk("You do not have 10 #i4031137#!");
                        cm.dispose();
                } else {
                        cm.gainItem(4031137, -10);
                        cm.gainItem(4031543, 10);
                        cm.sendOk("Here are your #i4031543#!");
                        cm.dispose();
                }
                } else if (selection == 2) {
                        var bop = cm.itemQuantity(4031137);
                        if (bop > 0) {
                                cm.gainItem(4031543, bop);
                                cm.gainItem(4031137, -bop);
                        } else {
                                cm.sendOk("You do not have any #i4031137#.");
                                cm.dispose();
                                }
                        }
                }
        }