var maps = new Array(100000000, 103000903);

function start() {
    status = 1;
    cm.sendNext("Congratulations on finishing the B1 Jump Quest! Here, you deserve 2 #bKarma levels!");
}

function action(mode, type, selection) {
    if (status == 1) {
        status = 2;
        var warpText = "Do you to go to the B2 Jump Quest or get out of here?";
        for (var i = 0; i < maps.length; i++)
            warpText += "\r\n#L" + i + "##m" + maps[i] + "##l";
        cm.sendSimple(warpText);
    } else if (status == 2) {
        cm.warp(maps[selection], 0);
	cm.gainKarma(2);
        cm.dispose();
    }
}	