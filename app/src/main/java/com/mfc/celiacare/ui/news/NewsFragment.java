package com.mfc.celiacare.ui.news;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.mfc.celiacare.R;
import com.mfc.celiacare.adapters.NewsAdapter;
import com.mfc.celiacare.model.News;
import com.mfc.celiacare.services.FirebaseService;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class NewsFragment extends Fragment {

    RecyclerView recyclerNews;
    NewsAdapter newsAdapter;
    List<News> newsList = new ArrayList<>();
    SwipeRefreshLayout swipeNews;
    FirebaseService firebaseService;
    private Map<String, Bitmap> imagesMap = new HashMap<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_news, container, false);

        initializeFirebase();
        return view;
    }

    private void initializeFirebase() {
        firebaseService = new FirebaseService();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getNewsFromFirebase();
        loadNewsImages();
        initializeElements(view);
    }

    private void getNewsFromFirebase() {
        firebaseService.getNews(new FirebaseService.NewsCallback() {
            @Override
            public void onNewsReceived(List<News> newsList) {
                NewsFragment.this.newsList.clear();
                NewsFragment.this.newsList.addAll(newsList);
                newsAdapter.notifyDataSetChanged();
                loadNewsImages();
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e("ERROR: ", errorMessage);
            }
        });
    }

    private void initializeElements(View view) {
        recyclerNews = view.findViewById(R.id.recyclerViewNews);
        recyclerNews.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerNews.addItemDecoration(new DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL));

        newsAdapter = new NewsAdapter(newsList, getContext(), imagesMap);
        newsAdapter.setNewsFragment(this);

        recyclerNews.setAdapter(newsAdapter);
        swipeNews = view.findViewById(R.id.swipeNews);
        swipeNews.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getNewsFromFirebase();
                swipeNews.setRefreshing(false);
                loadNewsImages();
            }
        });
    }

    public void openNewsDetails(News news) {
        NavController navController = Navigation.findNavController(requireView());
        Bundle args = new Bundle();
        args.putSerializable("news", news);

        String imageName = news.getImage();
        Bitmap image = imagesMap.get(imageName);
        args.putParcelable("image", image);

        navController.navigate(R.id.action_navigation_news_to_navigation_news_scrolling, args);
    }

    public void loadNewsImages(){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("news");

        storageRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                for (StorageReference item : listResult.getItems()) {
                    final String imageName = item.getName();
                    File localFile = new File(getContext().getFilesDir(), imageName);

                    if (localFile.exists()) {
                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getPath());
                        imagesMap.put(imageName, bitmap);
                    } else {
                        item.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Bitmap bitmap = BitmapFactory.decodeFile(localFile.getPath());
                                imagesMap.put(imageName, bitmap);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("TAG", "Failed to download image: " + imageName);
                            }
                        });
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Log.e("TAG", "Failed to retrieve image list: " + e.getMessage());
            }
        });
    }
}