/*
 * Cody NPC 9200000
 * @Author XoticStory.
 */

var status;
var possibleJobs = new Array();
var job;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
	if (mode == 1)
		status++;
	else {
		cm.dispose();
		return;
	}
	if (cm.getJob().getId() % 10 == 2) {
		cm.sendOk("Hey, how's it going? I've been doing well here.");
		cm.dispose();
	} else if (cm.getJob().getId() % 100 != 0) {
		var secondJob = (cm.getJob().getId() % 10 == 0);
		if ((secondJob && cm.getLevel() < 70) || (!secondJob && cm.getLevel() < 120)) {
			cm.sendOk("Hey, how's it going? I've been doing well here.");
			cm.dispose();
		} else {
		    var newJob = cm.getJob().getId() + 1;
		    if (status == 0)
			    cm.sendYesNo("Great job getting to level "+cm.getLevel()+". Would you like to become a #b"+cm.getJobById(newJob)+"#k ?");
		    else if (status == 1) {
			    cm.sendOk("Congratulations, you are now a #b"+cm.getJobById(newJob)+"#k.");
			    cm.getPlayer().setJob(newJob);
                cm.dispose();
		    }
	    }
	} else {
		if (status == 0) {
			if (cm.getJob().equals(Packages.client.MapleJob.BEGINNER)) {
				if (cm.getLevel() >= 8)
					possibleJobs.push(200);
				if (cm.getLevel() >= 10) {
					if (cm.getPlayer().getStr() >= 4) possibleJobs.push(100);
					if (cm.getPlayer().getDex() >= 4) { possibleJobs.push(300); possibleJobs.push(400);}
					if (cm.getPlayer().getDex() >= 4) possibleJobs.push(500);
				}
			} else {
				if (cm.getLevel() >= 30) {
					switch (cm.getJob().getId()) {
						case 100: possibleJobs = [110, 120, 130]; break;
						case 200: possibleJobs = [210, 220, 230]; break;
						case 300: possibleJobs = [310, 320]; break;
						case 400: possibleJobs = [410, 420]; break;
						case 500: possibleJobs = [510, 520]; break;
					}
				}
			}
			if (possibleJobs.length == 0) {
				cm.sendOk("Your level is too low to advance.");
				cm.dispose();
			} else {
				var text = "There are the available jobs you can take#b";
				for (var i = 0; i < possibleJobs.length; i++)
					text += "\r\n#L"+i+"#"+cm.getJobById(possibleJobs[i])+"#l";
				cm.sendSimple(text);
			}
		} else if (status == 1) {
			cm.sendYesNo("Are you sure you want to job advance?");
			job = selection;
		} else if (status == 2) {
			cm.sendOk("Congratulations on your job advancement.");
			cm.getPlayer().setJob(possibleJobs[job]);
            cm.dispose();
		}
	}
}