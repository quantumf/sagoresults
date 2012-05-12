package za.co.sagoclubs;

import android.content.Context;
import android.widget.ArrayAdapter;

public class PlayerArrayAdapter extends ArrayAdapter<Player> {

	Context context;
    int layoutResourceId;   
    Player data[] = null;
   
    public PlayerArrayAdapter(Context context, int layoutResourceId, Player[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

}
