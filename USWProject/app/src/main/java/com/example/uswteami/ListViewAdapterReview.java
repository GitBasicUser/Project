package com.example.uswteami;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class ListViewAdapterReview extends BaseAdapter {
    private TextView content;
    private TextView date;
    private TextView grade;

    private ArrayList<ListViewItem> listViewItemList = new ArrayList<>();

    public ListViewAdapterReview(){
    }


    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_list_review, parent, false);
        }

        grade = (TextView)convertView.findViewById(R.id.date);
        date = (TextView)convertView.findViewById(R.id.grade);
        content = (TextView)convertView.findViewById(R.id.content);

        ListViewItem listViewItem = listViewItemList.get(position);

        date.setText(listViewItem.getT1());
        grade.setText(listViewItem.getT2());
        content.setText(listViewItem.getT3());


        return convertView;
    }

    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public String getName(int position){
        return listViewItemList.get(position).getT3();
    }

    public void addItem(String t1, String t2, String t3){
        ListViewItem item = new ListViewItem();

        item.setT1(t1);
        item.setT2(t2);
        item.setT3(t3);

        listViewItemList.add(item);
    }
}
