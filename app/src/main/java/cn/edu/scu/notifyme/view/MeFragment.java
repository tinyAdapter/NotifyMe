package cn.edu.scu.notifyme.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.edu.scu.notifyme.R;
import cn.edu.scu.notifyme.SettingsActivity;
import cn.edu.scu.notifyme.SignInSignUpActivity;
import cn.edu.scu.notifyme.WebViewActivity;

public class MeFragment extends Fragment {
    private Unbinder unbinder;

    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.tv_username)
    TextView tvUsername;
    @BindView(R.id.slli_settings)
    LinearLayout slliSettings;
    @BindView(R.id.slli_internal_browser)
    SingleLineListItem slliInternalBrowser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.iv_avatar, R.id.slli_settings, R.id.slli_internal_browser})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_avatar: {
                Intent intent = new Intent(getActivity(), SignInSignUpActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.slli_settings: {
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.slli_internal_browser: {
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                startActivity(intent);
                break;
            }
        }
    }
}
