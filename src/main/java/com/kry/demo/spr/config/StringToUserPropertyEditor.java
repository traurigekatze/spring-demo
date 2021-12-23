package com.kry.demo.spr.config;

import com.kry.demo.spr.service.User;

import java.beans.PropertyEditorSupport;

public class StringToUserPropertyEditor extends PropertyEditorSupport {

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        User user = new User();
        user.setName(text);
        this.setValue(user);
    }
}
