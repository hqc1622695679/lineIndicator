package com.example.tangle;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hqc on 2017/2/24.
 */
public class EvaluationActivity  extends AppCompatActivity  implements View.OnClickListener{


    private FloatingActionButton floatingActionButton;

    private ListView contentListView;

    private ViewPager adViewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation);




        initViews();
    }

    /**
     *  about  ui
     */
    private void  initViews(){
       floatingActionButton= (FloatingActionButton) findViewById(R.id.fbtn);
       floatingActionButton.setOnClickListener(this);

        View headView=LayoutInflater.from(this).inflate(R.layout.header_ad,null);
//        CircleIndicator indicator= (CircleIndicator) headView.findViewById(R.id.indicator_default);
        LineIndicator indicator= (LineIndicator) headView.findViewById(R.id.line_indicator);

        adViewPager= (ViewPager) headView.findViewById(R.id.ad_viewpager);



        List<ImageView> images=new ArrayList<>();
        for(int i=0 ;i<4 ;i++){
            ImageView image = new ImageView(this);
            image.setBackgroundResource(R.mipmap.about);
            image.setPadding(40,40,40,40);

            images.add(image);

        }
        adViewPager.setOffscreenPageLimit(2);
        adViewPager.setAdapter(new MyPagerAdapter(this,images));


        contentListView= (ListView) findViewById(R.id.content_listview);
        contentListView.addHeaderView(headView);

        indicator.setViewPager(adViewPager);

        String[] ss=new String[]{"aaa","bbb","ccc","ddd"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ss);
        contentListView.setAdapter(adapter);

        }

    @Override
    public void onClick(View v) {

    }









    public  class  MyAdapter extends BaseAdapter{


        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return null;
        }
    }

    public class MyPagerAdapter extends PagerAdapter {

        private List<ImageView> items ;
        private Context mContext ;

        public MyPagerAdapter(Context context, List<ImageView> item){
            mContext = context ;
            items = item ;
        }

        @Override
        public int getCount() {
            return items == null ? 0 : items.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == (View)obj;
        }

        @Override
        public Object instantiateItem (ViewGroup container, int position) {
            ImageView image = items.get(position);
            container.addView(image, 0);
            return image;
        }

        @Override
        public void destroyItem (ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }
    }
}
