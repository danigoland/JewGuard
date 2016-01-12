package undot.jewguard;

/**
 * Created by mymac on 12/01/16.
 */
        import android.content.Context;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.View.OnClickListener;
        import android.view.ViewGroup;
        import android.widget.BaseAdapter;
        import android.widget.CheckBox;
        import android.widget.CompoundButton;
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Toast;

        import java.util.ArrayList;
        import java.util.List;

public class CustomAdapter extends BaseAdapter{
    Context context;
    List<PInfo> pkglist;
    List<String> checked;
    private static LayoutInflater inflater=null;
    public CustomAdapter(MainActivity mainActivity, List<PInfo> plist,List<String> checkedlist) {
        // TODO Auto-generated constructor stub
        pkglist=plist;
        context=mainActivity;

        checked = checkedlist;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return pkglist.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView name;
        ImageView img;
        CheckBox chk;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.item, null);
        holder.name=(TextView) rowView.findViewById(R.id.textView);
        holder.img=(ImageView) rowView.findViewById(R.id.imageView);
        holder.chk=(CheckBox) rowView.findViewById(R.id.checkBox);
       for(String a : checked)
       {
           if(a.equals(pkglist.get(position).pname))
           {
               holder.chk.setChecked(true);
           }
       }
        holder.name.setText(pkglist.get(position).appname);
        holder.img.setImageDrawable(pkglist.get(position).icon);
        holder.chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    checked.add(pkglist.get(position).pname);
                    Log.d("list size",checked.size()+"");
                } else {
                    checked.remove(pkglist.get(position).pname);
                }
            }
        });
        return rowView;
    }

    public List<String> getChecked() {
      return checked;
    }
}