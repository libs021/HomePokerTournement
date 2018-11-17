package com.example.libbys.homepokertournement.Adapters;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.libbys.homepokertournement.DataBaseFiles.PokerContract;
import com.example.libbys.homepokertournement.PlayerResultsActivity;
import com.example.libbys.homepokertournement.R;

import java.util.List;

public class RecyclerViewPlayers extends RecyclerView.Adapter<RecyclerViewPlayers.RecylerViewPlayersVH> {
    //Data that is to be represented by our recycler view. In this case a cursor with data retrieved from the local database;
    private Cursor data;
    //Used to represent the selection status of each item.
    private Boolean[] isPressed;
    private Context context;
    private Boolean addNewTournament;

    public RecyclerViewPlayers (Cursor listofData, Context context, boolean addNewTournament){
        data = listofData;
        if (listofData !=null) setUpBooleanArray();
        this.context = context;
        this.addNewTournament = addNewTournament;

    }

    private void setUpBooleanArray() {
        isPressed = null;
        isPressed = new Boolean[data.getCount()];
        for (int i=0; i<isPressed.length;i++) { isPressed[i] = false; }
    }

    /**
     * Called when RecyclerView needs a new {@link RecyclerView.ViewHolder} of the given type to represent
     * an item.
     * <p>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p>
     * The new ViewHolder will be used to display items of the adapter using
     * {@link #onBindViewHolder(RecyclerView.ViewHolder, int, List)}. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary {@link View#findViewById(int)} calls.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     * @see #onBindViewHolder(RecyclerView.ViewHolder, int)
     */
    @NonNull
    @Override
    public RecylerViewPlayersVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.playerlistview,parent,false);
        return new RecylerViewPlayersVH(view);
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link RecyclerView.ViewHolder#itemView} to reflect the item at the given
     * position.
     * <p>
     * Note that unlike {@link ListView}, RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use {@link RecyclerView.ViewHolder#getAdapterPosition()} which will
     * have the updated adapter position.
     * <p>
     * Override {@link #onBindViewHolder(RecyclerView.ViewHolder, int, List)} instead if Adapter can
     * handle efficient partial bind.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull RecylerViewPlayersVH holder, int position) {
        data.moveToPosition(position);
        if (isPressed[position]) holder.parent.setBackgroundColor(context.getColor(R.color.colorPrimary));
        else holder.parent.setBackgroundColor(0x00000);

        String Name = data.getString(data.getColumnIndex(PokerContract.PlayerEntry.NAME));
        int Buyin = data.getInt(data.getColumnIndex(PokerContract.PlayerEntry.BUYIN));
        int CashOut = data.getInt(data.getColumnIndex(PokerContract.PlayerEntry.CASHOUT));
        int TotalWinnings = data.getInt(data.getColumnIndex(PokerContract.PlayerEntry.WIN));
        String toInsert;
        toInsert = String.format(context.getString(R.string.Buyin), Buyin);
        holder.playerName.setText(Name);
        holder.playerDown.setText(toInsert);
        toInsert = String.format(context.getString(R.string.Cashout), CashOut);
        holder.playerUp.setText(toInsert);
        toInsert = String.format(context.getString(R.string.Total), TotalWinnings);
        holder.playerTotal.setText(toInsert);
        final int position1 = holder.getAdapterPosition();
        if (addNewTournament) {
            holder.parent.setOnClickListener(new View.OnClickListener() {
                /**
                 * Called when a view has been clicked.
                 *
                 * @param v The view that was clicked.
                 */
                @Override
                public void onClick(View v) {
                    data.moveToPosition(position1);
                    int playerID = data.getInt(data.getColumnIndex(PokerContract.PlayerEntry._ID));
                    String playerName = data.getString(data.getColumnIndex(PokerContract.PlayerEntry.NAME));
                    Uri uri = ContentUris.withAppendedId(PokerContract.BASE_CONTENT_URI, playerID);
                    Intent intent = new Intent(context, PlayerResultsActivity.class);
                    intent.setData(uri);
                    intent.putExtra("PLayerName", playerName);
                    context.startActivity(intent);
                }
            });
        }
        else {
                holder.parent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isPressed[position1]) {
                            v.setBackgroundColor(0x00000);
                            isPressed[position1] = false;
                        } else {
                            v.setBackgroundColor(context.getColor(R.color.colorPrimary));
                            isPressed[position1] = true;
                        }
                    }
                });
            }
        }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        if (data==null) return 0;
        return data.getCount();
    }

    public Boolean[] getIsPressed() {
        return isPressed;
    }

    public void setData (Cursor data) {
        this.data = data;
        setUpBooleanArray();
        notifyDataSetChanged();
    }

    public Cursor getData() {
        return data;
    }

    class RecylerViewPlayersVH extends RecyclerView.ViewHolder {
        TextView playerName;
        TextView playerUp;
        TextView playerDown;
        TextView playerTotal;
        View parent;

        RecylerViewPlayersVH(View itemView) {
            super(itemView);
            playerDown = itemView.findViewById(R.id.cashout);
            playerUp = itemView.findViewById(R.id.buyin);
            playerTotal = itemView.findViewById(R.id.totalwinnings);
            playerName = itemView.findViewById(R.id.playerName);
            parent = itemView;
        }
    }
}
