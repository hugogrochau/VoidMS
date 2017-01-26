//2042004 - Coconut Event Top - PeterPan

function start() {
    if (cm.haveItem(4000136)) {
        cm.gainItem(4000136,- 1);
        cm.gainItem(4001007,1);
            cm.sendOk("Thanks for your #i4000136#, here's your ticket! Now go down and get me another!");
            cm.dispose();
        } else {
            cm.sendOk("You don't have a #i4000136# Go get me one!");
            cm.dispose();
        }
}