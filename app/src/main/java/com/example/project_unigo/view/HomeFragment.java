package com.example.project_unigo.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.example.project_unigo.adapter.BannerAdapter;
import com.example.project_unigo.adapter.NewsAdapter;
import com.example.project_unigo.R;
import com.example.project_unigo.model.NewsModel;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private TextView tvName, tvInfo;
    private ViewPager2 viewPagerBanner;
    private RecyclerView rcvNews;
    private FirebaseFirestore db;

    // Handler cho Banner
    private Handler sliderHandler = new Handler(Looper.getMainLooper());
    private Runnable sliderRunnable;

    // Biến để quản lý lắng nghe Realtime Firebase
    private ListenerRegistration newsListenerRegistration;
    private NewsAdapter newsAdapter;
    private List<NewsModel> newsList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        tvName = view.findViewById(R.id.tvHomeName);
        tvInfo = view.findViewById(R.id.tvHomeInfo);
        viewPagerBanner = view.findViewById(R.id.viewPagerBanner);
        rcvNews = view.findViewById(R.id.rcvNews);

        db = FirebaseFirestore.getInstance();

        // Khởi tạo List và Adapter trước
        newsList = new ArrayList<>();
        newsAdapter = new NewsAdapter(newsList);
        rcvNews.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvNews.setAdapter(newsAdapter);

        loadUserInfo();
        setupBanner();

        // Gọi hàm load tin tức (Realtime)
        startRealtimeNewsUpdates();

        return view;
    }

    private void loadUserInfo() {
        if (getActivity() == null) return;

        SharedPreferences prefs = getActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        String mssv = prefs.getString("mssv", "");

        // 1. KIỂM TRA CACHE TRƯỚC (Load siêu nhanh)
        String cachedName = prefs.getString("fullName", null);
        String cachedDept = prefs.getString("department", null);

        // Nếu đã có dữ liệu trong máy, hiển thị luôn và KHÔNG gọi Firebase
        if (cachedName != null && cachedDept != null) {
            tvName.setText("Xin chào, " + cachedName);
            tvInfo.setText(mssv + ". " + cachedDept);
            return; // Kết thúc hàm tại đây
        }

        // 2. NẾU CHƯA CÓ CACHE THÌ MỚI GỌI FIREBASE (Chỉ chạy lần đầu)
        if (!mssv.isEmpty()) {
            db.collection("users")
                    .whereEqualTo("mssv", mssv)
                    .get()
                    .addOnSuccessListener(querySnapshot -> {
                        if (!querySnapshot.isEmpty()) {
                            DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                            String name = document.getString("fullName");
                            String department = document.getString("department");

                            // Cập nhật UI
                            tvName.setText("Xin chào, " + name);
                            tvInfo.setText(mssv + ". " + department);

                            // 3. LƯU VÀO CACHE ĐỂ LẦN SAU DÙNG
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("fullName", name);
                            editor.putString("department", department);
                            editor.apply(); // Lưu bất đồng bộ (nhanh hơn commit)
                        }
                    });
        }
    }

    private void setupBanner() {
        List<String> bannerList = new ArrayList<>();
        bannerList.add("https://yersin.edu.vn/wp-content/uploads/2025/03/480190771_1032293308931857_6377901377532286656_n-min.jpg");
        bannerList.add("https://yersin.edu.vn/wp-content/uploads/2025/11/B45I0378-min-scaled.jpg");
        bannerList.add("https://yersin.edu.vn/wp-content/uploads/2024/11/ask-min.png");
        bannerList.add("https://yersin.edu.vn/wp-content/uploads/2024/06/co-hoi-viec-lam-nganh-luat-kinh-te-5.jpg");

        BannerAdapter adapter = new BannerAdapter(bannerList);
        viewPagerBanner.setAdapter(adapter);

        viewPagerBanner.setOffscreenPageLimit(3);
        viewPagerBanner.setClipToPadding(false);
        viewPagerBanner.setClipChildren(false);

        CompositePageTransformer transformer = new CompositePageTransformer();
        transformer.addTransformer(new MarginPageTransformer(30));
        transformer.addTransformer((page, position) -> {
            float r = 1 - Math.abs(position);
            page.setScaleY(0.85f + r * 0.15f);
        });
        viewPagerBanner.setPageTransformer(transformer);
        viewPagerBanner.setCurrentItem(1000, false);

        sliderRunnable = new Runnable() {
            @Override
            public void run() {
                slowScrollToNext();
                sliderHandler.postDelayed(this, 3500);
            }
        };

        sliderHandler.postDelayed(sliderRunnable, 3500);

        viewPagerBanner.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 3500);
            }
        });
    }

    private void slowScrollToNext() {
        if (viewPagerBanner.isFakeDragging()) return;

        int duration = 500;

        if (!viewPagerBanner.beginFakeDrag()) return;

        final int width = viewPagerBanner.getWidth();
        ValueAnimator animator = ValueAnimator.ofInt(0, width);
        animator.setDuration(duration);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());

        animator.addUpdateListener(animation -> {
            int currentDragPosition = (int) animation.getAnimatedValue();

            float fraction = animation.getAnimatedFraction();
        });

        // --- SỬ DỤNG LẠI LOGIC CHUẨN CỦA BẠN ---
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            int oldDragPosition = 0;
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int currentDragPosition = (int) animation.getAnimatedValue();
                int dragOffset = currentDragPosition - oldDragPosition;
                oldDragPosition = currentDragPosition;
                viewPagerBanner.fakeDragBy(-dragOffset);
            }
        });

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (viewPagerBanner.isFakeDragging()) viewPagerBanner.endFakeDrag();
            }
            @Override
            public void onAnimationCancel(Animator animation) {
                if (viewPagerBanner.isFakeDragging()) viewPagerBanner.endFakeDrag();
            }
        });

        animator.start();
    }

    /**
     * Hàm lấy tin tức Realtime (Tự cập nhật khi DB thay đổi)
     */
    private void startRealtimeNewsUpdates() {
        Query query = db.collection("news")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(10); // GIỚI HẠN 10 ITEM

        // Sử dụng addSnapshotListener thay vì get()
        newsListenerRegistration = query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Firestore", "Listen failed.", error);
                    return;
                }

                if (value != null) {
                    newsList.clear(); // Xóa list cũ
                    for (DocumentSnapshot doc : value) {
                        NewsModel news = doc.toObject(NewsModel.class);
                        newsList.add(news);
                    }
                    newsAdapter.notifyDataSetChanged(); // Cập nhật giao diện ngay lập tức
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable, 3500);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // QUAN TRỌNG: Hủy đăng ký lắng nghe realtime khi thoát màn hình để tránh leak memory
        if (newsListenerRegistration != null) {
            newsListenerRegistration.remove();
        }
    }
}