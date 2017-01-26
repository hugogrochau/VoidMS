var status = -1;
var choice = -1; // undefined

function start() {
    cm.sendSimple("#eHello there #h #,  I can exchange your #i4001113# and #i4001114# eggs. What would you like to exchange?#n\r\n\r\n#b#L0# 10 #i4001113# for one #i4002000##l\r\n#L1# 10 #i4001114# for one #i4031544# \r\n#l");
}

function action(m,t,s) {
    if (m == 1) {
        status++;
        if (status == 0) {
            choice = s;
            cm.sendGetNumber("How many " + (s == 0 ? "#i4002000#" : "#i4031544#") + " would you like?",1,1,100);
        } else if (status == 1) {
            var amount = s;
            if (choice == 0) {
                if ((cm.itemQuantity(4001113)) >= (amount * 10) && !isNaN(amount)) {
                    cm.gainItem(4001113, -(amount * 10));
                    cm.gainItem(4002000, amount);
                    cm.sendOk("You exchanged " + (amount * 10) + " #i4001113# for " + amount + " #i4002000#");
                } else {
                    cm.sendOk("#rYou don't have that amount in your inventory");
                }
            } else if (choice == 1) {
                if ((cm.itemQuantity(4001114)) >= (amount * 10) && !isNaN(amount)) {
                    cm.gainItem(4001114, -(amount * 10));
                    cm.gainItem(4031544, amount);
                    cm.sendOk("You exchanged " + (amount * 10)  + " #i4001114# for " + amount + " #i4031544#");
                } else {
                    cm.sendOk("#rYou don't have that amount in your inventory");
                }
            }
            cm.dispose();
        }
    } else
        cm.dispose();
}