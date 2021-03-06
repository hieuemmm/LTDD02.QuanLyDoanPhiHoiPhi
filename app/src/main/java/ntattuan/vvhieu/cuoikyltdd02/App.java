package ntattuan.vvhieu.cuoikyltdd02;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.io.ByteArrayOutputStream;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;

import ntattuan.vvhieu.cuoikyltdd02.Data.RoundDAO;
import ntattuan.vvhieu.cuoikyltdd02.Model.Round;
import ntattuan.vvhieu.cuoikyltdd02.Model.User;

public class App {
    public static User UserLogined = null;
    public static int DoanVien_Tab_Current = 0;
    public static int Round_Tab_Current = 0;
    public static int DotNopTienDoanPhi_Current = -1;
    public static int DotNopTienHoiPhi_Current = -2;
    public static int Price_DoanPhi_Current = 0;
    public static int Price_HoiPhi_Current = 0;
    public static final int ROLE_ADMIN = 1;
    public static final int ROLE_BITHU = 0;
    public static final String STRING_NO_ROLE_ADMIN = "Bạn không có quyền Adminitartor.";
    public static final int GENDER_NAM = 1;
    public static final int GENDER_NU = 0;
    public static final int ACTIVE = 1;
    public static final int NO_ACTIVE = 0;
    public static final int SHOW = 1;
    public static final int NO_SHOW = 0;
    public static final int ACTIONT_COLLECT = 0;
    public static final int ACTIONT_UN_COLLECT = 1;
    public static final int REQUEST_CODE_CAMERA = 123;
    public static final int REQUEST_CODE_FOLDER = 456;
    public static final int LAUNCH_SECOND_ACTIVITY = 321;
    public static final int DOANVIEN_TAB_ALL = 0;
    public static final int DOANVIEN_TAB_DOAN_PHI = 1;
    public static final int DOANVIEN_TAB_HOI_PHI = 2;
    public static final int DOANVIEN_TAB_CHO_DUYET = 3;
    public static final int TYPE_ROUND_DOAN_PHI = 0;
    public static final int TYPE_ROUND_HOI_PHI = 1;
    public static final int ROUND_TAB_DOAN_PHI = TYPE_ROUND_DOAN_PHI;
    public static final int ROUND_TAB_HOI_PHI = TYPE_ROUND_HOI_PHI;

    public static byte[] DrawableToByteArray(int drawableID, Context context) {
        Drawable drawable = context.getResources().getDrawable(drawableID);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bytedata = stream.toByteArray();
        return bytedata;
    }

    public static byte[] getImageBitmap(ImageView img) {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) img.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] image = byteArrayOutputStream.toByteArray();
        return image;
    }

    public static void ToastShow(Context context, String s) {
        Toast toast = Toast.makeText(context, s, Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static String GetTimeCurrent() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        return formatter.format(date);
    }

    public static boolean CheckIsAdministrator() {
        if (UserLogined.getRole() == ROLE_ADMIN)
            return true;
        return false;
    }
    public static boolean CheckIsadmin() {
        if (UserLogined.getUserName().equals("admin"))
            return true;
        return false;
    }
    public static boolean CheckIsMe(User user) {
        if (UserLogined.getUserName().equals(user.getUserName()))
            return true;
        return false;
    }
    public static String chuannHoaHoTen(String st) {
        st = st.trim().toLowerCase();
        st = st.replaceAll("\\s+", " ");
        String[] temp = st.split(" ");
        // sau khi tách xong, gán xâu st thành sâu rỗng
        st = "";
        for (int i = 0; i < temp.length; i++) {
            st += String.valueOf(temp[i].charAt(0)).toUpperCase() + temp[i].substring(1);
            if (i < temp.length - 1) // nếu tempt[i] không phải từ cuối cùng
                st += " ";   // cộng thêm một khoảng trắng
        }
        return st;
    }

    public static String CurrencytoVN(int currency) {
        Locale vn = new Locale("vi", "VN");
        NumberFormat vnFormat = NumberFormat.getCurrencyInstance(vn);
        String VND = vnFormat.format(currency);
        VND = VND.replace("₫", "VNĐ");
        return VND;
    }

    public static String CurrencytoK(String currencyStr) {
        currencyStr = currencyStr.replace(".0", "");
        int currencyInt = Integer.parseInt(currencyStr);
        currencyInt /= 1000;
        currencyStr = CurrencytoVN(currencyInt);
        currencyStr = currencyStr.replace("VNĐ","K");
        return currencyStr;
    }
    public static String CurrencytoK(int currencyInt) {
        currencyInt /= 1000;
        String currencyStr = CurrencytoVN(currencyInt);
        currencyStr = currencyStr.replace("VNĐ","K");
        return currencyStr;
    }
    public static String StringtoPeople(String PeopleStr) {
        PeopleStr = PeopleStr.replace(".0", "");
        int PeopleInt = Integer.parseInt(PeopleStr);
        PeopleStr = CurrencytoVN(PeopleInt);
        PeopleStr = PeopleStr.replace("VNĐ"," Người");
        return PeopleStr;
    }

    public static boolean CheckIsSDT(String SDT) {
        if (Integer.parseInt(String.valueOf(SDT.charAt(0))) == 0) {
            return true;
        }
        return false;
    }

    public static void getRoundCurrent(Context context) {
        try {
            RoundDAO roundDAO = new RoundDAO(context);
            Round DoanPhi = roundDAO.getRound_ID_isShow(TYPE_ROUND_DOAN_PHI);
            Round HoiPhi = roundDAO.getRound_ID_isShow(TYPE_ROUND_HOI_PHI);
            DotNopTienDoanPhi_Current = DoanPhi.getId();
            DotNopTienHoiPhi_Current = HoiPhi.getId();
            Price_DoanPhi_Current = DoanPhi.getPrice();
            Price_HoiPhi_Current = HoiPhi.getPrice();
        } catch (Exception e) {
        }
    }

}
