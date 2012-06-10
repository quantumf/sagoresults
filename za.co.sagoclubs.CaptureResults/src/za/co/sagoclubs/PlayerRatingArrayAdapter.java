package za.co.sagoclubs;

/**
 * This is an array adapter used to render the items on the player rating list view
 * 
 */
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

public class PlayerRatingArrayAdapter extends ArrayAdapter<Player> {

	Context context;
    int layoutResourceId;   
    Player data[] = null;
   
    public PlayerRatingArrayAdapter(Context context, int layoutResourceId, Player[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        PlayerHolder holder = null;
       
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
           
            holder = new PlayerHolder();
            holder.txtName = (TextView)row.findViewById(R.id.txtName);
            holder.txtRank = (TextView)row.findViewById(R.id.txtRank);
           
            row.setTag(holder);
        }
        else
        {
            holder = (PlayerHolder)row.getTag();
        }
       
        Player player = data[position];
        holder.txtName.setText(player.getName());
        holder.txtRank.setText(player.getRank()+"("+player.getIndex()+")");
       
        return row;
    }

    static class PlayerHolder {
    	TextView txtName;
    	TextView txtRank;
    	TextView txtIndex;
    }
    
}
