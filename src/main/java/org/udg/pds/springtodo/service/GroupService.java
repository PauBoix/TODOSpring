package org.udg.pds.springtodo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.udg.pds.springtodo.controller.exceptions.ServiceException;
import org.udg.pds.springtodo.entity.IdObject;
import org.udg.pds.springtodo.entity.Group;
import org.udg.pds.springtodo.entity.User;
import org.udg.pds.springtodo.repository.GroupRepository;

import java.util.Collection;
import java.util.Date;
import java.util.Optional;

@Service
public class GroupService {

    @Autowired
    GroupRepository GroupRepository;

    @Autowired
    UserService userService;

    public GroupRepository crud() {
        return GroupRepository;
    }

    @Transactional
    public IdObject addGroup(String name, String description,Long userId) {
        try {
            User user = userService.getUser(userId);

            Group group = new Group(name, description);

            group.setOwner(user);

            user.addGroup(group);

            GroupRepository.save(group);
            return new IdObject(group.getId());
        } catch (Exception ex) {
            // Very important: if you want that an exception reaches the EJB caller, you have to throw an ServiceException
            // We catch the normal exception and then transform it in a ServiceException
            throw new ServiceException(ex.getMessage());
        }
    }

    public Collection<Group> getGroups(Long id) {
        return userService.getUser(id).getGroups();
    }

    public Collection<Group> getMembership(Long userId) {
        User user = userService.getUser(userId);
        return user.getMembership();
    }

    @Transactional
    public IdObject addMember(Long ownerId, Long userId, Long groupId) {
        try {
            User user = userService.getUser(userId);

            Group group = this.getOwnedGroup(ownerId, groupId);

            group.addMember(user);

            user.addMembership(group);

            GroupRepository.save(group);
            return new IdObject(group.getId());
        } catch (Exception ex) {
            // Very important: if you want that an exception reaches the EJB caller, you have to throw an ServiceException
            // We catch the normal exception and then transform it in a ServiceException
            throw new ServiceException(ex.getMessage());
        }
    }

    public Group getOwnedGroup(Long userId, Long id) {
        Optional<Group> g = GroupRepository.findById(id);
        if (!g.isPresent())
            throw new ServiceException("Group doesn't exist");
        if (g.get().getOwner().getId() != userId)
            throw new ServiceException("Group not owned");
        return g.get();
    }

}
