function start() {
cm.sendYesNo("Are you sure you want to set your keymap to default?");
}

function action(mode, type, selection) {
if (mode == 1) {
cm.getP().setDefaultKeyMap();
cm.sendOk("Your keys were resetted");
} else {
cm.sendOk("Ok. Bye.");
}
cm.dispose();}