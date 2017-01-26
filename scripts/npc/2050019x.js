function start() {
    status = -1;
    action(1, 0, 0);
}
 
function action(mode, type, selection) {
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
cm.sendSimple("#eCongratualtions on gaining your first 5 rebirths! You are now done VoidMS' Noob Introduction! Good job!\r\n\r\n#b#L0#I am ready to go to Henesys!#l");
} else if (selection == 0) {
cm.warp(100000000);
cm.sendOk("#eWelcome to the world of #dVoidMS#k! Enjoy your stay, and feel free to ask any questions!\r\n You can use #r@help #kand #r@commands #kto see a list of commands. You can use #r@gmmsg #kto ask any online Staff questions, but please don't flood this.");
cm.dispose();
}
}
}


/* Made by Hector from LastStory. 
Do not take credits for something you didn't do!.  

var wui = 0; 

function start() { 
    cm.sendSimple ("Hello, I'm the Slime Eraser trader. If you got any #i4001013# you could exchange them here for prizes. \r\n#L0# #b#b10000#k Slime erasers for #rGM Dragon Roar#k.#k #l\r\n#L1##b8000#k Slime erasers for #rGm HAT!#k #l\r\n#L2##b50#k Slime erasers for #rDark Crystal Blade <Warror>!#k #l\r\n#L3##b20#k Slime erasers for #rBlack Bow Tie!#k #l\r\n#L4##b20#k Slime erasers for #rLunch Boxes!#k #l\r\n#L5##b20#k Slime erasers for 3 #rDragon Stones!#k #l\r\n#L6##b10#k Slime erasers for #rRaven CrossBows!#k #l\r\n#L7##b10#k Slime erasers for #rRaven Swords!#k #l\r\n#L8##b10#k Slime erasers for #rRaven Claws and Daggers!#k #l\r\n#L9##b10#k Slime erasers for #rElemental Wands & Staffs!#k #l\r\n#L10##b8#k Slime erasers for #rMounts + Saddle!#k #l\r\n#L11##b5#k Slime erasers for #rAll bars , (Etc.. Chocolate)#k #l\r\n#L12##b5#k Slime erasers for #rTimeless Equips!#k #l\r\n#L13##b5#k Slime erasers for #rGlowing Smile Cap!#k #l\r\n#L14##b3#k Slime erasers for #rBalanced Furys!#k"); 
} 

function action(mode, type, selection) { 
    cm.dispose();
    if (selection == 0) {
        var gmJobId = 900; // change to 900 if v62. 500 is for v55
        if (cm.haveItem(4001013, 10000)) {
            cm.teachSkill(9001001,1,1);
            cm.gainItem(4001013, -10000);
            cm.changeJob(gmJobId);
            cm.sendOk("Well done!, Here is your #rGm Roar!#k\r\nPlease rebirth out of the GM job ASAP");
            cm.dispose();
        } else {
            cm.sendOk(" Sorry. You do not have enough Slime erasers. ");
            cm.dispose();
        }


    } else if (selection == 1) {


        if (cm.haveItem(4001013, 8000)) {
            cm.gainItem(1002140,1);
            cm.gainItem(4001013, -8000);
            cm.sendOk("Well done, Here are is your Gm hat!");
            cm.dispose();
        } else {
            cm.sendOk(" Sorry. You do not have enough Slime Erasers. ");
            cm.dispose();
        }



    } else if (selection == 2) {


        if (cm.haveItem(4001013, 50)) {
            cm.gainItem(1302107,1);
            cm.gainItem(4001013, -50);
            cm.sendOk("Well done, Here is your #r Dark Crystal Blade!#k.");
            cm.dispose();
        } else {
            cm.sendOk(" Sorry. You do not have enough Slime Erasers. ");
            cm.dispose();
        }



    } else if (selection == 3) {

        if (cm.haveItem(4001013, 20)) {
            cm.gainItem(1122005,1);
            cm.gainItem(4001013, -20);
            cm.sendOk("Well done, Here is your Black Bow tie!! Good luck!!");
            cm.dispose();
        } else {
            cm.sendOk(" Sorry. You do not have enough Slime Erasers. ");
            cm.dispose();
        }



    } else if (selection == 4) {


        if (cm.haveItem(4001013, 20)) {
            cm.gainItem(1302098,1);
            cm.gainItem(1302099,1);
            cm.gainItem(1302100,1);
            cm.gainItem(1302101,1);
            cm.gainItem(4001013, -20);
            cm.sendOk("Well done, Here is your #r Lunch Boxes!!#k.");
            cm.dispose();
        } else {
            cm.sendOk(" Sorry. You do not have enough Slime Erasers. ");
            cm.dispose();
        }



    } else if (selection == 5) {


        if (cm.haveItem(4001013, 20)) {
            cm.gainItem(2041200,3);
            cm.gainItem(4001013, -20);
            cm.sendOk("Well done, Here are your 3 Dragon Stones.");
            cm.dispose();
        } else {
            cm.sendOk(" Sorry. You do not have enough Slime Erasers. ");
            cm.dispose();
        }


    } else if (selection == 6) {


        if (cm.haveItem(4001013, 10)) {
            cm.gainItem(1462052,1);
            cm.gainItem(1462053,1);
            cm.gainItem(1462054,1);
            cm.gainItem(1462055,1);
            cm.gainItem(4001013, -10);
            cm.sendOk("Well done, Here is your #r Raven CrossBows!!!#k.");
            cm.dispose();
        } else {
            cm.sendOk(" Sorry. You do not have enough Slime Erasers. ");
            cm.dispose();
        }



    } else if (selection == 7) {


        if (cm.haveItem(4001013, 10)) {
            cm.gainItem(1402048,1);
            cm.gainItem(1402049,1);
            cm.gainItem(1402050,1);
            cm.gainItem(1402051,1);
            cm.gainItem(4001013, -10);
            cm.sendOk("Well done, Here is your #r Raven Swords!!!#k.");
            cm.dispose();
        } else {
            cm.sendOk(" Sorry. You do not have enough Slime Erasers. ");
            cm.dispose();
        }



    } else if (selection == 8) {


        if (cm.haveItem(4001013, 10)) {
            cm.gainItem(1472073,1);
            cm.gainItem(1472072,1);
            cm.gainItem(1472074,1);
            cm.gainItem(1472075,1);
            cm.gainItem(1332077,1);
            cm.gainItem(1332078,1);
            cm.gainItem(1332079,1);
            cm.gainItem(1332080,1);
            cm.gainItem(4001013, -10);
            cm.sendOk("Well done, Here is your #r Raven Daggers and claws!!!#k.");
            cm.dispose();
        } else {
            cm.sendOk(" Sorry. You do not have enough Slime Erasers. ");
            cm.dispose();
        }



    } else if (selection == 9) {

        if (cm.haveItem(4001013, 10)) {
            cm.gainItem(1372035,1);
            cm.gainItem(1372036,1);
            cm.gainItem(1372037,1);
            cm.gainItem(1372038,1);
            cm.gainItem(1372039,1);
            cm.gainItem(1372040,1);
            cm.gainItem(1372041,1);
            cm.gainItem(1372042,1);
            cm.gainItem(1382045,1);
            cm.gainItem(1382046,1);
            cm.gainItem(1382047,1);
            cm.gainItem(1382048,1);
            cm.gainItem(1382049,1);
            cm.gainItem(1382050,1);
            cm.gainItem(1382051,1);
            cm.gainItem(1382052,1);
            cm.gainItem(4001013, -10);
            cm.sendOk("Well done, Here is your #r Elemental Items!#k.");
            cm.dispose();
        } else {
            cm.sendOk(" Sorry. You do not have enough Slime erasers. ");
            cm.dispose();
        } 
    } else if (selection == 10) {
        if (cm.haveItem(4001013, 8)) {
            cm.gainItem(1902000,1);
            cm.gainItem(1902001,1);
            cm.gainItem(1902002,1);
            cm.gainItem(1912000,1);
            cm.gainItem(4001013, -8);
            cm.sendOk("Well done, Here are your #r Taming mobs + Saddle!!#k.");
            cm.dispose();
        } else {
            cm.sendOk(" Sorry. You do not have enough Slime Erasers. ");
            cm.dispose();
        }
    } else if (selection == 11) {
        if (cm.haveItem(4001013, 5)) {
            cm.gainItem(1012071,1);
            cm.gainItem(1012072,1);
            cm.gainItem(1012073,1);
            cm.gainItem(4001013, -5);
            cm.sendOk("Well done, Here are your bars.");
            cm.dispose();
        } else {
            cm.sendOk(" Sorry. You do not have enough Slime Erasers. ");
            cm.dispose();
        }
    } else if (selection == 12) {
        if (cm.haveItem(4001013, 5)) {
            cm.gainItem(1302081,1);
            cm.gainItem(1312037,1);
            cm.gainItem(1322060,1);
            cm.gainItem(1402046,1);
            cm.gainItem(1412033,1);
            cm.gainItem(1422037,1);
            cm.gainItem(1442063 ,1);
            cm.gainItem(1482023,1);
            cm.gainItem(4001013, -5);
            cm.sendOk("Well done, Here are your #r Timeless equips!!!#k.");
            cm.dispose();
        } else {
            cm.sendOk(" Sorry. You do not have enough Slime Erasers. ");
            cm.dispose();
        }
    } else if (selection == 13) {
        if (cm.haveItem(4001013, 5)) {
            cm.gainItem(1002735,1);
            cm.gainItem(4001013, -5);
            cm.sendOk("Well done, Here is your #r Glowing Smile cap!!#k.");
            cm.dispose();
        } else {
            cm.sendOk(" Sorry. You do not have enough Slime Erasers. ");
            cm.dispose();
        }
    } else if (selection == 14) {
        if (cm.haveItem(4001013, 3)) {
            cm.gainItem(2070018,1);
            cm.gainItem(4001013, -3);
            cm.sendOk("Well done, Here is your #r Balanced Fury!#k.");
            cm.dispose();
        } else {
            cm.sendOk(" Sorry. You do not have enough Slime Erasers. ");
            cm.dispose();
        }
        cm.dispose(); 
    }


     
}  */