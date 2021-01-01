package com.example.moss;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdapterAlerts extends RecyclerView.Adapter<AdapterAlerts.ViewHolderAlerts> {
    private List<ClassAlert> AllAlerts;

    private Context context;

    public AdapterAlerts(List<ClassAlert> AllAlerts, Context context) {
        this.AllAlerts = AllAlerts;

        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolderAlerts onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.adapter_alerts, parent, false);

        return new ViewHolderAlerts(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderAlerts holder, int position) {
        String image = AllAlerts.get(position).getImage();
        String type = AllAlerts.get(position).getType();
        String time = AllAlerts.get(position).getTime();

        Picasso.get().load(image).into(holder.AlertImage);
        holder.AlertTime.setText(time);

        String temp;
        if (!type.isEmpty()) {
            if (type.equals("fire")) {
                temp = "Fire Alert";
                holder.AlertType.setText(temp);
            }
            else if (type.equals("intruder")) {
                temp = "Intruder Alert";
                holder.AlertType.setText(temp);
            }
        }

        holder.AlertImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CapturedImage.class);
                intent.putExtra("alert_image", image);
                context.startActivity(intent);
            }
        });

        holder.AlertEmergencyCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type.equals("fire")) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:998"));
                    context.startActivity(intent);
                }
                else if (type.equals("intruder")) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:15"));
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return AllAlerts.size();
    }

    public class ViewHolderAlerts extends RecyclerView.ViewHolder {
        ImageView AlertImage;
        TextView AlertType;
        TextView AlertTime;
        TextView AlertEmergencyCall;

        public ViewHolderAlerts(@NonNull View itemView) {
            super(itemView);
            AlertImage = (ImageView) itemView.findViewById(R.id.alert_image);
            AlertType = (TextView) itemView.findViewById(R.id.alert_type);
            AlertTime = (TextView) itemView.findViewById(R.id.alert_time);
            AlertEmergencyCall = (TextView) itemView.findViewById(R.id.alert_emergency_call);
        }
    }
}
