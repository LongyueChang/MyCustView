package com.example.longyue.mycustview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.util.Log;
import android.util.SparseIntArray;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import IndexSider.CustIndex;
import IndexSider.IndexSiderBar;

public class MainActivity extends AppCompatActivity {
    private TextView index_info;
    private IndexSiderBar siderBar;
    private ListView mListView;
    private List<String> datas=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        index_info= (TextView) findViewById(R.id.index_info);
        siderBar= (IndexSiderBar) findViewById(R.id.main_index_barview);
        mListView= (ListView) findViewById(R.id.main_listView);


        for (int i = 0; i < siderBar.b.length; i++) {
            datas.add(siderBar.b[i]+((i+1)%2));
            datas.add(siderBar.b[i]+((i+2)%2));
        }

        final CustIndex custIndex = new CustIndex(IndexSiderBar.b, datas);

        mListView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,datas));

        siderBar.setTextInfo(index_info);
        siderBar.setChoceInterFace(new IndexSiderBar.ChoceInterFace() {
            @Override
            public void onChose(int chose, String info) {
                Log.i("TAG------------>", "chose:"+chose+",info："+info);
                int selection = custIndex.getPositionForSection(chose);
                Log.i("TAG------------>", "获取的位置:"+selection);
                mListView.setSelection(selection);
            }
        });
    }
}
