package practice.kn1gh7.com.chatproject.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.util.List;

import practice.kn1gh7.com.chatproject.MainActivity;
import practice.kn1gh7.com.chatproject.R;
import practice.kn1gh7.com.chatproject.model.UserModelCount;
import practice.kn1gh7.com.chatproject.presenter.TabsPresenterImpl;
import practice.kn1gh7.com.chatproject.presenter.UsersCountPresenterImpl;
import practice.kn1gh7.com.chatproject.view.adapter.UsersAdapter;

/**
 * Created by kn1gh7 on 17/9/16.
 */
public class ConversationFragment extends Fragment implements UsersCountPresenterImpl.UserCountPresenterOps {
    public static final String FRAGMENT_TYPE = "fragment_type";
    public static final String FRAGMENT_TYPE_USER = "fragment_type_user";
    public static final String FRAGMENT_TYPE_COUNT = "fragment_type_count";
    private UsersCountPresenterImpl presenter;
    private RecyclerView rv;
    String fragmentType;

    public static ConversationFragment newInstance(int position) {
        ConversationFragment fragment = new ConversationFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ConversationFragment.FRAGMENT_TYPE,
                position == 0 ? ConversationFragment.FRAGMENT_TYPE_USER : ConversationFragment.FRAGMENT_TYPE_COUNT);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        presenter = new UsersCountPresenterImpl(this);
        View view = inflater.inflate(R.layout.conversation_layout, container, false);
        initViews(view);

        fragmentType = getArguments().getString(FRAGMENT_TYPE);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.fetchUsersWithCountFromDB();
    }

    private void initViews(View view) {
        rv = (RecyclerView) view.findViewById(R.id.conversation_recyclerview);
        rv.setLayoutManager(new LinearLayoutManager(this.getActivity()));
    }

    @Override
    public void setResult(List<UserModelCount> userModelList) {
        if (fragmentType.equals(FRAGMENT_TYPE_USER)) {
            rv.setAdapter(new UsersAdapter(this.getActivity(), userModelList));
        } else if (fragmentType.equals(FRAGMENT_TYPE_COUNT)) {
            rv.setAdapter(new UsersAdapter(this.getActivity(), userModelList));
        } else {
            throw new RuntimeException("Unsupported Fragment Type");
        }

    }
}
