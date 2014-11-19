package com.color.whattoeat;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class MainActivity extends FragmentActivity {
	@ViewInject(R.id.title)
	private TextView tv;
	@ViewInject(R.id.viewpager)
	private ViewPager viewpager;
	@ViewInject(R.id.add_iv)
	private ImageView add_iv;
	
	private ViewpageAdaper adapter;
	
	private DbUtils dbUtils;
	public static ArrayList<Food> foodlist=new ArrayList<Food>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		ViewUtils.inject(this);
		
		dbUtils=DbUtils.create(this, "food.db");
		try {
			foodlist=(ArrayList<Food>) dbUtils.findAll(Food.class);
			if(foodlist==null){
				foodlist=new ArrayList<Food>();
			}
		} catch (DbException e) {
			e.printStackTrace();
			foodlist=new ArrayList<Food>();
		}
		
		viewpager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				if(0==arg0){
					tv.setText("摇一摇");
					add_iv.setVisibility(View.GONE);
					adapter.setflag(true);
				}else if(1==arg0){
					tv.setText("食物列表");
					add_iv.setVisibility(View.VISIBLE);
					adapter.setflag(false);
				}
			}
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
	
		adapter=new ViewpageAdaper(getSupportFragmentManager());
		viewpager.setAdapter(adapter);
		
	}
	
	
	@OnClick(R.id.add_iv)
	public void addClick(View v){
		mydialog();
	}


	private void mydialog(){
		View v=LayoutInflater.from(this).inflate(R.layout.dialog, null);
		final EditText et=(EditText) v.findViewById(R.id.add_et);
		
		final Dialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("食物名称")
                .setIcon(R.drawable.ic_launcher)
                .setView(v)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {                 
                    public void onClick(DialogInterface dialog, int which) { 
                    	String name=et.getEditableText().toString();
                    	if(null!=name&&!"".equals(name)){
                    		Food f=new Food();
                    		f.setName(name);
                    		try {
								dbUtils.save(f);
							} catch (DbException e) {
								e.printStackTrace();
							}
                    		refresh();
                    		
                    	}
                    } 
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() { 
                    public void onClick(DialogInterface dialog, int which) { 
                    	
                    } 
                })
                .create(); 
		LogUtils.i("dialog show");
		alertDialog.setCanceledOnTouchOutside(true);
		alertDialog.setCancelable(true);
        alertDialog.show(); 
	}
	
	private void refresh(){
		try {
			foodlist=(ArrayList<Food>) dbUtils.findAll(Food.class);
			if(foodlist==null){
				foodlist=new ArrayList<Food>();
			}
			adapter.refresh();
		} catch (DbException e) {
			e.printStackTrace();
			foodlist=new ArrayList<Food>();
		}
	}
	
	public void delete(int i){
		try {
			dbUtils.delete(foodlist.get(i));
			refresh();
		} catch (DbException e) {
			e.printStackTrace();
			LogUtils.i("delete failed "+i);
		}
	}


}
