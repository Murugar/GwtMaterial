/*
 * #%L
 * GwtMaterial
 * %%
 * Copyright (C) 2015 - 2017 GwtMaterialDesign
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.iqmsoft.client.application;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import gwt.material.design.client.base.helper.ColorHelper;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.pwa.PwaManager;
import gwt.material.design.client.pwa.push.js.Notification;
import gwt.material.design.client.pwa.push.js.NotificationOptions;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialContainer;
import gwt.material.design.client.ui.MaterialNavBar;
import gwt.material.design.client.ui.MaterialToast;

public class ApplicationView extends ViewImpl implements ApplicationPresenter.MyView {
    interface Binder extends UiBinder<Widget, ApplicationView> {
    }

    @UiField
    MaterialContainer container;

    @UiField
    MaterialButton btnAdd;

    @UiField
    MaterialNavBar navBar;

    @Inject
    ApplicationView(Binder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    protected void onAttach() {
        super.onAttach();

        if (PwaManager.isPwaSupported()) {
            PwaManager.getInstance()
                    .setServiceWorker(new AppServiceWorker())
                    .setThemeColor(ColorHelper.setupComputedBackgroundColor(Color.BLUE_DARKEN_3))
                    .setWebManifest("manifest.url")
                    .load();

            // Will request a notification
            Notification.requestPermission(status -> MaterialToast.fireToast("Permission Status: " + status));
        }

        Document.get().getElementById("splashscreen").removeFromParent();
    }

    @UiHandler("notify")
    void notify(ClickEvent e) {
        if (Notification.getPermission().equals("granted")) {
            NotificationOptions options = new NotificationOptions();
            options.body = "I love GMD";
            options.icon = "https://user.oc-static.com/upload/2017/05/03/14938342186053_01-duration-and-easing.png";
            // Will show the Notification provided by NotificationOptions
            Notification notification = new Notification("GMD Says", options);
            // Listen to any Notification events
            notification.setOnclick(param1 -> MaterialToast.fireToast("Clicked"));
            notification.setOnclose(param1 -> MaterialToast.fireToast("Closed"));
            notification.setOnerror(param1 -> MaterialToast.fireToast("Error"));
            notification.setOnshow(param1 -> MaterialToast.fireToast("Shown"));
        } else {
            MaterialToast.fireToast("Permission Denied. Update it thru the browser setting");
        }
    }

    @UiHandler("serviceWorker")
    void serviceWorker(ClickEvent e) {
        MaterialToast.fireToast(PwaManager.getInstance().getServiceWorkerManager().getServiceWorker().state);
    }

    @UiHandler("btnAdd")
    void onAdd(ClickEvent e) {
        MaterialToast.fireToast("I love GMD");
    }
}
