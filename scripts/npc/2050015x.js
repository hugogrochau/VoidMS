var numberrand; // math.floor always rounds down. 1.8 -> 1// Math.ceil (or ceiling) always rounds up. 1.3 -> 2 so it will be 1, 2, 3 instead of 0, 1, 2
var count = 0;
var number = 0;
 
function start() {
    status = -1;
    action(1, 0, 0);
}
 
function action(mode, type, selection) {
    selected = selection
    if (mode == -1) {
        cm.dispose();
    } else {
        if (status >= 0 && mode == 0) {
            cm.sendOk("Hope to see you later for more training!");
            cm.dispose();
            return;
        }
        if (mode == 1)
            status ++;
        else
            status --;
        if (status == 0) {
            cm.sendSimple("Hello, I am the second NPC in your quest to gain 5 rebirths! My job is to randomize a few numbers for you. Would you like to take the challenge?\r\n\r\n#b#L1#Yes!#l\r\n#L0#I am level 200 and would like to move on!#l");
       } else if (selection == 0) {
                        if (cm.getP().getLevel() == 200) {
                        cm.getP().doReborn();
                        cm.warp(925100300, 0);
                        cm.dispose();
                                        } else {
                                                cm.sendOk("Uh-Oh! You are not Level 200!");
                                                cm.dispose();
                                        }
                                                }
 
                        if (status == 1) {
            if (selection == 1) {
                cm.sendNext("Ok, since you want to try it, let me explain it for you. You will need to pick the right number twice in a row. If you get it right, you will move on. If you get it wrong, you have to start over!");
            }
        } else if (status == 2) { // it's gonna come here. And repeat until count >= 2
            if (count < 2) {// if count is smaller than 4
                if (count == 0) {
                    count++;
                    cm.sendGetText("Pick a number between one and three."); //, 1, 1, 3);
                    status = 1;
                } else {
                    numberrand = Math.ceil(Math.random() * 3); // randomizes
                    if (cm.getText() != numberrand) {  // if the number is not equals to the randm number
                        cm.sendOk("Sorry, you got the number wrong. The number was #r" + numberrand + "#k Please try again!");
                        count = 0;
                        status = 1; // go back to 0
                    } else { // if right
                        cm.sendGetText("Congratulations, #r" + cm.getText() + "#k was the right number. Please pick another number 1-3!");
                        count++;    // count = count + 1;
 
                        status = 1;// status is going to be 1 again // we don't need the action since it will run when you click ok to the sendnumber. But since status ==1
                    }
                }
            } else {
                cm.sendOk("Congratulations! You got both numbers correct!");
                cm.dispose();
                cm.getP().setLevel(199);
		    cm.getP().levelUp();
                    cm.fixExp();
                
            }      
                }
                }
                }
    
