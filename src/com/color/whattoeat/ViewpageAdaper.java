package com.color.whattoeat;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ViewpageAdaper extends FragmentPagerAdapter {
	private ArrayList<Fragment> list;
	
	public ViewpageAdaper(FragmentManager fm) {
		super(fm);
		list=new  ArrayList<Fragment>();
		ShakeFragment sf=new  ShakeFragment();
		list.add(sf);
		FoodListFragment ff=new FoodListFragment();
		list.add(ff);
	}
	
	public void refresh(){
		((FoodListFragment)list.get(1)).refresh();
	}
	public void setflag(boolean flag){
		((ShakeFragment)list.get(0)).flag=flag;
	}

	@Override
	public Fragment getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public int getCount() {
		return list.size();
	}

}
