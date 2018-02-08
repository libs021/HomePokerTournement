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
import android.widget.Toast;

import com.example.libbys.homepokertournement.R;


/**
 * Created by Libby's on 2/8/2018.
 */

public class NewPlayerDialog extends DialogFragment {
    NewPlayerInterface listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (NewPlayerInterface) context;
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
        View view = inflater.inflate(R.layout.newplayer, null);
        final EditText nameEditText = view.findViewById(R.id.nameEditText);
        Button addPlayer = view.findViewById(R.id.button);
        addPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEditText.getText().toString().trim();
                if (name.isEmpty()) {
                    //Show user an error message and then do nothing
                    Toast.makeText(getActivity(), getString(R.string.PlayerNameError), Toast.LENGTH_LONG).show();
                    return;
                }
                listener.add(name);
                dismiss();
            }
        });
        return view;
    }

    public interface NewPlayerInterface {
        void add(String name);
    }
}


