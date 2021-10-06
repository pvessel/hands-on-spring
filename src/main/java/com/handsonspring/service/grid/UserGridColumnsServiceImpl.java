package com.handsonspring.service.grid;

import com.handsonspring.model.grid.GridEntries;
import com.handsonspring.model.User;
import com.handsonspring.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
public class UserGridColumnsServiceImpl extends GridColumnsService<User> {

    private final UserServiceImpl userService;

    @Autowired
    public UserGridColumnsServiceImpl(UserServiceImpl userService) {
        this.userService = userService;
    }

    @Override
    protected User getNewEntity() {
        return new User();
    }

    @Override
    protected User findEntityById(UUID id) {
        return userService.findById(id);
    }

    @Override
    protected void saveEntity(User entity) {
        userService.save(entity);
    }

    @Override
    public List<Object> getGridValues(List<User> entities) {
        List<Object> data = new ArrayList<Object>();

        for (User entity : entities) {
            List<Object> userData = new ArrayList<Object>();
            userData.add(entity.getId());
            userData.add(entity.getUsername());
            userData.add(entity.getEmail());
            userData.add(""); // we don't want to send any passwords
            userData.add(entity.getRole().getName());

            data.add(userData);
        }
        return data;
    }

    @Override
    public void saveEntities(GridEntries entries) {
        for (Object[] data : entries.getData()) {
            if(null == data) {
                continue;
            }
            User entity = getEntity(data[0]);

            entity.setUsername((String) data[1]);
            entity.setEmail((String) data[2]);
            String password = (String) data[3];
            if(!password.isEmpty()){
                entity.setPassword(password);
            }
            entity.setRoleAsString((String) data[4]);

            saveEntity(entity);
        }
    }
}
