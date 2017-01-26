function start() {
	cm.sendSimple("Hey there! I am Paul, the Event assistant. \n I help the GM's with their big events! \n\
If you have an #eEvent Trophy#n you can give it to me, and I will teleport you to a very special training map where you can train for 5 minutes! \n\n\
In this map there are special monsters that give #e1,000,000,00#n EXP! \n\n So, do you have an #eEvent Trophy#n?\n\r\n\
 #L0# Yeah I got it right here!#l \n\n\r\
 #L1# Sadly, No. :(   ");

}

function action(m, t, s) {
    if (s == 0){
        if(cm.itemQuantity(4000038) >= 1){
	    cm.loseItem(4000038, 1);
            cm.warp(920011200, 0);
            cm.startTraining(300);
             cm.sendSimple("Alright, enjoy your training");
        }else{
            cm.sendSimple("I'm sorry but you do not have an #eEvent Trophy#n.");
        }
        
    }

    
    else if (s == 1){
        cm.sendOk("Alright, come back when you have one.");
        cm.dispose();
            }

}