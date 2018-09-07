package com.example.youngk.communication.admin;

/**
 * Created by Eun_A on 2017-07-31.
 */


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.example.youngk.communication.R;

/**
 * Created by YoungK on 2017-07-27.
 */

public class MainTabViewPagerAdapter
        extends FragmentStatePagerAdapter
        implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener{
    /* private Context context;
     private ViewPager viewPager;
     private TabHost tabHost;



     public MainTabViewPagerAdapter(
             Context context,
             TabHost tabHost,
             ViewPager viewPager ) {

         super( ( (FragmentActivity) context ).getSupportFragmentManager() );

         this.context = context;
         this.tabHost = tabHost;
         this.viewPager = viewPager;

         // ViewPager 에 Adapter(this) 등록
         this.viewPager.setAdapter( this );

         // TabHost 초기화
         this.tabHost.setup();

         // 탭 위젯안의 탭 버튼의뷰 초기화
         for( int i = 0; i <3; i++ ) {
             View view = LayoutInflater.from( this.context ).inflate( R.layout.tab_background, null );

             TabHost.TabSpec tabSpec = this.tabHost.newTabSpec( "탭1" ).setIndicator( view );
             tabSpec.setContent( new DummyTabFactory( this.context ) );

             this.tabHost.addTab( tabSpec );


             ImageView imageView = (ImageView)view.findViewById( R.id.tab_image );
             imageView.setVisibility( View.VISIBLE );
             imageView.setImageDrawable( this.context.getResources().getDrawable( R.drawable.ic_online ) );
         }

         this.tabHost.setOnTabChangedListener( this );
         this.viewPager.setOnPageChangeListener( this );
     }


     @Override
     public Fragment getItem(int position) {
         Fragment fragment = null;
         if( position == 0 ) {
             fragment = Fragment.instantiate( context, Tab1Fragment.class.getName(), null  );
         } else if( position == 1 ) {
             fragment = Fragment.instantiate( context, Tab2Fragment.class.getName(), null  );
         }else if( position == 2 ) {
             fragment = Fragment.instantiate(context, Tab3Fragment.class.getName(), null);
         }else {
             Log.e( "MainTabViewPagerAdapter",
                     "Item Position:" + position );
         }

         return fragment;
     }

     @Override
     public int getCount() {
         return 3;
     }

     @Override
     public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
     }

     @Override
     public void onPageScrollStateChanged(int state) {
     }

     @Override
     public void onTabChanged(String tabId) {
         int index = tabHost.getCurrentTab();
         viewPager.setCurrentItem( index );
     }

     @Override
     public void onPageSelected(int position) {
         TabWidget widget = tabHost.getTabWidget();
         int oldFocusability = widget.getDescendantFocusability();

         widget.setDescendantFocusability( ViewGroup.FOCUS_BLOCK_DESCENDANTS );
         tabHost.setCurrentTab( position );
         widget.setDescendantFocusability( oldFocusability );
     }

     private static class DummyTabFactory implements TabHost.TabContentFactory {
         private final Context context;

         public DummyTabFactory( Context context ) {
             this.context = context;
         }

         @Override
         public View createTabContent( String tag ) {
             View v = new View( context );
             v.setMinimumWidth( 0 );
             v.setMinimumHeight( 0 );
             return v;
         }
     };*/
    private final Context context;
    private final TabHost tabHost;
    private final ViewPager viewPager;

    private View currentTabView;
    private int currentIndex;

    public MainTabViewPagerAdapter( Context context, TabHost tabHost, ViewPager viewPager ) {

        super( ( ( FragmentActivity ) context ).getSupportFragmentManager() );

        currentTabView = null;
        currentIndex = -1;

        this.context = context;
        this.tabHost = tabHost;
        this.viewPager = viewPager;

        this.tabHost.setup();
        this.tabHost.getTabWidget().setDividerDrawable( null );

        // 페이지 사이의 간격
        // this.viewPager.setPageMargin(20);
        // viewPager.setPageMarginDrawable( R.color.bg_color_fragment_page );

        this.viewPager.setAdapter( this );

        this.tabHost.setOnTabChangedListener( this );
        this.viewPager.setOnPageChangeListener( this );

        // init
        int count = MainTabsConfig.COUNT_TABS();
        for( int i = 0; i < count; i++ ) {
            MainTabsConfig.TabInfo tabInfo = MainTabsConfig.TABINFO( i );

            View view = LayoutInflater.from( this.context ).inflate( R.layout.tab_background, null );
            TabHost.TabSpec tabSpec = tabHost.newTabSpec( tabInfo.tag ).setIndicator( view );
            tabSpec.setContent( new DummyTabFactory( this.context ) );

            this.tabHost.addTab( tabSpec );

            if( tabInfo.drawableNormal != 0  && tabInfo.drawableSelected != 0 ) {
                ImageView imageView = ( ImageView ) view.findViewById( R.id.tab_image );
                imageView.setVisibility( View.VISIBLE );
                imageView.setImageDrawable(  this.context.getResources().getDrawable( tabInfo.drawableNormal ) );
            } else {
                TextView textView = ( TextView ) view.findViewById( R.id.tab_text );
                textView.setVisibility( View.VISIBLE );
                textView.setText( tabInfo.tag );
            }

            notifyDataSetChanged();
        }
    }

    public void selectTab( int position ) {
        tabHost.setCurrentTab( position );
    }

    @Override
    public int getItemPosition(Object object){
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public int getCount() {
        return MainTabsConfig.COUNT_TABS();
    }

    @Override
    public Fragment getItem( int index ) {
        MainTabsConfig.TabInfo tabInfo = MainTabsConfig.TABINFO( index );
        return Fragment.instantiate( context, tabInfo.klass.getName(), tabInfo.bundle );
    }


    @Override
    public void destroyItem( ViewGroup container, int position, Object object ) {
        // onCreate의 반복적 호출 조절
        // super.destroyItem( container, position, object );
    }

    @Override
    public void onTabChanged( String tabId ) {
        MainTabsConfig.TabInfo tabInfo;

        // previous tab
        if( currentTabView != null && currentIndex >= 0 ) {
            tabInfo = MainTabsConfig.TABINFO( currentIndex );
            if( tabInfo.drawableNormal != 0  && tabInfo.drawableSelected != 0 ) {
                ( ( ImageView ) currentTabView.findViewById( R.id.tab_image ) ).setImageDrawable(  context.getResources().getDrawable( tabInfo.drawableNormal ) );
            }
        }

        // currentTab
        currentIndex = tabHost.getCurrentTab();
        Log.d("현재 탭", currentIndex+"");
        currentTabView = tabHost.getCurrentTabView();

        tabInfo = MainTabsConfig.TABINFO( currentIndex );
        if( tabInfo.drawableNormal != 0  && tabInfo.drawableSelected != 0 ) {
            ( ( ImageView ) currentTabView.findViewById( R.id.tab_image ) ).setImageDrawable(  context.getResources().getDrawable( tabInfo.drawableSelected ) );
        }

        // pager and title
        viewPager.setCurrentItem( currentIndex );
        //( (Activity) context ).setTitle( context.getResources().getString( R.string.app_name ) + " - " + tabInfo.tag );
    }

    @Override
    public void onPageSelected( int position ) {
        TabWidget widget = tabHost.getTabWidget();
        int oldFocusability = widget.getDescendantFocusability();

        widget.setDescendantFocusability( ViewGroup.FOCUS_BLOCK_DESCENDANTS );
        tabHost.setCurrentTab( position );
        widget.setDescendantFocusability( oldFocusability );

        if( position == 0 ) {
        }
    }

    @Override
    public void onPageScrollStateChanged( int arg0 ) {
    }

    @Override
    public void onPageScrolled( int arg0, float arg1, int arg2 ) {
    }

    private static class DummyTabFactory implements TabHost.TabContentFactory {
        private final Context context;

        public DummyTabFactory( Context context ) {
            this.context = context;
        }

        @Override
        public View createTabContent( String tag ) {
            View v = new View( context );
            v.setMinimumWidth( 0 );
            v.setMinimumHeight( 0 );
            return v;
        }
    };
}
