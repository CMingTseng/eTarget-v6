package com.example.etarge;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment_1.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment_1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_1 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Intent intent;
    private Button turn;
    private View v;
    private static final String[] name={"語系設定","中文","English"};
    private Spinner spinner;
    //private ArrayAdapter<String> adapter;
    //private ListView mDevicesListView;
    private TextView mBluetoothStatus;
    private Button mListPairedDevicesBtn;
    private BluetoothAdapter mBTAdapter;
    private Set<BluetoothDevice> mPairedDevices;
    private ArrayAdapter<String> mBTArrayAdapter;
    private ListView mDevicesListView;
    private Handler mHandler;
    // Our main handler that will receive callback notifications
    private connectedThread mConnectThread;
    // bluetooth background worker thread to send and receive data
    private BluetoothSocket mBTSocket = null;
    BluetoothDevice device;
    // bi-directional client-to-client data path
    private connectedThread mConnectedThread;
    private static final UUID BTMODULEUUID = UUID.fromString
            ("00001101-0000-1000-8000-00805F9B34FB"); // "random" unique identifier

    // #defines for identifying shared types between calling functions
    private final static int REQUEST_ENABLE_BT = 1;
    // used to identify adding bluetooth names
    private final static int MESSAGE_READ = 2;
    // used in bluetooth handler to identify message update
    private final static int CONNECTING_STATUS = 3;
    // used in bluetooth handler to identify message status
    private String _recieveData = "";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Fragment_1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_1.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_1 newInstance(String param1, String param2) {
        Fragment_1 fragment = new Fragment_1();
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
        v = inflater.inflate(R.layout.fragment_fragment_1,container,false);
        //設定初始畫面為fragment_1
        turn = (Button)v.findViewById(R.id.TurnOn);
        //mBluetoothStatus = (TextView) v.findViewById(R.id.bluetoothStatus);
        mBTArrayAdapter = new ArrayAdapter<String>
                (this.getActivity(), android.R.layout.simple_list_item_1);
        mBTAdapter = BluetoothAdapter.getDefaultAdapter();
        // get a handle on the bluetooth radio
        mDevicesListView = (ListView) v.findViewById(R.id.devicesListView);
        mDevicesListView.setAdapter(mBTArrayAdapter); // assign model to view
        mDevicesListView.setOnItemClickListener(mDeviceClickListener);

        int flag = -1;
        turn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), 1);
                listPairedDevices(v);
            }
        });

        // 詢問藍芽裝置權限
        /*if (ContextCompat.checkSelfPermission(this.getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this.getActivity(), new String[]
                    {Manifest.permission.ACCESS_COARSE_LOCATION}, 1);*/

        //定義執行緒 當收到不同的指令做對應的內容


        /*String [] values =
                {"語系設定","繁體中文","English"};
        Spinner spinner = (Spinner) v.findViewById(R.id.langeage_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, values);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);*/
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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

    class connectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public connectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }
    }

    private void listPairedDevices(View view) {
        mPairedDevices = mBTAdapter.getBondedDevices();
        if (mBTAdapter.isEnabled()) {
            // put it's one to the adapter
            for (BluetoothDevice device : mPairedDevices)
                mBTArrayAdapter.add(device.getName() + "\n" + device.getAddress());

        }
    }

    private AdapterView.OnItemClickListener mDeviceClickListener = new
            AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {

                    // Get the device MAC address, which is the last 17 chars in the View
                    String info = ((TextView) v).getText().toString();
                    final String address = info.substring(info.length() - 17);
                    final String name = info.substring(0, info.length() - 17);
                    //Log.e("log_tag_q", "address="+address);

                    // Spawn a new thread to avoid blocking the GUI one
                    new Thread() {
                        public void run() {
                            boolean fail = false;
                            //取得裝置MAC找到連接的藍芽裝置
                            device = mBTAdapter.getRemoteDevice(address);
                            //Log.e("log_tag", "MAC="+device);
                            String mac = String.valueOf(device);
                            intent = new Intent(getActivity(),Main3Activity.class);
                            intent.putExtra("MAC",mac);
                            startActivity(intent);
                        }
                    }.start();

                }
            };

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws
            IOException {
        return device.createRfcommSocketToServiceRecord(BTMODULEUUID);
        //creates secure outgoing connection with BT device using UUID
    }

    //返回鍵傳MAC位址到Main3Activity
   /* @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

            return true;//不执行父类的点击事件
        }
        return super.onKeyDown(keyCode, event);//继续执行父类的其他点击事件
    }*/

}
