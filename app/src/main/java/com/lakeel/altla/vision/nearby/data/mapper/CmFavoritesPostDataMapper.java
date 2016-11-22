package com.lakeel.altla.vision.nearby.data.mapper;

import com.lakeel.altla.cm.data.FavoritesPostData;
import com.lakeel.altla.cm.resource.Contact;
import com.lakeel.altla.cm.resource.Group;
import com.lakeel.altla.vision.nearby.domain.model.CmFavoritesData;

import java.util.ArrayList;
import java.util.List;

public final class CmFavoritesPostDataMapper {

    public FavoritesPostData map(CmFavoritesData apiData) {
        FavoritesPostData postData = new FavoritesPostData();

        postData.version = apiData.mVersion;

        List<Contact> contacts = new ArrayList<>(apiData.mContactIds.size());

        List<Group> groups = new ArrayList<>(apiData.mGroups.size());
        for (String groupName : apiData.mGroups) {
            Group group = new Group();
            group.name = groupName;
            groups.add(group);
        }

        for (String jid : apiData.mContactIds) {
            Contact contact = new Contact();
            contact.jid = jid;
            contact.groups = groups;
            contacts.add(contact);
        }

        postData.contacts = contacts;

        return postData;
    }
}
