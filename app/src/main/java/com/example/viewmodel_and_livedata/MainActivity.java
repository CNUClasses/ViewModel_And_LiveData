package com.example.viewmodel_and_livedata;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.viewmodel_and_livedata.DataVM;

public class MainActivity extends AppCompatActivity {
    private static final int P_BAR_MAX = 100;
    private Integer myInt=100;
    private TextView tv;
    private Button butStart;
    private Button butCancel;
    ProgressBar pBar;

    //persists accross config changes
    DataVM myVM;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = (TextView) findViewById(R.id.textView2);
        butStart = (Button) findViewById(R.id.bStart);
        butCancel = (Button) findViewById(R.id.bCancel);
        pBar = (ProgressBar) findViewById(R.id.progressBar1);
        pBar.setMax(P_BAR_MAX);

        // Create a ViewModel the first time the system calls an activity's
        // onCreate() method.  Re-created activities receive the same
        // MyViewModel instance created by the first activity.
        myVM = new ViewModelProvider(this).get(DataVM.class);
        //if we have a thread running then attach this activity
//        if (myVM.myTask != null) {
//            myVM.myTask.set(new WeakReference<MainActivity>(this));

        //a thread is running have the UI show that
//            setUIState(false);
//    }

        // Create the observer which updates the UI.
        final Observer<String> textObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String newText) {
                // Update the UI, in this case, a TextView.
                tv.setText(newText);
            }
        };

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        myVM.getCurrentText().observe(this, textObserver);

        // Create the observer which updates the UI.
        final Observer<Integer> pbarObserver = new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable final Integer newInt) {
                // Update the UI, in this case, a TextView.
                pBar.setProgress(newInt);
            }
        };
        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        myVM.getCurrentProgress().observe(this, pbarObserver);

        // Create the observer which updates the UI.
        final Observer<Boolean> pbutObserver = new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable final Boolean newBoolean) {
                // Update the UI, in this case, a TextView.
                butStart.setEnabled(newBoolean);
                butCancel.setEnabled(!newBoolean);
            }
        };
        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        myVM.getCurrentbutState().observe(this, pbutObserver);
    }

    public void doCancel(View view) {
        //try to cancel thread
        if (myVM.myTask != null) {
            myVM.myTask.cancel(true);
        }
    }

    public void doStart(View view) {
        myVM.myTask = myVM.new AddTask();
        myVM.myTask.execute(myInt);
    }

    public void setUIState(boolean b){

        setUIState(b, null);
    }
    public void setUIState(boolean b, String s) {
        butStart.setEnabled(b);
        butCancel.setEnabled(!b);
        if(s != null)
            tv.setText(s);
        pBar.setProgress(0);
    }
}
