package yeah.cstriker1407.android.hellofragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class BottomFragment extends Fragment {

private String text= " test";

public static BottomFragment newInstance(String str) {
	BottomFragment df = new BottomFragment();
    Bundle args = new Bundle();
    args.putString("str", str);
    df.setArguments(args);
    return df;
}
@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
    TextView tv = new TextView(getActivity());
    tv.setText(getArguments().getString("str"));
    return tv;
}

}
