package com.lakeel.altla.vision.nearby.data.mapper;

import com.lakeel.altla.cm.data.FavoritesPostData;
import com.lakeel.altla.cm.resource.Contact;
import com.lakeel.altla.cm.resource.Group;
import com.lakeel.altla.vision.nearby.presentation.presenter.data.CmFavoriteData;

import java.util.ArrayList;
import java.util.List;

public final class CmFavoritePostDataMapper {

    public FavoritesPostData map(CmFavoriteData apiData) {
        FavoritesPostData postData = new FavoritesPostData();

        postData.version = apiData.version;

        List<Contact> contacts = new ArrayList<>(apiData.contactIds.size());

        List<Group> groups = new ArrayList<>(apiData.groups.size());
        for (String groupName : apiData.groups) {
            Group group = new Group();
            group.name = groupName;
            groups.add(group);
        }

        for (String jid : apiData.contactIds) {
            Contact contact = new Contact();
            contact.jid = jid;
            contact.groups = groups;
            contacts.add(contact);
        }

        postData.contacts = contacts;

        return postData;
    }
}
