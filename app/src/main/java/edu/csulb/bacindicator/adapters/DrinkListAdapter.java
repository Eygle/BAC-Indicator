package edu.csulb.bacindicator.adapters;

import android.app.Activity;
import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import edu.csulb.bacindicator.R;
import edu.csulb.bacindicator.models.BAC;
import edu.csulb.bacindicator.models.Drink;

public class DrinkListAdapter extends ArrayAdapter<Drink> {

    public DrinkListAdapter(Context context) {
        super(context, 0);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Drink drink = getItem(position);
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();

            convertView = inflater.inflate(R.layout.drink_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.description.setText(getContext().getString(R.string.drink_format, drink.getQuantity(), drink.getMeasure(), drink.getAlcohol()));
        holder.time.setText(DateUtils.getRelativeTimeSpanString(BAC.NanoToMillis(drink.getTime()), BAC.NanoToMillis(System.nanoTime()), 0, 0));
        return convertView;
    }

    private static class ViewHolder {
        TextView description;
        TextView time;

        ViewHolder(View layout) {
            description = (TextView) layout.findViewById(R.id.drink_description);
            time = (TextView) layout.findViewById(R.id.drink_time);
        }
    }
}
