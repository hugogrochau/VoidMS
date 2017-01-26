var wui = 0;
var chairs = Array(3010128,3010127);


function start() {
    cm.sendSimple ("Hsello #e#h #.#n  I'm the VoidMS Chair Exchanger! You can exchange #v4031137# for chairs here! \r\n#L0#10 #v4031137# for #v3010128# #l\r\n#L1#20 #v4031137# for #v3010127#  #l\r\n#L2#30 #v4031137# for #v3012005# #l\r\n#L3#30 #v4031137# for #v1382060# #l\r\n#L4# 30 #v4031137# for #v1442068# #l\r\n#L5#30 #v4031137# for #v1452060# #l\r\n#L6#40 #v4031137# for #v1022060# #l\r\n#L7#40 #v4031137# for #v1022058# #l\r\n#L8#40 #v4031137# for #v1012110# #l\r\n#L9#50 #v4031137# for #v1012108# #l\r\n#L10#50 #v4031137# for #v1012111# #l\r\n#L11#50 #v4031137# for #v1012106# #l\r\n#L12#50 #v4031137# for #v1012084# #l\r\n#L13#50 #v4031137# for #v1002707# ")
}

function action(mode, type, selection) {
cm.dispose();
    if (selection == 0) {
for (var i = 0; i < chairs.length; i++) {
selStr += "\r\n#L" + i + "##m" + chairs[i] + "#";
}
cm.sendSimple(selStr);
    }
}
