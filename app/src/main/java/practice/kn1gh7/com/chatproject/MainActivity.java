package practice.kn1gh7.com.chatproject;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import java.lang.ref.WeakReference;
import java.util.List;

import practice.kn1gh7.com.chatproject.model.MessageModel;
import practice.kn1gh7.com.chatproject.model.UserModelCount;
import practice.kn1gh7.com.chatproject.presenter.TabsPresenterImpl;
import practice.kn1gh7.com.chatproject.utility.LogHelper;
import practice.kn1gh7.com.chatproject.view.adapter.ConversationsAdapter;

public class MainActivity extends AppCompatActivity {
    private static final int INTERNET_PERMISSION_REQUEST_CODE = 10;
    private static final String TAG = MainActivity.class.getSimpleName();
    private ViewPager conversationPager;
    TabLayout conversationTabsLayout;
    ProgressBar progressBar;
    private LogHelper logHelper;
    private TabsPresenterImpl presenter;
    private ConversationsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initAdapter();
        otherSetups();
        initPermissions();
    }

    private void otherSetups() {
        logHelper = new LogHelper(TAG);
        presenter = new TabsPresenterImpl(new WeakReference<MainActivity>(this));
    }

    private void initViews() {
        conversationPager = (ViewPager) findViewById(R.id.conversation_pager);
        conversationTabsLayout = (TabLayout) findViewById(R.id.tab_layout);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
    }

    private void initAdapter() {
        adapter = new ConversationsAdapter(this, getSupportFragmentManager());
        conversationPager.setAdapter(adapter);

        conversationTabsLayout.setupWithViewPager(conversationPager);
    }

    private void initPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED
                ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE},
                    INTERNET_PERMISSION_REQUEST_CODE);
        } else {
            presenter.fetchUsersFromDB();
        }
    }

    public void showSnackbarMessage(String msg) {
        Snackbar.make(conversationPager, msg, Snackbar.LENGTH_SHORT).show();
    }

    public void setInsertSuccessful() {
        initAdapter();
    }

    public void showResult(List<UserModelCount> usersList) {
        if (usersList == null) {
            presenter.fetchConversationsFromServer();
        } else {
            logHelper.showLog(usersList.size() + "");
            progressBar.setVisibility(View.GONE);
            conversationPager.setVisibility(View.VISIBLE);
            conversationTabsLayout.setVisibility(View.VISIBLE);
        }
    }
}