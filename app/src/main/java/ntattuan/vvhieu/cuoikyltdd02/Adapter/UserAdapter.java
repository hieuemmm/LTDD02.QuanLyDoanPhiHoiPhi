package ntattuan.vvhieu.cuoikyltdd02.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import java.util.List;

import ntattuan.vvhieu.cuoikyltdd02.App;
import ntattuan.vvhieu.cuoikyltdd02.CustomEvent.OnChangeAdapter;
import ntattuan.vvhieu.cuoikyltdd02.Data.UserDAO;
import ntattuan.vvhieu.cuoikyltdd02.Model.User;
import ntattuan.vvhieu.cuoikyltdd02.R;

public class UserAdapter extends BaseAdapter {
    private static OnChangeAdapter onChangeAdapter;
    private UserDAO userDAO;
    private List<User> ListUser;
    private LayoutInflater layoutInflater;
    private Context context;

    public UserAdapter(Context aContext) {
        this.context = aContext;
        layoutInflater = LayoutInflater.from(aContext);
        userDAO = new UserDAO(context);
    }

    // START CustomEvent
    public void setOnChangeAdapter(OnChangeAdapter eventListener) {
        onChangeAdapter = eventListener;
    }

    public void setListUser(List<User> ListUser) {
        this.ListUser = ListUser;
    }

    public static void Change() {
        if (onChangeAdapter != null) {
            onChangeAdapter.onChange();
        }
    }

    public void Edit(int id) {
        if (onChangeAdapter != null) {
            onChangeAdapter.onEdit(id);
        }
    }

    @Override
    public int getCount() {
        return ListUser.size();
    }

    @Override
    public Object getItem(int position) {
        return ListUser.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_user, null);
            holder = new ViewHolder();
            holder.User_avatar = (ImageView) convertView.findViewById(R.id.User_avatar);
            holder.User_Button_Edit = (ImageView) convertView.findViewById(R.id.User_Button_Menu);
            holder.User_Name = (TextView) convertView.findViewById(R.id.User_Name);
            holder.User_SDT = (TextView) convertView.findViewById(R.id.User_SDT);
            holder.User_Role = (TextView) convertView.findViewById(R.id.User_Role);
            holder.User_Button_Lock = (Button) convertView.findViewById(R.id.User_Button_Lock);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        User user = this.ListUser.get(position);
        if (App.CheckIsadmin()) {
            if (App.CheckIsMe(user)){
                holder.User_Button_Lock.setVisibility(View.GONE);
            }else{
                holder.User_Button_Lock.setVisibility(View.VISIBLE);
                holder.User_Button_Edit.setVisibility(View.VISIBLE);
            }
        } else {
            if (user.getRole() == App.ROLE_ADMIN) {
                holder.User_Button_Lock.setVisibility(View.GONE);
                holder.User_Button_Edit.setVisibility(View.GONE);
            }
        }
        if (user.getIsActive() == App.ACTIVE) {//ki???m tra kh??a
            if (user.getRole() == App.ROLE_ADMIN) {
                holder.User_avatar.setBackground(context.getResources().getDrawable(R.drawable.user_admin));
            } else {
                holder.User_avatar.setBackground(context.getResources().getDrawable(R.drawable.user_bithu));
            }
            holder.User_Button_Lock.setBackground(context.getResources().getDrawable(R.drawable.btn_red_border));
            holder.User_Button_Lock.setText("Kh??a");
        } else {
            holder.User_avatar.setBackground(context.getResources().getDrawable(R.drawable.user_bithu_lock));
            holder.User_Button_Lock.setBackground(context.getResources().getDrawable(R.drawable.btn_green_border));
            holder.User_Button_Lock.setText("M??? kh??a");
        }
        holder.User_Name.setText("T??i kho???n: " + user.getUserName());
        holder.User_SDT.setText("S??T: " + user.getSDT());
        holder.User_Role.setText(user.getRole() == App.ROLE_ADMIN ? "Ch???c v???: Admin" : "Ch???c v???: B?? th??");
        holder.User_Button_Lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.getIsActive() == App.ACTIVE) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setTitle("Kh??a t??i kho???n");
                    builder.setMessage("B???n mu???n kh??a " + user.getUserName() + " ?");
                    builder.setPositiveButton("Kh??a ngay", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            userDAO.setActiveUser(user,App.NO_ACTIVE);
                            App.ToastShow(v.getContext().getApplicationContext(), "Kh??a " + user.getUserName() + " th??nh c??ng");
                            Change();
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton("Quay l???i", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setTitle("M??? kh??a t??i kho???n");
                    builder.setMessage("B???n mu???n m??? kh??a " + user.getUserName() + " ?");
                    builder.setPositiveButton("M??? kh??a", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            userDAO.setActiveUser(user,App.ACTIVE);
                            App.ToastShow(v.getContext().getApplicationContext(), "M??? kh??a " + user.getUserName() + " th??nh c??ng");
                            Change();
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton("Quay l???i", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });
        return convertView;
    }

    static class ViewHolder {
        ImageView User_avatar, User_Button_Edit;
        TextView User_Name;
        TextView User_SDT;
        TextView User_Role;
        Button User_Button_Lock;
    }
}