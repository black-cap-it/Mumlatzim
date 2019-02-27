package com.mumlatzim.mumlatzimv2.listeners;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public interface OrderItemClickListener {

    public void onOrderClick(int position, View view, TextView track_number, ImageButton btn_order_edit);
}

