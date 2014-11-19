package com.color.whattoeat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.daimajia.swipe.implments.SwipeItemMangerImpl;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class FoodListFragment extends Fragment {
	@ViewInject(R.id.foodlist_lv)
	private ListView foodlist;
	private MyListAdapter adapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v=inflater.inflate(R.layout.foodlist_layout, null);
		ViewUtils.inject(this, v);
		
		adapter=new MyListAdapter();
		adapter.setMode(SwipeItemMangerImpl.Mode.Single);
		foodlist.setAdapter(adapter);
		return v;
	}
	
	public void refresh(){
		if(adapter!=null){
			adapter.notifyDataSetChanged();
		}
	}
	
	class MyListAdapter extends BaseSwipeAdapter{
		private LayoutInflater inflater;
		
		public MyListAdapter(){
			inflater=LayoutInflater.from(getActivity());
			
		}
		@Override
		public int getCount() {
			if(null!=MainActivity.foodlist){
				
				return MainActivity.foodlist.size();
			}else{
				LogUtils.i("MainActivity.list null");
				return 0;
			}
		}
		@Override
		public Object getItem(int position) {
			return MainActivity.foodlist.get(position);
		}
		@Override
		public long getItemId(int position) {
			return position;
		}
		
		@Override
		public int getSwipeLayoutResourceId(int position) {
			return R.id.swip;
		}
		
		@Override
		public View generateView(int position, ViewGroup parent) {
			View v=inflater.inflate(R.layout.food_item, null);
			final SwipeLayout swipeLayout = (SwipeLayout)v.findViewById(getSwipeLayoutResourceId(position));
			swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
			swipeLayout.setDragEdge(SwipeLayout.DragEdge.Right);
			final int p=position;
			TextView tv_delete=(TextView) swipeLayout.findViewById(R.id.delete);
			tv_delete.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					LogUtils.i("delete"+p);
					if(MainActivity.foodlist.size()>1){
						swipeLayout.close(true);
					}else{
						swipeLayout.close(false);
					}
					if(MainActivity.foodlist.size()>p){
						((MainActivity)getActivity()).delete(p);
					}
					
				}
			});
			
			return v;
		}
		
		@Override
		public void fillValues(int position, View convertView) {
			TextView tv=(TextView) convertView.findViewById(R.id.foodname_item_tv);
			tv.setText(MainActivity.foodlist.get(position).getName());
		}
	}
}
