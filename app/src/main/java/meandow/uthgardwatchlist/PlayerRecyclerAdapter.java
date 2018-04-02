package meandow.uthgardwatchlist;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class PlayerRecyclerAdapter extends RecyclerView.Adapter<PlayerRecyclerAdapter.ViewHolder> {

    private List<Player> playerData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context context;

    PlayerRecyclerAdapter(Context context, List<Player> data) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.playerData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.listitem, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Drawable img;
        Player player = playerData.get(position);
        switch (player.getRealm()) {
            case "HIBERNIA":
                img = context.getResources().getDrawable(R.drawable.hib_text);
                break;
            case "ALBION":
                img = context.getResources().getDrawable(R.drawable.alb_text);
                break;
            case "MIDGARD":
                img = context.getResources().getDrawable(R.drawable.mid_text);
                break;
            default:
                img = context.getResources().getDrawable(R.drawable.mid_text);
                break;
        }
        holder.realmIcon.setImageDrawable(img);
        holder.playerName.setText(player.getName());
        holder.playerGuild.setText("<" + player.getGuild() + ">");
        holder.playerRace.setText(player.getRace());
        holder.playerClass.setText("- " + player.getPlayerClass());
        holder.playerRp.setText("RP: " + player.getRp());
        holder.playerLevel.setText("LVL: " + player.getLvl());
        holder.playerRr.setText("RR: " + player.getRR());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return playerData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView realmIcon;
        TextView playerName;
        TextView playerGuild;
        TextView playerRace;
        TextView playerClass;
        TextView playerRp;
        TextView playerLevel;
        TextView playerRr;

        ViewHolder(View itemView) {
            super(itemView);
            realmIcon = (ImageView) itemView.findViewById(R.id.realm);
            playerName = (TextView) itemView.findViewById(R.id.name);
            playerGuild = (TextView) itemView.findViewById(R.id.guild);
            playerClass = (TextView) itemView.findViewById(R.id.player_class);
            playerRace = (TextView) itemView.findViewById(R.id.player_race);
            playerRp = (TextView) itemView.findViewById(R.id.player_rp);
            playerLevel = (TextView) itemView.findViewById(R.id.player_level);
            playerRr = (TextView) itemView.findViewById(R.id.player_realmrank);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    Player getItem(int id) {
        return playerData.get(id);
    }

    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}