package meandow.uthgardwatchlist;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class PlayerListAdapter extends ArrayAdapter<Player> {
    private List<Player> mZones;
    private int mResourceId = 0;
    private LayoutInflater mLayoutInflater;
    private RadioButton mSelectedRB;
    private int mSelectedPosition = -1;
    private Context mContext;

    public PlayerListAdapter(Context context, int resource, List<Player> objects) {
        super(context, resource, objects);
        mResourceId = resource;
        mZones = objects;
        mContext = context;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private class ViewHolder {
        ImageView mImageView;
        TextView mName;
        TextView mGuild;
        TextView mRace;
        TextView mClass;
        TextView mRp;
        TextView mLevel;
        TextView mRr;

    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;

        if (view == null) {

            view = mLayoutInflater.inflate(R.layout.listitem, parent, false);
            holder = new ViewHolder();
            holder.mImageView = (ImageView) view.findViewById(R.id.realm);
            holder.mName = (TextView) view.findViewById(R.id.name);
            holder.mGuild = (TextView) view.findViewById(R.id.guild);
//            holder.mRace = (TextView) view.findViewById(R.id.player_race);
            holder.mClass = (TextView) view.findViewById(R.id.player_class);
            holder.mRp = (TextView) view.findViewById(R.id.player_rp);
            holder.mLevel = (TextView) view.findViewById(R.id.player_level);
            holder.mRr = (TextView) view.findViewById(R.id.player_realmrank);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        Drawable img;
        Player playur = mZones.get(position);
        switch (playur.getRealm()){
            case "HIBERNIA":
                img = getContext().getResources().getDrawable(R.drawable.hib_text);
                break;
            case "ALBION":
                img = getContext().getResources().getDrawable(R.drawable.alb_text);
                break;
            case "MIDGARD":
                img = getContext().getResources().getDrawable(R.drawable.mid_text);
                break;
            default:
                img = getContext().getResources().getDrawable(R.drawable.mid_text);
                break;
        }
        holder.mImageView.setImageDrawable(img);
        holder.mName.setText(playur.getName());
        holder.mGuild.setText("<"+playur.getGuild()+">");
        holder.mClass.setText(playur.getPlayerClass());
        holder.mRp.setText("RP: "+playur.getRp());
        holder.mLevel.setText("LVL: "+playur.getLvl());
        holder.mRr.setText("RR: "+playur.getRR());
        return view;
    }

    public void setSelectedPosition(int i) {
        mSelectedPosition = i;
    }
}