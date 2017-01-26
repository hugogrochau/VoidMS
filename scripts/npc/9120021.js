var status = -1;

function start()
{
	cm.sendSimple("#r#L0# List of rares\r\n");
}

function action(mode,type,selection)
{
	status++;
	
	if(status == 0)
	{
		if(selection == 0)
		{
			cm.sendSimple("#L0#Hats\r\n#L1#Face Accessory\r\n#L2#Eye Accessory\r\n#L3#Earrings\r\n#L4#Top\r\n#L5#Overall\r\n#L6#Bottom\r\n#L7#Shoes\r\n#L8#Gloves\r\n#L9#Shield\r\n#L10#Capes\r\n#L11#Ring\r\n#L12#Weapons\r\n#L13#Pets\r\n#L14#Mounts\r\n#L15#Pendants\r\n");
		}	
	}
	if(status == 1)
	{
		var index = 1;
		switch(selection)
		{
			case 0: //hat
				var item = cm.GetRareId(index);
				var text = "List of rare hats: \r\n\r\n\r\n";
				var i = 0;
				while(index != cm.GetRareCount())
				{
					if( item.toString().substring(0,3) == "100" )
					{
						text += "\r\n" + item;
					}
					index++;
					item = cm.GetRareId(index);
				}
				cm.sendSimple(text);
			break;
			
			case 1: //Face acc
				var item = cm.GetRareId(index);
				var text = "List of rare face accessories: \r\n\r\n\r\n";
				var i = 0;
				while(index != cm.GetRareCount())
				{
					if( item.toString().substring(0,3) == "101" )
					{
						text += "\r\n" + item;
					}
					index++;
					item = cm.GetRareId(index);
				}
				cm.sendSimple(text);
			break;
			
			case 2: //Eye accessories
				var item = cm.GetRareId(index);
				var text = "List of rare eye accessories: \r\n\r\n\r\n";
				var i = 0;
				while(index != cm.GetRareCount())
				{
					if( item.toString().substring(0,3) == "102" )
					{
						text += "\r\n" + item;
					}
					index++;
					item = cm.GetRareId(index);
				}
				cm.sendSimple(text);
			break;
			
			case 3: //Earrings
				var item = cm.GetRareId(index);
				var text = "List of rare earrings: \r\n\r\n\r\n";
				var i = 0;
				while(index != cm.GetRareCount())
				{
					if( item.toString().substring(0,3) == "103" )
					{
						text += "\r\n" + item;
					}
					index++;
					item = cm.GetRareId(index);
				}
				cm.sendSimple(text);
			break;
			
			case 4: //Top
				var item = cm.GetRareId(index);
				var text = "List of rare tops: \r\n\r\n\r\n";
				var i = 0;
				while(index != cm.GetRareCount())
				{
					if( item.toString().substring(0,3) == "104" )
					{
						text += "\r\n" + item;
					}
					index++;
					item = cm.GetRareId(index);
				}
				cm.sendSimple(text);
			break;
			
			case 5: //overall
				var item = cm.GetRareId(index);
				var text = "List of rare overalls: \r\n\r\n\r\n";
				var i = 0;
				while(index != cm.GetRareCount())
				{
					if( item.toString().substring(0,3) == "105" )
					{
						text += "\r\n" + item;
					}
					index++;
					item = cm.GetRareId(index);
				}
				cm.sendSimple(text);
			break;
			
			case 6: //pants
				var item = cm.GetRareId(index);
				var text = "List of rare bottoms: \r\n\r\n\r\n";
				var i = 0;
				while(index != cm.GetRareCount())
				{
					if( item.toString().substring(0,3) == "106" )
					{
						text += "\r\n" + item;
					}
					index++;
					item = cm.GetRareId(index);
				}
				cm.sendSimple(text);
			break;
			
			case 7: //shoes
				var item = cm.GetRareId(index);
				var text = "List of rare shoes: \r\n\r\n\r\n";
				var i = 0;
				while(index != cm.GetRareCount())
				{
					if( item.toString().substring(0,3) == "107" )
					{
						text += "\r\n" + item;
					}
					index++;
					item = cm.GetRareId(index);
				}
				cm.sendSimple(text);
			break;
			
			case 8: //gloves
				var item = cm.GetRareId(index);
				var text = "List of rare gloves: \r\n\r\n\r\n";
				var i = 0;
				while(index != cm.GetRareCount())
				{
					if( item.toString().substring(0,3) == "108" )
					{
						text += "\r\n" + item;
					}
					index++;
					item = cm.GetRareId(index);
				}
				cm.sendSimple(text);
			break;
			
			case 9: //shield
				var item = cm.GetRareId(index);
				var text = "List of rare shields: \r\n\r\n\r\n";
				var i = 0;
				while(index != cm.GetRareCount())
				{
					if( item.toString().substring(0,3) == "109" )
					{
						text += "\r\n" + item;
					}
					index++;
					item = cm.GetRareId(index);
				}
				cm.sendSimple(text);
			break;
			
			case 10: //capes
				var item = cm.GetRareId(index);
				var text = "List of rare capes: \r\n\r\n\r\n";
				var i = 0;
				while(index != cm.GetRareCount())
				{
					if( item.toString().substring(0,3) == "110" )
					{
						text += "\r\n" + item;
					}
					index++;
					item = cm.GetRareId(index);
				}
				cm.sendSimple(text);
			break;
			
			case 11: //rings
				var item = cm.GetRareId(index);
				var text = "List of rare rings: \r\n\r\n\r\n";
				var i = 0;
				while(index != cm.GetRareCount())
				{
					if( item.toString().substring(0,3) == "111" )
					{
						text += "\r\n" + item;
					}
					index++;
					item = cm.GetRareId(index);
				}
				cm.sendSimple(text);
			break;
			
			case 12: //weapons
				var item = cm.GetRareId(index);
				var text = "List of rare weapons: \r\n\r\n\r\n";
				var i = 0;
				while(index != cm.GetRareCount())
				{
					if( item.toString().substring(0,3) == "170" )
					{
						text += "\r\n" + item;
					}
					index++;
					item = cm.GetRareId(index);
				}
				cm.sendSimple(text);
			break;
			
			case 13: //pets
				var item = cm.GetRareId(index);
				var text = "List of rare pets: \r\n\r\n\r\n";
				var i = 0;
				while(index != cm.GetRareCount())
				{
					if( item.toString().substring(0,3) == "500" )
					{
						text += "\r\n" + item;
					}
					index++;
					item = cm.GetRareId(index);
				}
				cm.sendSimple(text);
			break;
			
			case 14: //Mounts
				var item = cm.GetRareId(index);
				var text = "List of rare mounts: \r\n\r\n\r\n";
				var i = 0;
				while(index != cm.GetRareCount())
				{
					if( item.toString().substring(0,3) == "190" )
					{
						text += "\r\n" + item;
					}
					index++;
					item = cm.GetRareId(index);
				}
				cm.sendSimple(text);
			break;
			
			case 15: //Pendants
				var item = cm.GetRareId(index);
				var text = "List of rare pendants: \r\n\r\n\r\n";
				var i = 0;
				while(index != cm.GetRareCount())
				{
					if( item.toString().substring(0,3) == "112" )
					{
						text += "\r\n" + item;
					}
					index++;
					item = cm.GetRareId(index);
				}
				cm.sendSimple(text);
			break;
		}
	}
}