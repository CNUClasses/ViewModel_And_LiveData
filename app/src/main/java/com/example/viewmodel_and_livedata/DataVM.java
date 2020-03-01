package com.example.viewmodel_and_livedata;

import android.os.AsyncTask;
import android.os.SystemClock;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.lang.ref.WeakReference;

public class DataVM extends ViewModel {
    AddTask myTask;

    // Create LiveData
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

    /*
   If the following class is non static then it will have a hidden reference to its
   parent, MainActivity.  If the device is rotated while the following
   thread is running then this reference will keep the garbage collector from
   collecting the parent activity.  If the thread runs long enough and the
   device keeps rotating, its memory footprint grows and grows.
   If its static then no hidden reference, so no memory problems, but its harder to
   manipulate activity UI.
    */
    public class AddTask extends AsyncTask<Integer,Integer,String> {
        // if an object can only be reached by a weak reference then its
        // eligible for garbage collection.  So on a confgurationchanged
        // event when the activity is destroyed, it can be GCed even
        // though ma has a weak reference to it
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
//            //set the UI
//            if (ma.get()!=null) {
//                ma.get().setUIState(false, "Launching async task...");
//            }
            etText.setValue("Launching async task...");
            butState.setValue(false);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Integer progress = values[0] * 1;

            //set the UI
//            if (ma.get() !=null)
//                ma.get().pBar.setProgress(progress);
            pbProgress.setValue(progress);
        }

        @Override
        protected void onPostExecute(String retval) {
            //occurs in main thread, called upon completion of doInBackground
            super.onPostExecute(retval);

//            //set the UI
//            if (ma.get()!=null) {
//                ma.get().setUIState(true, retval);
//            }
            etText.setValue(retval);
            butState.setValue(true);
        }

        @Override
        protected void onCancelled(String retval) {
            //occurs in main thread, called upon completion of doInBackground
            super.onCancelled();

//            //set the UI
//            if (ma.get()!=null) {
//                ma.get().setUIState(true, retval);
//            }
            etText.setValue(retval);
            butState.setValue(true);
            pbProgress.setValue(0);
        }
    }
}
