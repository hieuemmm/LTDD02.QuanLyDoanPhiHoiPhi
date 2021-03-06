package ntattuan.vvhieu.cuoikyltdd02;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ntattuan.vvhieu.cuoikyltdd02.Data.UserDAO;
import ntattuan.vvhieu.cuoikyltdd02.Model.User;

public class LoginActivity extends AppCompatActivity {
    private UserDAO userDAO;
    private TextView loginUsername,loginPassWord;
    private Button loginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userDAO = new UserDAO(this);
        //TaoDuLieuMau();
        loginUsername = (TextView) findViewById(R.id.doanvien_textSearch);
        loginPassWord = (TextView) findViewById(R.id.loginPassWord);
        loginButton = (Button) findViewById(R.id.Candidate_Button_DoanPhi);
        App.getRoundCurrent(getBaseContext());
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PressLogin();
            }
        });
    }
    private void PressLogin() {
        //Kiểm tra NULL
        if (loginUsername.getText().toString().trim().equals("")) {
            loginUsername.requestFocus();
            Toast toast = Toast.makeText(LoginActivity.this, "Vui lòng nhập tài khoản", Toast.LENGTH_SHORT);
            toast.show();
        } else if (loginPassWord.getText().toString().trim().equals("")) {
            loginPassWord.requestFocus();
            Toast toast = Toast.makeText(LoginActivity.this, "Vui lòng nhập mật khẩu", Toast.LENGTH_SHORT);
            toast.show();
            //Kiểm tra đăng nhập
        } else {
            User user = new User(
                    loginUsername.getText().toString().trim(),
                    loginPassWord.getText().toString().trim()
            );
            if (userDAO.CheckLogin(user)) {
                if (userDAO.CheckLogin(user.getUserName())){
                    App.UserLogined = userDAO.getInforUser(user);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }else{
                    Toast toast = Toast.makeText(LoginActivity.this, "Tài khoản đang bị khóa", Toast.LENGTH_SHORT);
                    toast.show();
                }

            } else {
                Toast toast = Toast.makeText(LoginActivity.this, "Sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
    private void TaoDuLieuMau() {
        List<User> listUser = new ArrayList<User>();
        User user1 = new User("admin","admin","0398503361",App.ACTIVE,App.ROLE_ADMIN);
        User user5 = new User("admin1","admin","0398503364",App.ACTIVE,App.ROLE_ADMIN);
        User user2 = new User("bithu","admin","0398503362",App.ACTIVE,App.ROLE_BITHU);
        User user3 = new User("bithu1","admin","0398503363",App.NO_ACTIVE,App.ROLE_BITHU);
        User user4 = new User("bithu2","admin","0398503364",App.NO_ACTIVE,App.ROLE_BITHU);
        listUser.add(user1);
        listUser.add(user2);
        listUser.add(user3);
        listUser.add(user4);
        listUser.add(user5);
        for (User user : listUser) {
            if (!userDAO.CheckUserExits(user.getUserName())) {
                userDAO.addUser(user);
            }
        }
    }
}