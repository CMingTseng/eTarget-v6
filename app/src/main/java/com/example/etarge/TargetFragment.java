package com.example.etarge;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TargetFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TargetFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TargetFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private View v;
    Button T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,
           T11,T12,T13,T14,T15,T16;
    SeekBar seekBar;
    TextView seekBarValue;
    String seekbarV="";
    int t1,t2,t3,t4,t5;
    int[] blue;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public TargetFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TargetFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TargetFragment newInstance(String param1, String param2) {
        TargetFragment fragment = new TargetFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v =inflater.inflate(R.layout.fragment_target, container, false);
        Log.e("log_tag_q","blue="+blue[0]);
        t1=12;
        t2=10;
        T1 = (Button)v.findViewById(R.id.T1);
        T2 = (Button)v.findViewById(R.id.T2);
        T3 = (Button)v.findViewById(R.id.T3);
        T4 = (Button)v.findViewById(R.id.T4);
        T5 = (Button)v.findViewById(R.id.T5);
        T6 = (Button)v.findViewById(R.id.T6);
        T7 = (Button)v.findViewById(R.id.T7);
        T8 = (Button)v.findViewById(R.id.T8);
        T9 = (Button)v.findViewById(R.id.T9);
        T10 = (Button)v.findViewById(R.id.T10);
        T11 = (Button)v.findViewById(R.id.T11);
        T12 = (Button)v.findViewById(R.id.T12);
        T13 = (Button)v.findViewById(R.id.T13);
        T14 = (Button)v.findViewById(R.id.T14);
        T15 = (Button)v.findViewById(R.id.T15);
        T16 = (Button)v.findViewById(R.id.T16);
        if(t1==12){
            T1.setBackground(this.getResources().getDrawable(R.drawable.target2));  //頁面初始時判斷把麵是否連接，若有連接button將改變樣貌
        }
        else {
            T1.setEnabled(false);
        }

        if(t2==12){
            T2.setBackground(this.getResources().getDrawable(R.drawable.target2));  //頁面初始時判斷把麵是否連接，若有連接button將改變樣貌
        }
        else {
            T2.setEnabled(false);
        }

        seekBar = (SeekBar)v.findViewById(R.id.seekBar);
        seekBarValue=(TextView)v.findViewById(R.id.Numtv);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // TODO Auto-generated method stub
                seekBarValue.setText(String.valueOf(progress));  //用texview顯示bar條數值
                seekbarV=String.valueOf(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        T1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                T1.setText(seekbarV);
            }
        });

        T2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                T2.setText(seekbarV);
            }
        });

        T3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                T3.setText(seekbarV);
            }
        });

        T4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                T4.setText(seekbarV);
            }
        });

        T5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                T5.setText(seekbarV);
            }
        });
        // Inflate the layout for this fragment
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

   /* @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        blue = ((Challenge) activity).getarray();
    }*/

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
