
var status = 0;
var maps = new Array(209000001, 209000002, 209000003, 209000004, 209000005, 209000006, 209000007, 209000008, 209000009, 209000010, 209000011, 209000012, 209000013, 209000014, 209000015);

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    selected = selection;
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
            var intro = "#eHello #h #, which christmas map do you want to go to?#n#b\r\n";
            for (i = 0; i < maps.length; i++) {
                intro += "#L" + i + "##m" + maps[i] + "# " + [i + 1] + "\r\n";
            }
            cm.sendSimple(intro);
        } else if (status == 1) {
            cm.warp(maps[selection]);

            cm.dispose();
        }
    }
}


