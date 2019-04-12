package com.example.bookthiti.masai2.bluetoothattackscreen;

import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bookthiti.masai2.R;

import java.util.List;

public class BluetoothDeviceRecyclerAdapter extends RecyclerView.Adapter<BluetoothDeviceRecyclerAdapter.ViewHolder> {
    private List<BluetoothDeviceModel> bluetoothDeviceModelList;
    private Context context;

    public BluetoothDeviceRecyclerAdapter(Context context, List<BluetoothDeviceModel> bluetoothDeviceList) {
        this.bluetoothDeviceModelList = bluetoothDeviceList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_bluetooth_device, viewGroup, false);
        return new BluetoothDeviceRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        BluetoothDeviceModel bluetoothDeviceModel = bluetoothDeviceModelList.get(i);
        BluetoothDevice bluetoothDevice = bluetoothDeviceModel.getBluetoothDevice();
        Integer rssi = bluetoothDeviceModel.getRssi();
        boolean isAtRisk = bluetoothDeviceModel.isAtRisk();

        if (bluetoothDevice.getName() == null) {
            viewHolder.textViewDeviceName.setText("N/A");
        } else {
            viewHolder.textViewDeviceName.setText(bluetoothDevice.getName());
        }
        viewHolder.textViewDeviceMacAddress.setText(bluetoothDevice.getAddress());
        viewHolder.textViewRssi.setText(Integer.toString(rssi) + " dBm");
        if (isAtRisk) {
            viewHolder.textViewRisk.setText("HIGH RISK");
            // TODO: Add text color

            viewHolder.buttonLaunchAttack.setVisibility(View.VISIBLE);
            viewHolder.buttonLaunchAttack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO: Launch BlueBorne attack
                }
            });
        } else {
            viewHolder.textViewRisk.setText("LOW RISK");
            // TODO: Add text color
        }
        viewHolder.imageViewDeviceType.setImageResource(getDeviceIconResource(bluetoothDevice.getBluetoothClass().getMajorDeviceClass()));
    }

    @Override
    public int getItemCount() {
        return bluetoothDeviceModelList.size();
    }

    private int getDeviceIconResource(int majorClass) {
        switch (majorClass) {
            case BluetoothClass.Device.Major.AUDIO_VIDEO:
                return R.drawable.ic_music_video_black_36dp;
            case BluetoothClass.Device.Major.COMPUTER:
                return R.drawable.ic_computer_black_36dp;
            case BluetoothClass.Device.Major.PHONE:
                return R.drawable.ic_phone_android_black_36dp;
            case BluetoothClass.Device.Major.TOY:
                return R.drawable.ic_toys_black_36dp;
            case BluetoothClass.Device.Major.WEARABLE:
                return R.drawable.ic_watch_black_36dp;
            case BluetoothClass.Device.Major.MISC:
            case BluetoothClass.Device.Major.IMAGING:
            case BluetoothClass.Device.Major.NETWORKING:
            case BluetoothClass.Device.Major.HEALTH:
            case BluetoothClass.Device.Major.UNCATEGORIZED:
            case BluetoothClass.Device.Major.PERIPHERAL:
            default:
                return R.drawable.ic_devices_other_black_36dp;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageViewDeviceType;
        private TextView textViewDeviceName;
        private TextView textViewDeviceMacAddress;
        private TextView textViewRisk;
        private TextView textViewRssi;
        private Button buttonLaunchAttack;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewDeviceType = itemView.findViewById(R.id.ic_device_class);
            textViewDeviceName = itemView.findViewById(R.id.text_device_name);
            textViewDeviceMacAddress = itemView.findViewById(R.id.text_mac_address);
            textViewRisk = itemView.findViewById(R.id.text_risk);
            textViewRssi = itemView.findViewById(R.id.text_rssi);
            buttonLaunchAttack = itemView.findViewById(R.id.button_launch_bt_attack);
        }
    }
}
