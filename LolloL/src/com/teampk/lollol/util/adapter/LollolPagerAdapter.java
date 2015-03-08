package com.teampk.lollol.util.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import com.teampk.lollol.EmptyFragment;
import com.teampk.lollol.FindFragment;
import com.teampk.lollol.MatchedFragment;
import com.teampk.lollol.RequestFragment;

public class LollolPagerAdapter extends FragmentStatePagerAdapter{
	
	public String jsonFindResult;
	public String jsonRequestResult;
	public String jsonMatchedResult;
	public boolean isFindEmpty;
	public boolean isRequestEmpty;
	public boolean isMatchedEmpty;
	public boolean isLimit;
	
	public LollolPagerAdapter(FragmentManager manager){
		super(manager);
	}
	
	@Override
	public Fragment getItem(int i) {
		switch(i){
			
			case 0 : 
				
				if(isFindEmpty){
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("isEmpty", true);
                    bundle.putInt("page", 0);

                    EmptyFragment empty = new EmptyFragment();
                    empty.setArguments(bundle);
					return empty;

				} else if(isLimit){
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("isEmpty", true);
                    bundle.putInt("page", 3);

                    EmptyFragment empty = new EmptyFragment();
                    empty.setArguments(bundle);
                    return empty;

				} else {
                    Bundle bundle = new Bundle();
                    bundle.putString("jsonString", jsonFindResult);

                    FindFragment findFragment = new FindFragment();
                    findFragment.setArguments(bundle);
                    return findFragment;
				}
			
			case 1 : 
				
				if(isRequestEmpty){
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("isEmpty", true);
                    bundle.putInt("page", 1);

                    EmptyFragment empty = new EmptyFragment();
                    empty.setArguments(bundle);
                    return empty;

				} else if(isLimit){
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("isEmpty", true);
                    bundle.putInt("page", 3);

                    EmptyFragment empty = new EmptyFragment();
                    empty.setArguments(bundle);
                    return empty;

                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("jsonString", jsonRequestResult);

                    RequestFragment requestFragment = new RequestFragment();
                    requestFragment.setArguments(bundle);
                    return requestFragment;
				}
				
			case 2 : 
				
				if(isMatchedEmpty){
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("isEmpty", true);
                    bundle.putInt("page", 2);

                    EmptyFragment empty = new EmptyFragment();
                    empty.setArguments(bundle);
                    return empty;
				} else {
                    Bundle bundle = new Bundle();
                    bundle.putString("jsonString", jsonMatchedResult);

                    MatchedFragment matchedFragment = new MatchedFragment();
                    matchedFragment.setArguments(bundle);
                    return matchedFragment;
				}
			
			default : return null;
		}
	}

	@Override
	public int getCount() {
		return 3;
	}
	
	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}
	
	@Override
	public int getItemPosition(Object object) {
		return PagerAdapter.POSITION_NONE;
	}

	public void setJsonResult(String result, int type) {
		switch(type){
		case(0) : 
			jsonFindResult = result;
			isFindEmpty = false;
			break;
		
		case(1) : 
			jsonRequestResult = result;
			isRequestEmpty = false;
			break;
		
		case(2) : 
			jsonMatchedResult = result;
			isMatchedEmpty = false;
			break;
		
		default : break;
		}
	}
	
	public void setEmptyPage(int type){
		switch(type){
		case(0) : 
			isFindEmpty = true;
			break;
		case(1) : 
			isRequestEmpty = true;
			break;
		case(2) : 
			isMatchedEmpty = true;
			break;
			
		default : break;
		}
	}
	
	public void setErrorPage(int type){
		switch(type){
		case(0) : 
			isLimit = true;
			break;
		default : break;
		}
	}
}
