package edu.csulb.bacindicator.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import edu.csulb.bacindicator.R;
import edu.csulb.bacindicator.models.Alcohol;

/**
 * Created by Johan on 30/04/2015.
 */
public class AlcoholAdapter extends ArrayAdapter<Alcohol> {

    public AlcoholAdapter(Context context, List<Alcohol> list) {
        super(context, android.R.layout.select_dialog_item, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Alcohol alcohol = getItem(position);
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();

            convertView = inflater.inflate(R.layout.single_string_list_item, parent, false);
            holder = new ViewHolder(convertView);

            holder.alcohol = (TextView) convertView.findViewById(R.id.string);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.alcohol.setText(alcohol.name);
        return convertView;
    }

    private static class ViewHolder {
        TextView alcohol;

        ViewHolder(View layout) {
            alcohol = (TextView) layout.findViewById(R.id.string);
        }
    }
}
