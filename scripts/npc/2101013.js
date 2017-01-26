var status; 
var mobs = Array (100100,100101 ,1110100 , 1110101 ,1210100 ,2100103 ,210100 ); 
var rand = 0;
var randcard = 0;

function start() {
    rand = Math.floor(Math.random()*mobs.length);
    randcard = mobs[rand];
    status = -1;
    action(1, 0, 0);
    
}

function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    }else{
        status--;
    }
    
    if (status == 0) {
        cm.sendSimple("Hi! I'm Karcasa, the game master of #bGuess The Mob#k. \r\nWhat can I do for you?\r\n\r\n#L0#I want a new monster.#l \r\n#L1#I want to leave#l");
    } else if (status == 1) {
        if (selection == 0) {
			cm.sendOk("This is your monster for this round, take a good look at it and go sit next to the wall when your ready. \r\n\r\n\r\n         #r#o"+randcard+"##k");
            cm.dispose();
        }
         else if (selection == 1) {
            cm.sendOk("Bye!");
			cm.warpAllInMap(100000000, 0);
            cm.dispose();
		}
    }
}