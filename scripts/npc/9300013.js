/**
 * Mastermind NPC
 * Author: Emilyx3 of UMaple
 */
var status;
var CorrectCombo;
var CurrentCombo;
var PreviousGuesses = new Array();
var NumSpaces, NumColors;
var SpaceDiffSelected, PieceDiffSelected;
var Pieces = [4030002, 4030003, 4030004, 4030005, 4030006, 4030007, 4030008];
var SpaceDifficulties = [["Beginner", 3], ["Easy", 4], ["Intermediate", 5], ["Hard", 6], ["Expert", 7]];
var PieceDifficulties = [["Beginner", 3], ["Easy", 4], ["Intermediate", 5], ["Hard", 6], ["Expert", 7]];
var MaxGuesses = 25;
//var CorrectPiece = 4031140, CorrectPieceAndSpace = 4005004;
var CorrectPiece = 4001007, CorrectPieceAndSpace = 4001008, Blank = 4031325, DeletePrevious = 3991002;

function Header() {
    return "#d============#k #e#bMastermind#k#n #d============#k\r\n\r\n";
}
function SendMainMenu() {
    cm.sendSimple(
        Header() +
        "#L0#Start Game#l\r\n" +
        "#L1#Game Instructions#l"
    );
}
function SendGameInstructions() {
    cm.sendNext(Header() +
        "#b#eWelcome to the game Mastermind.#k#n\r\n" +
        "#eGame Instructions:#n\r\n" +
        "1) The goal of the game is to guess the correct sequence of pieces.\r\n" +
        "2) After each guess, you will receive feedback as to how close your guess is to the correct answer.\r\n" +
        "        #v"+CorrectPieceAndSpace+"# - Indicates a #bcorrect#k piece in the #bcorrect#k location\r\n" +
        "        #v"+CorrectPiece+"# - Indicates a #bcorrect#k piece in an #rincorrect#k location\r\n\r\n" +
        "    Example: " + GetFeedback([1,2]) + " - Indicates 1 correct piece in the correct location, and 2 other correct pieces but in wrong locations.\r\n" +
        "3) You have a maximum of #r" + MaxGuesses + "#k tries to get the correct combination.\r\n"
    );
}
function SendNumSpacesSelectionMenu() {
    var SendStr = Header();
    SendStr += "#ePlease select the length of the sequence you would like to play with:#n\r\n"
    for (var i = 0;i < SpaceDifficulties.length;i++) {
        SendStr += "#L" + i + "#" + SpaceDifficulties[i][0] + " - Number of Spaces: " + SpaceDifficulties[i][1] + "\r\n";
    }
    cm.sendSimple(SendStr);
}
function SendNumPiecesSelectionMenu() {
    var SendStr = Header();
    SendStr += "#ePlease select the number of piece types you would like to play with:#n\r\n"
    for (var i = 0;i < PieceDifficulties.length;i++) {
        SendStr += "#L" + i + "#" + PieceDifficulties[i][0] + " - Number of Piece Types: " + PieceDifficulties[i][1] + "\r\n";
    }
    cm.sendSimple(SendStr);
}
function GetGameSettingsDescription() {
    return "\r\n#d#eGame Settings:#n#k\r\n" +
        SpaceDifficulties[SpaceDiffSelected][0] + " - Number of Spaces: " + SpaceDifficulties[SpaceDiffSelected][0] + "\r\n" +
        PieceDifficulties[PieceDiffSelected][0] + " - Number of Piece Types: " +  PieceDifficulties[PieceDiffSelected][1] + "\r\n";
}
function InitializeCorrectSequence() {
    Shuffle(Pieces);
    CorrectCombo = new Array(NumSpaces);
    for (var i = 0;i < CorrectCombo.length;i++) {
        CorrectCombo[i] = Math.floor(Math.random() * NumColors);
    }
}
function GetSequenceString(Sequence) {
    var Ret = "";
    for (var i = 0;i < Sequence.length;i++) {
        Ret += "#v" + Pieces[Sequence[i]] + "#";
    }
    return Ret;
}
function GetCurrentCombo() {
    var Ret = "Your Current Guess: ";
    for (var i = 0;i < CurrentCombo.length;i++) {
        Ret += "#v" + Pieces[CurrentCombo[i]] + "#";
    }
    for (var j = 0;j < NumSpaces - CurrentCombo.length;j++) {
        Ret += "#v" + Blank + "#";
    }
    return Ret + "\r\n";
}
function GetPieceSelectionString() {
    var Ret = "Please choose a piece:\r\n";
    for (var i = 0;i < NumColors;i++) {
        Ret += "#L" + i + "##v" + Pieces[i] + "##l ";
    }
    if (CurrentCombo.length > 0) {
        Ret += "#L" + NumColors + "##v" + DeletePrevious + "##l";
    }
    return Ret + "\r\n";

}
function CalculateMatch() {
    var Correct = new Array();
    for (var i = 0;i < NumSpaces;i++) {
        if (CorrectCombo[i] == CurrentCombo[i]) {
            Correct.push(i);
        }
    }
    var Ret1 = Correct.length;
    for (var j = 0;j < NumSpaces;j++) {
        if (!Contains(j, Correct)) {
            for (var k = 0;k < NumSpaces;k++) {
                if (!Contains(k, Correct)) {
                    if (CurrentCombo[j] == CorrectCombo[k]) {
                        Correct.push(k);
                        break;
                    }
                }
            }
        }
    }
    return [Ret1, Correct.length - Ret1];
}
function Contains(i, arr) {
    for (var j = 0;j < arr.length;j++) {
        if (arr[j] == i) {
            return true;
        }
    }
    return false;
}
function GetFeedback(CorrectNumbers) {
    var Ret = "";
    for (var i = 0;i < CorrectNumbers[0];i++) {
        Ret += "#v" + CorrectPieceAndSpace + "#";
    }
    for (var j = 0;j < CorrectNumbers[1];j++) {
        Ret += "#v" + CorrectPiece + "#";
    }
    return Ret;
}
function GetPreviousCombos() {
    if (PreviousGuesses.length == 0) {
        return "";
    }
    var Ret = "\r\n=== #ePrevious Guesses#n ===\r\n";
    for (var i = PreviousGuesses.length - 1;i >= 0;i--) {
        var PreviousGuess = PreviousGuesses[i];
        Ret += ((i + 1) + ". ") + GetSequenceString(PreviousGuess[0]) + "    " + GetFeedback(PreviousGuess[1]) + "\r\n";
    }
    return Ret + "\r\n";
}
function Shuffle(arr) {
    var i = arr.length;
    if (i == 0)
        return;
    while (--i) {
        var j = Math.floor( Math.random() * ( i + 1 ) );
        var tempi = arr[i];
        var tempj = arr[j];
        arr[i] = tempj;
        arr[j] = tempi;
    }
}
function CheckSelection(s, low, high) {
    return s < low && s > high;
}
function start() {
    status = -1;
    action(1, 0, 0);
}
function action(mode, type, selection) {
    if (mode == -1 || (mode == 0 && (status != 5 || CurrentCombo == null || CurrentCombo.length != NumSpaces))) {
        cm.dispose();
        return;
    }
    if (status != 5)
        status++;

    if (status == 0) {
        SendMainMenu();
    } else if (status == 1) {
        if (selection == 0) {
            action(1, 0, 0);
        } else if (selection == 1) {
            SendGameInstructions();
        } else {
            cm.dispose();
        }
    } else if (status == 2) {
        SendNumSpacesSelectionMenu();
    } else if (status == 3) {
        if (CheckSelection(selection, 0, SpaceDifficulties.length - 1)) {
            cm.dispose();
            return;
        }
        SpaceDiffSelected = selection;
        NumSpaces = SpaceDifficulties[selection][1];
        SendNumPiecesSelectionMenu();
    } else if (status == 4) {
        if (CheckSelection(selection, 0, PieceDifficulties.length - 1)) {
            cm.dispose();
            return;
        }
        PieceDiffSelected = selection;
        NumColors = PieceDifficulties[selection][1];
        InitializeCorrectSequence();
        CurrentCombo = new Array();
        cm.sendSimple(Header() + GetCurrentCombo() + GetPieceSelectionString()
            + GetPreviousCombos() + GetGameSettingsDescription());
    } else if (status == 5) {
        if (CurrentCombo.length == NumSpaces) {
            if (mode == 0) { //No
                CurrentCombo.pop();
                cm.sendSimple(Header() + GetCurrentCombo() + GetPieceSelectionString()
                    + GetPreviousCombos() + GetGameSettingsDescription());
            } else if (mode == 1) { //Yes
                var Match = CalculateMatch();
                if (Match[0] == NumSpaces) {
                    //Win
                    cm.sendOk(Header() + "#b#eCongrats, You won in "+(PreviousGuesses.length + 1)+" guesses!#n#k\r\n"
                        + GetSequenceString(CurrentCombo) + "    " + GetFeedback(Match) + "\r\n"
                        + GetPreviousCombos() + GetGameSettingsDescription());
                    cm.dispose();
                } else if (PreviousGuesses.length + 1 == MaxGuesses) {
                    //Lose
                    PreviousGuesses.push([CurrentCombo, Match]);
                    cm.sendOk(Header() + "#b#eSorry, You lost! You exceeded the maximum number of guesses ("+MaxGuesses+")!#n#k\r\n"
                        + "The correct combination was: " +  GetSequenceString(CorrectCombo) + "\r\n"
                        + GetPreviousCombos() + GetGameSettingsDescription());
                    cm.dispose();
                } else {
                    //Continue
                    PreviousGuesses.push([CurrentCombo, Match]);
                    CurrentCombo = new Array();
                    cm.sendSimple(Header() +
                        "#rSorry, that sequence was incorrect.#k\r\n"
                        + GetCurrentCombo() + GetPieceSelectionString()
                        + GetPreviousCombos() + GetGameSettingsDescription());
                }
            }
        } else {
            if (CheckSelection(selection, 0, (CurrentCombo.length > 0 ? NumColors : NumColors - 1))) {
                cm.dispose();
                return;
            }
            if (selection == NumColors) {
                CurrentCombo.pop();
            } else {
                CurrentCombo.push(selection);
            }
            if (CurrentCombo.length == NumSpaces) {
                cm.sendYesNo(Header() + "#rPlease confirm your selection:#k\r\n"
                    + GetCurrentCombo() + GetPreviousCombos() + GetGameSettingsDescription()
                );
            } else {
                cm.sendSimple(Header() + GetCurrentCombo() + GetPieceSelectionString()
                    + GetPreviousCombos() + GetGameSettingsDescription());
            }
        }
    } else {
        cm.dispose();
    }
}