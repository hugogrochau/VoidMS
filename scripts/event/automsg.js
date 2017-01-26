var setupTask;

function init() {
    scheduleNew();
}

function scheduleNew() {
    setupTask = em.scheduleAtFixedRate("start", 1000 * 60 * 10);
}

function cancelSchedule() {
    setupTask.cancel(true);
}

function start() {
    var Message = new Array(
    "Having a hard time training? Check out Shane in Henesys or use '@spawner' to warp to a variety of different boss warpers",
    "Did you know you can exchange your vote points for awesome prizes!? Just have a talk with Helena at Henesys, or simply click on the vote exchanger in '@all'",
    "You can use '@commands' to get a list of available commands",
    "Remember to vote every 12 hours. You don't only help the server get new players, but you can also get awesome rewards with your vote points",
    "You can use @go <mapname> or @spinel to quickly teleport around",
    "Several useful NPCs can be found at the Free Market and Henesys",
    "Don't be afraid to ask other players or staff members for help",
    "Keep an eye on the website and forums for new Notices",
    "You can use '@all' to open the All in One NPC",
    "Why not give the forum a look? Just go to voiddev.com/forum",
    "The Gachapon at the Free Market can give rare items.",
    "Got stuck and can't log in? Use the unstuck function on the site",
    "There are a variety of bosses in the FM rooms",
    "PVP with other players with the @pvp command.",
    "To buy Female NX, equip it in the CS, and choose 'Buy Equipped'. Or just buy nx items from '@shop'",
    "Want to know the staff team? Check out @stafflist!",
    "Have a suggestion for the server? Put it in the 'Suggestions' section on the forum!",
    "Have you visited our forums yet? It's at www.voiddev.com/forum! Check the forums daily for fun events in and out of game, important server notices, and to talk with members of the VoidDev community.",
    "You can visit the VoidMS Casino using @casino! Try out your luck",
    "You can earn jq points by finishing Jump Quests quickly. To start use '@jq'",
    "You can use your snail stamps to buy cool items from the items vendor, check out Wisp in Henesys or type '@all' and click on 'items vendor'"
);
    em.getChannelServer().broadcastPacket(tools.MaplePacketCreator.sendYellowTip("[Void Tip] " + Message[Math.floor(Math.random() * Message.length)]));
}