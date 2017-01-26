/**
 * @Author: Moogra
 */

function start() {
    cm.sendYesNo("We have a beautiful christmas tree. Do you want to decorate it?");
}

function action(mode, type, selection) {
    if (mode == 1) {
        cm.warp(209000001);
        cm.dispose();
    } else {
        cm.sendNext("I see..");
        cm.dispose();
        return;
    }
//kept so people go WTF
//		if (cm.getChar().getGender() == 0) {
//			cm.warp(209000001);
//		} else {
//			cm.warp(209000001);
//		}
}