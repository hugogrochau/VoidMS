//2042003 - Coconut event middle - coupons to pass - PeterPan
function start() {
    if (cm.haveItem(4001007, 5)) {
        cm.gainItem(4001007,- 5);
        cm.gainItem(4001008,1);
            cm.sendOk("Thanks for your coupons, here's your #i4001008#! Now go down and give it to the GM!");
            cm.dispose();
        } else {
            cm.sendOk("You don't have 5 #i4001007# \r\nYou and your party need to get me 5 #bCoupons#k!");
            cm.dispose();
        }
}
