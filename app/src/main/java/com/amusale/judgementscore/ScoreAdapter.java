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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amusale.judgementscore.activity.GameActivity;
import com.amusale.judgementscore.activity.MainActivity;
import com.amusale.judgementscore.model.Game;
import com.amusale.judgementscore.model.User;

import java.util.HashMap;
import java.util.List;

/**
 * Created by amusale on 4/5/16.
 */
public class ScoreAdapter extends ArrayAdapter<User> {

    private Context context;
    private int layoutResourceId;
    private LayoutInflater inflater;
    private List<User> users;

    private Game gameInfo;

    public ScoreAdapter(Context c, int layoutResourceId, List<User> objects){
        super(c, layoutResourceId, objects);
        this.users = objects;
        this.layoutResourceId = layoutResourceId;
        this.context = c;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setGameInfo(Game gameInfo) {
        this.gameInfo = gameInfo;
    }

    public class ViewHolder {
        TextView _id;
        TextView userName;
        EditText numOfHands;
        TextView numOfHandsReadOnly;
    }


    private HashMap<String, String> textValues = new HashMap<String, String>();
    private HashMap<String, Integer> wonStatus = new HashMap<String, Integer>();



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

            holder.numOfHandsReadOnly = (TextView) convertView.findViewById(R.id.numOfHandsReadOnly);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
//        Log.i("GET_VIEW", "getting view on screen");
        final User currentUser = users.get(position);

        holder._id.setText(String.format("%d", currentUser.getUserId()));
        holder.userName.setText(currentUser.getUserName());

        String value = textValues.get("editTextPosition:" + currentUser.getUserId());
        if (null == value) {
            value = "0";
            textValues.put("editTextPosition:" + currentUser.getUserId(), "0");
        }
        holder.numOfHands.setText(value);

        if (gameInfo.getGameAction().equals(MainActivity.SCORE_ACTION_EDIT)) {

            Log.i("ScoreAdapter", "Edit Mode ON");
            Log.i("ScoreAdapter", "Score: " + gameInfo.getScore().getPoints());

            String[] points = gameInfo.getScore().getPoints().split(";");

            for (String s : points) {
                Log.i("ScoreAdapter", "points: " + points);

                String[] userPoints = s.split(":");
                if (userPoints.length > 1 &&
                        Integer.parseInt(userPoints[0]) == currentUser.getUserId()) {
                    Log.i("ScoreAdapter", "user: " + currentUser.getUserId() + "points: " + points);
                    holder.numOfHandsReadOnly.setText(userPoints[1]);
                    if (null != userPoints[1]) {
                        textValues.put("editTextPosition:" + currentUser.getUserId(), userPoints[1]);
                    }
                    wonStatus.put(Integer.toString(currentUser.getUserId()), GameActivity.STATUS_LOST);

                    break;
                } else {
                    Log.i("ScoreAdapter", "user did not match: " + currentUser.getUserId());

                }
            }
        }

        if(convertViewWasNull){
            //be aware that you shouldn't do this for each call on getView, just once by listItem when convertView is null
            holder.numOfHands.addTextChangedListener(new GenericTextWatcher(holder.numOfHands, currentUser.getUserId()));
        }

        //Log.i("POSITION", Integer.toString(position));
        //whereas, this should be called on each getView call, to update view tags.
        holder.numOfHands.setTag("editTextPosition:" + currentUser.getUserId());

        setupImageListener(convertView, currentUser);
        setupViews(convertView, holder);

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

    public HashMap<String, Integer> getWonStatus() {
        return wonStatus;
    }

    private void setupImageListener(View view, final User currentUser) {
        ImageView wonImageView = (ImageView) view.findViewById(R.id.wonBtn);
        ImageView loseImageView = (ImageView) view.findViewById(R.id.loseBtn);

        if (null == wonImageView || null == loseImageView) {
            return;
        }



        wonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.i(SystemSettings.APP_TAG + " : " + HomeActivity.class.getName(), "Entered onClick method");
                Toast.makeText(v.getContext(),
                        "Won Clicked",
                        Toast.LENGTH_SHORT).show();
                View root = (View)(v.getParent()).getParent();
                root.setBackgroundResource(R.drawable.custom_border);
                wonStatus.put(Integer.toString(currentUser.getUserId()), GameActivity.STATUS_WON);

            }
        });

        loseImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.i(SystemSettings.APP_TAG + " : " + HomeActivity.class.getName(), "Entered onClick method");
                Toast.makeText(v.getContext(),
                        "Loss Clicked",
                        Toast.LENGTH_SHORT).show();
                View root = (View)(v.getParent()).getParent();
                root.setBackgroundResource(0);
                wonStatus.put(Integer.toString(currentUser.getUserId()), GameActivity.STATUS_LOST);

            }
        });
    }

    private void setupViews(final View view, final ViewHolder holder) {

        LinearLayout editLayout = (LinearLayout)view.findViewById(R.id.editInput);
        LinearLayout addLayout = (LinearLayout)view.findViewById(R.id.addInput);
        if (gameInfo.getGameAction().equals(MainActivity.SCORE_ACTION_NEW)) {
            editLayout.setVisibility(View.GONE);
            addLayout.setVisibility(View.VISIBLE);
        } else if (gameInfo.getGameAction().equals(MainActivity.SCORE_ACTION_EDIT)) {
            editLayout.setVisibility(View.VISIBLE);
            addLayout.setVisibility(View.GONE);
        }

        ImageView increment = (ImageView) view.findViewById(R.id.plus);
        ImageView decrement = (ImageView) view.findViewById(R.id.minus);

        increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer value;
                View parent = (View) v.getParent();
                EditText valueTextView = (EditText)parent.findViewById(R.id.numOfHands);
                try {
                    value = Integer.parseInt(valueTextView.getText().toString());
                } catch (NumberFormatException ex) {
                    value = 0;
                }
                if (value <= gameInfo.getScore().getMaxNumOfCards()) {
                    valueTextView.setText(String.format("%d", value + 1));
                }

            }
        });

        decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View parent = (View) v.getParent();
                EditText valueTextView = (EditText)parent.findViewById(R.id.numOfHands);
                Integer value;
                try {
                    value = Integer.parseInt(valueTextView.getText().toString());
                } catch (NumberFormatException ex) {
                    value = 0;
                }
                if (value > 0) {
                    valueTextView.setText(String.format("%d", value - 1));
                }
            }
        });

    }
}
