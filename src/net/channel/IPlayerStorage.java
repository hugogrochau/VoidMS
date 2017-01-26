package net.channel;

import java.util.Collection;
import client.MapleCharacter;

public interface IPlayerStorage {

    public MapleCharacter getCharacterByName(String name);

    public MapleCharacter getCharacterById(int id);

    Collection<MapleCharacter> getAllCharacters();
}