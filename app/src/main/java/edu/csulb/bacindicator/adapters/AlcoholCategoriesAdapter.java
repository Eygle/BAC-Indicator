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
import edu.csulb.bacindicator.models.AlcoholCategory;

/**
 * Created by Johan
 */
public class AlcoholCategoriesAdapter extends ArrayAdapter<AlcoholCategory> {

    public AlcoholCategoriesAdapter(Context context, List<AlcoholCategory> list) {
        super(context, android.R.layout.select_dialog_singlechoice, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AlcoholCategory cat = getItem(position);
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();

            convertView = inflater.inflate(R.layout.single_string_list_item, parent, false);
            holder = new ViewHolder(convertView);

            holder.category = (TextView) convertView.findViewById(R.id.string);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.category.setText(cat.name);
        return convertView;
    }

    private static class ViewHolder {
        TextView category;

        ViewHolder(View layout) {
            category = (TextView) layout.findViewById(R.id.string);
        }
    }
}
