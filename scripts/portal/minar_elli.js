function enter(pi) {
if (pi.haveItem(3992039)) {
	if (pi.getPlayer().getMapId() == 240010100) {
		pi.warp(101010000, "minar00");
		return true;
	} else if (pi.getPlayer().getMapId() == 101010000) {
		pi.warp(240010100, "elli00");
		return true;
	}
		pi.gainItem(3992039, -1);
		pi.playerMessage("The Magical Seed is spent and you are transferred to somewhere.");
	} else {
	pi.playerMessage("Magic Seed is needed to go through the portal.");
}