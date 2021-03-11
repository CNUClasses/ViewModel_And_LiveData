package com.example.viewmodel_and_livedata;

import android.os.AsyncTask;
import android.os.SystemClock;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class DataVM extends ViewModel {
    AddTask myTask;

//    // Create LiveData
//    private MutableLiveData<List<String>> fruitList;
//    LiveData<List<String>> getFruitList() {
//        if (fruitList == null) {
//            fruitList = new MutableLiveData<>();
//            loadFruits();
//        }
//        return fruitList;
//    }
//    private void loadFruits() {
//        // do async operation to fetch users
//        List<String> fruitsStringList = new ArrayList<>();
//        fruitsStringList.add("Mango");
//        fruitsStringList.add("Apple");
//        fruitsStringList.add("Orange");
//        fruitList.setValue(fruitsStringList);
//    }

    private MutableLiveData<String> etText;
    public MutableLiveData<String> getCurrentText() {
        if (etText == null) {
            etText = new MutableLiveData<String>();
        }
        return etText;
    }

    private MutableLiveData<Integer> pbProgress;
    public MutableLiveData<Integer> getCurrentProgress() {
        if (pbProgress == null) {
            pbProgress = new MutableLiveData<Integer>();
        }
        return pbProgress;
    }

    private MutableLiveData<Boolean> butState;
    public MutableLiveData<Boolean> getCurrentbutState() {
        if (butState == null) {
            butState = new MutableLiveData<Boolean>();
        }
        return butState;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if(myTask != null)
            myTask.cancel(true);
    }


       public class AddTask extends AsyncTask<Integer,Integer,String> {
          /**
         * @param integers varargs- array of ints passed in
         * @return
         */
        @Override
        protected String doInBackground(Integer... integers) {
            //runs in new thread
            Integer imaxval = integers[0];

            for (int i=0;i<imaxval;i++){

                //simulate some work sleep for .5 seconds
                SystemClock.sleep(100);

                //let main thread know how we are doing
                publishProgress(i);

                //periodically check if the user canceled
                if (isCancelled())
                    return ("Canceled");
            }
            return "Completed";
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            etText.setValue("Launching async task...");
            butState.setValue(false);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Integer progress = values[0] * 1;

            pbProgress.setValue(progress);
        }

        @Override
        protected void onPostExecute(String retval) {
            //occurs in main thread, called upon completion of doInBackground
            super.onPostExecute(retval);

            etText.setValue(retval);
            butState.setValue(true);
        }

        @Override
        protected void onCancelled(String retval) {
            //occurs in main thread, called upon completion of doInBackground
            super.onCancelled();

            etText.setValue(retval);
            butState.setValue(true);
            pbProgress.setValue(0);
        }
    }
}
