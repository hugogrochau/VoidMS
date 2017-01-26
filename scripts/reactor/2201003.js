function act() {
	switch (rm.getPlayer().getMapId()) {
		case 922010900:
			rm.mapMessage(5, "Alishar has been summoned.");
			rm.spawnMonster(9300012, 941, 184);
			break;
		case 922010700:
			rm.mapMessage(5, "Rombard has been summoned somewhere in the map.");
			rm.spawnMonster(9300010, 1, -211);
			break;
		default:
			rm.mapMessage(5, "The box has been broken !");
	}
}