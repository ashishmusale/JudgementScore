package com.amusale.judgementscore;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.amusale.judgementscore.model.Score;
import com.amusale.judgementscore.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by amusale on 4/5/16.
 */
public class ScoreAdapter extends ArrayAdapter<User> {

    private Context context;
    private int layoutResourceId;
    private LayoutInflater inflater;
    private List<User> users;

    public ScoreAdapter(Context c, int layoutResourceId, List<User> objects){
        super(c, layoutResourceId, objects);
        this.users = objects;
        this.layoutResourceId = layoutResourceId;
        this.context = c;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class ViewHolder {
        TextView _id;
        TextView userName;
        EditText numOfHands;
    }


    private HashMap<String, String> textValues = new HashMap<String, String>();

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        ViewHolder holder=null;
//        View view = convertView;
        boolean convertViewWasNull = false;
        if(convertView == null){
            convertView = inflater.inflate(layoutResourceId, parent, false);
            convertViewWasNull = true;
//            Log.i("GET_VIEW", "convert view was null");
            holder = new ViewHolder();
            holder._id = (TextView)convertView.findViewById(R.id._id);
            holder.userName = (TextView)convertView.findViewById(R.id.userName);
            holder.numOfHands = (EditText)convertView.findViewById(R.id.numOfHands);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
//        Log.i("GET_VIEW", "getting view on screen");
        final User currentUser = users.get(position);

        holder._id.setText(String.format("%d", currentUser.getUserId()));
        holder.userName.setText(currentUser.getUserName());

        String value = textValues.get("editTextPosition:" + currentUser.getUserId());
        holder.numOfHands.setText(value);

        if(convertViewWasNull){
            //be aware that you shouldn't do this for each call on getView, just once by listItem when convertView is null
            holder.numOfHands.addTextChangedListener(new GenericTextWatcher(holder.numOfHands, currentUser.getUserId()));
        }

        //Log.i("POSITION", Integer.toString(position));
        //whereas, this should be called on each getView call, to update view tags.
        holder.numOfHands.setTag("editTextPosition:" + currentUser.getUserId());

        return convertView;
    }


    private class GenericTextWatcher implements TextWatcher {

        private View view;
        private int id;
        private GenericTextWatcher(View view, int id) {
            this.view = view;
            this.id = id;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            Log.i("TEXT_WATCHER", "before text changed " + charSequence);
        }
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            Log.i("TEXT_WATCHER", "on text changed " + charSequence);
        }

        public void afterTextChanged(Editable editable) {
            //save the value for the given tag :
            ScoreAdapter.this.textValues.put((String)view.getTag(), editable.toString());
        }
    }

    public HashMap<String, String> getTextValues() {
        return textValues;
    }
}
