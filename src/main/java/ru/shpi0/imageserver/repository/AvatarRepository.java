package ru.shpi0.imageserver.repository;

import ru.shpi0.imageserver.model.Avatar;

public interface AvatarRepository extends CommonRepository<Avatar> {

    Avatar getById(long id);

    Long getCount();

}
