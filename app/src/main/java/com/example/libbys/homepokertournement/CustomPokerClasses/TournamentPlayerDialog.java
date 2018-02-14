package com.example.libbys.homepokertournement.CustomPokerClasses;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.libbys.homepokertournement.R;

/**
 * Allows users to click on a player during a tournament, they will then be alert with this dialog that will allow them ot update
 * the players chip count or bust them.
 */

public class TournamentPlayerDialog extends DialogFragment {
    EditText chipCount;
    Button cancel, update, bust;
    TournamentPlayerInterface listener;
    //represents the player that needs to be updated based on the Player that was clicked in the activity.
    int PlayertoUpdate;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (TournamentPlayerInterface) context;
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     * This is optional, and non-graphical fragments can return null (which
     * is the default implementation).  This will be called between
     * {@link #onCreate(Bundle)} and {@link #onActivityCreated(Bundle)}.
     * <p>
     * <p>If you return a View from here, you will later be called in
     * {@link #onDestroyView} when the view is being released.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to.  The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tournamentplayeredit, container, false);
        cancel = view.findViewById(R.id.cancelButton);
        update = view.findViewById(R.id.updateButton);
        bust = view.findViewById(R.id.bustButton);
        chipCount = view.findViewById(R.id.newChipCount);
        PlayertoUpdate = getArguments().getInt("PlayerToUpdate");
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if the input box is empty assume the user meant to cancel
                if (chipCount.getText().toString().length() == 0) {
                    dismiss();
                    return;
                }
                int Count = Integer.parseInt(chipCount.getText().toString());
                listener.updatePlayer(PlayertoUpdate, Count);
                dismiss();
            }
        });
        bust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.bust(PlayertoUpdate);
                dismiss();
            }
        });
        return view;

    }

    public interface TournamentPlayerInterface {
        void bust(int player);

        void updatePlayer(int player, int count);
    }
}
