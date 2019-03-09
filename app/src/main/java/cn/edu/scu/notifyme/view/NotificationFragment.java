package cn.edu.scu.notifyme.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.edu.scu.notifyme.DatabaseManager;
import cn.edu.scu.notifyme.R;
import cn.edu.scu.notifyme.adapter.MessageAdapter;
import cn.edu.scu.notifyme.model.Message;

public class NotificationFragment extends Fragment {

    @BindView(R.id.no_card_hint)
    RelativeLayout noCardHint;
    private List<Message> messages;
    private MessageAdapter adapter;


    @BindView(R.id.rv_messages)
    RecyclerView rvMessages;
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        unbinder = ButterKnife.bind(this, view);

        //test
//        Rule test_rule1 = new Rule();
//        Rule test_rule2 = new Rule();
//        Message test_msg1 = new Message();
//        Message test_msg2 = new Message();
//        List<Message> test;

//        test_rule1.setIconUrl("http://i1.hdslb.com/bfs/face/c49eef0123d8e51ee2e5fc249b11937dc970a8b9.jpg");
//        test_msg1.setRule(test_rule1);
//        test_msg1.setImgUrl("asdasdasdasd");
//        test_msg1.setTitle("test 1 title");
//        test_msg1.setUpdateTime(new Date());
//        test_msg1.setContent("test 1 content");
//        test_msg1.setTargetUrl("https://www.baidu.com/");
//
//        test_rule2.setIconUrl("asdasdasd");
//        test_msg2.setRule(test_rule2);
//        test_msg2.setImgUrl("http://i1.hdslb.com/bfs/archive/32fe5bce34d0f2f21a8399d801d5959be2c7d885.jpg");
//        test_msg2.setTitle("test 2 title");
//        test_msg2.setUpdateTime(new Date());
//        test_msg2.setContent("test 2 content");
//        test_msg2.setTargetUrl("https://www.bilibili.com/");
//
//        test = new ArrayList<Message>();
//        test.add(test_msg1);
//        test.add(test_msg2);


//        this.messages = test;
        this.messages = DatabaseManager.getInstance().getMessages();


        this.adapter = new MessageAdapter(R.layout.item_message_card, messages, this.getContext());
        this.adapter.openLoadAnimation();
        rvMessages.setLayoutManager(new LinearLayoutManager(this.getContext()));
        rvMessages.setAdapter(this.adapter);

        uiUpdateNoCardHint();

        return view;
    }

    private void uiUpdateNoCardHint() {
        if (this.messages.size() > 0) {
            noCardHint.setVisibility(View.INVISIBLE);
        } else {
            noCardHint.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
