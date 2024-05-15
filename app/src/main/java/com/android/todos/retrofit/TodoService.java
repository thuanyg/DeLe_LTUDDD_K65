package com.android.todos.retrofit;

import com.android.todos.Todo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TodoService {
    @GET("todos")
    Call<List<Todo>> fetchTodos();
}
