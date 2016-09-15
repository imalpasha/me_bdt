package com.app.tbd.ui.Model.Adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.tbd.R;
import com.app.tbd.ui.Model.Receive.TransactionHistoryReceive;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class TransactionHistoryAdapter extends BaseAdapter {

    private final Context context;
    private final List<TransactionHistoryReceive.Transaction> obj;
    private Integer selected_position = -1;
    private Boolean COLOUR = false;

    public TransactionHistoryAdapter(Context context, List<TransactionHistoryReceive.Transaction> arrayList, String module) {
        this.context = context;
        this.obj = arrayList;
    }

    @Override
    public int getCount() {
        return obj == null ? 0 : obj.size();
    }

    @Override
    public Object getItem(int position) {
        return obj == null ? null : obj.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    static class ViewHolder {

        @InjectView(R.id.txtTransactionDate)
        TextView txtTransactionDate;

        @InjectView(R.id.txtProcessingDate)
        TextView txtProcessingDate;

        @InjectView(R.id.txtHistoryMerchant)
        TextView txtHistoryMerchant;

        @InjectView(R.id.txtHistoryDescription)
        TextView txtHistoryDescription;

        @InjectView(R.id.txtHistoryPoints)
        TextView txtHistoryPoints;

        @InjectView(R.id.transactionDetailLayout)
        LinearLayout transactionDetailLayout;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        ViewHolder vh;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.transaction_history_detail, parent, false);
            vh = new ViewHolder();
            ButterKnife.inject(vh, view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }

        if (COLOUR) {
            COLOUR = false;
            vh.transactionDetailLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.dark_lvl0));
        } else {
            COLOUR = true;
            vh.transactionDetailLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.dark_lvl0));
        }

        vh.txtTransactionDate.setText(obj.get(position).getTxnDate());
        vh.txtProcessingDate.setText(obj.get(position).getPostDate());
        vh.txtHistoryMerchant.setText(obj.get(position).getMID());
        vh.txtHistoryDescription.setText(obj.get(position).getDescription());
        vh.txtHistoryPoints.setText(obj.get(position).getTxnPts());

        return view;

    }

}
