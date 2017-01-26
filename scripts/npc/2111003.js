var random; 
var previous_position = 0; 
var status = 0;

var points = 0;

function start()
{
	cm.sendSimple("#L0#Randomize#l");
}

function action(mode,type,selection)
{
	if(status == 0) {
		if(selection == 0) {
			points++;
			random = Math.floor((Math.random()*3) + 1);
			
			var board = ["o","o","o","o","o","o","o","o","o","o"];
			board[(random - 1)+previous_position] = "x";
			previous_position += random;
			if( (previous_position + random - 1) >= 10)
			{
				cm.sendSimple("You completed with "+ points +" amount of points");
			} else {
				cm.sendSimple("You rolled a " + random + " \r\n\r\n @ " + board + "\r\n\r\n#L0#Roll again#l");
			}
		}
	} 
}

