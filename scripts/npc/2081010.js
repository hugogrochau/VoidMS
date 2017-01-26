var status;

function start() {
    status = -1;
    action(1, 0, 0);
   
}
 
function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else {
        status--;
    }
   
    if (status == 0) {
        cm.sendSimple("*looking at his paper* Wood: check, Silk: check, now what else did I have too bring..\r\n\r\n*looks up* Oh! Hello! Who are you? #L0#I'm a racist, I hate little hairy midgets#l \r\n#L1#I'm a friend of Jerry#l");
    } else if (status == 1) {
        if (selection == 0) {
                cm.sendSimple("Get lost before I punch you in the face!");
                cm.dispose();
        } else if (selection == 1) {
            cm.sendSimple("Jerry! Man I'm surprised he even had friends besides us.. \r\n\r\n#L2#Is he still here in Leafre?#l");
            cm.dispose();
			}
    } else if (status == 2) {
                if (selection == 2) {
                cm.sendSimple("No, he left a long while ago. He lived with us for a while, because he didn't like Kerning anymore.\r\nI think he left Leafre 3 years ago.\r\n\r\n#L3#Where did he go?#l"");
                cm.dispose();
				}
    } else if (status == 3) {
        if (selection == 3) {
            cm.sendOk("I think he mentioned something about Ariant, but I'm not that sure.");
            cm.dispose();

        }
    }
}
