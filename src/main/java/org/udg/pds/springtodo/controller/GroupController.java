package org.udg.pds.springtodo.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.udg.pds.springtodo.entity.Group;
import org.udg.pds.springtodo.entity.User;
import org.udg.pds.springtodo.entity.Views;
import org.udg.pds.springtodo.service.GroupService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@RequestMapping(path="/groups")
@RestController
public class GroupController extends BaseController{
    @Autowired
    GroupService groupService;

    @PostMapping(consumes = "application/json")
    public String addGroup(HttpSession session, @Valid @RequestBody GroupController.R_Group group) {

        Long userId = getLoggedUser(session);

        groupService.addGroup(group.name, group.description, userId);
        return BaseController.OK_MESSAGE;
    }

    @GetMapping
    @JsonView(Views.Private.class)
    public Collection<Group> listAllOwnedGroups(HttpSession session) {
        Long userId = getLoggedUser(session);

        return groupService.getGroups(userId);
    }

    @GetMapping(path="/membership")
    @JsonView(Views.Complete.class)
    public Collection<Group> listGroupMembers(HttpSession session) {
        Long userId = getLoggedUser(session);
        return groupService.getMembership(userId);
    }

    @PostMapping(path="/{gid}/members/{uid}")
    public String addMember(HttpSession session,
                            @PathVariable("gid") Long groupId, @PathVariable("uid") Long userId) {

        Long ownerId = getLoggedUser(session);
        groupService.addMember(ownerId, userId, groupId);
        return BaseController.OK_MESSAGE;
    }

    static class R_Group {

        @NotNull
        public String name;

        @NotNull
        public String description;
    }
}
