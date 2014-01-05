package yeah.cstriker1407.android.rider.fragment;

import yeah.cstriker1407.android.rider.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class OffLineFragment extends Fragment 
{
	private FragmentTabHost tabHost;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) 
	{
		View rootView = inflater.inflate(R.layout.fragment_offlinemap, container,false); 
		
		tabHost = (FragmentTabHost)rootView.findViewById(android.R.id.tabhost);
		tabHost.setup(getActivity(), getChildFragmentManager(), R.id.relative_offlinemap);
		tabHost.addTab(tabHost.newTabSpec("downloadmanage").setIndicator(getText(R.string.download_manage)),OffLineDownloadManageFragment.class, null);
		tabHost.addTab(tabHost.newTabSpec("citylist").setIndicator(getText(R.string.city_list)),OffLineDownloadManageFragment.class, null);
		
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) 
	{
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		tabHost.clearAllTabs();
		tabHost = null;
	}
}
