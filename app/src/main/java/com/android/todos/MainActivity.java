package com.android.todos;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.todos.retrofit.RetrofitClient;
import com.android.todos.retrofit.TodoService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Todo> todos = new ArrayList<>();
    private TodoAdapter adapter;
    private AirplaneModeReceiver broadCastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (isNetworkAvailable(this)) {
            initData();
        } else {
            Toast.makeText(this, "Không có kết nối mạng, vui lòng thử lại.", Toast.LENGTH_SHORT).show();
        }
    }

    private void initData() {
        Retrofit retrofit = RetrofitClient.getClient(this);
        TodoService service = retrofit.create(TodoService.class);
        Call<List<Todo>> call = service.fetchTodos();
        call.enqueue(new Callback<List<Todo>>() {
            @Override
            public void onResponse(Call<List<Todo>> call, Response<List<Todo>> response) {
                if (response.isSuccessful()) {
                    todos = response.body();
                    adapter = new TodoAdapter(MainActivity.this, todos, new TodoAdapter.onClickItemListener() {
                        @Override
                        public void onClickItem(Todo todo) {
                            Toast.makeText(MainActivity.this, "UserID: " + todo.getUserId(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onLongClickItem(Todo todo) {
                            showDeleteDialog(MainActivity.this, todo);
                        }
                    });
                    recyclerView = findViewById(R.id.recyclerView);
                    recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Todo>> call, Throwable throwable) {

            }
        });
    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }

    private void showDeleteDialog(Context context, Todo todo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Bạn có thực sự muốn xóa todo này không?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteItem(todo);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteItem(Todo todo) {
        todos.remove(todo);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        broadCastReceiver = new AirplaneModeReceiver();
        registerReceiver(broadCastReceiver, new IntentFilter("android.intent.action.AIRPLANE_MODE"));
    }

    public void onDestroy()
    {
        super.onDestroy();
        unregisterReceiver(broadCastReceiver);
    }
}