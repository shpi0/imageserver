package ru.shpi0.imageserver.service;

import ru.shpi0.imageserver.model.Avatar;

public interface AvatarService {

    void save(Avatar avatar);

    Avatar getById(long id);

    Long getCount();

}
