/*
Tic-Tac-Toe NPC
Written by Emilyx3
*/
var status = 0;

var pieces = Array(3991023,3991040);

var player = 0;
var cp = 0;
var empty = 5010049;

var gamestarted = false;
var goesfirst = Math.floor(Math.random()*2) + 1;

var aichoice = true; // 0|False = Winnable Sometimes , 1|True = Not Winnable

var board = Array(0,0,0,0,0,0,0,0,0);
var boardstr = "n/a";

function start() {
status = -1;
action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (mode == 0) {
            if (gamestarted) {
                cm.dispose();
            } else {
                cm.sendOk("Okay, see you next time.");
                cm.dispose();
                return;
            }
        }
        if (mode == 1) {
            status++;
        } else {
            if (gamestarted) {
                status++;
            } else {
                status--;
            }
        }

        if (status == 0) {
            cm.sendYesNo("Would you like to play a game of Tic-Tac-Toe?\n\r This game does not have any reward yet.");
        } else if (status == 1) {
            var selStr = "Choose the piece you would like to use:"
            for (var i = 0; i < pieces.length; i++) {
                    selStr += "\r\n#L" + pieces[i] + "##v" + pieces[i] + "##l";
            }
            cm.sendSimple(selStr);
        } else if (status == 2) {
            player = selection;
            cp = pieces[Math.floor(Math.random()*pieces.length)];
            while (cp == player) {
                cp = pieces[Math.floor(Math.random()*pieces.length)];
            }
            cm.sendSimple("Choose the A Level: \r\n#L0#~Easy#l\r\n#L1#~Hard#l");
        } else if (status == 3) {
            if (selection == 0) {
                aichoice = false;
            } else if (selection == 1) {
                aichoice = true;
            }
            gamestarted = true;

            if (goesfirst == 1) {
                boardstr = cm.tTTGenerateBoard(board,player,cp,empty,false,0);
                cm.sendSimple(boardstr);
            } else {
                board = cm.tTTLogicMain(board,aichoice);
                boardstr = cm.tTTGenerateBoard(board,player,cp,empty,false,0);
                cm.sendSimple(boardstr);
            }            
        } else if (status > 3) {
            if (selection != 1000) {
            try {
            board[selection] = 1;
            } catch (Exception) {
                //Unknown Exception here when game ends? Suppress?
            }
            boardstr = cm.tTTGameOver(board,player,cp,empty);
            if (boardstr != "0") {
                cm.sendSimple(boardstr);
                cm.dispose();
            } else {
                board = cm.tTTLogicMain(board,aichoice);
                boardstr = cm.tTTGameOver(board,player,cp,empty);
                    if (boardstr != "0") {
                        cm.sendSimple(boardstr);
                        cm.dispose();
                    } else {
                        boardstr = cm.tTTGenerateBoard(board,player,cp,empty,false,0);
                        cm.sendSimple(boardstr);
                    }
            }
            } else {
                boardstr = cm.tTTGenerateBoard(board,player,cp,empty,false,0);
                cm.sendSimple(boardstr + "There is already a piece there, choose another space!");
            }
        }
    }
}  