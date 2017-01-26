var bossmaps = Array(100000005, 105070002, 105090900, 230040420, 280030000, 220080001, 240020402, 240020101, 801040100, 240060200, 610010005, 610010012, 610010013, 610010100, 610010101, 610010102, 610010103, 610010104, 541010100); // Someone else's House, The Grave of Mushmom, The cursed Sanctuary, The Cave of Pianus, Zakums Altar, Origin of Clocktower, Manons Forest, Griffey Forest, The Nightmarish Last Days, Horntails Cave, Bigfoot- Phantom Forest-Forgotten Path, Phantom Forest-Evil Rising, Phantom Forest-The Evil Dead, Phantom Forest-Twisted Path 1, Phantom Forest-Twisted Path 2, Phantom Forest-Twisted Path 3, Phantom Forest-Twisted Path 4, Phantom Forest-Twisted Path 5, Engine Room
var monstermaps = Array(100040001, 101010100, 104040000, 103000101, 103000105, 101030110, 106000002, 101030103, 101040001, 101040003, 101030001, 104010001, 105070001, 105090300, 105040306, 230020000, 230010400, 211041400, 222010000, 220080000, 220070301, 220070201, 220050300, 220010500, 250020000, 251010000, 200040000, 200010301, 240020100, 240040500, 240040000, 600020300, 801040004, 800020130, 610020002); // Dungeon Southern Forest I, Tree that Grew 1, Henesys Hunting Ground 1, Line 1 Area 1, Line 1 Area 4, Camp 1, Dangerous Valley II, Excavation Site III, Land of Wild Boar, Iron Boar Land, The Land of Wild Boar II, The Pig Beach, Ant Tunnel Park, Drakes Meal Table, The Forest of Golem, Forked Road: East Sea, Forked Road: West Sea, Forest of Dead Trees 4, Entrance to Black Mountain, Deep Inside the Clock Tower, Forbidden Time, Lost Time, Path of Time, Terrace Hall, Practice Field, Beginner, 10-Year-Old Herb Garden, Cloud Park 3, Garden of Darkness 1, Battlefield of Fire & Darkness, Entrance to Dragon Nest, The Dragon Canyon, Wolf Spider Cavern, Armory, Encounter with the Budda, Lower Ascent
var townmaps = Array(680000000, 230000000, 101000000, 211000000, 100000000, 251000000, 103000000, 222000000, 104000000, 240000000, 220000000, 250000000, 800000000, 600000000, 221000000, 200000000, 102000000, 801000000, 105040300, 610010004, 260000000, 540010000, 120000000); // Amoria, Aquarium, Ellinia, El Nath, Henesys, Herb Town, Kerning City, Korean Folk Town, Leafre, Lith Harbor, Ludibrium, Mu Lung, Mushroom Shrine, New Leaf City, Omega Sector, Orbis, Perion, Showa Town, Sleepywood, Crimsonwood, Ariant, Singapore, Nautilus Port
var chosenMap;
var typee;
var info = 0;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else if (status > 1){
        status--;
    } else {
        cm.dispose();
        return;
    }
    if (status == 0) {
        cm.sendYesNo("Hi #h #! I can teleport you just about anywhere. Would you like to see where you can go?");
    }
    if (status == 1) {
        cm.sendSimple("#fUI/UIWindow.img/QuestIcon/3/0#\r\n#L0#Towns#l\r\n#L1#Monster Maps#l\r\n#L2#Boss Maps#l\r\n#L3#Information#l");
        info = 0;
    } else if (status == 2) {
        if (selection == 3 || info == 1) {
            cm.sendNextPrev("#bBy selecting Towns, Monster Maps, or Boss Maps, you can pick from a list of maps in that category. For example, I click on Boss Maps, I can teleport to Zakum. There's a command called #r@go#b so you can do this much faster.\r\n\r\nSyntax: #r@go <place> #b \r\nAvailable Places (Alphabetical Order):\r\n\r\naqua\r\nellinia\r\nelnath\r\nexcavation\r\nflorina\r\ngriffey\r\nguild\r\nhenesys\r\nherb\r\nhorseman\r\nkerning\r\nkorean\r\nleafre\r\nlith\r\nludi\r\nmanon\r\nmulung\r\nmushmom\r\nnest\r\nnlc\r\nomega\r\norbis\r\nperion\r\nshowa\r\nshrine\r\nskele\r\nsleepywood\r\nfm\r\nfm1\r\nfm2\r\nfm3\r\nfm4\r\nfm5\r\nfm6\r\nfm7\r\nfm8\r\nfm9\r\nfm10\r\nfm11\r\nfm12\r\nfm13\r\nfm14\r\nfm15\r\nfm16\r\nfm17\r\nfm18\r\nfm19\r\nfm20\r\nfm21\r\nfm22\r\n\r\n\r\n#eCurrent Page:#n #rIntroduction \r\n#eNext Page:#n #rWrap Up");
            info = 1;
        } else {
            switch(selection) {
                case 0:
                    typee = townmaps;
                    break;
                case 1:
                    typee = monstermaps;
                    break;
                case 2:
                    typee = bossmaps;
                    break;
                case 3:
                    cm.sendNext('#bThis is the teleporting NPC. You can open it up by typing "@go" without the "". \r\n\r\n\r\n#eCurrent Page:#n #rIntroduction \r\n#eNext Page:#n #rMap List');
                    break;
            }
            var selStr = "Select your destination.#b";
            for (var i = 0; i < typee.length; i++) {
                selStr += "\r\n#L" + i + "##m" + typee[i] + "#";
            }
            cm.sendSimple(selStr);
        }
    } else if (status == 3) {
        if (info == 1) {
            cm.sendPrev("#bYou can type #r@go map help#b to get a list of all those maps quickly.");
        } else {
            chosenMap = typee[selection];
            cm.sendYesNo("Do you want to go to #m" + chosenMap + "#?");
        }
    } else if (status == 4) {
        if (info == 0) {
            cm.warp(chosenMap, 0);
        }
        cm.dispose();
    }
}
