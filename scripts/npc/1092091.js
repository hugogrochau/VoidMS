function start() {
    cm.sendSimple("#eWelcome to the All In One Shop!\n\rPlease choose a category#n#b" +
        "\r\n#L14000#Accessory" +
        "\r\n#L14001#Cap" +
        "\r\n#L14002#Cape" +
        "\r\n#L14003#Coat (tops)" +
        "\r\n#L14004#Face" +
        "\r\n#L14005#Glove" +
        "\r\n#L14006#Longcoat (overall)" +
        "\r\n#L14007#Pants" +
        "\r\n#L14008#PetEquip" +
        "\r\n#L14009#Ring" +
        "\r\n#L14010#Shield" +
        "\r\n#L14011Shoes" +
        "\r\n#L14012#TamingMob" +
        "\r\n#L14013#Weapon") ;
}

function action(mode, type, selection) {
    if (mode == 1) {
        cm.openShop(selection);
    }
    cm.dispose();
}
