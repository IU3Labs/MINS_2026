package ru.iu3.service;

import ru.iu3.entity.MeetingRoom;
import ru.iu3.entity.Workplace;
import ru.iu3.entity.interfaces.Room;
import ru.iu3.enums.RoomEnum;
import ru.iu3.service.interfaces.RoomFactory;

public class DefaultRoomFactory implements RoomFactory {

    @Override
    public Room create(RoomEnum type, int id, String name, int hourlyRate) {
        return switch (type) {
            case WORKPLACE -> new Workplace(id, name, hourlyRate);
            case MEETING_ROOM -> new MeetingRoom(id, name, hourlyRate);
        };
    }
}
