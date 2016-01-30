package com.seventh_root.mummy_nose_best.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.seventh_root.mummy_nose_best.MummyNoseBest;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(800, 640);
        }

        @Override
        public ApplicationListener createApplicationListener () {
                return new MummyNoseBest();
        }
}