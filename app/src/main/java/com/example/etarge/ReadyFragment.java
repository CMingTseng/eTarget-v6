package com.example.etarge;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ReadyFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ReadyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReadyFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private int[] md = new int[]{}; //接收img陣列值
    private String[] sd = new String[]{}; //接收STI陣列值
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Button rt1,rt2,rt3,rt4,rt5,rt6,rt7,rt8,rt9,rt10,rt11,rt12,rt13,rt14,rt15,rt16;
    Button toSB;
    FrameLayout SF;
    private OnFragmentInteractionListener mListener;

    public ReadyFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static ReadyFragment newInstance(int[] data, String[] sdata) {
        ReadyFragment fragment = new ReadyFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        args.putIntArray(ARG_PARAM1,data);
        args.putStringArray(ARG_PARAM2,sdata);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            md=getArguments().getIntArray(ARG_PARAM1);
            sd=getArguments().getStringArray(ARG_PARAM2);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_ready, container, false);
        SF = (FrameLayout) v.findViewById(R.id.fragment_containerC);
        toSB = (Button) v.findViewById(R.id.toSB);
        rt1 = (Button) v.findViewById(R.id.RT1);
        rt2 = (Button) v.findViewById(R.id.RT2);
        rt3 = (Button) v.findViewById(R.id.RT3);
        rt4 = (Button) v.findViewById(R.id.RT4);
        rt5 = (Button) v.findViewById(R.id.RT5);
        rt6 = (Button) v.findViewById(R.id.RT6);
        rt7 = (Button) v.findViewById(R.id.RT7);
        rt8 = (Button) v.findViewById(R.id.RT8);
        rt9 = (Button) v.findViewById(R.id.RT9);
        rt10 = (Button) v.findViewById(R.id.RT10);
        rt11 = (Button) v.findViewById(R.id.RT11);
        rt12 = (Button) v.findViewById(R.id.RT12);
        rt13 = (Button) v.findViewById(R.id.RT13);
        rt14 = (Button) v.findViewById(R.id.RT14);
        rt15 = (Button) v.findViewById(R.id.RT15);
        rt16 = (Button) v.findViewById(R.id.RT16);

        initTarget();
        TargetIn(md);
        showSTI(sd);

        //下一步按鈕
        toSB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder next = new AlertDialog.Builder(getActivity());
                next.setTitle("待命\nStand By");
                next.setPositiveButton("按下後開始\nPress to start", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openStartGame();
                    }
                });
                next.show();
            }
        });
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    //初始靶面資訊
    public void initTarget(){
        rt1.setText("");
        rt2.setText("");
        rt3.setText("");
        rt4.setText("");
        rt5.setText("");
        rt6.setText("");
        rt7.setText("");
        rt8.setText("");
        rt9.setText("");
        rt10.setText("");
        rt11.setText("");
        rt12.setText("");
        rt13.setText("");
        rt14.setText("");
        rt15.setText("");
        rt16.setText("");
    }

    //接收以配對靶面資訊
    public void showSTI(String sd[]){
        if(sd[0].length()>5){
            rt1.setText(sd[0]);
        }
        if(sd[1].length()>5){
            rt2.setText(sd[1]);
        }
        if(sd[2].length()>5){
            rt3.setText(sd[2]);
        }
        if(sd[3].length()>5){
            rt4.setText(sd[3]);
        }
        if(sd[4].length()>5){
            rt5.setText(sd[4]);
        }
        if(sd[5].length()>5){
            rt6.setText(sd[5]);
        }
        if(sd[6].length()>5){
            rt7.setText(sd[6]);
        }
        if(sd[7].length()>5){
            rt8.setText(sd[7]);
        }
        if(sd[8].length()>5){
            rt9.setText(sd[8]);
        }
        if(sd[9].length()>5){
            rt10.setText(sd[9]);
        }
        if(sd[10].length()>5){
            rt11.setText(sd[10]);
        }
        if(sd[11].length()>5){
            rt12.setText(sd[11]);
        }
        if(sd[12].length()>5){
            rt13.setText(sd[12]);
        }
        if(sd[13].length()>5){
            rt14.setText(sd[13]);
        }
        if(sd[14].length()>5){
            rt15.setText(sd[14]);
        }
        if(sd[15].length()>5){
            rt16.setText(sd[15]);
        }
    }

    //判斷靶面是否有連接
    public void TargetIn(int img[]){


        if(img[5] == 1) {
            rt1.setEnabled(true);
            rt1.setBackground(getResources().getDrawable(R.drawable.target2));
            rt1.setText("1A\n"+Integer.toString(img[7]));
        }
        else {
            rt1.setBackground(getResources().getDrawable(R.drawable.target1));
            rt1.setEnabled(false);
        }
        if(img[29] == 1){
            rt2.setEnabled(true);
            rt2.setBackground(getResources().getDrawable(R.drawable.target2));
            //Log.e("log_tag_q", "img[13]="+Integer.toString(img[13]));
            rt2.setText("2A\n"+Integer.toString(img[31]));
            //t2.setText(seekbarV);
        }
        else {
            rt2.setBackground(getResources().getDrawable(R.drawable.target1));
            rt2.setEnabled(false);
        }
        if(img[53] == 1){
            rt3.setEnabled(true);
            rt3.setBackground(getResources().getDrawable(R.drawable.target2));
            rt3.setText("3A\n"+Integer.toString(img[55]));
            //t3.setText(seekbarV);
        }
        else {
            rt3.setBackground(getResources().getDrawable(R.drawable.target1));
            rt3.setEnabled(false);
        }
        if(img[77] == 1){
            rt4.setEnabled(true);
            rt4.setBackground(getResources().getDrawable(R.drawable.target2));
            rt4.setText("4A\n"+Integer.toString(img[79]));
            //t4.setText(seekbarV);
        }
        else {
            rt4.setBackground(getResources().getDrawable(R.drawable.target1));
            rt4.setEnabled(false);
        }
        if(img[11] == 1){
            rt5.setEnabled(true);
            rt5.setBackground(getResources().getDrawable(R.drawable.target2));
            rt5.setText("1B\n"+Integer.toString(img[13]));
            //t5.setText(seekbarV);
        }
        else {
            rt5.setBackground(getResources().getDrawable(R.drawable.target1));
            rt5.setEnabled(false);
        }
        if(img[35] == 1){
            rt6.setEnabled(true);
            rt6.setBackground(getResources().getDrawable(R.drawable.target2));
            rt6.setText("2B\n"+Integer.toString(img[37]));
            //t6.setText(seekbarV);
        }
        else {
            rt6.setBackground(getResources().getDrawable(R.drawable.target1));
            rt6.setEnabled(false);
        }
        if(img[59] == 1){
            rt7.setEnabled(true);
            rt7.setBackground(getResources().getDrawable(R.drawable.target2));
            rt7.setText("3B\n"+Integer.toString(img[61]));
            //t7.setText(seekbarV);
        }
        else {
            rt7.setBackground(getResources().getDrawable(R.drawable.target1));
            rt7.setEnabled(false);
        }
        if(img[83] == 1){
            rt8.setEnabled(true);
            rt8.setBackground(getResources().getDrawable(R.drawable.target2));
            rt8.setText("4B\n"+Integer.toString(img[85]));
            //t8.setText(seekbarV);
        }
        else {
            rt8.setBackground(getResources().getDrawable(R.drawable.target1));
            rt8.setEnabled(false);
        }
        if(img[17] == 1){
            rt9.setEnabled(true);
            rt9.setBackground(getResources().getDrawable(R.drawable.target2));
            rt9.setText("1C\n"+Integer.toString(img[19]));
            //t9.setText(seekbarV);
        }
        else {
            rt9.setBackground(getResources().getDrawable(R.drawable.target1));
            rt9.setEnabled(false);
        }
        if(img[41] == 1){
            rt10.setEnabled(true);
            rt10.setBackground(getResources().getDrawable(R.drawable.target2));
            rt10.setText("2C\n"+Integer.toString(img[43]));
            //t10.setText(seekbarV);
        }
        else {
            rt10.setBackground(getResources().getDrawable(R.drawable.target1));
            rt10.setEnabled(false);
        }
        if(img[65] == 1){
            rt11.setEnabled(true);
            rt11.setBackground(getResources().getDrawable(R.drawable.target2));
            rt11.setText("3C\n"+Integer.toString(img[67]));
            //t11.setText(seekbarV);
        }
        else {
            rt11.setBackground(getResources().getDrawable(R.drawable.target1));
            rt11.setEnabled(false);
        }
        if(img[89] == 1){
            rt12.setEnabled(true);
            rt12.setBackground(getResources().getDrawable(R.drawable.target2));
            rt12.setText("4C\n"+Integer.toString(img[91]));
            //t12.setText(seekbarV);
        }
        else {
            rt12.setBackground(getResources().getDrawable(R.drawable.target1));
            rt12.setEnabled(false);
        }
        if(img[23] == 1){
            rt13.setEnabled(true);
            rt13.setBackground(getResources().getDrawable(R.drawable.target2));
            rt13.setText("1D\n"+Integer.toString(img[25]));
            //t13.setText(seekbarV);
        }
        else {
            rt13.setBackground(getResources().getDrawable(R.drawable.target1));
            rt13.setEnabled(false);
        }
        if(img[47] == 1){
            rt14.setEnabled(true);
            rt14.setBackground(getResources().getDrawable(R.drawable.target2));
            rt14.setText("2D\n"+Integer.toString(img[49]));
            //t14.setText(seekbarV);
        }
        else {
            rt14.setBackground(getResources().getDrawable(R.drawable.target1));
            rt14.setEnabled(false);
        }
        if(img[71] == 1){
            rt15.setEnabled(true);
            rt15.setBackground(getResources().getDrawable(R.drawable.target2));
            rt15.setText("3D\n"+Integer.toString(img[73]));
            //t15.setText(seekbarV);
        }
        else {
            rt15.setBackground(getResources().getDrawable(R.drawable.target1));
            rt15.setEnabled(false);
        }
        if(img[95] == 1){
            rt16.setEnabled(true);
            rt16.setBackground(getResources().getDrawable(R.drawable.target2));
            rt16.setText("4D\n"+Integer.toString(img[97]));
        }
        else {
            rt16.setBackground(getResources().getDrawable(R.drawable.target1));
            rt16.setEnabled(false);
        }
    }


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

    //切換Fragment到start game
    public void openStartGame(){
        StartGameFragment fragment = StartGameFragment.newInstance("1","2");
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction(); //執行切換fragment
        transaction.setCustomAnimations(R.anim.next_in,R.anim.next_out,R.anim.next_in,R.anim.next_out);
        transaction.addToBackStack(null);
        transaction.add(R.id.fragment_containerC,fragment,"StartGameFragment").commit();
    }
}
