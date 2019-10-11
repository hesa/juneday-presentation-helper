package se.juneday.presentationhelper.ui.loudmouth;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import se.juneday.presentationhelper.R;

public class LoudMouthFragment extends Fragment {

    private LoudMouthViewModel mViewModel;

    public static LoudMouthFragment newInstance() {
        return new LoudMouthFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.loud_mouth_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(LoudMouthViewModel.class);
        // TODO: Use the ViewModel
    }

}
