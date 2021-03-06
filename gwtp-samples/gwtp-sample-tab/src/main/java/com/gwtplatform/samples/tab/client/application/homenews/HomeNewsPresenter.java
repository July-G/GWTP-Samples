/**
 * Copyright 2011 ArcBees Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.gwtplatform.samples.tab.client.application.homenews;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.TabData;
import com.gwtplatform.mvp.client.TabDataBasic;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.TabInfo;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.TabContentProxyPlace;
import com.gwtplatform.samples.tab.client.application.ApplicationPresenter;
import com.gwtplatform.samples.tab.client.application.home.HomePresenter;
import com.gwtplatform.samples.tab.client.place.NameTokens;
import com.gwtplatform.samples.tab.client.resources.AppConstants;

/**
 * A sample {@link Presenter} filled with arbitrary content. It appears as a tab within {@link HomePresenter}, which is
 * itself a s tab in {@link ApplicationPresenter}.
 * <p/>
 * It demonstrates the option 3 described in {@link TabInfo}.
 */
public class HomeNewsPresenter extends Presenter<HomeNewsPresenter.MyView, HomeNewsPresenter.MyProxy> implements
        HomeNewsUiHandler {
    /**
     * {@link HomeNewsPresenter}'s proxy.
     */
    @ProxyCodeSplit
    @NameToken(NameTokens.homeNewsPage)
    public interface MyProxy extends TabContentProxyPlace<HomeNewsPresenter> {
    }

    @TabInfo(container = HomePresenter.class)
    static TabData getTabLabel(AppConstants constants) {
        // Priority = 0, means it will be the left-most tab in the home tab
        return new TabDataBasic(constants.news(), 0);
    }

    /**
     * {@link HomeNewsPresenter}'s view.
     */
    public interface MyView extends View, HasUiHandlers<HomeNewsUiHandler> {
        void setConfirmationText(String text);

        void display();
    }

    private final PlaceManager placeManager;

    private boolean confirmationEnabled;

    @Inject
    HomeNewsPresenter(EventBus eventBus,
                      MyView view,
                      MyProxy proxy,
                      PlaceManager placeManager) {
        super(eventBus, view, proxy, HomePresenter.TYPE_SetTabContent);

        this.placeManager = placeManager;

        getView().setUiHandlers(this);
    }

    @Override
    public void onReveal() {
        enableConfirmation(false);

        getView().display();
    }

    /**
     * Toggles the state of the confirmation dialog.
     */
    @Override
    public void toggleConfirmation() {
        enableConfirmation(!confirmationEnabled);
    }

    /**
     * Enables or disables the confirmation dialog.
     *
     * @param enabled {@code true} to enable the confirmation dialog, {@code false} to disable it.
     */
    private void enableConfirmation(boolean enabled) {
        this.confirmationEnabled = enabled;
        if (enabled) {
            placeManager.setOnLeaveConfirmation("Are you sure you want to navigate away from this page?");
            getView().setConfirmationText("Navigation confirmation ON, click here to disable it!");
        } else {
            placeManager.setOnLeaveConfirmation(null);
            getView().setConfirmationText("Navigation confirmation OFF, click here to enable it!");
        }
    }
}
