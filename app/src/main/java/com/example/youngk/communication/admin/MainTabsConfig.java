package com.example.youngk.communication.admin;

/**
 * Created by Eun_A on 2017-07-31.
 */

import android.os.Bundle;

import com.example.youngk.communication.R;

/**
 * Created by YoungK on 2017-07-28.
 */

public final class MainTabsConfig {

    private static final TabInfo[] TABINFOS = {
            new TabInfo( "직원", R.drawable.ic_work, R.drawable.ic_work_selected, Tab1Fragment.class, null ),
            new TabInfo( "공지사항", R.drawable.ic_notice, R.drawable.ic_notice_selected, Tab2Fragment.class, null ),
            new TabInfo( "근무표", R.drawable.ic_calendar, R.drawable.ic_calendar_selected, Tab3Fragment.class, null )
    };

    public static final class TABINDEX {
        public static final int USERLIST = 0;
        public static final int CHANNELLIST = 1;
        public static final int SETTINGS = 2;

        public static final int FIRST = 0;
        public static final int LAST = TABINFOS.length;
    };

    public static final int COUNT_TABS() {
        return TABINFOS.length;
    }

    public static final TabInfo TABINFO( int index ) {
        return ( index < 0 || index >= COUNT_TABS() )  ? null : TABINFOS[ index ];
    }

    public static final class TabInfo {
        public final String tag;
        public final int drawableNormal;
        public final int drawableSelected;
        public final Class<?> klass;
        public final Bundle bundle;
        TabInfo( String tag, int drawableNormal, int drawableSelected, Class<?> klass, Bundle bundle ) {
            this.tag = tag;
            this.drawableNormal = drawableNormal;
            this.drawableSelected = drawableSelected;
            this.klass = klass;
            this.bundle = bundle;
        }
    }
}
